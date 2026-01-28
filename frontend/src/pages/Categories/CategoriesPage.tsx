import { useMemo, useState } from "react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { categoriesApi } from "../../api/services";
import type { CreateCategoryRequest } from "../../types";
import { DEMO_USER_ID } from "../../config/demoUser";

export function CategoriesPage() {
  const queryClient = useQueryClient();

  const {
    data: categories = [],
    isLoading,
    isError,
    error,
  } = useQuery({
    queryKey: ["categories", DEMO_USER_ID],
    queryFn: () => categoriesApi.getAll(DEMO_USER_ID),
  });

  const [name, setName] = useState("");
  const [parentId, setParentId] = useState<string>("");
  const [formError, setFormError] = useState<string | null>(null);

  const createMutation = useMutation({
    mutationFn: async (payload: CreateCategoryRequest) =>
      categoriesApi.create(payload),
    onSuccess: async () => {
      await queryClient.invalidateQueries({
        queryKey: ["categories", DEMO_USER_ID],
      });
      setName("");
      setParentId("");
      setFormError(null);
    },
  });

  const deleteMutation = useMutation({
    mutationFn: async (categoryId: string) => categoriesApi.delete(categoryId),
    onSuccess: async () => {
      await queryClient.invalidateQueries({
        queryKey: ["categories", DEMO_USER_ID],
      });
    },
  });

  const canCreate = useMemo(() => name.trim().length > 0, [name]);

  const onCreate = () => {
    if (!canCreate) {
      setFormError("Nome é obrigatório");
      return;
    }

    setFormError(null);
    createMutation.mutate({
      userId: DEMO_USER_ID,
      name: name.trim(),
      parentId: parentId || undefined,
    });
  };

  const onDelete = (categoryId: string, categoryName: string) => {
    const confirmed = window.confirm(`Excluir a categoria "${categoryName}"?`);
    if (!confirmed) return;
    deleteMutation.mutate(categoryId);
  };

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-gray-500">Carregando categorias...</div>
      </div>
    );
  }

  if (isError) {
    const message =
      error instanceof Error ? error.message : "Erro ao carregar categorias";
    return (
      <div className="card">
        <h2 className="text-lg font-semibold text-gray-900 mb-2">
          Falha ao carregar categorias
        </h2>
        <p className="text-sm text-gray-600">{message}</p>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold text-gray-900">Categorias</h1>
        <p className="text-gray-600 mt-2">Crie e organize suas categorias</p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="card">
          <h2 className="text-lg font-semibold text-gray-900 mb-4">
            Nova categoria
          </h2>

          <div className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Nome
              </label>
              <input
                className="input"
                value={name}
                onChange={(e) => setName(e.target.value)}
              />
            </div>

            <div>
              <p className="text-xs text-gray-500">
                A API de categorias exige{" "}
                <span className="font-medium">userId</span>.
              </p>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Categoria pai
              </label>
              <select
                className="input"
                value={parentId}
                onChange={(e) => setParentId(e.target.value)}
              >
                <option value="">Nenhuma</option>
                {categories.map((c) => (
                  <option key={c.id} value={c.id}>
                    {c.name}
                  </option>
                ))}
              </select>
            </div>

            {formError && <p className="text-sm text-red-600">{formError}</p>}
            {createMutation.isError && (
              <p className="text-sm text-red-600">
                {createMutation.error instanceof Error
                  ? createMutation.error.message
                  : "Erro ao criar categoria"}
              </p>
            )}

            <div className="flex items-center justify-end">
              <button
                type="button"
                className="btn-primary"
                onClick={onCreate}
                disabled={!canCreate || createMutation.isPending}
              >
                {createMutation.isPending ? "Criando..." : "Criar"}
              </button>
            </div>
          </div>
        </div>

        <div className="card">
          <h2 className="text-lg font-semibold text-gray-900 mb-4">Lista</h2>

          {categories.length === 0 ? (
            <p className="text-gray-600">Nenhuma categoria cadastrada.</p>
          ) : (
            <div className="space-y-2">
              {categories.map((c) => (
                <div
                  key={c.id}
                  className="flex items-center justify-between p-3 rounded-lg border border-gray-200"
                >
                  <div>
                    <p className="font-medium text-gray-900">{c.name}</p>
                    <p className="text-xs text-gray-500">
                      {c.parentId ? `Parent: ${c.parentId}` : "Sem parent"}
                    </p>
                  </div>

                  <button
                    type="button"
                    className="btn-secondary"
                    onClick={() => onDelete(c.id, c.name)}
                    disabled={deleteMutation.isPending}
                  >
                    Excluir
                  </button>
                </div>
              ))}
            </div>
          )}

          {deleteMutation.isError && (
            <p className="text-sm text-red-600 mt-3">
              {deleteMutation.error instanceof Error
                ? deleteMutation.error.message
                : "Erro ao excluir categoria"}
            </p>
          )}
        </div>
      </div>
    </div>
  );
}
