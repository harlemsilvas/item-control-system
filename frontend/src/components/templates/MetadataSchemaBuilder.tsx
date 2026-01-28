import { Plus, Trash2 } from "lucide-react";
import { useMemo } from "react";
import type { TemplateMetadataFieldType } from "../../types";
import type { MetadataSchemaRow } from "./metadataSchemaUtils";

type Props = {
  value: MetadataSchemaRow[];
  onChange: (next: MetadataSchemaRow[]) => void;
  locale?: "pt-BR" | "en-US";
  fieldLabelByKey?: Record<string, string>;
};

const TYPE_OPTIONS: TemplateMetadataFieldType[] = [
  "string",
  "number",
  "boolean",
  "date",
];

function makeRow(seed?: Partial<MetadataSchemaRow>): MetadataSchemaRow {
  return {
    id: typeof crypto !== "undefined" && "randomUUID" in crypto ? crypto.randomUUID() : `${Date.now()}_${Math.random().toString(16).slice(2)}`,
    key: seed?.key ?? "",
    type: seed?.type ?? "string",
    required: seed?.required ?? false,
  };
}

export function MetadataSchemaBuilder({ value, onChange, locale = "pt-BR", fieldLabelByKey = {} }: Props) {
  const isDuplicateKey = useMemo(() => {
    const seen = new Map<string, number>();
    for (const row of value) {
      const k = row.key.trim();
      if (!k) continue;
      seen.set(k, (seen.get(k) ?? 0) + 1);
    }
    return (key: string) => (seen.get(key.trim()) ?? 0) > 1;
  }, [value]);

  const addRow = () => {
    onChange([...value, makeRow()]);
  };

  const removeRow = (index: number) => {
    onChange(value.filter((_, i) => i !== index));
  };

  const updateRow = (index: number, next: Partial<MetadataSchemaRow>) => {
    const updated = value.map((row, i) => (i === index ? { ...row, ...next } : row));
    onChange(updated);
  };

  return (
    <div className="space-y-3">
      <div className="flex items-center justify-between">
        <p className="text-sm text-gray-700 font-medium">
          {locale === "pt-BR" ? "Campos do metadataSchema" : "metadataSchema fields"}
        </p>
        <button
          type="button"
          className="btn-secondary inline-flex items-center gap-2"
          onClick={addRow}
        >
          <Plus className="h-4 w-4" />
          {locale === "pt-BR" ? "Adicionar campo" : "Add field"}
        </button>
      </div>

      {value.length === 0 ? (
        <div className="text-sm text-gray-500">
          {locale === "pt-BR"
            ? "Nenhum campo. Clique em \"Adicionar campo\"."
            : "No fields. Click \"Add field\"."}
        </div>
      ) : (
        <div className="space-y-2">
          {value.map((row, index) => {
            const duplicated = row.key.trim() ? isDuplicateKey(row.key) : false;
            const displayLabel = fieldLabelByKey[row.key] ?? row.key;

            return (
              <div
                key={row.id}
                className="grid grid-cols-1 md:grid-cols-12 gap-2 items-end border border-gray-200 rounded-lg p-3 bg-white"
              >
                <div className="md:col-span-5">
                  <label className="block text-xs font-medium text-gray-600 mb-1">
                    {locale === "pt-BR" ? "Campo (chave)" : "Field (key)"}
                  </label>

                  <div className="flex flex-col gap-1">
                    <input
                      className={`input ${duplicated ? "border-red-300" : ""}`}
                      value={row.key}
                      onChange={(e) => updateRow(index, { key: e.target.value })}
                      placeholder={
                        locale === "pt-BR"
                          ? "Ex: mes, marca, dataCompra (chave deve ser em inglês/sem espaços)"
                          : "Ex: month, brand, purchaseDate"
                      }
                    />
                    {row.key.trim() && displayLabel !== row.key ? (
                      <span className="text-[11px] text-gray-500">
                        {locale === "pt-BR" ? "Rótulo:" : "Label:"} {displayLabel}
                      </span>
                    ) : null}
                  </div>

                  {duplicated ? (
                    <p className="text-xs text-red-600 mt-1">
                      {locale === "pt-BR" ? "Chave duplicada" : "Duplicate key"}
                    </p>
                  ) : null}
                </div>

                <div className="md:col-span-4">
                  <label className="block text-xs font-medium text-gray-600 mb-1">
                    Tipo
                  </label>
                  <select
                    className="input"
                    value={row.type}
                    onChange={(e) =>
                      updateRow(index, {
                        type: e.target.value as TemplateMetadataFieldType,
                      })
                    }
                  >
                    {TYPE_OPTIONS.map((t) => (
                      <option key={t} value={t}>
                        {t}
                      </option>
                    ))}
                  </select>
                </div>

                <div className="md:col-span-2">
                  <label className="block text-xs font-medium text-gray-600 mb-1">
                    Required
                  </label>
                  <select
                    className="input"
                    value={row.required ? "true" : "false"}
                    onChange={(e) =>
                      updateRow(index, { required: e.target.value === "true" })
                    }
                  >
                    <option value="false">false</option>
                    <option value="true">true</option>
                  </select>
                </div>

                <div className="md:col-span-1 flex md:justify-end">
                  <button
                    type="button"
                    className="btn-secondary inline-flex items-center gap-2"
                    onClick={() => removeRow(index)}
                    title="Remover"
                  >
                    <Trash2 className="h-4 w-4" />
                  </button>
                </div>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
}
