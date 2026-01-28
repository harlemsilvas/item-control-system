# 046 - Templates (CRUD) e Testes no Postman

Data: 2026-01-27

Este documento descreve **como funciona o módulo de Templates** no backend e como **testar via Postman** (ou curl) os endpoints disponíveis.

## 1) O que é um Template?
Um **Template** define:
- um **código estável** (ex: `VEHICLE`, `GENERAL`)
- **nome e descrição por idioma** (`nameByLocale`, `descriptionByLocale`)
- um **esquema da metadata** (`metadataSchema`) para guiar os campos do Item

Ele é usado principalmente quando você cria um Item, via:
- `templateCode` (compatibilidade)
- `templateId` (preferido)
- `newTemplate` (criar template + item juntos)

> Prioridade usada na criação do Item:
> 1) `newTemplate`  → 2) `templateId` → 3) `templateCode`

## 2) Escopo: GLOBAL vs USER
O template pode ser:

### `GLOBAL`
- Disponível para todos.
- `userId` deve ser **null** (ou omitido) no momento da criação.

### `USER`
- Privado do usuário.
- `userId` é **obrigatório**.

## 3) Base URL
Se você está rodando local:
- `http://localhost:8080/api/v1`

Os endpoints ficam em:
- `/templates`

## 4) Endpoints disponíveis

### 4.1) Criar Template
**POST** `/api/v1/templates`

Headers:
- `Content-Type: application/json`
- `Accept-Language: pt-BR` (opcional, para retornar `name`/`description` resolvidos)

#### Exemplo 1: Criar Template GLOBAL
Body:
```json
{
  "scope": "GLOBAL",
  "code": "VEHICLE",
  "nameByLocale": {
    "pt-BR": "Veículo",
    "en-US": "Vehicle"
  },
  "descriptionByLocale": {
    "pt-BR": "Template padrão para veículos",
    "en-US": "Default template for vehicles"
  },
  "metadataSchema": {
    "brand": { "type": "string", "required": true },
    "model": { "type": "string", "required": true },
    "year": { "type": "number", "required": false }
  }
}
```

#### Exemplo 2: Criar Template USER
Body:
```json
{
  "userId": "550e8400-e29b-41d4-a716-446655440001",
  "scope": "USER",
  "code": "MY-CUSTOM-DEVICE",
  "nameByLocale": {
    "pt-BR": "Meu Dispositivo",
    "en-US": "My Device"
  },
  "descriptionByLocale": {
    "pt-BR": "Template privado do usuário"
  },
  "metadataSchema": {
    "serialNumber": { "type": "string", "required": true }
  }
}
```

Respostas comuns:
- `201 Created` → template criado
- `400 Bad Request` → validação do JSON
- `409/400` (dependendo do handler de exceção vigente) → `code` já existe no mesmo escopo

---

### 4.2) Buscar Template por ID
**GET** `/api/v1/templates/{id}`

Exemplo:
`GET /api/v1/templates/1b3f7c0e-7c68-4f4c-9c07-4e7c6c20a9e1`

Resposta:
- `200 OK` com payload
- `404 Not Found`

---

### 4.3) Listar Templates (disponíveis)
**GET** `/api/v1/templates?userId={userId}`

- Sempre retorna **GLOBAL**.
- Se `userId` for informado, retorna também os templates do usuário.

Exemplo:
`GET /api/v1/templates?userId=550e8400-e29b-41d4-a716-446655440001`

---

### 4.4) Listar apenas Globais
**GET** `/api/v1/templates/global`

---

### 4.5) Listar templates do usuário
**GET** `/api/v1/templates/user/{userId}`

---

### 4.6) Atualizar Template
**PUT** `/api/v1/templates/{id}`

> Observação: não é permitido trocar `scope`, `userId` e `code`. Apenas atualizar:
> - `nameByLocale`
> - `descriptionByLocale`
> - `metadataSchema`

Body:
```json
{
  "nameByLocale": {
    "pt-BR": "Veículo (Atualizado)",
    "en-US": "Vehicle (Updated)"
  },
  "descriptionByLocale": {
    "pt-BR": "Descrição atualizada"
  },
  "metadataSchema": {
    "brand": { "type": "string", "required": true },
    "model": { "type": "string", "required": true },
    "plate": { "type": "string", "required": false }
  }
}
```

Respostas:
- `200 OK`
- `404 Not Found`
- `400 Bad Request`

---

### 4.7) Deletar Template
**DELETE** `/api/v1/templates/{id}`

Respostas:
- `204 No Content`
- `404 Not Found`

## 5) Teste rápido no Postman (passo a passo)

1) Crie uma Collection (opcional): `Item Control - Templates`
2) Crie uma variável de collection:
   - `baseUrl = http://localhost:8080/api/v1`
3) Crie uma request:
   - **POST** `{{baseUrl}}/templates`
   - Headers:
     - `Content-Type: application/json`
     - `Accept-Language: pt-BR`
   - Cole o JSON de exemplo “Criar Template GLOBAL” e envie.
4) Copie o `id` da resposta
5) Teste:
   - **GET** `{{baseUrl}}/templates/{{id}}`
   - **PUT** `{{baseUrl}}/templates/{{id}}`
   - **DELETE** `{{baseUrl}}/templates/{{id}}`

## 6) Teste rápido via curl (opcional)

### Criar GLOBAL
```bash
curl -i -X POST "http://localhost:8080/api/v1/templates" \
  -H "Content-Type: application/json" \
  -H "Accept-Language: pt-BR" \
  -d "{\"scope\":\"GLOBAL\",\"code\":\"VEHICLE\",\"nameByLocale\":{\"pt-BR\":\"Veículo\"}}"
```

### Listar disponíveis (com userId)
```bash
curl -i "http://localhost:8080/api/v1/templates?userId=550e8400-e29b-41d4-a716-446655440001"
```

## 7) Como usar Template ao criar Item
Para criar Item usando Template:

### 7.1) Preferido: usar `templateId`
**POST** `/api/v1/items`

```json
{
  "userId": "550e8400-e29b-41d4-a716-446655440001",
  "name": "Meu carro",
  "nickname": "corolla-2020",
  "templateId": "<COLE_AQUI_O_ID_DO_TEMPLATE>",
  "metadata": {
    "brand": "Toyota",
    "model": "Corolla",
    "year": 2020
  }
}
```

### 7.2) Compatibilidade: `templateCode`
```json
{
  "userId": "550e8400-e29b-41d4-a716-446655440001",
  "name": "Notebook",
  "templateCode": "GENERAL",
  "metadata": {
    "brand": "Dell"
  }
}
```

### 7.3) Criar template junto (newTemplate)
```json
{
  "userId": "550e8400-e29b-41d4-a716-446655440001",
  "name": "Meu item com template novo",
  "newTemplate": {
    "scope": "USER",
    "code": "MY-NEW-TEMPLATE",
    "nameByLocale": { "pt-BR": "Novo Template" },
    "metadataSchema": { "serial": { "type": "string" } }
  },
  "metadata": {
    "serial": "ABC-123"
  }
}
```

> Observação: por validação atual do backend, é obrigatório informar **um** de: `templateId`, `templateCode`, `newTemplate`.

## 8) Troubleshooting rápido

### Erro 400 ao criar template
- Verifique se `scope` e `code` estão preenchidos.
- `nameByLocale` não pode ser vazio.
- Se `scope=USER`, `userId` é obrigatório.

### Erro de duplicidade de code
- `code` precisa ser único dentro do escopo.
  - GLOBAL: único globalmente
  - USER: único por usuário

---

### Próximo passo sugerido
Quando você estiver pronto, podemos:
- criar um script `populate-templates.ps1` para popular templates padrão
- adicionar validação de schema (metadataSchema) no backend
