# Migração de Monólito para API + Worker

Sim — começar monólito e depois migrar para API + worker é totalmente viável (e é um caminho bem comum). O segredo é começar o monólito já com algumas decisões de arquitetura que deixam a separação futura "barata".

## Dá para migrar depois? Sim, se você evitar acoplamentos errados

Você consegue migrar tranquilamente se, desde o início, você:

### 1) Separar "camadas" e "módulos lógicos" no código

Mesmo no monólito, organize como se fossem módulos:

- **domain** (regras de negócio puras)
- **application** (use cases / serviços)
- **infra** (Mongo, filas, providers)
- **web** (controllers, DTOs, security)

Assim, quando virar API + worker, você basicamente:

- mantém **domain** e parte de **application** como um módulo compartilhado (ou duplicado, dependendo da estratégia)
- cria dois apps Spring Boot: **api** e **worker**

### 2) Não "misturar" agendamentos e regras dentro do Controller

- Controller deve só orquestrar request/response
- O motor de regras deve viver em serviços próprios (ex.: `RuleEngineService`) com interfaces claras

### 3) Tratar o motor de regras como "consumidor" de eventos

Mesmo no monólito, pense assim:

- API grava events e rules
- um "processador" avalia regras e gera alerts

Hoje isso roda "no mesmo deploy". Amanhã esse processador roda separado.

### 4) Evitar dependência forte de memória/estado local

Não guarde estado de processamento em RAM (cache local como fonte de verdade).

Guarde:

- `lastEvaluatedAt` na Rule
- `status` do Alert
- ids de correlação (para idempotência)

Isso facilita rodar em múltiplas instâncias e separar serviços depois.

## Como costuma ser a migração monólito → API + worker

### Etapa A — Monólito com "worker interno"

Você começa com:

- Spring Boot único (API + scheduler `@Scheduled` ou Quartz)
- Tudo no mesmo repositório e deploy
- Regras rodam em background dentro do mesmo processo

### Etapa B — Extrair worker como segundo app no mesmo repo (zero drama)

Você transforma o repo em multi-módulo Maven (ou mantém single-module mas com dois `main()`):

- **core** (domain + application)
- **api** (controllers + auth + serviços HTTP)
- **worker** (scheduler + RuleEngine + dispatch de notificações)

Nesse momento já separou “processos”, mas ainda pode compartilhar código.

### Etapa C — Colocar fila (quando precisar)

Quando volume crescer:

- API publica mensagens (ex.: "novo evento", "item atualizado", "regra alterada")
- Worker consome mensagens e processa regras/alertas
- RabbitMQ/Kafka entra aqui

### Etapa D — Endurecer integração e consistência

Você adiciona:

- idempotência no consumo (para não gerar alertas duplicados)
- outbox pattern (se quiser garantir 100% que gravação + publicação não se percam)
- observabilidade (métricas, tracing)

## O que você precisa fazer HOJE no monólito para migrar com facilidade

### Checklist prático:

#### ✅ 1) Contratos internos claros

Crie interfaces tipo:

- `AlertService`
- `RuleEngine`
- `EventService`
- `NotificationDispatcher`

No futuro, `NotificationDispatcher` pode virar HTTP/gRPC/queue sem refatorar o domínio.

#### ✅ 2) Idempotência de alertas desde o início

Defina uma "chave natural" para evitar duplicação, por exemplo:

```
itemId + ruleId + dueAt + type
```

E use índice único no Mongo para isso (quando fizer sentido).

#### ✅ 3) Evite "chamar diretamente" o motor no fluxo de request

- **Não faça:** `POST /events` → já avalia tudo na hora
- **Faça:** `POST /events` grava e retorna rápido; o worker processa em background

No monólito, esse "worker" pode ser só um `@Scheduled` que roda a cada 1 min.

#### ✅ 4) Feature flags / toggles

Ter `engine.enabled=true/false` por profile ajuda na transição:

- no monólito: API e engine ligados
- depois: API com engine desligado, worker com engine ligado

## Quando vale a pena separar de verdade?

Sinais clássicos:

- alertas precisam rodar com mais frequência/volume
- processamento pesado (projeção de consumo, IA, etc.)
- múltiplas integrações (WhatsApp/email/push)
- picos de uso no app não podem sofrer com jobs

## Minha recomendação para você agora

Comece monólito, mas já estruturado para separação:

- Monólito com scheduler interno (MVP)
- Multi-módulo Maven cedo (core/api/worker)
- Fila só quando tiver motivo (volume/latência)

Se você quiser, eu te passo um layout de repositório (monólito já "pronto pra split") e um `pom.xml` multi-módulo com:

- módulo **core**
- módulo **api**
- módulo **worker**
- profiles para ligar/desligar scheduler por ambiente
