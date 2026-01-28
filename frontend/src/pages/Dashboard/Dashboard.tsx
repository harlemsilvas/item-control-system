import { useQuery } from "@tanstack/react-query";
import { itemsApi, alertsApi } from "../../api/services";
import { Package, Bell, Calendar, TrendingUp } from "lucide-react";
import { DEMO_USER_ID } from "../../config/demoUser";
import { api } from "../../api/client";

export function Dashboard() {
  const {
    data: items = [],
    isLoading: itemsLoading,
    isError: itemsIsError,
    error: itemsError,
  } = useQuery({
    queryKey: ["items", DEMO_USER_ID],
    queryFn: () => itemsApi.getByUser(DEMO_USER_ID),
  });

  const {
    data: alerts = [],
    isLoading: alertsLoading,
    isError: alertsIsError,
    error: alertsError,
  } = useQuery({
    queryKey: ["alerts", "pending"],
    queryFn: () => alertsApi.getPending(DEMO_USER_ID),
  });

  const activeItems = items.filter((item) => item.status === "ACTIVE").length;
  const pendingAlerts = alerts.filter(
    (alert) => alert.status === "PENDING",
  ).length;

  const stats = [
    {
      name: "Total de Items",
      value: items.length,
      icon: Package,
      color: "text-blue-600",
      bgColor: "bg-blue-50",
    },
    {
      name: "Items Ativos",
      value: activeItems,
      icon: TrendingUp,
      color: "text-green-600",
      bgColor: "bg-green-50",
    },
    {
      name: "Alertas Pendentes",
      value: pendingAlerts,
      icon: Bell,
      color: "text-yellow-600",
      bgColor: "bg-yellow-50",
    },
    {
      name: "Eventos do Mês",
      value: 0,
      icon: Calendar,
      color: "text-purple-600",
      bgColor: "bg-purple-50",
    },
  ];

  if (itemsLoading || alertsLoading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-gray-500">Carregando...</div>
      </div>
    );
  }

  if (itemsIsError || alertsIsError) {
    const itemsMessage =
      itemsError instanceof Error
        ? itemsError.message
        : "Erro desconhecido ao buscar items";
    const alertsMessage =
      alertsError instanceof Error
        ? alertsError.message
        : "Erro desconhecido ao buscar alertas";

    return (
      <div className="card">
        <h2 className="text-lg font-semibold text-gray-900 mb-2">
          Falha ao carregar dados
        </h2>
        <div className="space-y-2 text-sm text-gray-600">
          {itemsIsError && <p>Items: {itemsMessage}</p>}
          {alertsIsError && <p>Alertas: {alertsMessage}</p>}
        </div>
        <p className="text-xs text-gray-500 mt-3">
          API baseURL atual: {String(api.defaults.baseURL ?? "(vazio)")}
        </p>
        <div className="text-xs text-gray-500 mt-3 space-y-1">
          <p>Teste rápido no navegador (deve abrir um JSON):</p>
          <p>
            <a
              className="underline"
              href={`/api/v1/items?userId=${encodeURIComponent(DEMO_USER_ID)}`}
              target="_blank"
              rel="noreferrer"
            >
              /api/v1/items?userId={DEMO_USER_ID}
            </a>
          </p>
          <p>
            <a
              className="underline"
              href={`/api/v1/alerts/pending?userId=${encodeURIComponent(DEMO_USER_ID)}`}
              target="_blank"
              rel="noreferrer"
            >
              /api/v1/alerts/pending?userId={DEMO_USER_ID}
            </a>
          </p>
        </div>
        <p className="text-xs text-gray-500 mt-3">
          Dica: no desenvolvimento, prefira{" "}
          <span className="font-medium">/api/v1</span>
          (proxy do Vite) e o backend em http://localhost:8080.
        </p>
      </div>
    );
  }

  return (
    <div>
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900">Dashboard</h1>
        <p className="text-gray-600 mt-2">Bem-vindo ao Item Control System</p>
      </div>

      {/* Stats Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        {stats.map((stat) => {
          const Icon = stat.icon;
          return (
            <div key={stat.name} className="card">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-gray-600">
                    {stat.name}
                  </p>
                  <p className="text-3xl font-bold text-gray-900 mt-2">
                    {stat.value}
                  </p>
                </div>
                <div className={`${stat.bgColor} p-3 rounded-lg`}>
                  <Icon className={`h-6 w-6 ${stat.color}`} />
                </div>
              </div>
            </div>
          );
        })}
      </div>

      {/* Recent Items */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="card">
          <h2 className="text-lg font-semibold text-gray-900 mb-4">
            Items Recentes
          </h2>
          {items.length === 0 ? (
            <div className="text-center py-8 text-gray-500">
              <Package className="h-12 w-12 mx-auto mb-3 text-gray-400" />
              <p>Nenhum item cadastrado ainda</p>
              <p className="text-sm mt-1">Comece criando seu primeiro item</p>
            </div>
          ) : (
            <div className="space-y-3">
              {items.slice(0, 5).map((item) => (
                <div
                  key={item.id}
                  className="flex items-center justify-between p-3 rounded-lg hover:bg-gray-50"
                >
                  <div>
                    <p className="font-medium text-gray-900">{item.name}</p>
                    <p className="text-sm text-gray-500">{item.nickname}</p>
                  </div>
                  <span
                    className={`px-2 py-1 text-xs font-medium rounded-full ${
                      item.status === "ACTIVE"
                        ? "bg-green-100 text-green-700"
                        : "bg-gray-100 text-gray-700"
                    }`}
                  >
                    {item.status}
                  </span>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Recent Alerts */}
        <div className="card">
          <h2 className="text-lg font-semibold text-gray-900 mb-4">
            Alertas Recentes
          </h2>
          {alerts.length === 0 ? (
            <div className="text-center py-8 text-gray-500">
              <Bell className="h-12 w-12 mx-auto mb-3 text-gray-400" />
              <p>Nenhum alerta no momento</p>
              <p className="text-sm mt-1">Você está em dia! 🎉</p>
            </div>
          ) : (
            <div className="space-y-3">
              {alerts.slice(0, 5).map((alert) => (
                <div
                  key={alert.id}
                  className="flex items-start p-3 rounded-lg hover:bg-gray-50"
                >
                  <div
                    className={`p-2 rounded-lg mr-3 ${
                      alert.type === "URGENT"
                        ? "bg-red-100"
                        : alert.type === "WARNING"
                          ? "bg-yellow-100"
                          : "bg-blue-100"
                    }`}
                  >
                    <Bell
                      className={`h-4 w-4 ${
                        alert.type === "URGENT"
                          ? "text-red-600"
                          : alert.type === "WARNING"
                            ? "text-yellow-600"
                            : "text-blue-600"
                      }`}
                    />
                  </div>
                  <div className="flex-1">
                    <p className="font-medium text-gray-900 text-sm">
                      {alert.title}
                    </p>
                    <p className="text-xs text-gray-500 mt-1">
                      {alert.message}
                    </p>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
