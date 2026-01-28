import axios from "axios";

export const apiBaseUrlStorageKey = "item-control.apiBaseUrl";

function normalizeBaseUrl(input: string): string {
  const trimmed = input.trim();
  if (!trimmed) return "/api/v1";

  // Se for um path (não URL absoluta), garante que seja absoluto no host atual.
  // Ex.: "api/v1" -> "/api/v1" (evita resolver como /dashboard/api/v1).
  const looksLikeAbsoluteUrl = /^https?:\/\//i.test(trimmed);
  const withLeadingSlash =
    !looksLikeAbsoluteUrl && !trimmed.startsWith("/") ? `/${trimmed}` : trimmed;

  // Evita double-slash ao concatenar paths no axios ("/api/v1/" + "/items").
  return withLeadingSlash.endsWith("/")
    ? withLeadingSlash.slice(0, -1)
    : withLeadingSlash;
}

function devProxyRewrite(baseUrl: string): string {
  // No navegador, chamadas diretas para http://localhost:8080 costumam falhar por CORS.
  // Como o Vite já tem proxy para /api -> localhost:8080, no DEV preferimos usar path relativo.
  if (!import.meta.env.DEV) return baseUrl;

  if (!/^https?:\/\//i.test(baseUrl)) return baseUrl;

  try {
    const parsed = new URL(baseUrl);
    const isLocalBackend =
      (parsed.hostname === "localhost" || parsed.hostname === "127.0.0.1") &&
      parsed.port === "8080";

    if (isLocalBackend && parsed.pathname.startsWith("/api")) {
      return normalizeBaseUrl(parsed.pathname);
    }
  } catch {
    // Se não for uma URL válida, mantém como está.
  }

  return baseUrl;
}

export function getApiBaseUrl(): string {
  const env = import.meta.env.VITE_API_URL as string | undefined;

  if (typeof window === "undefined") {
    return devProxyRewrite(normalizeBaseUrl(env ?? "/api/v1"));
  }

  const stored = window.localStorage.getItem(apiBaseUrlStorageKey);
  return devProxyRewrite(normalizeBaseUrl(stored ?? env ?? "/api/v1"));
}

export const resolvedApiBaseUrl = getApiBaseUrl();

export const api = axios.create({
  baseURL: resolvedApiBaseUrl,
  headers: {
    "Content-Type": "application/json",
  },
});

const debugApi = (import.meta.env.VITE_DEBUG_API as string | undefined) === "true";

if (import.meta.env.DEV && debugApi) {
  // Útil para confirmar no console do browser se está usando /api (proxy) ou URL completa.
  // Habilite com: VITE_DEBUG_API=true
  console.info("[api] baseURL =", api.defaults.baseURL);
}

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error?.response?.status as number | undefined;
    const responseData = error?.response?.data as unknown;
    const url = error?.config?.url as string | undefined;
    const baseURL = error?.config?.baseURL as string | undefined;
    const params = error?.config?.params as Record<string, unknown> | undefined;
    const code = error?.code as string | undefined;
    const originalMessage = error?.message as string | undefined;
    const isNetworkError = !status;

    const suffix = status ? ` (HTTP ${status})` : "";
    const query =
      params && Object.keys(params).length > 0
        ? `?${new URLSearchParams(
            Object.entries(params)
              .filter(([, value]) => value != null)
              .map(([key, value]) => [key, String(value)]),
          ).toString()}`
        : "";
    const target = url ? `: ${baseURL ?? ""}${url}${query}` : "";

    if (isNetworkError) {
      return Promise.reject(
        new Error(
          `Falha ao comunicar com a API (rede/CORS)${target}${code ? ` [${code}]` : ""}${originalMessage ? `: ${originalMessage}` : ""}. Dica: no dev, use base URL /api/v1 (proxy do Vite).`,
        ),
      );
    }

    let serverMessage: string | undefined;
    if (typeof responseData === "string") {
      serverMessage = responseData;
    } else if (responseData && typeof responseData === "object") {
      const maybeMessage = (responseData as { message?: unknown }).message;
      if (typeof maybeMessage === "string" && maybeMessage.trim()) {
        serverMessage = maybeMessage;
      } else {
        try {
          serverMessage = JSON.stringify(responseData);
        } catch {
          serverMessage = undefined;
        }
      }
    }

    const details = serverMessage ? `: ${serverMessage}` : "";

    return Promise.reject(
      new Error(`Falha ao comunicar com a API${suffix}${target}${details}`),
    );
  },
);
