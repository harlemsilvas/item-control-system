# TODO (Backend): geração automática de `code`/unicidade para Templates

Status: **TODO / Planejado** (não implementado ainda)

## Contexto
Hoje o frontend permite criar Templates com `scope=GLOBAL` ou `scope=USER`.
O campo `code` precisa ser **único dentro do scope** (regra do backend) e é usado como identificador funcional.

Para melhorar UX e reduzir erros, queremos que o backend consiga **gerar automaticamente** um `code` válido e único quando o cliente não informar, ou quando explicitamente pedir auto-geração.

> Observação: a garantia real de unicidade deve ser responsabilidade do backend (regra de negócio + concorrência).

## Requisitos

### R1) Unicidade
- Garantir unicidade de `code` por:
  - `scope=GLOBAL`: único globalmente entre templates GLOBAL.
  - `scope=USER`: único por usuário (entre templates USER do mesmo `userId`).

### R2) Auto-geração
Proposta de comportamento (API):
- Permitir `code` opcional na criação (`POST /api/v1/templates`).
- Se `code` vier `null`/vazio **ou** se `autoCode=true`, o backend gera.

### R3) Formato do code gerado
Manter um formato simples, estável e legível:

#### GLOBAL
- Prefixo: `G-`
- Sufixo: base36 curto + timestamp (para reduzir colisões)
- Exemplo: `G-kr8p9-ldq2`

#### USER
- Prefixo: `U-`
- Identificador do usuário: um fragmento de `userId` normalizado
- Sufixo: base36 curto + timestamp
- Exemplo: `U-demo-kr8p9-ldq2`

**Normalização sugerida**:
- Converter para minúsculo
- Remover caracteres fora de `[a-z0-9-]`
- Limitar tamanho (ex.: 50 chars)

### R4) Idempotência (opcional, mas recomendado)
- Se o cliente enviar um `Idempotency-Key`, o backend pode evitar criar duplicado em caso de retry.

## Alterações sugeridas na API

### Opção A (mais simples): `code` opcional
- `POST /templates` aceita `code` opcional.
- Backend gera se ausente.

### Opção B (explícita): `autoCode` na request
- `POST /templates` aceita `autoCode=true|false`.
- Se `true`, ignora `code` do cliente e gera.

## Persistência / Índices
Garantir no banco:
- Índice único composto:
  - `scope + code` para GLOBAL
  - `scope + userId + code` para USER

> Alternativamente: um único índice composto que cubra ambos os casos com `userId` nullable, mas validar com cuidado.

## Concorrência
- Mesmo com geração local, pode haver corrida.
- Estratégia recomendada:
  - tentativa de insert
  - se violar índice único, regenerar e tentar novamente (com limite de retries, ex. 3-5)

## Testes (backend)
- Criar GLOBAL sem `code` → gera e persiste
- Criar USER sem `code` → gera com fragmento do `userId`
- Concorrência simulada (2 inserts simultâneos) → não quebra
- `code` informado manualmente continua funcionando

## Observações para frontend/mobile
- Frontend pode manter campo `code` editável (modo manual)
- Também pode ter toggle “Gerar automaticamente”
- Para mobile, o mesmo contrato da API pode ser reaproveitado
