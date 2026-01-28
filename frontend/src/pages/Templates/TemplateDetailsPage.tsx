import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { Link, useNavigate, useParams } from "react-router-dom";
import { templatesApi } from "../../api/templates";
import { InlineCode } from "../../components/ui/InlineCode";

export function TemplateDetailsPage() {
  const { id } = useParams();
  const templateId = id ?? "";
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const {
    data: template,
    isLoading,
    isError,
    error,
  } = useQuery({
    queryKey: ["template", templateId],
    queryFn: () => templatesApi.getById(templateId),
    enabled: Boolean(templateId),
  });

  const deleteMutation = useMutation({
    mutationFn: () => templatesApi.delete(templateId),
    onSuccess: async () => {
      await queryClient.invalidateQueries({ queryKey: ["templates"] });
      navigate("/templates");
    },
  });

  if (!templateId) {
    return (
      <div className="card">
        <h2 className="text-lg font-semibold text-gray-900 mb-2">
          Template inválido
        </h2>
        <p className="text-sm text-gray-600">URL sem id do template.</p>
        <div className="mt-4">
          <Link className="text-blue-600 hover:underline" to="/templates">
            Voltar para Templates
          </Link>
        </div>
      </div>
    );
  }

  if (isLoading) return <div className="card">Carregando template...</div>;

  if (isError) {
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

  if (!template) return null;

  const displayName =
    template.nameByLocale["pt-BR"] ??
    template.nameByLocale["en-US"] ??
    Object.values(template.nameByLocale)[0] ??
    template.code;

  return (
    <div className="space-y-6">
      <div className="flex items-start justify-between gap-4">
        <div>
          <Link className="text-sm text-blue-600 hover:underline" to="/templates">
            ← Voltar para Templates
          </Link>
          <h1 className="text-3xl font-bold text-gray-900 mt-2">{displayName}</h1>
          <div className="mt-2 flex flex-wrap items-center gap-2 text-sm text-gray-600">
            <span>code:</span> <InlineCode>{template.code}</InlineCode>
            <span>scope:</span> <InlineCode>{template.scope}</InlineCode>
            {template.scope === "USER" && template.userId ? (
              <>
                <span>userId:</span> <InlineCode>{template.userId}</InlineCode>
              </>
            ) : null}
          </div>
        </div>

        <div className="flex flex-col sm:flex-row gap-2">
          <Link className="btn-secondary" to={`/templates/${template.id}/edit`}>
            Editar
          </Link>
          <button
            type="button"
            className="btn-secondary"
            onClick={() => {
              const ok = window.confirm("Deletar este template?");
              if (!ok) return;
              deleteMutation.mutate();
            }}
            disabled={deleteMutation.isPending}
          >
            {deleteMutation.isPending ? "Deletando..." : "Deletar"}
          </button>
        </div>
      </div>

      {deleteMutation.isError ? (
        <div className="card border-red-200">
          <p className="text-sm text-red-700">
            {deleteMutation.error instanceof Error
              ? deleteMutation.error.message
              : "Erro ao deletar"}
          </p>
        </div>
      ) : null}

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="card">
          <h2 className="text-lg font-semibold text-gray-900 mb-4">
            Locales
          </h2>

          <div className="space-y-4">
            <div>
              <h3 className="text-sm font-medium text-gray-700">nameByLocale</h3>
              <pre className="mt-2 text-xs bg-gray-50 border border-gray-200 rounded p-3 overflow-auto">
{JSON.stringify(template.nameByLocale, null, 2)}
              </pre>
            </div>

            <div>
              <h3 className="text-sm font-medium text-gray-700">
                descriptionByLocale
              </h3>
              <pre className="mt-2 text-xs bg-gray-50 border border-gray-200 rounded p-3 overflow-auto">
{JSON.stringify(template.descriptionByLocale ?? {}, null, 2)}
              </pre>
            </div>
          </div>
        </div>

        <div className="card">
          <h2 className="text-lg font-semibold text-gray-900 mb-4">
            metadataSchema
          </h2>
          <pre className="text-xs bg-gray-50 border border-gray-200 rounded p-3 overflow-auto min-h-[200px]">
{JSON.stringify(template.metadataSchema ?? {}, null, 2)}
          </pre>
        </div>
      </div>
    </div>
  );
}
