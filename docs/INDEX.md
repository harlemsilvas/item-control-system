# 📚 Índice da Documentação

Bem-vindo à documentação do **Item Control System**!

---

## 📋 Documentos Principais

### 1. Visão Geral
- **[README Principal](../README.md)** - Visão geral do projeto, quick start e tecnologias

---

### 2. Architecture Decision Records (ADRs)

- **[ADR 001 - Arquitetura Multi-Módulo](ADRs/001-arquitetura-multi-modulo.md)**
  - Decisão de usar arquitetura hexagonal com 3 módulos Maven
  - Separação entre Core, API e Worker
  - Estratégia de migração monólito → microserviços

- **[Casos de Uso Reais](ADRs/CasosUso.md)**
  - Manutenção de veículos (Honda CB 500X)
  - Conta de água (controle mensal)
  - Galão de água e botijão de gás (consumíveis)

---

### 3. Análise e Design

- **[002 - Análise de Casos de Uso e Modelo de Domínio](002-analise-casos-uso-modelo-dominio.md)**
  - Análise detalhada dos 3 casos de uso
  - Modelo de domínio proposto (Entidades, Value Objects, Enums)
  - Templates de Item (VEHICLE, UTILITY_BILL, CONSUMABLE_ITEM)
  - Casos de uso (Use Cases) completos
  - Exemplos práticos mapeados

---

### 4. Planejamento

- **[003 - Roadmap de Implementação](003-roadmap-implementacao.md)**
  - Planejamento detalhado de 6 sprints (12 semanas)
  - Sprint 1: Fundação (entidades, ports)
  - Sprint 2: Use Cases básicos (CRUD)
  - Sprint 3: Motor de regras
  - Sprint 4: Templates e análises
  - Sprint 5: Refinamentos e UX
  - Sprint 6: MVP Final
  - Roadmap futuro (Notificações, OAuth, Mobile, ML)

---

### 5. Setup e Configuração ⭐ NOVO

- **[QUICK-START-DEV.md](../QUICK-START-DEV.md)**
  - Início rápido para desenvolvimento (1 comando)
  - URLs de acesso
  - Comandos essenciais

- **[DEV-LOCAL-GUIDE.md](../DEV-LOCAL-GUIDE.md)**
  - Guia completo de desenvolvimento local
  - Arquitetura detalhada
  - Troubleshooting
  - Workflows recomendados

- **[043 - Setup Dev Local Completo](dev/setup/043-setup-dev-local-completo.md)**
  - Documentação técnica completa
  - Detalhes de cada script
  - Fluxo de dados
  - Validações implementadas

- **[042 - Commits Git Atualizados](042-commits-git-atualizados.md)**
  - Status dos repositórios
  - Histórico de commits
  - Próximos passos

---- **[004 - Sprint 1 Progresso](history/sprints/004-sprint-1-progresso.md)**
  - Relatório de progresso do Sprint 1
  - Estatísticas de código
  - Arquivos criados e commits
  - Padrões aplicados
  - Próximos passos

---

### 6. Deploy (Railway / MongoDB / Docker)

- **Railway**
  - **[021 - Guia de Deploy Gratuito](deploy/railway/021-guia-deploy-gratuito.md)**
  - **[022 - Deploy no Railway (passo a passo)](deploy/railway/022-deploy-railway-passo-a-passo.md)**
  - **[011 - Migração Railway completa](deploy/railway/011-migracao-railway-completa.md)**
  - **[015 - Sucesso: Railway funcionando](deploy/railway/015-SUCESSO-RAILWAY-FUNCIONANDO.md)**

- **MongoDB**
  - **[GUIA-MONGODB](deploy/mongodb/GUIA-MONGODB.md)**
  - **[GUIA-VISUALIZAR-RAILWAY-MONGODB](deploy/mongodb/GUIA-VISUALIZAR-RAILWAY-MONGODB.md)**
  - **[009 - Migração Railway MongoDB](deploy/mongodb/009-migracao-railway-mongodb.md)**

- **Docker**
  - **[044 - Fix Docker Engine](deploy/docker/044-fix-docker-engine.md)**

---

### 7. Backend - Troubleshooting

- **Railway / Auth**
  - **[012 - Troubleshooting Auth (Railway)](backend/troubleshooting/railway/012-troubleshooting-auth-railway.md)**
  - **[013 - Railway: Problema Resolvido](backend/troubleshooting/railway/013-railway-problema-resolvido.md)**

- **API / Erros**
  - **[014 - Solução erro 400 ao criar item](backend/troubleshooting/api/014-solucao-erro-400-criar-item.md)**

- **Estabilização**
  - **[047 - Plano de estabilização (schema/logs/commit)](backend/troubleshooting/stabilization/047-plano-estabilizacao-schema-logs-e-commit.md)**

---

### 7. Guias Práticos (Raiz do Projeto)

- **[RESUMO-EXECUTIVO.md](../RESUMO-EXECUTIVO.md)**
  - Visão geral completa do sistema
  - Como usar o sistema (start, compile, run)
  - Exemplos de uso da API
  - Decisões técnicas importantes
  - Troubleshooting
  - Próximos passos recomendados

- **[GUIA-TESTES.md](../GUIA-TESTES.md)**
  - Passo a passo para testar a API
  - Testes via Swagger UI e PowerShell
  - Verificação no MongoDB
  - Checklist de validação
  - Problemas comuns e soluções

- **[PROXIMO-PASSO.md](dev/workflows/PROXIMO-PASSO.md)**
  - Roteiro para continuar o desenvolvimento
  - Opções de trabalho (Testes, GitHub, Sprint 2)
  - Comandos prontos para uso
  - Objetivos da Sprint 2
  - Dashboard de progresso

- **[CHECKLIST-RETOMADA.md](dev/workflows/CHECKLIST-RETOMADA.md)**
  - Verificações rápidas ao retomar trabalho
  - Opções de trabalho organizadas
  - Comandos úteis prontos
  - Checklist de fim de sessão

- **[PROXIMAS-ETAPAS.md](dev/workflows/PROXIMAS-ETAPAS.md)**
  - Próximas etapas e backlog de desenvolvimento
  - Organização de tarefas por prioridade

---

### 8. História / Sprints

- **[004 - Sprint 1 Progresso](history/sprints/004-sprint-1-progresso.md)**
- **[005 - Sprint 1 Status Final](history/sprints/005-sprint-1-status-final.md)**
- **[018 - Sprint 2 Completo (final)](history/sprints/018-sprint2-completo-final.md)**

---

### 9. Status do Projeto

- **[STATUS-ATUAL](project/status/STATUS-ATUAL.md)**
- **[016 - Status atual (Categorias)](project/status/016-status-atual-categorias.md)**
- **[RESUMO-POPULACAO](project/status/RESUMO-POPULACAO.md)**

---

### 10. Backend - Regras e Testes

- **Regras**
  - **[RULES-CRUD-COMPLETO](backend/rules/RULES-CRUD-COMPLETO.md)**

- **Testes**
  - **[017 - Testes automatizados (completo)](backend/testing/017-testes-automatizados-completo.md)**
  - **[019 - Testes core (sucesso)](backend/testing/core/019-testes-core-sucesso.md)**
  - **[020 - Testes (resumo final)](backend/testing/020-testes-resumo-final.md)**
  - **[046 - Templates (CRUD) e Testes no Postman](backend/testing/046-templates-crud-e-testes-postman.md)**

---

### 9. Backlog / TODO (Backend)

- **[048 - Backend TODO: geração automática de code/unicidade para Templates](048-backend-todo-geracao-code-template.md)**
  - Geração automática de `code` para scope GLOBAL e USER
  - Regras de unicidade
  - Estratégia de concorrência e índices

---

## 🧭 Navegação por Tópico

### Para Desenvolvedores

1. Começar por: **[README Principal](../README.md)**
2. Entender arquitetura: **[ADR 001](ADRs/001-arquitetura-multi-modulo.md)** e **[Arquitetura Detalhada](arquitetura.md)**
3. Ver modelo de domínio: **[002 - Análise de Casos de Uso](002-analise-casos-uso-modelo-dominio.md)**
4. Planejar trabalho: **[003 - Roadmap](003-roadmap-implementacao.md)**
5. Setup do projeto: **[GitHub Setup](dev/setup/GITHUB-SETUP.md)**

### Para Product Owners

1. Começar por: **[Casos de Uso Reais](ADRs/CasosUso.md)**
2. Ver funcionalidades: **[002 - Análise de Casos de Uso](002-analise-casos-uso-modelo-dominio.md)**
3. Roadmap: **[003 - Roadmap](003-roadmap-implementacao.md)**

### Para Arquitetos

1. Decisões arquiteturais: **[ADR 001](ADRs/001-arquitetura-multi-modulo.md)**
2. Arquitetura técnica: **[Arquitetura Detalhada](arquitetura.md)**
3. Observações: **[Observações de Arquitetura](iniciais/Observacao.md)**
4. Modelo de domínio: **[002 - Análise de Casos de Uso](002-analise-casos-uso-modelo-dominio.md)**

---

## 📊 Status da Documentação

| Documento | Status | Última Atualização |
|-----------|--------|-------------------|
| README Principal | ✅ Completo | 22/01/2026 |
| ADR 001 | ✅ Completo | 22/01/2026 |
| Casos de Uso | ✅ Completo | 22/01/2026 |
| 002 - Análise e Modelo | ✅ Completo | 22/01/2026 |
| 003 - Roadmap | ✅ Completo | 22/01/2026 |
| 004 - Sprint 1 Progresso | ✅ Completo | 22/01/2026 |
| 005 - Sprint 1 Status Final | ✅ Completo | 22/01/2026 |
| Arquitetura Detalhada | ✅ Completo | 22/01/2026 |
| GitHub Setup | ✅ Completo | 22/01/2026 |
| RESUMO-EXECUTIVO | ✅ Completo | 22/01/2026 |
| GUIA-TESTES | ✅ Completo | 22/01/2026 |
| Nomenclatura | ✅ Completo | 22/01/2026 |
| Layout Repositório | ✅ Completo | 22/01/2026 |
| Observações Arquitetura | ✅ Completo | 22/01/2026 |
| Projeto Java Original | ✅ Completo | 22/01/2026 |

---

## 🔄 Próximos Passos

1. ✅ Documentação completa
2. ✅ Estrutura de projeto criada
3. ✅ Repositório Git inicializado
4. ✅ **Entidades de domínio implementadas**
5. ✅ **Testes unitários do core (33 testes)**
6. ✅ **Repositório no GitHub criado** (remote configurado)
7. ✅ **Sprint 2 Fase 1** - Use Cases e Controllers REST implementados
8. ⏳ **Commit e Push final** - Versionar arquivos de hoje
9. ⏳ **Release v0.1.0** - Criar tag no GitHub
10. ⏳ **Sprint 2 Fase 2** - AlertRepository e sistema de alertas

---

## 📝 Como Contribuir com a Documentação

- Documentos devem ser escritos em Markdown
- ADRs devem seguir o formato do ADR 001
- Manter índice atualizado
- Data de atualização em cada documento
- Links entre documentos quando aplicável

---

## 🗂️ Organização de Documentos (estrutura atual)

A pasta `docs/` está sendo organizada por áreas:

- `docs/dev/` → setup local e workflows
- `docs/deploy/` → deploy e operação (ex.: Railway)
- `docs/backend/` → documentação técnica do backend (testing/troubleshooting)
- `docs/history/` → histórico e incidentes
- `docs/iniciais/` e `docs/ADRs/` → documentos de base e decisões

---

## 🗓️ Política de Datas (para novos docs e edições)

Sempre que **criar** ou **editar** um documento, adicionar ao topo ou ao fim:

- **Criado em:** DD/MM/AAAA
- **Última modificação:** DD/MM/AAAA
- **Data de liquidação:** DD/MM/AAAA (ou `—` enquanto não estiver concluído)

> Observação: a padronização completa dos docs antigos será feita de forma incremental, conforme forem sendo revisitados.

---

**Última atualização:** 28/01/2026
