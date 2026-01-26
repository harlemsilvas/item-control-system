import { useEffect, useMemo, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { categoriesApi, itemsApi } from "../../api/services";
import type { Category, Item } from "../../types";
import { DEMO_USER_ID } from "../../config/demoUser";

export default function ItemDetailsPage() {
  const { id } = useParams();
  const itemId = id ?? "";
  const queryClient = useQueryClient();

  const {
    data: item,
    isLoading,
    isError,
    error,
  } = useQuery<Item>({
    queryKey: ["items", itemId],
    queryFn: () => itemsApi.getById(itemId),
    enabled: Boolean(itemId),
  });

  const { data: categories = [] } = useQuery<Category[]>({
    queryKey: ["categories", DEMO_USER_ID],
    queryFn: () => categoriesApi.getAll(DEMO_USER_ID),
  });

  const [metadataText, setMetadataText] = useState<string>("{}");
  const [formError, setFormError] = useState<string | null>(null);

  useEffect(() => {
    if (!item) return;
    setMetadataText(JSON.stringify(item.metadata ?? {}, null, 2));
  }, [item]);

  const updateMutation = useMutation({
    mutationFn: async (metadata: Record<string, unknown>) =>
      itemsApi.updateMetadata(itemId, metadata),
    onSuccess: async () => {
      await queryClient.invalidateQueries({ queryKey: ["items", itemId] });
      await queryClient.invalidateQueries({ queryKey: ["items"] });
    },
  });

  const categoryName = useMemo(() => {
    if (!item?.categoryId) return "Sem";
    return (
      categories.find((c) => c.id === item.categoryId)?.name || item.categoryId
    );
  }, [categories, item?.categoryId]);

  const onSave = () => {
    if (!itemId) return;

    let parsedMetadata: Record<string, unknown>;
    try {
      parsedMetadata = JSON.parse(metadataText.trim() || "{}") as Record<
        string,
        unknown
      >;
    } catch {
      setFormError("Metadata precisa ser um JSON válido");
      return;
    }

    setFormError(null);
    updateMutation.mutate(parsedMetadata);
  };

  if (!itemId) {
    return (
      <div className="card">
        <h2 className="text-lg font-semibold text-gray-900 mb-2">
          Item inválido
        </h2>
        <p className="text-sm text-gray-600">URL sem id do item.</p>
        <div className="mt-4">
          <Link className="text-blue-600 hover:underline" to="/items">
            Voltar para Items
          </Link>
        </div>
      </div>
    );
  }

  if (isLoading) {
    return <div className="card">Carregando item...</div>;
  }

  if (isError) {
    const message =
      error instanceof Error ? error.message : "Erro ao carregar item";
    return (
      <div className="card">
        <h2 className="text-lg font-semibold text-gray-900 mb-2">
          Falha ao carregar item
        </h2>
        <p className="text-sm text-gray-600">{message}</p>
        <div className="mt-4">
          <Link className="text-blue-600 hover:underline" to="/items">
            Voltar para Items
          </Link>
        </div>
      </div>
    );
  }

  if (!item) return null;

  return (
    <div className="space-y-6">
      <div>
        <Link className="text-sm text-blue-600 hover:underline" to="/items">
          ← Voltar para Items
        </Link>
        <h1 className="text-3xl font-bold text-gray-900 mt-2">{item.name}</h1>
        {item.nickname ? (
          <p className="text-gray-600 mt-1">{item.nickname}</p>
        ) : null}
        <p className="text-xs text-gray-500 mt-2">
          Observação: a API atual só expõe atualização de{" "}
          <span className="font-medium">metadata</span> para items.
        </p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="card lg:col-span-2">
          <h2 className="text-lg font-semibold text-gray-900 mb-4">
            Editar metadata
          </h2>

          <div className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Metadata (JSON)
              </label>
              <textarea
                className="input font-mono text-sm min-h-[240px]"
                value={metadataText}
                onChange={(e) => setMetadataText(e.target.value)}
              />
            </div>

            {formError && <p className="text-sm text-red-600">{formError}</p>}
            {updateMutation.isError && (
              <p className="text-sm text-red-600">
                {updateMutation.error instanceof Error
                  ? updateMutation.error.message
                  : "Erro ao salvar metadata"}
              </p>
            )}

            <div className="flex items-center justify-end">
              <button
                className="btn-primary"
                type="button"
                onClick={onSave}
                disabled={updateMutation.isPending}
              >
                {updateMutation.isPending ? "Salvando..." : "Salvar"}
              </button>
            </div>
          </div>
        </div>

        <div className="space-y-6">
          <div className="card">
            <h2 className="text-lg font-semibold text-gray-900 mb-4">
              Detalhes
            </h2>
            <div className="space-y-2 text-sm text-gray-700">
              <p>
                <span className="font-medium">Status:</span> {item.status}
              </p>
              <p>
                <span className="font-medium">Template:</span>{" "}
                {item.templateCode}
              </p>
              <p>
                <span className="font-medium">Categoria:</span> {categoryName}
              </p>
              <p>
                <span className="font-medium">Criado em:</span>{" "}
                {new Date(item.createdAt).toLocaleString("pt-BR")}
              </p>
              <p>
                <span className="font-medium">Atualizado em:</span>{" "}
                {new Date(item.updatedAt).toLocaleString("pt-BR")}
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
