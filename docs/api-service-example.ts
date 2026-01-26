// 🚀 API Service - Item Control System
// Copie este arquivo para: frontend/src/services/api.ts

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api/v1';

// ============================================
// 📦 ITEMS
// ============================================

export interface Item {
  id: string;
  userId: string;
  name: string;
  nickname: string;
  categoryId: string;
  templateCode: string;
  status: string;
  tags: string[];
  metadata: Record<string, any>;
  createdAt: string;
  updatedAt: string;
}

export async function getItems(userId?: string): Promise<Item[]> {
  const url = userId
    ? `${API_URL}/items?userId=${userId}`
    : `${API_URL}/items`;

  const response = await fetch(url);
  if (!response.ok) throw new Error('Failed to fetch items');
  return response.json();
}

export async function getActiveItems(userId?: string): Promise<Item[]> {
  const url = userId
    ? `${API_URL}/items/active?userId=${userId}`
    : `${API_URL}/items/active`;

  const response = await fetch(url);
  if (!response.ok) throw new Error('Failed to fetch active items');
  return response.json();
}

export async function getItemById(id: string): Promise<Item> {
  const response = await fetch(`${API_URL}/items/${id}`);
  if (!response.ok) throw new Error('Failed to fetch item');
  return response.json();
}

export async function createItem(data: {
  userId: string;
  name: string;
  nickname: string;
  categoryId: string;
  templateCode: string;
  tags?: string[];
  metadata?: Record<string, any>;
}): Promise<Item> {
  const response = await fetch(`${API_URL}/items`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data),
  });

  if (!response.ok) throw new Error('Failed to create item');
  return response.json();
}

// ============================================
// 🔔 ALERTS
// ============================================

export interface Alert {
  id: string;
  ruleId?: string;
  itemId: string;
  userId: string;
  alertType: 'INFO' | 'WARNING' | 'URGENT';
  title: string;
  message: string;
  triggeredAt: string;
  dueAt?: string;
  status: 'PENDING' | 'ACKNOWLEDGED' | 'RESOLVED';
  priority: number;
  createdAt: string;
  readAt?: string;
  completedAt?: string;
}

export async function getPendingAlerts(userId?: string): Promise<Alert[]> {
  const url = userId
    ? `${API_URL}/alerts/pending?userId=${userId}`
    : `${API_URL}/alerts/pending`;

  const response = await fetch(url);
  if (!response.ok) throw new Error('Failed to fetch pending alerts');
  return response.json();
}

export async function getAlertsByStatus(
  status: 'PENDING' | 'ACKNOWLEDGED' | 'RESOLVED',
  userId?: string
): Promise<Alert[]> {
  const url = userId
    ? `${API_URL}/alerts?status=${status}&userId=${userId}`
    : `${API_URL}/alerts?status=${status}`;

  const response = await fetch(url);
  if (!response.ok) throw new Error('Failed to fetch alerts');
  return response.json();
}

export async function countPendingAlerts(userId?: string): Promise<number> {
  const url = userId
    ? `${API_URL}/alerts/count?userId=${userId}`
    : `${API_URL}/alerts/count`;

  const response = await fetch(url);
  if (!response.ok) throw new Error('Failed to count alerts');
  return response.json();
}

export async function acknowledgeAlert(id: string, userId?: string): Promise<Alert> {
  const url = userId
    ? `${API_URL}/alerts/${id}/acknowledge?userId=${userId}`
    : `${API_URL}/alerts/${id}/acknowledge`;

  const response = await fetch(url, { method: 'PUT' });
  if (!response.ok) throw new Error('Failed to acknowledge alert');
  return response.json();
}

export async function resolveAlert(id: string, userId?: string): Promise<Alert> {
  const url = userId
    ? `${API_URL}/alerts/${id}/resolve?userId=${userId}`
    : `${API_URL}/alerts/${id}/resolve`;

  const response = await fetch(url, { method: 'PUT' });
  if (!response.ok) throw new Error('Failed to resolve alert');
  return response.json();
}

// ============================================
// 📅 EVENTS
// ============================================

export interface Event {
  id: string;
  itemId: string;
  userId: string;
  eventType: string;
  eventDate: string;
  description: string;
  metrics: Record<string, any>;
  createdAt: string;
}

export async function getEventHistory(itemId: string): Promise<Event[]> {
  const response = await fetch(`${API_URL}/events?itemId=${itemId}`);
  if (!response.ok) throw new Error('Failed to fetch event history');
  return response.json();
}

export async function getRecentEvents(itemId: string, limit: number = 10): Promise<Event[]> {
  const response = await fetch(`${API_URL}/events/recent?itemId=${itemId}&limit=${limit}`);
  if (!response.ok) throw new Error('Failed to fetch recent events');
  return response.json();
}

export async function registerEvent(data: {
  itemId: string;
  userId: string;
  eventType: string;
  eventDate: string;
  description: string;
  metrics?: Record<string, any>;
}): Promise<Event> {
  const response = await fetch(`${API_URL}/events`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data),
  });

  if (!response.ok) throw new Error('Failed to register event');
  return response.json();
}

// ============================================
// 📁 CATEGORIES
// ============================================

export interface Category {
  id: string;
  name: string;
  description?: string;
  color?: string;
  icon?: string;
  createdAt: string;
  updatedAt: string;
}

export async function getCategories(): Promise<Category[]> {
  const response = await fetch(`${API_URL}/categories`);
  if (!response.ok) throw new Error('Failed to fetch categories');
  return response.json();
}

export async function getCategoryById(id: string): Promise<Category> {
  const response = await fetch(`${API_URL}/categories/${id}`);
  if (!response.ok) throw new Error('Failed to fetch category');
  return response.json();
}

export async function createCategory(data: {
  name: string;
  description?: string;
  color?: string;
  icon?: string;
}): Promise<Category> {
  const response = await fetch(`${API_URL}/categories`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data),
  });

  if (!response.ok) throw new Error('Failed to create category');
  return response.json();
}

// ============================================
// 🏥 HEALTH CHECK
// ============================================

export async function healthCheck(): Promise<{ status: string }> {
  const response = await fetch('http://localhost:8080/actuator/health');
  if (!response.ok) throw new Error('Health check failed');
  return response.json();
}

// ============================================
// 📊 DASHBOARD (agregados)
// ============================================

export async function getDashboardData(userId?: string) {
  const [items, alerts, categories] = await Promise.all([
    getActiveItems(userId),
    getPendingAlerts(userId),
    getCategories(),
  ]);

  return {
    totalItems: items.length,
    totalAlerts: alerts.length,
    totalCategories: categories.length,
    items,
    alerts,
    categories,
  };
}

// ============================================
// 🎯 EXEMPLO DE USO
// ============================================

/*
// Em um componente React:

import { getItems, getPendingAlerts, getDashboardData } from './services/api';

function Dashboard() {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function loadData() {
      try {
        const dashboardData = await getDashboardData();
        setData(dashboardData);
      } catch (error) {
        console.error('Erro ao carregar dados:', error);
      } finally {
        setLoading(false);
      }
    }

    loadData();
  }, []);

  if (loading) return <div>Carregando...</div>;

  return (
    <div>
      <h1>Dashboard</h1>
      <p>Items Ativos: {data.totalItems}</p>
      <p>Alertas Pendentes: {data.totalAlerts}</p>
      <p>Categorias: {data.totalCategories}</p>
    </div>
  );
}
*/
