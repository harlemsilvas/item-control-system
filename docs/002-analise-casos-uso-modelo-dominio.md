# 002 - Análise dos Casos de Uso e Modelo de Domínio

**Data:** 22/01/2026  
**Status:** ✅ Aprovado  
**Contexto:** Definição do modelo de domínio baseado em casos de uso reais

---

## 📊 Casos de Uso Analisados

### Caso 1: Manutenção de Veículos (Honda CB 500X)

**Requisitos:**
- Salvar informações do veículo: nome, apelido, ano, versão
- Controlar quilometragem atual e data atual
- Registrar última troca de óleo (km e data)
- Controlar outros itens: pneus, revisão, bateria, velas
- Alertas 500 km antes OU 15 dias antes da próxima manutenção
- Análise inteligente: o que vier primeiro dispara o alerta
- Registro semanal de quilometragem
- Relatórios de manutenção e histórico
- Previsões baseadas em consumo: km/semana, km/mês
- Controle de combustível e percurso

**Padrões Identificados:**
- ✅ Controle temporal (6 meses)
- ✅ Controle por métrica (10.000 km)
- ✅ Regra composta (OR lógico)
- ✅ Histórico de eventos
- ✅ Análises preditivas
- ✅ Alertas configuráveis

---

### Caso 2: Conta de Água

**Requisitos:**
- Classificação por tags: <casa mãe>, <casa de veraneio>
- Tipo de imóvel: residencial, comercial, outros
- Registrar valor médio (~R$ 160)
- Data de vencimento mensal (dia 10)
- Histórico de pagamentos
- Alertas: 5 dias antes e 1 dia antes do vencimento
- Leitura semanal do medidor
- Previsão de gastos baseada em consumo

**Padrões Identificados:**
- ✅ Controle temporal recorrente (mensal)
- ✅ Tags flexíveis
- ✅ Medição semanal (m³)
- ✅ Cálculo de consumo médio
- ✅ Previsão de valores
- ✅ Alertas múltiplos

---

### Caso 3: Galão de Água e Botijão de Gás

**Requisitos:**
- Data da compra
- Data de início de consumo
- Histórico de compras (gera dados de consumo)
- Consumo diário, semanal, mensal
- Sugestão de compras futuras
- Alerta quando próximo do fim (baseado em consumo médio)

**Padrões Identificados:**
- ✅ Controle por consumo
- ✅ Análise preditiva
- ✅ Histórico de eventos
- ✅ Cálculo de duração média
- ✅ Alertas inteligentes

---

## 🏗️ Modelo de Domínio Proposto

### Entidades Core

#### 1. Item (Agregado Raiz)

```java
package br.com.harlemsilvas.itemcontrol.core.domain.model;

public class Item {
    private UUID id;
    private UUID userId;
    private String name;              // "Honda CB 500X"
    private String nickname;          // "Motoca" (opcional)
    private UUID categoryId;          // Categoria pai
    private String templateCode;      // VEHICLE, UTILITY_BILL, CONSUMABLE
    private ItemStatus status;        // ACTIVE, INACTIVE, ARCHIVED
    private Set<String> tags;         // ["moto", "honda"], ["casa-mae"]
    private Map<String, Object> metadata; // Dados flexíveis por template
    private Instant createdAt;
    private Instant updatedAt;
}
```

**Metadata - Exemplos por Template:**

```json
// VEHICLE
{
  "brand": "Honda",
  "model": "CB 500X",
  "year": 2020,
  "version": "ABS",
  "currentKm": 15000,
  "lastOilChangeKm": 10000,
  "lastOilChangeDate": "2025-07-15"
}

// UTILITY_BILL
{
  "billType": "WATER",
  "propertyType": "residential",
  "dueDay": 10,
  "averageValue": 160.00,
  "lastReading": 1245,
  "lastReadingDate": "2026-01-10"
}

// CONSUMABLE
{
  "consumableType": "WATER_GALLON",
  "purchaseDate": "2026-01-15",
  "startUseDate": "2026-01-15",
  "quantity": 20,
  "unit": "LITERS"
}
```

---

#### 2. Event (Histórico)

```java
package br.com.harlemsilvas.itemcontrol.core.domain.model;

public class Event {
    private UUID id;
    private UUID itemId;
    private UUID userId;
    private EventType eventType;      // MAINTENANCE, PAYMENT, MEASUREMENT, PURCHASE
    private Instant eventDate;        // Quando aconteceu
    private String description;       // "Troca de óleo", "Pagamento conta"
    private Map<String, Object> metrics; // {km: 15000, cost: 250.00}
    private Instant createdAt;
}
```

**EventType Enum:**
- `MAINTENANCE` - Manutenção realizada
- `PAYMENT` - Pagamento efetuado
- `MEASUREMENT` - Medição (km, m³, litros)
- `PURCHASE` - Compra de item consumível
- `UPDATE` - Atualização de dados

**Metrics - Exemplos:**

```json
// MAINTENANCE (Troca de óleo)
{
  "km": 15000,
  "cost": 250.00,
  "liters": 3.5,
  "oilType": "10W-40"
}

// MEASUREMENT (Leitura de água)
{
  "reading": 1252,
  "consumption": 7,  // m³ desde última leitura
  "previousReading": 1245
}

// PURCHASE (Galão de água)
{
  "quantity": 20,
  "unit": "LITERS",
  "cost": 15.00
}
```

---

#### 3. Rule (Regra de Alerta)

```java
package br.com.harlemsilvas.itemcontrol.core.domain.model;

public class Rule {
    private UUID id;
    private UUID itemId;
    private UUID userId;
    private RuleType ruleType;        // TIME_BASED, METRIC_BASED, COMPOSITE, CONSUMPTION_BASED
    private String name;              // "Alerta Troca de Óleo"
    private RuleCondition condition;  // Objeto complexo
    private AlertSettings alertSettings;
    private boolean enabled;
    private Instant createdAt;
    private Instant updatedAt;
}
```

---

#### 4. Alert (Alerta Gerado)

```java
package br.com.harlemsilvas.itemcontrol.core.domain.model;

public class Alert {
    private UUID id;
    private UUID ruleId;
    private UUID itemId;
    private UUID userId;
    private AlertType alertType;      // WARNING, URGENT, INFO
    private String title;             // "Troca de Óleo Próxima"
    private String message;           // "Faltam 450 km para próxima troca"
    private Instant triggeredAt;      // Quando foi gerado
    private Instant dueAt;            // Quando deve ser feito
    private AlertStatus status;       // PENDING, READ, DISMISSED, COMPLETED
    private int priority;             // 1-5
    private Instant createdAt;
}
```

---

### Value Objects

#### RuleCondition

```java
package br.com.harlemsilvas.itemcontrol.core.domain.model;

public class RuleCondition {
    private ConditionOperator operator;  // EQUALS, GREATER_THAN, INTERVAL, OR, AND
    private List<SubCondition> conditions;
}

public class SubCondition {
    private String metric;        // "km", "time", "consumption"
    private Number threshold;     // 10000, 6, 3
    private String unit;          // "MONTHS", "DAYS", "KM"
    private String operator;      // ">=", "<=", "INTERVAL"
    private String baseMetric;    // Campo de referência: "lastOilChangeKm"
}
```

**Exemplo - Regra Composta (Troca de Óleo):**

```json
{
  "operator": "OR",
  "conditions": [
    {
      "metric": "km",
      "threshold": 10000,
      "operator": "INTERVAL",
      "baseMetric": "lastOilChangeKm"
    },
    {
      "metric": "time",
      "threshold": 6,
      "unit": "MONTHS",
      "operator": "INTERVAL",
      "baseMetric": "lastOilChangeDate"
    }
  ]
}
```

---

#### AlertSettings

```java
package br.com.harlemsilvas.itemcontrol.core.domain.model;

public class AlertSettings {
    private List<AlertTiming> alertBefore; // ["500km", "15days"]
    private List<NotificationChannel> channels; // [EMAIL, PUSH]
    private int priority;                  // 1-5
    private String customMessage;          // Mensagem personalizada
}

public class AlertTiming {
    private int value;     // 500, 15
    private String unit;   // "KM", "DAYS"
}
```

---

### Enums

```java
// ItemStatus
public enum ItemStatus {
    ACTIVE, INACTIVE, ARCHIVED
}

// EventType
public enum EventType {
    MAINTENANCE, PAYMENT, MEASUREMENT, PURCHASE, UPDATE
}

// RuleType
public enum RuleType {
    TIME_BASED,        // Baseado em data/recorrência
    METRIC_BASED,      // Baseado em métrica única (km, m³)
    COMPOSITE,         // Combinação lógica (OR, AND)
    CONSUMPTION_BASED  // Baseado em consumo médio preditivo
}

// AlertType
public enum AlertType {
    INFO, WARNING, URGENT
}

// AlertStatus
public enum AlertStatus {
    PENDING, READ, DISMISSED, COMPLETED
}

// NotificationChannel
public enum NotificationChannel {
    EMAIL, PUSH, SMS, WHATSAPP
}
```

---

## 📐 Templates de Item

### Template: VEHICLE

```json
{
  "code": "VEHICLE",
  "name": "Veículo",
  "description": "Controle de manutenção de veículos",
  "metadata": {
    "brand": { "type": "string", "required": true, "label": "Marca" },
    "model": { "type": "string", "required": true, "label": "Modelo" },
    "year": { "type": "integer", "required": true, "label": "Ano" },
    "version": { "type": "string", "required": false, "label": "Versão" },
    "currentKm": { "type": "number", "required": true, "label": "Km Atual" },
    "lastOilChangeKm": { "type": "number", "required": false, "label": "Km Última Troca Óleo" },
    "lastOilChangeDate": { "type": "date", "required": false, "label": "Data Última Troca Óleo" }
  },
  "defaultRules": [
    {
      "type": "COMPOSITE",
      "name": "Troca de Óleo",
      "conditions": [
        { "metric": "km", "threshold": 10000, "operator": "INTERVAL", "baseMetric": "lastOilChangeKm" },
        { "metric": "time", "threshold": 6, "unit": "MONTHS", "operator": "INTERVAL", "baseMetric": "lastOilChangeDate" }
      ],
      "alertBefore": [
        { "value": 500, "unit": "KM" },
        { "value": 15, "unit": "DAYS" }
      ]
    }
  ],
  "trackableMetrics": ["km", "fuelCost", "maintenanceCost", "fuelConsumption"],
  "recommendedEvents": ["MAINTENANCE", "MEASUREMENT"]
}
```

---

### Template: UTILITY_BILL

```json
{
  "code": "UTILITY_BILL",
  "name": "Conta de Utilidade",
  "description": "Controle de contas recorrentes (água, luz, gás)",
  "metadata": {
    "billType": { "type": "string", "required": true, "label": "Tipo de Conta", "options": ["WATER", "ENERGY", "GAS"] },
    "propertyType": { "type": "string", "required": false, "label": "Tipo de Imóvel", "options": ["residential", "commercial", "other"] },
    "dueDay": { "type": "integer", "required": true, "label": "Dia de Vencimento", "min": 1, "max": 31 },
    "averageValue": { "type": "number", "required": false, "label": "Valor Médio" },
    "lastReading": { "type": "number", "required": false, "label": "Última Leitura" },
    "lastReadingDate": { "type": "date", "required": false, "label": "Data Última Leitura" }
  },
  "defaultRules": [
    {
      "type": "TIME_BASED",
      "name": "Vencimento Mensal",
      "recurrence": "MONTHLY",
      "dueDay": "{{dueDay}}",
      "alertBefore": [
        { "value": 5, "unit": "DAYS" },
        { "value": 1, "unit": "DAYS" }
      ]
    }
  ],
  "trackableMetrics": ["consumption", "cost", "reading"],
  "recommendedEvents": ["PAYMENT", "MEASUREMENT"]
}
```

---

### Template: CONSUMABLE_ITEM

```json
{
  "code": "CONSUMABLE_ITEM",
  "name": "Item Consumível",
  "description": "Controle de itens consumíveis (galão de água, botijão de gás)",
  "metadata": {
    "consumableType": { "type": "string", "required": true, "label": "Tipo", "options": ["WATER_GALLON", "GAS_CYLINDER", "OTHER"] },
    "purchaseDate": { "type": "date", "required": true, "label": "Data da Compra" },
    "startUseDate": { "type": "date", "required": false, "label": "Início do Consumo" },
    "quantity": { "type": "number", "required": false, "label": "Quantidade" },
    "unit": { "type": "string", "required": false, "label": "Unidade", "options": ["LITERS", "KG", "UNITS"] }
  },
  "defaultRules": [
    {
      "type": "CONSUMPTION_BASED",
      "name": "Previsão de Reposição",
      "predictiveWindow": 7,
      "alertBefore": [
        { "value": 3, "unit": "DAYS" },
        { "value": 1, "unit": "DAYS" }
      ]
    }
  ],
  "trackableMetrics": ["dailyConsumption", "weeklyConsumption", "cost"],
  "recommendedEvents": ["PURCHASE"]
}
```

---

## 🎯 Casos de Uso (Use Cases)

### 1. Gestão de Itens

```java
// Create
CreateItemUseCase(userId, templateCode, name, metadata, tags)

// Read
GetItemByIdUseCase(itemId)
GetItemsByUserUseCase(userId, filters)
GetItemWithHistoryUseCase(itemId)

// Update
UpdateItemMetadataUseCase(itemId, metadata)
UpdateItemMetricUseCase(itemId, metricName, value)  // Ex: atualizar KM
UpdateItemStatusUseCase(itemId, status)

// Delete
ArchiveItemUseCase(itemId)
```

---

### 2. Gestão de Eventos

```java
RegisterEventUseCase(itemId, eventType, eventDate, description, metrics)
GetEventHistoryUseCase(itemId, filters)
GetEventsByTypeUseCase(itemId, eventType)
GetEventStatisticsUseCase(itemId, metricName, period)  // Média, soma, projeção
DeleteEventUseCase(eventId)
```

---

### 3. Motor de Regras

```java
// Evaluation
EvaluateAllRulesUseCase()            // Chamado pelo Scheduler
EvaluateItemRulesUseCase(itemId)     // Avaliar regras de um item específico

// Management
CreateRuleFromTemplateUseCase(itemId, templateRuleId)
CreateCustomRuleUseCase(itemId, ruleCondition, alertSettings)
UpdateRuleUseCase(ruleId, condition, settings)
EnableDisableRuleUseCase(ruleId, enabled)
DeleteRuleUseCase(ruleId)
```

---

### 4. Gestão de Alertas

```java
GetPendingAlertsUseCase(userId)
GetAlertsByItemUseCase(itemId)
MarkAlertAsReadUseCase(alertId)
DismissAlertUseCase(alertId)
CompleteAlertUseCase(alertId)         // Marca como resolvido
DeleteOldAlertsUseCase(olderThan)     // Limpeza
```

---

### 5. Análises e Previsões

```java
CalculateAverageConsumptionUseCase(itemId, metricName, period)
PredictNextMaintenanceUseCase(itemId)
CalculateWeeklyKmAverageUseCase(itemId)
PredictBillValueUseCase(itemId)
GenerateItemReportUseCase(itemId, period)
```

---

## 🔄 Relacionamentos

```
User (1) ──┬─> (N) Item ──┬─> (N) Event
           │              ├─> (N) Rule ──> (N) Alert
           │              └─> (1) ItemTemplate (reference)
           │
           └─> (N) Category
```

---

## 📊 Estratégia de Implementação

### Fase 1: Core Essencial
- ✅ Entidades: Item, Event, Rule, Alert
- ✅ Value Objects: RuleCondition, AlertSettings
- ✅ Enums
- ✅ Ports (interfaces de repositórios)

### Fase 2: Use Cases Básicos
- ✅ CRUD de Item
- ✅ Registro de Events
- ✅ CRUD de Rule

### Fase 3: Motor de Regras
- ✅ RuleEngine com Strategy Pattern
- ✅ TimeBasedRule
- ✅ MetricBasedRule
- ✅ CompositeRule
- ✅ ConsumptionBasedRule

### Fase 4: Análises
- ✅ Cálculo de médias
- ✅ Previsões simples
- ✅ Relatórios

---

**Próximo:** [003-roadmap-implementacao.md](003-roadmap-implementacao.md)
