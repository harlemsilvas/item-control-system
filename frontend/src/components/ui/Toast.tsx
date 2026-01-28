/* eslint-disable react-refresh/only-export-components */
import { createContext, useCallback, useContext, useEffect, useMemo, useState } from "react";
import { makeId, toastDurationMs } from "./toastUtils";

export type ToastVariant = "success" | "error" | "info";

export type ToastMessage = {
  id: string;
  title?: string;
  message: string;
  variant: ToastVariant;
  durationMs?: number;
};

type ToastContextValue = {
  showToast: (toast: Omit<ToastMessage, "id">) => void;
};

const ToastContext = createContext<ToastContextValue | null>(null);

export function ToastProvider({ children }: { children: React.ReactNode }) {
  const [toasts, setToasts] = useState<ToastMessage[]>([]);

  const removeToast = useCallback((id: string) => {
    setToasts((prev) => prev.filter((t) => t.id !== id));
  }, []);

  const showToast = useCallback(
    (toast: Omit<ToastMessage, "id">) => {
      const id = makeId();
      const durationMs = toast.durationMs ?? toastDurationMs(toast.variant);
      const next: ToastMessage = { ...toast, id, durationMs };

      setToasts((prev) => [...prev, next]);

      window.setTimeout(() => {
        removeToast(id);
      }, durationMs);
    },
    [removeToast],
  );

  const value = useMemo(() => ({ showToast }), [showToast]);

  return (
    <ToastContext.Provider value={value}>
      {children}
      <ToastViewport toasts={toasts} onClose={removeToast} />
    </ToastContext.Provider>
  );
}

export function useToast() {
  const ctx = useContext(ToastContext);
  if (!ctx) {
    throw new Error("useToast must be used within ToastProvider");
  }
  return ctx;
}

function variantClasses(variant: ToastVariant) {
  switch (variant) {
    case "success":
      return "border-green-200 bg-green-50 text-green-900";
    case "error":
      return "border-red-200 bg-red-50 text-red-900";
    case "info":
    default:
      return "border-blue-200 bg-blue-50 text-blue-900";
  }
}

function ToastViewport({
  toasts,
  onClose,
}: {
  toasts: ToastMessage[];
  onClose: (id: string) => void;
}) {
  // Esc to close newest
  useEffect(() => {
    const handler = (e: KeyboardEvent) => {
      if (e.key !== "Escape") return;
      const last = toasts[toasts.length - 1];
      if (last) onClose(last.id);
    };
    window.addEventListener("keydown", handler);
    return () => window.removeEventListener("keydown", handler);
  }, [onClose, toasts]);

  return (
    <div className="fixed top-4 right-4 z-50 flex flex-col gap-2 w-[calc(100vw-2rem)] sm:w-[420px]">
      {toasts.map((t) => (
        <div
          key={t.id}
          className={`border rounded-lg shadow-sm p-3 ${variantClasses(t.variant)}`}
          role="status"
        >
          <div className="flex items-start justify-between gap-3">
            <div className="min-w-0">
              {t.title ? (
                <div className="text-sm font-semibold truncate">{t.title}</div>
              ) : null}
              <div className="text-sm break-words">{t.message}</div>
            </div>
            <button
              type="button"
              className="text-xs underline opacity-80 hover:opacity-100"
              onClick={() => onClose(t.id)}
            >
              Fechar
            </button>
          </div>
        </div>
      ))}
    </div>
  );
}
