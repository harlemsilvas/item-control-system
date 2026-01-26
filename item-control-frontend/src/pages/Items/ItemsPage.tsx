import { useMemo, useState } from "react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { categoriesApi, itemsApi } from "../../api/services";
import { Plus, Package } from "lucide-react";
import { Link } from "react-router-dom";
import { Modal } from "../../components/ui/Modal";
import type { CreateItemRequest } from "../../types";
import { ItemTemplate } from "../../types";
import { DEMO_USER_ID } from "../../config/demoUser";

export function ItemsPage() {
  const queryClient = useQueryClient();
  const [createOpen, setCreateOpen] = useState(false);
  const [name, setName] = useState("");
  const [nickname, setNickname] = useState("");
  const [templateCode, setTemplateCode] = useState<string>(
    ItemTemplate.GENERAL,
  );
  const [categoryId, setCategoryId] = useState<string>("");
  const [metadataText, setMetadataText] = useState<string>("{}");
  const [createError, setCreateError] = useState<string | null>(null);

  const {
    data: items = [],
    isLoading,
    isError,
    error,
  } = useQuery({
    queryKey: ["items", DEMO_USER_ID],
    queryFn: () => itemsApi.getByUser(DEMO_USER_ID),
  });

  const { data: categories = [] } = useQuery({
    queryKey: ["categories", DEMO_USER_ID],
    queryFn: () => categoriesApi.getAll(DEMO_USER_ID),
  });

  const createMutation = useMutation({
    mutationFn: async () => {
      let parsedMetadata: Record<string, unknown> | undefined;
      try {
        parsedMetadata = JSON.parse(metadataText.trim() || "{}") as Record<
          string,
          unknown
        >;
      } catch {
        throw new Error("Metadata precisa ser um JSON válido");
      }

      return itemsApi.create({
        userId: DEMO_USER_ID,
        name: name.trim(),
        nickname: nickname.trim() || undefined,
        templateCode: templateCode as CreateItemRequest["templateCode"],
        categoryId: categoryId || undefined,
        tags: [],
        metadata: parsedMetadata,
      });
    },
    onSuccess: async () => {
      await queryClient.invalidateQueries({
        queryKey: ["items", DEMO_USER_ID],
      });
      setCreateOpen(false);
      setName("");
      setNickname("");
      setTemplateCode(ItemTemplate.GENERAL);
      setCategoryId("");
      setMetadataText("{}");
      setCreateError(null);
    },
    onError: (err) => {
      setCreateError(err instanceof Error ? err.message : "Erro ao criar item");
    },
  });

  const canCreate = useMemo(() => name.trim().length > 0, [name]);

  const onOpenCreate = () => {
    setCreateError(null);
    setCreateOpen(true);
  };

  const onCreate = () => {
    if (!canCreate) {
      setCreateError("Nome é obrigatório");
      return;
    }
    createMutation.mutate();
  };

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-gray-500">Carregando items...</div>
      </div>
    );
  }

  if (isError) {
    const message =
      error instanceof Error
        ? error.message
        : "Erro desconhecido ao buscar items";
    return (
      <div className="card">
        <h2 className="text-lg font-semibold text-gray-900 mb-2">
          Falha ao carregar items
        </h2>
        <p className="text-sm text-gray-600">{message}</p>
        <p className="text-xs text-gray-500 mt-3">
          Dica: verifique se o backend está rodando em http://localhost:8080 e
          se o proxy do Vite está ativo.
        </p>
      </div>
    );
  }

  return (
    <div>
      <div className="flex justify-between items-center mb-8">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Items</h1>
          <p className="text-gray-600 mt-2">Gerencie todos os seus items</p>
        </div>
        <button
          className="btn-primary flex items-center space-x-2"
          onClick={onOpenCreate}
        >
          <Plus className="h-5 w-5" />
          <span>Novo Item</span>
        </button>
      </div>

      {items.length === 0 ? (
        <div className="card text-center py-12">
          <Package className="h-16 w-16 mx-auto text-gray-400 mb-4" />
          <h3 className="text-lg font-semibold text-gray-900 mb-2">
            Nenhum item cadastrado
          </h3>
          <p className="text-gray-600 mb-6">
            Comece criando seu primeiro item para começar a controlar
          </p>
          <button
            className="btn-primary inline-flex items-center space-x-2"
            onClick={onOpenCreate}
          >
            <Plus className="h-5 w-5" />
            <span>Criar Primeiro Item</span>
          </button>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {items.map((item) => (
            <Link
              key={item.id}
              to={`/items/${item.id}`}
              className="card hover:shadow-md transition-shadow cursor-pointer"
            >
              <div className="flex items-start justify-between mb-4">
                <div className="flex items-center space-x-3">
                  <div className="bg-primary-50 p-2 rounded-lg">
                    <Package className="h-5 w-5 text-primary-600" />
                  </div>
                  <div>
                    <h3 className="font-semibold text-gray-900">{item.name}</h3>
                    <p className="text-sm text-gray-500">{item.nickname}</p>
                  </div>
                </div>
                <span
                  className={`px-2 py-1 text-xs font-medium rounded-full ${
                    item.status === "ACTIVE"
                      ? "bg-green-100 text-green-700"
                      : item.status === "INACTIVE"
                        ? "bg-gray-100 text-gray-700"
                        : "bg-red-100 text-red-700"
                  }`}
                >
                  {item.status}
                </span>
              </div>

              <div className="flex items-center justify-between text-xs text-gray-500">
                <span className="px-2 py-1 bg-gray-100 rounded">
                  {item.templateCode}
                </span>
                <span>
                  Criado em{" "}
                  {new Date(item.createdAt).toLocaleDateString("pt-BR")}
                </span>
              </div>
            </Link>
          ))}
        </div>
      )}

      <Modal
        title="Novo Item"
        isOpen={createOpen}
        onClose={() => setCreateOpen(false)}
      >
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
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Apelido (nickname)
            </label>
            <input
              className="input"
              value={nickname}
              onChange={(e) => setNickname(e.target.value)}
            />
            <p className="text-xs text-gray-500 mt-1">Opcional</p>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Template
            </label>
            <select
              className="input"
              value={templateCode}
              onChange={(e) => setTemplateCode(e.target.value)}
            >
              {Object.values(ItemTemplate).map((t) => (
                <option key={t} value={t}>
                  {t}
                </option>
              ))}
            </select>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Categoria
            </label>
            <select
              className="input"
              value={categoryId}
              onChange={(e) => setCategoryId(e.target.value)}
            >
              <option value="">Sem categoria</option>
              {categories.map((c) => (
                <option key={c.id} value={c.id}>
                  {c.name}
                </option>
              ))}
            </select>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Metadata (JSON)
            </label>
            <textarea
              className="input font-mono text-sm min-h-[160px]"
              value={metadataText}
              onChange={(e) => setMetadataText(e.target.value)}
            />
          </div>

          {createError && <p className="text-sm text-red-600">{createError}</p>}

          <div className="flex items-center justify-end gap-2">
            <button
              className="btn-secondary"
              type="button"
              onClick={() => setCreateOpen(false)}
              disabled={createMutation.isPending}
            >
              Cancelar
            </button>
            <button
              className="btn-primary"
              type="button"
              onClick={onCreate}
              disabled={!canCreate || createMutation.isPending}
            >
              {createMutation.isPending ? "Criando..." : "Criar"}
            </button>
          </div>
        </div>
      </Modal>
    </div>
  );
}
