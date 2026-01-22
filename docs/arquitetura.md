# Arquitetura do Item Control System

## Visão Geral

O Item Control System utiliza uma arquitetura **hexagonal** (ports and adapters) com separação em 3 módulos Maven.

## Diagrama de Módulos

```
┌─────────────────────────────────────────┐
│           MÓDULOS SPRING BOOT           │
├─────────────────┬───────────────────────┤
│   API Module    │   Worker Module       │
│                 │                       │
│  Controllers    │   Schedulers          │
│  DTOs           │   Jobs                │
│  Security       │                       │
└────────┬────────┴──────────┬────────────┘
         │                   │
         │   Dependem de     │
         │                   │
         └───────┬───────────┘
                 │
         ┌───────▼────────┐
         │  CORE Module   │
         │                │
         │  Domain        │
         │  Use Cases     │
         │  Ports         │
         └────────────────┘
```

## Core Module (Domínio)

### Responsabilidades

- Entidades de domínio (Item, Rule, Alert, Event)
- Regras de negócio puras
- Casos de uso (application layer)
- Interfaces (ports) para I/O

### Regras

- ❌ NÃO depende de frameworks
- ❌ NÃO conhece MongoDB, HTTP, etc
- ✅ SIM testável 100% com mocks
- ✅ SIM reutilizável em qualquer módulo

### Estrutura

```
core/
├── domain/
│   ├── model/        # Entidades
│   ├── rules/        # Motor de regras
│   └── exceptions/   # Exceções de domínio
└── application/
    ├── ports/        # Interfaces
    │   ├── repositories/
    │   └── notifications/
    └── usecases/     # Casos de uso
```

## API Module

### Responsabilidades

- Expor REST API
- Autenticação/Autorização
- Validação de entrada
- Adapters MongoDB

### Estrutura

```
api/
├── web/
│   ├── controllers/  # Endpoints REST
│   ├── request/      # DTOs de entrada
│   └── response/     # DTOs de saída
├── config/           # Configurações Spring
├── security/         # JWT, OAuth, etc
└── infra/
    └── mongo/        # Implementações dos ports
```

## Worker Module

### Responsabilidades

- Processamento em background
- Scheduler de regras
- Geração de alertas
- Notificações

### Estrutura

```
worker/
├── scheduler/        # Jobs agendados
│   ├── RuleEngineScheduler.java
│   └── SchedulerConfig.java
└── infra/
    └── mongo/        # Implementações dos ports
```

## Fluxo de Dados

### 1. Criação de Item (API)

```
HTTP POST /items
    ↓
Controller (API)
    ↓
CreateItemUseCase (Core)
    ↓
ItemRepository (Port - Core)
    ↓
MongoItemRepository (API)
    ↓
MongoDB
```

### 2. Processamento de Regras (Worker)

```
@Scheduled (Worker)
    ↓
RuleEngineScheduler (Worker)
    ↓
EvaluateRulesUseCase (Core)
    ↓
RuleRepository (Port - Core)
    ↓
MongoRuleRepository (Worker)
    ↓
MongoDB
```

## Patterns Utilizados

### Hexagonal Architecture

- Core isolado de frameworks
- Ports (interfaces) para I/O
- Adapters (implementações) nos módulos externos

### Repository Pattern

- Abstração do acesso a dados
- Interfaces no Core
- Implementações MongoDB nos módulos

### Use Case Pattern

- Um caso de uso = uma ação
- Entrada/Saída bem definidas
- Orquestração de domínio

### Strategy Pattern (Motor de Regras)

- Diferentes tipos de regras
- Extensível para novas regras
- Factory para criação

## Escalabilidade

### Fase 1: Monólito

- API e Worker juntos
- Mesmo banco MongoDB
- Deploy único

### Fase 2: Separação

- API em um processo
- Worker em outro processo
- Banco compartilhado

### Fase 3: Microserviços

- API + Worker separados
- Fila (RabbitMQ/Kafka)
- Bancos separados (opcional)

## Decisões Técnicas

- **Java 17**: LTS, performance, records
- **Spring Boot 3.2**: Última versão estável
- **MongoDB**: Flexibilidade de schema
- **Maven**: Build multi-módulo
- **Lombok**: Reduz boilerplate

---

**Última atualização:** 22/01/2026
