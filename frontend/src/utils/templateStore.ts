import type {
  Template,
  CreateTemplateRequest,
  TemplateMetadataSchema,
  TemplateScope,
  UpdateTemplateRequest,
} from "../types";

const STORAGE_KEY = "item-control.templates.v1";

const makeId = () => {
  if (typeof crypto !== "undefined" && "randomUUID" in crypto) {
    return crypto.randomUUID();
  }
  return `tpl_${Date.now()}_${Math.random().toString(36).slice(2, 8)}`;
};

const nowIso = () => new Date().toISOString();

const seedTemplates = (): Template[] => {
  const timestamp = nowIso();
  const generalSchema: TemplateMetadataSchema = {
    observacoes: { type: "string", required: false },
    numeroSerie: { type: "string", required: false },
  };
  const vehicleSchema: TemplateMetadataSchema = {
    placa: { type: "string", required: true },
    ano: { type: "number", required: true },
    kmAtual: { type: "number", required: false },
  };

  return [
    {
      id: makeId(),
      scope: "GLOBAL",
      code: "GENERAL",
      nameByLocale: { "pt-BR": "Geral" },
      descriptionByLocale: { "pt-BR": "Campos basicos para qualquer item." },
      metadataSchema: generalSchema,
      createdAt: timestamp,
      updatedAt: timestamp,
    },
    {
      id: makeId(),
      scope: "GLOBAL",
      code: "VEHICLE",
      nameByLocale: { "pt-BR": "Veiculo" },
      descriptionByLocale: { "pt-BR": "Informacoes comuns para veiculos." },
      metadataSchema: vehicleSchema,
      createdAt: timestamp,
      updatedAt: timestamp,
    },
  ];
};

const safeParse = (value: string | null): Template[] => {
  if (!value) return [];
  try {
    const parsed = JSON.parse(value) as Template[];
    return Array.isArray(parsed) ? parsed : [];
  } catch {
    return [];
  }
};

const ensureTemplates = (): Template[] => {
  if (typeof window === "undefined") return seedTemplates();

  const existing = safeParse(window.localStorage.getItem(STORAGE_KEY));
  if (existing.length > 0) return existing;

  const seeded = seedTemplates();
  window.localStorage.setItem(STORAGE_KEY, JSON.stringify(seeded));
  return seeded;
};

const saveTemplates = (templates: Template[]) => {
  if (typeof window === "undefined") return;
  window.localStorage.setItem(STORAGE_KEY, JSON.stringify(templates));
};

export const listTemplates = (): Template[] => {
  return ensureTemplates();
};

export const getTemplate = (id: string): Template | undefined => {
  return ensureTemplates().find((template) => template.id === id);
};

export const createTemplate = (input: CreateTemplateRequest): Template => {
  const templates = ensureTemplates();
  const timestamp = nowIso();
  const template: Template = {
    id: makeId(),
    scope: input.scope as TemplateScope,
    code: input.code.trim(),
    userId: input.userId ?? null,
    nameByLocale: input.nameByLocale,
    descriptionByLocale: input.descriptionByLocale,
    metadataSchema: input.metadataSchema,
    createdAt: timestamp,
    updatedAt: timestamp,
  };

  const next = [template, ...templates];
  saveTemplates(next);
  return template;
};

export const updateTemplate = (
  id: string,
  input: UpdateTemplateRequest,
): Template | null => {
  const templates = ensureTemplates();
  const index = templates.findIndex((template) => template.id === id);
  if (index < 0) return null;

  const current = templates[index];
  const updated: Template = {
    ...current,
    nameByLocale: input.nameByLocale,
    descriptionByLocale: input.descriptionByLocale,
    metadataSchema: input.metadataSchema,
    updatedAt: nowIso(),
  };

  const next = [...templates];
  next[index] = updated;
  saveTemplates(next);
  return updated;
};

export const deleteTemplate = (id: string): boolean => {
  const templates = ensureTemplates();
  const next = templates.filter((template) => template.id !== id);
  if (next.length === templates.length) return false;
  saveTemplates(next);
  return true;
};

export const normalizeFields = (schema: TemplateMetadataSchema) => schema;
