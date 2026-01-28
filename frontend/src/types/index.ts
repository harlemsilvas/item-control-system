export interface Item {
  id: string;
  userId: string;
  name: string;
  nickname: string;
  categoryId?: string;
  templateCode: string;
  status: ItemStatus;
  tags?: string[];
  metadata: Record<string, unknown>;
  createdAt: string;
  updatedAt: string;
}

export const ItemTemplate = {
  GENERAL: "GENERAL",
  VEHICLE: "VEHICLE",
  RECURRING_BILL: "RECURRING_BILL",
  CONSUMABLE: "CONSUMABLE",
} as const;

export type ItemTemplate = (typeof ItemTemplate)[keyof typeof ItemTemplate];

export const TemplateScope = {
  GLOBAL: "GLOBAL",
  USER: "USER",
} as const;

export type TemplateScope = (typeof TemplateScope)[keyof typeof TemplateScope];

export type TemplateLocaleMap = Record<string, string>;

export type TemplateMetadataFieldType =
  | "string"
  | "number"
  | "boolean"
  | "date";

export interface TemplateMetadataFieldSchema {
  type: TemplateMetadataFieldType;
  required?: boolean;
}

export type TemplateMetadataSchema = Record<string, TemplateMetadataFieldSchema>;

export interface Template {
  id: string;
  scope: TemplateScope;
  code: string;
  userId?: string | null;
  nameByLocale: TemplateLocaleMap;
  descriptionByLocale?: TemplateLocaleMap;
  metadataSchema: TemplateMetadataSchema;
  createdAt: string;
  updatedAt: string;
}

export interface CreateTemplateRequest {
  scope: TemplateScope;
  code: string;
  userId?: string | null;
  nameByLocale: TemplateLocaleMap;
  descriptionByLocale?: TemplateLocaleMap;
  metadataSchema: TemplateMetadataSchema;
}

export interface UpdateTemplateRequest {
  nameByLocale: TemplateLocaleMap;
  descriptionByLocale?: TemplateLocaleMap;
  metadataSchema: TemplateMetadataSchema;
}

export const ItemStatus = {
  ACTIVE: "ACTIVE",
  INACTIVE: "INACTIVE",
  ARCHIVED: "ARCHIVED",
} as const;

export type ItemStatus = (typeof ItemStatus)[keyof typeof ItemStatus];

export interface Event {
  id: string;
  itemId: string;
  type: EventType;
  description: string;
  value?: number;
  metricValue?: number;
  eventDate: string;
  metadata?: Record<string, unknown>;
  createdAt: string;
}

export const EventType = {
  MAINTENANCE: "MAINTENANCE",
  CONSUMPTION: "CONSUMPTION",
  PAYMENT: "PAYMENT",
  REPLACEMENT: "REPLACEMENT",
  OTHER: "OTHER",
} as const;

export type EventType = (typeof EventType)[keyof typeof EventType];

export interface Alert {
  id: string;
  itemId: string;
  userId: string;
  ruleId?: string;
  type: AlertType;
  status: AlertStatus;
  title: string;
  message: string;
  scheduledFor: string;
  isRead: boolean;
  createdAt: string;
}

export const AlertType = {
  INFO: "INFO",
  WARNING: "WARNING",
  URGENT: "URGENT",
} as const;

export type AlertType = (typeof AlertType)[keyof typeof AlertType];

export const AlertStatus = {
  PENDING: "PENDING",
  READ: "READ",
  DISMISSED: "DISMISSED",
  COMPLETED: "COMPLETED",
} as const;

export type AlertStatus = (typeof AlertStatus)[keyof typeof AlertStatus];

export interface Category {
  id: string;
  userId: string;
  name: string;
  parentId?: string;
  createdAt: string;
  updatedAt?: string;
}

export interface Rule {
  id: string;
  itemId: string;
  userId: string;
  name: string;
  description?: string;
  type: RuleType;
  enabled: boolean;
  conditions: unknown;
  alertSettings: unknown;
  createdAt: string;
}

export const RuleType = {
  TIME_BASED: "TIME_BASED",
  METRIC_BASED: "METRIC_BASED",
  COMPOSITE: "COMPOSITE",
  CONSUMPTION_BASED: "CONSUMPTION_BASED",
} as const;

export type RuleType = (typeof RuleType)[keyof typeof RuleType];

// Request DTOs
export interface CreateItemRequest {
  userId: string;
  name: string;
  templateCode: string;
  nickname?: string;
  categoryId?: string;
  tags?: string[];
  metadata?: Record<string, unknown>;
}

export interface CreateEventRequest {
  itemId: string;
  type: EventType;
  description: string;
  value?: number;
  metricValue?: number;
  eventDate?: string;
  metadata?: Record<string, unknown>;
}

export interface CreateCategoryRequest {
  userId: string;
  name: string;
  parentId?: string;
}

export interface CreateAlertRequest {
  itemId: string;
  type: AlertType;
  title: string;
  message: string;
  scheduledFor?: string;
}
