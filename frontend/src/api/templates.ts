import { api } from "./client";
import type {
  CreateTemplateRequest,
  Template,
  TemplateScope,
  UpdateTemplateRequest,
} from "../types";

export const templatesApi = {
  listAvailable: async (userId?: string): Promise<Template[]> => {
    const { data } = await api.get("/templates", {
      params: userId ? { userId } : undefined,
    });
    return data;
  },

  listGlobal: async (): Promise<Template[]> => {
    const { data } = await api.get("/templates/global");
    return data;
  },

  listUser: async (userId: string): Promise<Template[]> => {
    const { data } = await api.get(`/templates/user/${userId}`);
    return data;
  },

  getById: async (id: string): Promise<Template> => {
    const { data } = await api.get(`/templates/${id}`);
    return data;
  },

  create: async (payload: CreateTemplateRequest): Promise<Template> => {
    const normalized: CreateTemplateRequest = {
      scope: payload.scope as TemplateScope,
      code: payload.code.trim(),
      userId: payload.userId ?? null,
      nameByLocale: payload.nameByLocale,
      descriptionByLocale: payload.descriptionByLocale,
      metadataSchema: payload.metadataSchema,
    };

    if (normalized.scope === "GLOBAL") {
      // backend expects null/omitted for GLOBAL
      delete (normalized as { userId?: string | null }).userId;
    }

    const { data } = await api.post("/templates", normalized);
    return data;
  },

  update: async (id: string, payload: UpdateTemplateRequest): Promise<Template> => {
    const { data } = await api.put(`/templates/${id}`, payload);
    return data;
  },

  delete: async (id: string): Promise<void> => {
    await api.delete(`/templates/${id}`);
  },
};
