# Projeto Modelo 3 (Escalável) — Java + MongoDB + Motor de Regras e Alertas

Este documento descreve uma proposta **Modelo 3** (Data-first + Motor de regras) usando **Java (Spring Boot)** com **MongoDB**, focado em escalabilidade, manutenção e evolução para integrações futuras (assinantes/terceiros).

---

## 1) Objetivo do Sistema

Criar um sistema de cadastro e controle de itens do dia a dia, com estrutura “pai → filhos” e múltiplos modelos de controle:

Exemplos de itens:

- Manutenção de veículos (troca de óleo, velas, pneus, revisão)
- Contas recorrentes (água, gás, energia)
- Consumo/medição (leitura de água, gasolina do mês)
- Duração estimada (resistência do chuveiro, galões de água)
- Reservas financeiras (alertar para guardar dinheiro antes de um gasto previsto)

O sistema deve:

- Armazenar registros em nuvem
- Permitir templates dinâmicos por tipo de item
- Manter histórico (eventos)
- Gerar alertas e notificações com base em regras
- Preparar a base para integrações futuras (serviços e promoções)

---

## 2) Visão de Arquitetura (alto nível)

### Componentes principais

- **API (Spring Boot)**  
  Responsável por autenticação, CRUD de entidades e consultas do app.

- **Motor de Regras e Alertas (Worker)**  
  Responsável por avaliar regras periodicamente e criar alertas.

- **MongoDB (Atlas ou local)**  
  Banco principal, armazenando templates, itens, eventos, regras e alertas.

- **Fila (RabbitMQ ou Kafka)**  
  Opcional no MVP, recomendada para escalabilidade e desacoplamento.

- **Notificações (fase 2+)**
  Push / E-mail / WhatsApp (opt-in)

---

## 3) Stack Técnica Recomendada

### Backend

- **Java 21 LTS**
- **Spring Boot 3**
- Spring Web (REST)
- Spring Validation
- Spring Security + OAuth2
- Spring Data MongoDB

### Banco de Dados

- **MongoDB** (Atlas recomendado)

### Jobs / Agendamentos

- MVP:
  - `@Scheduled` do Spring
- Escalável:
  - Quartz Scheduler
  - Worker dedicado (aplicação separada)

### Mensageria (opcional)

- **RabbitMQ** (simples e direto)
- Kafka (se virar algo maior)

### Qualidade + Observabilidade

- Testes: JUnit 5 + Mockito
- Logs: Logback (JSON opcional)
- Métricas: Micrometer + Prometheus (futuro)
- Tracing: OpenTelemetry (futuro)
- Erros: Sentry

### DevOps

- Docker + Docker Compose (dev)
- CI/CD via GitHub Actions (build + testes + imagem + deploy)

---

## 4) Fluxo de Trabalho (Workflow) Recomendado

### Metodologia

- **Scrum (sprints de 2 semanas)** para roadmap
- **Kanban dentro da sprint** (limite de WIP) para execução

### Git + Padrões de Branch

- `main` (protegida)
- `feature/*` (branches temporárias)
- Merge via Pull Request + review obrigatório

### Pipeline CI

- Build (Gradle/Maven)
- Testes
- Lint/format (opcional, mas recomendado)
- Docker build
- Deploy automático (quando estiver pronto)

### Definition of Done (DoD)

- Implementação concluída
- Testes cobrindo regra/serviço principal
- Endpoint documentado (Swagger/OpenAPI)
- Logs e erros rastreáveis
- Índices de banco revisados (quando necessário)

---

## 5) Modelagem do Domínio (conceitos)

### Entidades centrais

- **User**: usuário autenticado por login social
- **Category**: árvore de categorias (ex: Manutenção do carro → Troca de óleo)
- **ItemTemplate**: define um tipo de item e seus campos
- **Item**: instância cadastrada pelo usuário (um controle real)
- **Event**: histórico de registros (pago, trocado, leitura, manutenção)
- **Rule**: regra de alerta (tempo, vencimento, km, consumo)
- **Alert**: alerta gerado e exibido no app

---

## 6) Coleções MongoDB (recomendadas)

> Importante: **não aninhar histórico infinito** dentro do usuário.
> `events` e `alerts` devem ser coleções próprias para escalar.

### 6.1 users

```json
{
  "_id": "uuid",
  "provider": "google",
  "providerSub": "1234567890",
  "email": "user@email.com",
  "name": "Nome",
  "createdAt": "2026-01-01T10:00:00Z"
}
```

### 6.2 categories

```json
{
  "_id": "uuid",
  "userId": "uuid",
  "name": "Manutenção Veículo",
  "parentId": null,
  "createdAt": "2026-01-01T10:00:00Z"
}
```

### 6.3 item_templates

```json
{
  "_id": "uuid",
  "code": "BILL_DUE_DATE",
  "name": "Conta com Vencimento",
  "schemaJson": {
    "fields": [
      { "key": "dueDay", "type": "number", "required": true },
      { "key": "expectedValue", "type": "number", "required": false },
      { "key": "recurrence", "type": "string", "required": true }
    ]
  }
}
```

### 6.4 items

```json
{
  "_id": "uuid",
  "userId": "uuid",
  "categoryId": "uuid",
  "templateCode": "BILL_DUE_DATE",
  "title": "Conta de Água",
  "status": "ACTIVE",
  "configJson": {
    "dueDay": 10,
    "expectedValue": 160.0,
    "recurrence": "MONTHLY"
  },
  "createdAt": "2026-01-01T10:00:00Z"
}
```

### 6.5 events

```json
{
  "_id": "uuid",
  "userId": "uuid",
  "itemId": "uuid",
  "kind": "payment",
  "payloadJson": {
    "value": 173.2,
    "paidAt": "2026-01-10T10:00:00Z",
    "note": "Pagamento via app do banco"
  },
  "occurredAt": "2026-01-10T10:00:00Z",
  "createdAt": "2026-01-10T10:00:00Z"
}
```

### 6.6 rules

```json
{
  "_id": "uuid",
  "userId": "uuid",
  "itemId": "uuid",
  "ruleType": "DUE_DATE",
  "enabled": true,
  "paramsJson": {
    "notifyBeforeDays": 7,
    "notifyOnDueDay": true,
    "notifyOverdue": true
  },
  "lastEvaluatedAt": null,
  "createdAt": "2026-01-01T10:00:00Z"
}
```

### 6.7 alerts

```json
{
  "_id": "uuid",
  "userId": "uuid",
  "itemId": "uuid",
  "ruleId": "uuid",
  "severity": "MEDIUM",
  "status": "OPEN",
  "message": "Conta de Água vence em 7 dias",
  "dueAt": "2026-01-03T10:00:00Z",
  "createdAt": "2026-01-01T10:00:00Z"
}
```

## 7) Índices MongoDB (crítico para performance)

### items

- { userId: 1, categoryId: 1 }
- { userId: 1, templateCode: 1 }

### events

- { userId: 1, itemId: 1, occurredAt: -1 }
- { userId: 1, occurredAt: -1 }

### alerts

- { userId: 1, status: 1, dueAt: 1 }
- { userId: 1, itemId: 1, createdAt: -1 }

### rules

- { userId: 1, enabled: 1 }
- { itemId: 1, enabled: 1 }

## 8) Templates iniciais (MVP)

## Template 1 — Conta com Vencimento (BILL_DUE_DATE)

Uso: água, energia, gás, internet
Campos típicos:

- dueDay (dia do mês)
- recurrence (mensal)
- expectedValue (opcional)
  Regra:
- Notificar X dias antes
- Notificar no dia do vencimento
- Notificar vencido

---

## Template 2 — Manutenção por Tempo (REPLACEMENT_CYCLE)

Uso: resistência, limpeza, troca periódica
Campos típicos:

- everyDays
- lastDoneAt
  Regra:
- Notificar quando faltar X dias para completar o ciclo

---

## Template 3 — Manutenção por Km (VEHICLE_KM)

Uso: óleo, pneus, revisão
Campos típicos:

- kmInterval
- lastKm
- currentKm
- warningKmBefore
  Regra:
- Notificar quando currentKm >= lastKm + kmInterval - warningKmBefore

---

## Template 4 — Leitura / Consumo (METER_READING)

Uso: água, gasolina, energia
Campos típicos:

- leituras registradas como eventos
- projeção no futuro (fase 2)
  Regra (fase 2):
- tendência de consumo
- previsão “vai acabar” / “gastou acima do normal”

## 9) Motor de Regras (Design em Java)

### Objetivo

Separar o core em classes testáveis e extensíveis.

### Abordagem recomendada: Strategy Pattern

Ao invés de herança no banco, persistimos:

- ruleType + paramsJson no Mongo e aplicamos o polimorfismo no domínio Java com um Factory.

---

## 10) Estrutura de Pacotes (sugestão)

```txt
src/main/java/com/seuprojeto/app
├── config
├── auth
├── users
│   ├── UserController.java
│   ├── UserService.java
│   ├── UserRepository.java
│   └── User.java
├── categories
├── templates
├── items
├── events
├── rules
├── alerts
└── engine
    ├── RuleEngineService.java
    ├── RuleEvaluator.java
    ├── RuleFactory.java
    ├── rules
    │   ├── RuleStrategy.java
    │   ├── DueDateRule.java
    │   ├── TimeIntervalRule.java
    │   └── KmThresholdRule.java
    └── dto
```

---

## 11) Interfaces (esqueleto do motor)

### 11.1 RuleStrategy (core)

```java
public interface RuleStrategy {
    String getType();
    RuleEvaluationResult evaluate(RuleContext ctx);
}
```

### 11.2 RuleContext

```java
public class RuleContext {
    private final User user;
    private final Item item;
    private final Rule rule;
    private final List<Event> recentEvents;

    public RuleContext(User user, Item item, Rule rule, List<Event> recentEvents) {
        this.user = user;
        this.item = item;
        this.rule = rule;
        this.recentEvents = recentEvents;
    }

    public User getUser() { return user; }
    public Item getItem() { return item; }
    public Rule getRule() { return rule; }
    public List<Event> getRecentEvents() { return recentEvents; }
}
```

### 11.3 Resultado da avaliação

```java
import java.time.Instant;

public class RuleEvaluationResult {
    private final boolean shouldCreateAlert;
    private final String message;
    private final String severity;
    private final Instant dueAt;

    public RuleEvaluationResult(boolean shouldCreateAlert, String message, String severity, Instant dueAt) {
        this.shouldCreateAlert = shouldCreateAlert;
        this.message = message;
        this.severity = severity;
        this.dueAt = dueAt;
    }

    public boolean shouldCreateAlert() { return shouldCreateAlert; }
    public String getMessage() { return message; }
    public String getSeverity() { return severity; }
    public Instant getDueAt() { return dueAt; }
}
```

### 11.4 Factory de regras

```java
import java.util.Map;

public class RuleFactory {

    private final Map<String, RuleStrategy> strategies;

    public RuleFactory(Map<String, RuleStrategy> strategies) {
        this.strategies = strategies;
    }

    public RuleStrategy getStrategy(String ruleType) {
        RuleStrategy strategy = strategies.get(ruleType);
        if (strategy == null) {
            throw new IllegalArgumentException("Rule type not supported: " + ruleType);
        }
        return strategy;
    }
}
```

---

## Execução do Worker (Scheduler)

## MVP simples com Spring Scheduler

```java
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RuleEngineScheduler {

    private final RuleEngineService engineService;

    public RuleEngineScheduler(RuleEngineService engineService) {
        this.engineService = engineService;
    }

    @Scheduled(fixedDelay = 60000) // a cada 60s (ajustar para produção)
    public void run() {
        engineService.evaluateAllEnabledRules();
    }
}
```

---

## 13) Endpoints (API REST sugerida)

### Auth / Usuário

- GET /me
- POST /auth/login (se usar provider externo pode não precisar)

### Categorias

- POST /categories
- GET /categories
- PUT /categories/{id}
- DELETE /categories/{id} (soft delete recomendado)

### Templates

- GET /templates
- POST /templates (admin-only no futuro)

### Itens

- POST /items
- GET /items
- GET /items/{id}
- PUT /items/{id}
- PATCH /items/{id}/status

### Eventos (histórico)

- POST /items/{id}/events
- GET /items/{id}/events

### Regras

- POST /items/{id}/rules
- GET /items/{id}/rules
- PATCH /rules/{id}

### Alertas

- GET /alerts?status=open
- PATCH /alerts/{id} (close/snooze)

---

## 14) Roadmap (6 a 8 semanas)

## Sprint 1 (Fundação)

- Setup Spring Boot + MongoDB local (docker)
- Estrutura de entidades e repositórios
- CRUD de itens + templates fixos iniciais

## Sprint 2 (Eventos)

- CRUD de eventos por item
- Timeline e filtros
- Índices básicos em Mongo

## Sprint 3 (Motor de regras e alertas)

- Scheduler + RuleEngineService
- Regras: vencimento e ciclo por tempo
- Criação e listagem de alertas

## Sprint 4 (KM + reservas)

- Regra por km
- Reserva financeira simples
- Melhorias de UX e performance

---

## 15) Conclusão: Java + Mongo é escalável aqui?

Sim, é uma boa opção e é escalável.
O diferencial não é só a linguagem, e sim:

- separar histórico (events) em coleção própria
- indexar corretamente
- manter motor de regras desacoplado
- usar job scheduler/worker
- aplicar CI/CD e observabilidade
  Java traz grande vantagem no motor de regras por organização, tipagem e testabilidade, e Mongo encaixa muito bem no modelo flexível de templates e config dinâmico.

---
