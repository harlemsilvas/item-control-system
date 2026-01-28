import { useMemo, useState } from "react";
import { useQuery } from "@tanstack/react-query";
import { Link } from "react-router-dom";
import { Plus, FileText, Globe, User } from "lucide-react";
import { templatesApi } from "../../api/templates";
import type { Template, TemplateScope } from "../../types";
import { DEMO_USER_ID } from "../../config/demoUser";
import { InlineCode } from "../../components/ui/InlineCode";

function scopeLabel(scope: TemplateScope) {
  return scope === "GLOBAL" ? "GLOBAL" : "USER";
}

function scopeBadgeClass(scope: TemplateScope) {
  return scope === "GLOBAL"
    ? "bg-blue-100 text-blue-700"
    : "bg-purple-100 text-purple-700";
}

export function TemplatesPage() {
  const [onlyScope, setOnlyScope] = useState<"ALL" | TemplateScope>("ALL");

  const {
    data: templates = [],
    isLoading,
    isError,
    error,
  } = useQuery({
    queryKey: ["templates", DEMO_USER_ID],
    queryFn: () => templatesApi.listAvailable(DEMO_USER_ID),
  });

  const filtered = useMemo(() => {
    if (onlyScope === "ALL") return templates;
    return templates.filter((t) => t.scope === onlyScope);
  }, [onlyScope, templates]);

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-gray-500">Carregando templates...</div>
      </div>
    );
  }

  if (isError) {
    const message =
      error instanceof Error ? error.message : "Erro ao carregar templates";
    return (
      <div className="card">
        <h2 className="text-lg font-semibold text-gray-900 mb-2">
          Falha ao carregar templates
        </h2>
        <p className="text-sm text-gray-600">{message}</p>
        <p className="text-xs text-gray-500 mt-3">
          Dica: confirme que o backend está rodando em{" "}
          <InlineCode>http://localhost:8080</InlineCode>.
        </p>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Templates</h1>
          <p className="text-gray-600 mt-2">
            Crie e gerencie templates (GLOBAL e USER) para guiar a metadata dos
            items.
          </p>
        </div>

        <Link
          className="btn-primary inline-flex items-center gap-2"
          to="/templates/new"
        >
          <Plus className="h-5 w-5" />
          Novo Template
        </Link>
      </div>

      <div className="card">
        <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
          <div className="flex items-center gap-2 text-sm text-gray-700">
            <span className="font-medium">Disponíveis para:</span>
            <InlineCode>{DEMO_USER_ID}</InlineCode>
          </div>

          <div className="flex items-center gap-2">
            <button
              type="button"
              className={
                onlyScope === "ALL" ? "btn-primary" : "btn-secondary"
              }
              onClick={() => setOnlyScope("ALL")}
            >
              Todos
            </button>
            <button
              type="button"
              className={
                onlyScope === "GLOBAL" ? "btn-primary" : "btn-secondary"
              }
              onClick={() => setOnlyScope("GLOBAL")}
            >
              <span className="inline-flex items-center gap-1">
                <Globe className="h-4 w-4" /> GLOBAL
              </span>
            </button>
            <button
              type="button"
              className={
                onlyScope === "USER" ? "btn-primary" : "btn-secondary"
              }
              onClick={() => setOnlyScope("USER")}
            >
              <span className="inline-flex items-center gap-1">
                <User className="h-4 w-4" /> USER
              </span>
            </button>
          </div>
        </div>
      </div>

      {filtered.length === 0 ? (
        <div className="card text-center py-12">
          <FileText className="h-16 w-16 mx-auto text-gray-400 mb-4" />
          <h3 className="text-lg font-semibold text-gray-900 mb-2">
            Nenhum template encontrado
          </h3>
          <p className="text-gray-600 mb-6">
            Crie um template para começar a padronizar a metadata.
          </p>
          <Link className="btn-primary inline-flex items-center gap-2" to="/templates/new">
            <Plus className="h-5 w-5" /> Criar Template
          </Link>
        </div>
      ) : (
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {filtered.map((template) => (
            <TemplateCard key={template.id} template={template} />
          ))}
        </div>
      )}
    </div>
  );
}

function TemplateCard({ template }: { template: Template }) {
  const displayName = template.nameByLocale["pt-BR"] ??
    template.nameByLocale["en-US"] ??
    Object.values(template.nameByLocale)[0] ??
    template.code;

  const description =
    template.descriptionByLocale?.["pt-BR"] ??
    template.descriptionByLocale?.["en-US"] ??
    (template.descriptionByLocale ? Object.values(template.descriptionByLocale)[0] : undefined);

  return (
    <div className="card">
      <div className="flex items-start justify-between gap-4">
        <div className="min-w-0">
          <h3 className="font-semibold text-gray-900 truncate">{displayName}</h3>
          <div className="mt-1 flex flex-wrap items-center gap-2 text-xs text-gray-500">
            <InlineCode>{template.code}</InlineCode>
            <span
              className={`px-2 py-1 rounded-full font-medium ${scopeBadgeClass(template.scope)}`}
            >
              {scopeLabel(template.scope)}
            </span>
            {template.scope === "USER" && template.userId ? (
              <span className="text-gray-500">userId: {template.userId}</span>
            ) : null}
          </div>
          {description ? (
            <p className="text-sm text-gray-600 mt-3 line-clamp-2">
              {description}
            </p>
          ) : null}
        </div>

        <div className="flex flex-col gap-2 shrink-0">
          <Link className="btn-secondary" to={`/templates/${template.id}`}>
            Ver
          </Link>
          <Link className="btn-secondary" to={`/templates/${template.id}/edit`}>
            Editar
          </Link>
        </div>
      </div>

      <div className="mt-4 text-xs text-gray-500">
        Campos: <span className="font-medium">{Object.keys(template.metadataSchema ?? {}).length}</span>
      </div>
    </div>
  );
}
