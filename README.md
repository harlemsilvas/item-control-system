# 🎯 Item Control System

> Sistema inteligente de controle de itens do dia a dia com motor de regras e alertas automáticos

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MongoDB](https://img.shields.io/badge/MongoDB-7.0-green.svg)](https://www.mongodb.com/)
[![Maven](https://img.shields.io/badge/Maven-3.8+-blue.svg)](https://maven.apache.org/)

---

## 📋 Sobre o Projeto

O **Item Control System** é uma solução completa para controle e monitoramento de itens do dia a dia, como:

- 🚗 **Manutenção de Veículos** - Controle de trocas de óleo, pneus, revisões baseado em km ou tempo
- 💧 **Contas Recorrentes** - Alertas para contas de água, luz, gás com previsão de consumo
- 🛢️ **Consumíveis** - Previsão de reposição de galões de água, botijões de gás baseado em histórico

### ✨ Características Principais

- **Motor de Regras Flexível** - Suporta regras temporais, baseadas em métricas e compostas
- **Alertas Inteligentes** - Notificações configuráveis antes de vencimentos
- **Análise Preditiva** - Previsões baseadas em consumo histórico
- **Arquitetura Hexagonal** - Core isolado, fácil de testar e estender
- **Multi-Módulo Maven** - Separação clara entre API, Worker e Domínio
- **Templates Customizáveis** - Tipos de itens pré-definidos e extensíveis

---

## 🚀 Tecnologias

### Backend
- **Java 17** - LTS, Records, Pattern Matching
- **Spring Boot 3.2.1** - Framework principal
- **Spring Data MongoDB** - Persistência
- **Lombok** - Redução de boilerplate

### Banco de Dados
- **MongoDB 7.0** - Flexibilidade de schema

### Build & DevOps
- **Maven** - Gerenciamento de dependências
- **Docker** - Containerização

## 🏗️ Arquitetura

### 📁 Estrutura do Projeto

```
item-control-system/
├── 📄 README.md                    # Este arquivo
├── 📄 pom.xml                      # Configuração Maven raiz
├── 📄 docker-compose.yml           # Configuração Docker
├── 📁 docs/                        # 📚 Documentação completa
│   ├── INDEX.md                    # Índice da documentação
│   ├── arquitetura.md              # Arquitetura detalhada
│   ├── GUIA-TESTES.md              # Guia de testes
│   ├── GUIA-MONGODB.md             # Guia do MongoDB
│   ├── ADRs/                       # Architecture Decision Records
│   └── iniciais/                   # Documentos de planejamento
├── 📁 scripts/                     # 🔧 Scripts de automação
│   ├── start-api.ps1               # Iniciar API
│   ├── populate-test-data.ps1      # Popular dados de teste
│   ├── test-api.ps1                # Testar endpoints
│   ├── view-mongodb.ps1            # Visualizar MongoDB
│   └── Encerrar.ps1                # Parar todos os serviços
└── 📁 modules/                     # Módulos do projeto
    ├── core/                       # Lógica de negócio (Domain + Use Cases)
    ├── api/                        # REST API (Controllers + Adapters)
    └── worker/                     # Processamento assíncrono (Rules Engine)
```

### 🎯 Módulos Maven

```
┌─────────────────────────────────────────┐
│        MÓDULOS SPRING BOOT              │
├─────────────────┬───────────────────────┤
│   API Module    │   Worker Module       │
│                 │                       │
│  Controllers    │   Schedulers          │
│  DTOs           │   Jobs                │
│  Security       │                       │
└────────┬────────┴──────────┬────────────┘
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

### Módulos

- **`core`** - Domínio puro (sem Spring), casos de uso e interfaces
- **`api`** - REST API com Spring Boot, controllers e adapters MongoDB
- **`worker`** - Processamento background, scheduler de regras e alertas

---

## 🚀 Quick Start

### Pré-requisitos

- Java 17+
- Maven 3.8+
- Docker & Docker Compose

### 1. Clone o repositório

```bash
git clone https://github.com/harlemsilvas/item-control-system.git
cd item-control-system
```

### 2. Inicie o MongoDB via Docker

```bash
docker-compose up -d
```

### 3. Compile o projeto

```bash
mvn clean install
```

### 4. Execute a API

```bash
mvn -pl modules/api spring-boot:run
```

### 5. Execute o Worker (opcional)

```bash
mvn -pl modules/worker spring-boot:run -Dspring-boot.run.profiles=dev
```

### 6. Acesse a documentação Swagger

```
http://localhost:8080/swagger-ui.html
```

---

## 📚 Documentação

- [ADR 001 - Arquitetura Multi-Módulo](docs/ADRs/001-arquitetura-multi-modulo.md)
- [Análise de Casos de Uso e Modelo de Domínio](docs/002-analise-casos-uso-modelo-dominio.md)
- [Roadmap de Implementação](docs/003-roadmap-implementacao.md)
- [Arquitetura Detalhada](docs/arquitetura.md)
- [Casos de Uso Reais](docs/ADRs/CasosUso.md)

---

## 🎯 Casos de Uso

### Exemplo 1: Troca de Óleo de Veículo

```json
POST /api/items
{
  "name": "Honda CB 500X",
  "nickname": "Motoca",
  "templateCode": "VEHICLE",
  "tags": ["moto", "honda"],
  "metadata": {
    "brand": "Honda",
    "model": "CB 500X",
    "year": 2020,
    "currentKm": 15000,
    "lastOilChangeKm": 10000,
    "lastOilChangeDate": "2025-07-15"
  }
}
```

**Alerta gerado:** 500 km antes OU 15 dias antes da próxima troca (o que vier primeiro)

### Exemplo 2: Conta de Água

```json
POST /api/items
{
  "name": "Conta de Água - Casa Mãe",
  "templateCode": "UTILITY_BILL",
  "tags": ["casa-mae", "residencial"],
  "metadata": {
    "billType": "WATER",
    "dueDay": 10,
    "averageValue": 160.00
  }
}
```

**Alertas:** 5 dias antes e 1 dia antes do vencimento mensal

---

## 🧪 Testes

### Executar todos os testes

```bash
mvn test
```

### Executar testes de um módulo específico

```bash
mvn -pl modules/core test
```

---

## 📦 Estrutura do Projeto

```
item-control-system/
├── docs/                           # Documentação
│   ├── ADRs/                       # Architecture Decision Records
│   └── iniciais/                   # Documentação inicial
├── modules/
│   ├── core/                       # Domínio e casos de uso
│   │   └── src/main/java/
│   │       └── br/com/harlemsilvas/itemcontrol/core/
│   │           ├── domain/         # Entidades, VOs, Enums
│   │           └── application/    # Use Cases e Ports
│   ├── api/                        # REST API
│   │   └── src/main/java/
│   │       └── br/com/harlemsilvas/itemcontrol/api/
│   │           ├── web/            # Controllers
│   │           ├── config/         # Configurações
│   │           └── infra/          # Adapters MongoDB
│   └── worker/                     # Background Jobs
│       └── src/main/java/
│           └── br/com/harlemsilvas/itemcontrol/worker/
│               └── scheduler/      # Schedulers
├── docker-compose.yml
├── pom.xml                         # POM Pai
└── README.md
```

---

## 📅 Roadmap

### ✅ Sprint 1 (Atual)
- [x] Estrutura multi-módulo Maven
- [x] Docker Compose
- [x] Documentação inicial
- [ ] Entidades de domínio
- [ ] Ports e interfaces

### 🚧 Sprint 2
- [ ] Use Cases básicos (CRUD)
- [ ] MongoDB Adapters
- [ ] Controllers REST
- [ ] Testes de integração

### 📋 Sprint 3
- [ ] Motor de regras
- [ ] Scheduler
- [ ] Geração de alertas

### 🔮 Futuro
- [ ] Notificações (E-mail, Push, WhatsApp)
- [ ] Autenticação OAuth2
- [ ] Mobile App
- [ ] Machine Learning para previsões

[Ver roadmap completo](docs/003-roadmap-implementacao.md)

---

## 🤝 Contribuindo

Contribuições são bem-vindas! Por favor:

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'feat: Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

### Convenção de Commits

Seguimos [Conventional Commits](https://www.conventionalcommits.org/):

- `feat:` - Nova funcionalidade
- `fix:` - Correção de bug
- `docs:` - Documentação
- `chore:` - Tarefas de manutenção
- `test:` - Adição/correção de testes
- `refactor:` - Refatoração de código

---

## 👨‍💻 Autor

**Harlem Silvas**

- GitHub: [@harlemsilvas](https://github.com/harlemsilvas)

---

## ⭐ Mostre seu apoio

Se este projeto te ajudou, dê uma ⭐️!

---

**Versão:** 0.1.0-SNAPSHOT  
**Status:** 🚧 Em desenvolvimento ativo
