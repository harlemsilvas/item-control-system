import { useMemo, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import type {
  CreateTemplateRequest,
  Template,
  TemplateMetadataFieldType,
  TemplateMetadataSchema,
  TemplateScope,
  UpdateTemplateRequest,
} from "../../types";
import { templatesApi } from "../../api/templates";
import { DEMO_USER_ID } from "../../config/demoUser";
import { InlineCode } from "../../components/ui/InlineCode";
import { MetadataSchemaBuilder } from "../../components/templates/MetadataSchemaBuilder";
import {
  TEMPLATE_PRESETS,
  getBrowserLocale,
  t,
  rowsToSchema,
  type MetadataSchemaRow,
  type TemplatePresetKey,
} from "../../components/templates/metadataSchemaUtils";
import { useToast } from "../../components/ui/Toast";

const DEFAULT_LOCALE = "pt-BR";

type Mode = "create" | "edit";

function emptySchema(): TemplateMetadataSchema {
  return {
    field1: { type: "string", required: false },
  };
}

function inferDefaultNameByLocale(code: string) {
  return { [DEFAULT_LOCALE]: code };
}

function defaultPresetRows(): MetadataSchemaRow[] {
  return TEMPLATE_PRESETS.find((p) => p.key === "GENERAL")?.rows ?? [];
}

export function TemplateFormPage() {
  const { id } = useParams();
  const mode: Mode = id ? "edit" : "create";
  const templateId = id ?? "";

  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const { showToast } = useToast();

  const {
    data: template,
    isLoading,
    isError,
    error,
  } = useQuery<Template>({
    queryKey: ["template", templateId],
    queryFn: () => templatesApi.getById(templateId),
    enabled: mode === "edit" && Boolean(templateId),
  });

  // CREATE mode uses local state
  const [scope, setScope] = useState<TemplateScope>("GLOBAL");
  const [code, setCode] = useState<string>("");
  const [userId, setUserId] = useState<string>(DEMO_USER_ID);
  const [namePt, setNamePt] = useState<string>("");
  const [descPt, setDescPt] = useState<string>("");
  const [schemaText, setSchemaText] = useState<string>(
    JSON.stringify(emptySchema(), null, 2),
  );
  const [formError, setFormError] = useState<string | null>(null);

  // EDIT state (initialized once when template loads)
  const templateKey = mode === "edit" ? templateId : "create";

  const [editNamePtState, setEditNamePtState] = useState<string>(
    template?.nameByLocale[DEFAULT_LOCALE] ?? "",
  );
  const [editDescPtState, setEditDescPtState] = useState<string>(
    template?.descriptionByLocale?.[DEFAULT_LOCALE] ?? "",
  );
  const [editSchemaTextState, setEditSchemaTextState] = useState<string>(
    JSON.stringify(template?.metadataSchema ?? {}, null, 2),
  );

  const browserLocale = getBrowserLocale();

  const [selectedPreset, setSelectedPreset] = useState<TemplatePresetKey>("GENERAL");

  const [schemaMode, setSchemaMode] = useState<"BUILDER" | "JSON">("BUILDER");
  const [schemaRows, setSchemaRows] = useState<MetadataSchemaRow[]>(
    defaultPresetRows(),
  );

  const schemaTextFromBuilder = useMemo(() => {
    return JSON.stringify(rowsToSchema(schemaRows), null, 2);
  }, [schemaRows]);

  // EDIT mode derived from loaded template (read-only constraints)
  const editScope = template?.scope ?? "GLOBAL";
  const editCode = template?.code ?? "";
  const editUserId = template?.userId ?? "";

  const effectiveScope = mode === "edit" ? editScope : scope;
  const effectiveCode = mode === "edit" ? editCode : code;
  const effectiveUserId = mode === "edit" ? editUserId : userId;
  const effectiveNamePt = mode === "edit" ? editNamePtState : namePt;
  const effectiveDescPt = mode === "edit" ? editDescPtState : descPt;
  const effectiveSchemaText =
    mode === "edit"
      ? editSchemaTextState
      : schemaMode === "JSON"
        ? schemaText
        : schemaTextFromBuilder;

  const setEffectiveNamePt = (value: string) => {
    if (mode === "edit") setEditNamePtState(value);
    else setNamePt(value);
  };

  const setEffectiveDescPt = (value: string) => {
    if (mode === "edit") setEditDescPtState(value);
    else setDescPt(value);
  };

  const applyPreset = (presetKey: TemplatePresetKey) => {
    const preset = TEMPLATE_PRESETS.find((p) => p.key === presetKey);
    if (!preset) return;

    // Pre-fill code/name/description (user can still edit)
    setCode(preset.code);
    setNamePt(preset.nameByLocale[browserLocale] ?? preset.nameByLocale[DEFAULT_LOCALE] ?? "");
    setDescPt(
      preset.descriptionByLocale[browserLocale] ??
        preset.descriptionByLocale[DEFAULT_LOCALE] ??
        "",
    );

    // Pre-fill schema builder
    setSchemaRows(preset.rows);
    setSchemaMode("BUILDER");
    setSchemaText(JSON.stringify(rowsToSchema(preset.rows), null, 2));
  };

  const canSubmit = useMemo(() => {
    const codeOk = mode === "edit" ? true : effectiveCode.trim().length > 0;
    const nameOk = (effectiveNamePt || effectiveCode).trim().length > 0;
    const userOk = effectiveScope === "GLOBAL" ? true : effectiveUserId.trim().length > 0;
    return codeOk && nameOk && userOk;
  }, [effectiveCode, effectiveNamePt, effectiveScope, effectiveUserId, mode]);

  const parseSchema = (): TemplateMetadataSchema => {
    const trimmed = effectiveSchemaText.trim();
    if (!trimmed) return {};

    const parsed = JSON.parse(trimmed) as unknown;
    if (!parsed || typeof parsed !== "object" || Array.isArray(parsed)) {
      throw new Error("metadataSchema precisa ser um objeto JSON");
    }

    // shallow validation
    const schema = parsed as Record<string, unknown>;
    for (const [key, value] of Object.entries(schema)) {
      if (!value || typeof value !== "object") {
        throw new Error(`Campo '${key}' precisa ser um objeto`);
      }
      const entry = value as { type?: unknown; required?: unknown };
      const type = entry.type as TemplateMetadataFieldType | undefined;
      if (!type || !["string", "number", "boolean", "date"].includes(type)) {
        throw new Error(
          `Campo '${key}': type precisa ser 'string' | 'number' | 'boolean' | 'date'`,
        );
      }
      if (entry.required != null && typeof entry.required !== "boolean") {
        throw new Error(`Campo '${key}': required precisa ser boolean`);
      }
    }

    return parsed as TemplateMetadataSchema;
  };

  const createMutation = useMutation({
    mutationFn: async () => {
      const schema = parseSchema();
      const payload: CreateTemplateRequest = {
        scope: effectiveScope,
        code: effectiveCode.trim(),
        userId: effectiveScope === "USER" ? effectiveUserId.trim() : null,
        nameByLocale: effectiveNamePt.trim()
          ? { [DEFAULT_LOCALE]: effectiveNamePt.trim() }
          : inferDefaultNameByLocale(effectiveCode.trim()),
        descriptionByLocale: effectiveDescPt.trim()
          ? { [DEFAULT_LOCALE]: effectiveDescPt.trim() }
          : undefined,
        metadataSchema: schema,
      };

      return templatesApi.create(payload);
    },
    onSuccess: async (created) => {
      await queryClient.invalidateQueries({ queryKey: ["templates"] });
      showToast({ variant: "success", title: "Templates", message: "Template criado." });
      navigate(`/templates/${created.id}`);
    },
    onError: (err) => {
      const message = err instanceof Error ? err.message : "Erro ao criar template";
      setFormError(message);
      showToast({ variant: "error", title: "Templates", message });
    },
  });

  const updateMutation = useMutation({
    mutationFn: async () => {
      const schema = parseSchema();
      const payload: UpdateTemplateRequest = {
        nameByLocale: effectiveNamePt.trim()
          ? { [DEFAULT_LOCALE]: effectiveNamePt.trim() }
          : template?.nameByLocale ?? { [DEFAULT_LOCALE]: effectiveCode.trim() },
        descriptionByLocale: effectiveDescPt.trim()
          ? { [DEFAULT_LOCALE]: effectiveDescPt.trim() }
          : undefined,
        metadataSchema: schema,
      };

      return templatesApi.update(templateId, payload);
    },
    onSuccess: async (updated) => {
      await queryClient.invalidateQueries({ queryKey: ["templates"] });
      await queryClient.invalidateQueries({ queryKey: ["template", templateId] });
      showToast({ variant: "success", title: "Templates", message: "Template atualizado." });
      navigate(`/templates/${updated.id}`);
    },
    onError: (err) => {
      const message = err instanceof Error ? err.message : "Erro ao atualizar template";
      setFormError(message);
      showToast({ variant: "error", title: "Templates", message });
    },
  });

  const onSubmit = () => {
    setFormError(null);

    try {
      // Validate schema early
      parseSchema();
    } catch (e) {
      const message = e instanceof Error ? e.message : "metadataSchema inválido";
      setFormError(message);
      showToast({ variant: "error", title: "Validação", message });
      return;
    }

    if (!canSubmit) {
      const message =
        "Preencha os campos obrigatórios (code, name e userId para USER)";
      setFormError(message);
      showToast({ variant: "error", title: "Validação", message });
      return;
    }

    if (mode === "create") createMutation.mutate();
    else updateMutation.mutate();
  };

  const fieldLabelByKey = useMemo(() => {
    const map: Record<string, string> = {};
    // Only relevant for create-mode builder; safe to compute always.
    for (const row of schemaRows) {
      const label =
        row.labelByLocale?.[browserLocale] ??
        row.labelByLocale?.[DEFAULT_LOCALE];
      if (label) map[row.key] = label;
    }
    return map;
  }, [schemaRows, browserLocale]);

  if (mode === "edit" && isLoading) {
    return <div className="card">Carregando template...</div>;
  }

  if (mode === "edit" && isError) {
    const message =
      error instanceof Error ? error.message : "Erro ao carregar template";
    return (
      <div className="card">
        <h2 className="text-lg font-semibold text-gray-900 mb-2">
          Falha ao carregar template
        </h2>
        <p className="text-sm text-gray-600">{message}</p>
        <div className="mt-4">
          <Link className="text-blue-600 hover:underline" to="/templates">
            Voltar para Templates
          </Link>
        </div>
      </div>
    );
  }

  const title = mode === "create" ? "Novo Template" : "Editar Template";

  return (
    <div className="space-y-6" key={templateKey}>
      <div>
        <Link className="text-sm text-blue-600 hover:underline" to="/templates">
          ← Voltar para Templates
        </Link>
        <h1 className="text-3xl font-bold text-gray-900 mt-2">{title}</h1>
        <p className="text-gray-600 mt-2">
          Backend: <InlineCode>POST/PUT /api/v1/templates</InlineCode>. Regras:{" "}
          <InlineCode>GLOBAL</InlineCode> não exige userId;{" "}
          <InlineCode>USER</InlineCode> exige userId.
        </p>
      </div>

      <div className="card space-y-6">
        {formError ? <p className="text-sm text-red-600">{formError}</p> : null}

        {mode === "create" ? (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Preset
              </label>
              <select
                className="input"
                value={selectedPreset}
                onChange={(e) => {
                  const value = e.target.value as TemplatePresetKey;
                  setSelectedPreset(value);
                  applyPreset(value);
                }}
              >
                {TEMPLATE_PRESETS.map((p) => (
                  <option key={p.key} value={p.key}>
                    {t(p.label, browserLocale)}
                  </option>
                ))}
              </select>
              <p className="text-xs text-gray-500 mt-1">
                {t(
                  TEMPLATE_PRESETS.find((p) => p.key === selectedPreset)?.description ??
                    TEMPLATE_PRESETS[0].description,
                  browserLocale,
                )}
              </p>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Scope
              </label>
              <select
                className="input"
                value={scope}
                onChange={(e) => setScope(e.target.value as TemplateScope)}
              >
                <option value="GLOBAL">GLOBAL</option>
                <option value="USER">USER</option>
              </select>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Code
              </label>
              <input
                className="input"
                value={code}
                onChange={(e) => setCode(e.target.value)}
                placeholder="Ex: VEHICLE, GENERAL, MY-CUSTOM-DEVICE"
              />
              <p className="text-xs text-gray-500 mt-1">
                Obrigatório. Precisa ser único dentro do scope.
              </p>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Nome (auto por preset, pode editar)
              </label>
              <input
                className="input"
                value={namePt}
                onChange={(e) => setNamePt(e.target.value)}
              />
            </div>

            <div className="md:col-span-2">
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Descrição (auto por preset, pode editar)
              </label>
              <input
                className="input"
                value={descPt}
                onChange={(e) => setDescPt(e.target.value)}
              />
            </div>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Scope
              </label>
              <input className="input" value={editScope} disabled />
              <p className="text-xs text-gray-500 mt-1">
                Não é permitido alterar scope.
              </p>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Code
              </label>
              <input className="input" value={editCode} disabled />
              <p className="text-xs text-gray-500 mt-1">
                Não é permitido alterar code.
              </p>
            </div>
          </div>
        )}

        {mode === "create" && scope === "USER" ? (
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              userId
            </label>
            <input
              className="input"
              value={userId}
              onChange={(e) => setUserId(e.target.value)}
            />
            <p className="text-xs text-gray-500 mt-1">
              Obrigatório quando scope=USER.
            </p>
          </div>
        ) : null}

        {mode === "edit" && editScope === "USER" ? (
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              userId
            </label>
            <input className="input" value={editUserId} disabled />
            <p className="text-xs text-gray-500 mt-1">
              Não é permitido alterar userId.
            </p>
          </div>
        ) : null}

        {mode === "edit" ? (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Nome (pt-BR)
              </label>
              <input
                className="input"
                value={effectiveNamePt}
                onChange={(e) => setEffectiveNamePt(e.target.value)}
                placeholder="Ex: Veículo"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Descrição (pt-BR)
              </label>
              <input
                className="input"
                value={effectiveDescPt}
                onChange={(e) => setEffectiveDescPt(e.target.value)}
                placeholder="Ex: Template padrão para veículos"
              />
            </div>
          </div>
        ) : null}

        <div>
          <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
            <label className="block text-sm font-medium text-gray-700">
              metadataSchema
            </label>

            {mode === "create" ? (
              <div className="flex items-center gap-2">
                <button
                  type="button"
                  className={schemaMode === "BUILDER" ? "btn-primary" : "btn-secondary"}
                  onClick={() => setSchemaMode("BUILDER")}
                >
                  Builder
                </button>
                <button
                  type="button"
                  className={schemaMode === "JSON" ? "btn-primary" : "btn-secondary"}
                  onClick={() => setSchemaMode("JSON")}
                >
                  JSON
                </button>
              </div>
            ) : null}
          </div>

          {mode === "create" && schemaMode === "BUILDER" ? (
            <div className="mt-3">
              <MetadataSchemaBuilder
                value={schemaRows}
                onChange={setSchemaRows}
                locale={browserLocale}
                fieldLabelByKey={fieldLabelByKey}
              />
              <div className="mt-3">
                <p className="text-xs text-gray-500 mb-2">
                  Preview (gerado automaticamente):
                </p>
                <pre className="text-xs bg-gray-50 border border-gray-200 rounded p-3 overflow-auto">
{schemaTextFromBuilder}
                </pre>
              </div>
            </div>
          ) : (
            <div className="mt-3">
              <textarea
                className="input font-mono text-sm min-h-[260px]"
                value={mode === "edit" ? editSchemaTextState : schemaText}
                onChange={(e) =>
                  mode === "edit"
                    ? setEditSchemaTextState(e.target.value)
                    : setSchemaText(e.target.value)
                }
              />
              <p className="text-xs text-gray-500 mt-1">
                Formato: <InlineCode>{`{"brand": {"type": "string", "required": true }}`}</InlineCode>
              </p>
            </div>
          )}
        </div>

        <div className="flex items-center justify-end gap-2">
          <Link className="btn-secondary" to="/templates">
            Cancelar
          </Link>
          <button
            type="button"
            className="btn-primary"
            onClick={onSubmit}
            disabled={(mode === "create" ? createMutation : updateMutation).isPending}
          >
            {mode === "create"
              ? createMutation.isPending
                ? "Criando..."
                : "Criar"
              : updateMutation.isPending
                ? "Salvando..."
                : "Salvar"}
          </button>
        </div>
      </div>

      <div className="card">
        <h2 className="text-lg font-semibold text-gray-900 mb-2">
          Checklist rápido (backend)
        </h2>
        <ul className="list-disc pl-5 text-sm text-gray-700 space-y-1">
          <li>
            Base URL local: <InlineCode>http://localhost:8080/api/v1</InlineCode>
          </li>
          <li>
            Criar GLOBAL: não enviar <InlineCode>userId</InlineCode>.
          </li>
          <li>
            Criar USER: enviar <InlineCode>userId</InlineCode> obrigatório.
          </li>
          <li>
            Atualizar: não pode trocar <InlineCode>scope</InlineCode>,{" "}
            <InlineCode>userId</InlineCode> e <InlineCode>code</InlineCode>.
          </li>
        </ul>
      </div>
    </div>
  );
}
