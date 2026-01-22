# Item Control System

Sistema de controle de itens com alertas e regras customizáveis. Arquitetura multi-módulo Maven com separação entre API REST e Worker (processamento em background).

## 🚀 Tecnologias

- **Java 17**
- **Spring Boot 3.2.1**
- **MongoDB**
- **Maven**
- **Lombok**

## 📦 Arquitetura

O projeto está dividido em 3 módulos:

### Core

- Domínio e regras de negócio
- Casos de uso (application layer)
- Interfaces (ports) para persistência
- **Não depende de Spring Boot**

### API

- REST API com Spring Boot
- Controllers e DTOs
- Autenticação e segurança
- Adapters MongoDB

### Worker

- Processamento em background
- Scheduler de regras
- Motor de alertas
- Pode rodar separado da API

## 🏗️ Estrutura do Projeto

```
item-control-system/
├── modules/
│   ├── core/          # Domínio e regras de negócio
│   ├── api/           # REST API
│   └── worker/        # Processamento background
├── docs/              # Documentação
├── docker-compose.yml # MongoDB local
└── pom.xml           # POM parent
```

## 🔧 Como Rodar

### Pré-requisitos

- Java 17+
- Maven 3.8+
- MongoDB (ou Docker)

### 1. Subir MongoDB (Docker)

```bash
docker-compose up -d
```

### 2. Rodar API

```bash
mvn -pl modules/api spring-boot:run
```

A API estará disponível em: `http://localhost:8080`

Swagger UI: `http://localhost:8080/swagger-ui.html`

### 3. Rodar Worker (em outro terminal)

```bash
mvn -pl modules/worker spring-boot:run -Dspring-boot.run.profiles=dev
```

### 4. Rodar Monólito (API + Worker juntos)

Você pode rodar ambos no mesmo processo ativando ambos os módulos.

## 🧪 Executar Testes

```bash
# Todos os testes
mvn clean test

# Apenas um módulo
mvn -pl modules/core test
```

## 📝 Profiles Spring

### API

- `default`: Configuração padrão
- `dev`: Desenvolvimento com logs verbosos

### Worker

- `default`: Scheduler **desligado**
- `dev`: Scheduler **ligado**

## 🛠️ Build

```bash
# Build completo
mvn clean install

# Build sem testes
mvn clean install -DskipTests

# Package (gerar JARs)
mvn clean package
```

## 📚 Documentação

- [Nomenclatura e Estratégia](../Docs/Nomenclatura-Projeto.md)
- [Layout de Repositório](../Docs/Layout%20de%20Repositório.md)
- [Observações de Arquitetura](../Docs/Observacao.md)

## 🎯 Roadmap

- [x] Estrutura multi-módulo Maven
- [x] Módulo core com domínio
- [ ] Implementar entidades (Item, Rule, Alert, Event)
- [ ] Endpoints REST da API
- [ ] Motor de regras no Worker
- [ ] Autenticação JWT
- [ ] Testes unitários e integração
- [ ] CI/CD com GitHub Actions
- [ ] Deploy em Cloud (futuro)

## 👨‍💻 Autor

**Harlem Silvas**

- GitHub: [@harlemsilvas](https://github.com/harlemsilvas)

## 📄 Licença

Este projeto está em desenvolvimento para fins de aprendizado e portfólio.

---

**Versão:** 0.1.0-SNAPSHOT  
**Status:** Em desenvolvimento
