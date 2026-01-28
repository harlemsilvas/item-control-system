import { api } from "./client";
import type {
  Item,
  CreateItemRequest,
  Event,
  CreateEventRequest,
  Alert,
  Category,
  CreateCategoryRequest,
} from "../types";

// Items API
export const itemsApi = {
  getAll: async (): Promise<Item[]> => {
    const { data } = await api.get("/items");
    return data;
  },

  getById: async (id: string): Promise<Item> => {
    const { data } = await api.get(`/items/${id}`);
    return data;
  },

  getByUser: async (userId: string): Promise<Item[]> => {
    const { data } = await api.get(`/items`, { params: { userId } });
    return data;
  },

  create: async (item: CreateItemRequest): Promise<Item> => {
    const { data } = await api.post("/items", item);
    return data;
  },

  updateMetadata: async (
    id: string,
    metadata: Record<string, unknown>,
  ): Promise<Item> => {
    const { data } = await api.put(`/items/${id}/metadata`, metadata);
    return data;
  },
};

// Events API
export const eventsApi = {
  getByItem: async (itemId: string): Promise<Event[]> => {
    const { data } = await api.get("/events", { params: { itemId } });
    return data;
  },

  create: async (event: CreateEventRequest): Promise<Event> => {
    const { data } = await api.post("/events", event);
    return data;
  },

  delete: async (id: string): Promise<void> => {
    await api.delete(`/events/${id}`);
  },
};

// Alerts API
export const alertsApi = {
  getPending: async (userId: string): Promise<Alert[]> => {
    const { data } = await api.get("/alerts/pending", { params: { userId } });
    return data;
  },

  getByItem: async (itemId: string): Promise<Alert[]> => {
    const { data } = await api.get(`/alerts/item/${itemId}`);
    return data;
  },

  markAsRead: async (id: string): Promise<void> => {
    await api.put(`/alerts/${id}/read`);
  },

  dismiss: async (id: string): Promise<void> => {
    await api.put(`/alerts/${id}/dismiss`);
  },

  complete: async (id: string): Promise<void> => {
    await api.put(`/alerts/${id}/complete`);
  },
};

// Categories API
export const categoriesApi = {
  getAll: async (userId: string): Promise<Category[]> => {
    const { data } = await api.get("/categories", { params: { userId } });
    return data;
  },

  create: async (category: CreateCategoryRequest): Promise<Category> => {
    const { data } = await api.post("/categories", category);
    return data;
  },

  delete: async (id: string): Promise<void> => {
    await api.delete(`/categories/${id}`);
  },
};

// Health Check
export const healthApi = {
  check: async (): Promise<{ status: string }> => {
    const { data } = await api.get("/admin/database/health");
    return data;
  },
};
