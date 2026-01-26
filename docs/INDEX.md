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

- **[043 - Setup Dev Local Completo](043-setup-dev-local-completo.md)**
  - Documentação técnica completa
  - Detalhes de cada script
  - Fluxo de dados
  - Validações implementadas

- **[042 - Commits Git Atualizados](042-commits-git-atualizados.md)**
  - Status dos repositórios
  - Histórico de commits
  - Próximos passos

---- **[004 - Sprint 1 Progresso](004-sprint-1-progresso.md)**
  - Relatório de progresso do Sprint 1
  - Estatísticas de código
  - Arquivos criados e commits
  - Padrões aplicados
  - Próximos passos

---

### 5. Arquitetura Técnica

- **[Arquitetura Detalhada](arquitetura.md)**
  - Diagrama de módulos
  - Responsabilidades de cada módulo
  - Estrutura de pacotes
  - Fluxo de dados
  - Patterns utilizados
  - Estratégia de escalabilidade

---

### 6. Guias Operacionais

- **[GitHub Setup](GITHUB-SETUP.md)**
  - Como criar o repositório no GitHub
  - Conectar repositório local ao remote
  - Workflow de desenvolvimento (feature branches)
  - Convenção de commits
  - Comandos úteis do Git

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

- **[PROXIMO-PASSO.md](../PROXIMO-PASSO.md)**
  - Roteiro para continuar o desenvolvimento
  - Opções de trabalho (Testes, GitHub, Sprint 2)
  - Comandos prontos para uso
  - Objetivos da Sprint 2
  - Dashboard de progresso

- **[CHECKLIST-RETOMADA.md](../CHECKLIST-RETOMADA.md)**
  - Verificações rápidas ao retomar trabalho
  - Opções de trabalho organizadas
  - Comandos úteis prontos
  - Checklist de fim de sessão

---

### 8. Documentos Iniciais

Pasta `iniciais/` - Documentos de concepção do projeto:

- **[Nomenclatura do Projeto](iniciais/Nomenclatura-Projeto.md)**
  - Nomenclatura atual: `item-control-system`
  - Nomenclatura futura: `ItemFlow`
  - Maven coordinates e packages
  - Versionamento SemVer
  - Estratégia de branches

- **[Layout de Repositório](iniciais/Layout%20de%20Reposit%C3%B3rio.md)**
  - Estrutura de pastas multi-módulo
  - Organização de pacotes por módulo
  - Dependências Maven
  - Configuração de profiles
  - Comandos de execução

- **[Observações de Arquitetura](iniciais/Observacao.md)**
  - Estratégia de migração gradual
  - Separação de camadas desde o início
  - Contratos internos claros
  - Idempotência de alertas
  - Processamento assíncrono
  - Feature toggles

- **[Projeto Java - Conceito Original](iniciais/Projeto-Java.md)**
  - Objetivo do sistema completo
  - Visão de arquitetura
  - Stack técnica recomendada
  - Fluxo de trabalho
  - Modelagem do domínio
  - Coleções MongoDB

---

## 🗺️ Navegação por Tópico

### Para Desenvolvedores

1. Começar por: **[README Principal](../README.md)**
2. Entender arquitetura: **[ADR 001](ADRs/001-arquitetura-multi-modulo.md)** e **[Arquitetura Detalhada](arquitetura.md)**
3. Ver modelo de domínio: **[002 - Análise de Casos de Uso](002-analise-casos-uso-modelo-dominio.md)**
4. Planejar trabalho: **[003 - Roadmap](003-roadmap-implementacao.md)**
5. Setup do projeto: **[GitHub Setup](GITHUB-SETUP.md)**

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

**Última atualização:** 22/01/2026
