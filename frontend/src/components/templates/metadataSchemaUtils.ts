import type { TemplateMetadataSchema, TemplateMetadataFieldType } from "../../types";

export type MetadataSchemaRow = {
  id: string;
  key: string;
  type: TemplateMetadataFieldType;
  required: boolean;
  labelByLocale?: Record<string, string>;
};

export type TemplatePresetKey =
  | "GENERAL"
  | "VEHICLE"
  | "RECURRING_BILL"
  | "CONSUMABLE";

export type PresetText = {
  "pt-BR": string;
  "en-US": string;
};

export type TemplatePreset = {
  key: TemplatePresetKey;
  label: PresetText;
  description: PresetText;
  code: string;
  nameByLocale: Record<string, string>;
  descriptionByLocale: Record<string, string>;
  rows: MetadataSchemaRow[];
};

export function getBrowserLocale(): "pt-BR" | "en-US" {
  if (typeof navigator === "undefined") return "pt-BR";
  const lang = (navigator.languages?.[0] ?? navigator.language ?? "pt-BR").toLowerCase();
  return lang.startsWith("pt") ? "pt-BR" : "en-US";
}

export function t(text: PresetText, locale: "pt-BR" | "en-US"): string {
  return text[locale] ?? text["pt-BR"];
}

function makeRow(row: Omit<MetadataSchemaRow, "id">): MetadataSchemaRow {
  return {
    id:
      typeof crypto !== "undefined" && "randomUUID" in crypto
        ? crypto.randomUUID()
        : `${Date.now()}_${Math.random().toString(16).slice(2)}`,
    ...row,
  };
}

export const TEMPLATE_PRESETS: TemplatePreset[] = [
  {
    key: "GENERAL",
    label: { "pt-BR": "Geral", "en-US": "General" },
    description: {
      "pt-BR": "Campos básicos para uso geral.",
      "en-US": "Basic fields for general usage.",
    },
    code: "GENERAL",
    nameByLocale: { "pt-BR": "Geral", "en-US": "General" },
    descriptionByLocale: {
      "pt-BR": "Campos básicos para qualquer item.",
      "en-US": "Basic fields for any item.",
    },
    rows: [
      makeRow({
        key: "brand",
        type: "string",
        required: false,
        labelByLocale: { "pt-BR": "Marca", "en-US": "Brand" },
      }),
      makeRow({
        key: "model",
        type: "string",
        required: false,
        labelByLocale: { "pt-BR": "Modelo", "en-US": "Model" },
      }),
      makeRow({
        key: "serialNumber",
        type: "string",
        required: false,
        labelByLocale: { "pt-BR": "Nº de série", "en-US": "Serial number" },
      }),
      makeRow({
        key: "notes",
        type: "string",
        required: false,
        labelByLocale: { "pt-BR": "Observações", "en-US": "Notes" },
      }),
    ],
  },
  {
    key: "VEHICLE",
    label: { "pt-BR": "Veículo", "en-US": "Vehicle" },
    description: {
      "pt-BR": "Campos comuns para veículos.",
      "en-US": "Common vehicle fields.",
    },
    code: "VEHICLE",
    nameByLocale: { "pt-BR": "Veículo", "en-US": "Vehicle" },
    descriptionByLocale: {
      "pt-BR": "Template padrão para veículos.",
      "en-US": "Default template for vehicles.",
    },
    rows: [
      makeRow({
        key: "brand",
        type: "string",
        required: true,
        labelByLocale: { "pt-BR": "Marca", "en-US": "Brand" },
      }),
      makeRow({
        key: "model",
        type: "string",
        required: true,
        labelByLocale: { "pt-BR": "Modelo", "en-US": "Model" },
      }),
      makeRow({
        key: "year",
        type: "number",
        required: false,
        labelByLocale: { "pt-BR": "Ano", "en-US": "Year" },
      }),
      makeRow({
        key: "plate",
        type: "string",
        required: false,
        labelByLocale: { "pt-BR": "Placa", "en-US": "Plate" },
      }),
      makeRow({
        key: "km",
        type: "number",
        required: false,
        labelByLocale: { "pt-BR": "Quilometragem", "en-US": "Mileage" },
      }),
    ],
  },
  {
    key: "RECURRING_BILL",
    label: { "pt-BR": "Conta recorrente", "en-US": "Recurring bill" },
    description: {
      "pt-BR": "Campos comuns para contas/assinaturas.",
      "en-US": "Fields for bills/subscriptions.",
    },
    code: "RECURRING_BILL",
    nameByLocale: { "pt-BR": "Conta recorrente", "en-US": "Recurring bill" },
    descriptionByLocale: {
      "pt-BR": "Template para contas recorrentes.",
      "en-US": "Template for recurring bills.",
    },
    rows: [
      makeRow({
        key: "provider",
        type: "string",
        required: true,
        labelByLocale: { "pt-BR": "Fornecedor", "en-US": "Provider" },
      }),
      makeRow({
        key: "amount",
        type: "number",
        required: true,
        labelByLocale: { "pt-BR": "Valor", "en-US": "Amount" },
      }),
      makeRow({
        key: "billingDay",
        type: "number",
        required: true,
        labelByLocale: { "pt-BR": "Dia de cobrança", "en-US": "Billing day" },
      }),
      makeRow({
        key: "startsAt",
        type: "date",
        required: false,
        labelByLocale: { "pt-BR": "Início", "en-US": "Starts at" },
      }),
      makeRow({
        key: "endsAt",
        type: "date",
        required: false,
        labelByLocale: { "pt-BR": "Fim", "en-US": "Ends at" },
      }),
    ],
  },
  {
    key: "CONSUMABLE",
    label: { "pt-BR": "Consumível", "en-US": "Consumable" },
    description: {
      "pt-BR": "Campos comuns para itens consumíveis.",
      "en-US": "Fields for consumable items.",
    },
    code: "CONSUMABLE",
    nameByLocale: { "pt-BR": "Consumível", "en-US": "Consumable" },
    descriptionByLocale: {
      "pt-BR": "Template para itens consumíveis.",
      "en-US": "Template for consumable items.",
    },
    rows: [
      makeRow({
        key: "unit",
        type: "string",
        required: false,
        labelByLocale: { "pt-BR": "Unidade", "en-US": "Unit" },
      }),
      makeRow({
        key: "quantity",
        type: "number",
        required: true,
        labelByLocale: { "pt-BR": "Quantidade", "en-US": "Quantity" },
      }),
      makeRow({
        key: "minQuantity",
        type: "number",
        required: false,
        labelByLocale: { "pt-BR": "Quantidade mínima", "en-US": "Minimum quantity" },
      }),
      makeRow({
        key: "expiresAt",
        type: "date",
        required: false,
        labelByLocale: { "pt-BR": "Validade", "en-US": "Expires at" },
      }),
    ],
  },
];

export function rowsToSchema(rows: MetadataSchemaRow[]): TemplateMetadataSchema {
  const schema: TemplateMetadataSchema = {};
  for (const row of rows) {
    const key = row.key.trim();
    if (!key) continue;
    schema[key] = { type: row.type, required: row.required };
  }
  return schema;
}

export function schemaToRows(schema: TemplateMetadataSchema): MetadataSchemaRow[] {
  return Object.entries(schema).map(([key, value]) => ({
    id:
      typeof crypto !== "undefined" && "randomUUID" in crypto
        ? crypto.randomUUID()
        : `${Date.now()}_${Math.random().toString(16).slice(2)}`,
    key,
    type: value.type,
    required: Boolean(value.required),
  }));
}
