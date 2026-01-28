# 047 - Plano de Estabilização (Schema + Logs + Commit)

**Data de criação:** 2026-01-27  
**Data de finalização:** 2026-01-27

Objetivo: estabilizar a validação de `metadataSchema` (templates) no backend, padronizar logs de execução de scripts localmente (sem commitar logs) e preparar commits com o repositório limpo.

---

## ✅ Status atual (resumo)
- Implementada validação de `metadata` contra `metadataSchema`.
- Ajustados testes do `core` para respeitar o schema (ex.: `newTemplate` agora precisa preencher campos required).
- Ajustados testes `WebMvcTest` do módulo `api` para não inicializarem Mongo/SpringData.
- Criado script `scripts/populate-templates.ps1` para popular templates.

---

## 1) Estabilizar schema (core)

### 1.1 Contrato
- Se o `Template` tem `metadataSchema` com campos `required=true`, então ao criar `Item`:
  - `metadata` deve conter as chaves required.
  - quando `type` é definido, o valor deve ser compatível: `string`, `number`, `boolean`, `object`, `array`.

### 1.2 Checklist
- [x] Criar validador no core (`MetadataSchemaValidator`).
- [x] Chamar o validador na criação do item (`CreateItemUseCase`).
- [x] Ajustar testes do core para incluir metadata coerente.
- [x] Rodar `mvn -pl modules/core test`.

---

## 2) Logging de scripts PowerShell (sem commitar logs)

### 2.1 Regras
- Logs devem existir para troubleshooting.
- Logs **NÃO** devem ser commitados.
- Não logar segredos (tokens/URIs completas).

### 2.2 Checklist
- [x] Adicionar `.logs/` no `.gitignore`.
- [x] Criar helper `scripts/run-with-log.ps1`.

### 2.3 Como usar
- Exemplo: rodar testes e salvar log
  - `./scripts/run-with-log.ps1 -Name "tests" -Command "mvn -q test"`

- Exemplo: rodar populate templates e salvar log
  - `./scripts/run-with-log.ps1 -Name "populate-templates" -Command "./scripts/populate-templates.ps1"`

---

## 3) Qualidade antes do commit

### 3.1 Gates
- [x] Build: `mvn -q test` (repo todo)
- [x] `git status` sem artefatos (`target/`, `.logs/` etc.)

---

## 4) Commits sugeridos (para histórico limpo)

1. `feat(core): validate item metadata against template schema`
2. `test(api): stabilize WebMvc tests without Mongo`
3. `docs/scripts: add template populate script and local logging helper`

> Observação: manter commits pequenos ajuda reverter/inspecionar depois.
