# 🧪 Guia Rápido de Testes - Item Control System

**Objetivo:** Validar que a API está funcionando e gravando dados no MongoDB

---

## ✅ Pré-requisitos

1. **Docker** rodando com MongoDB:
   ```bash
   docker compose up -d
   docker ps  # deve mostrar: item-control-mongodb e item-control-mongo-express
   ```

2. **Projeto compilado**:
   ```bash
   mvn clean install -DskipTests
   ```

3. **API iniciada** (escolha uma opção):
   ```bash
   # Opção A: Via script
   .\start-api.ps1
   
   # Opção B: Diretamente
   cd modules/api
   java -jar target/item-control-api-0.1.0-SNAPSHOT.jar --spring.profiles.active=dev
   ```

---

## 🔍 Verificação 1: API está rodando?

```bash
# PowerShell
Invoke-RestMethod -Uri "http://localhost:8082/actuator/health"

# Deve retornar:
# status: UP
```

Ou acesse no navegador: http://localhost:8082/actuator/health

---

## 🔍 Verificação 2: Swagger UI

Acesse: **http://localhost:8082/swagger-ui.html**

Você deve ver:
- ✅ Item Controller (4 endpoints)
- ✅ Event Controller (3 endpoints)

---

## 📝 Teste 1: Criar um Item

### Via Swagger UI

1. Acesse http://localhost:8082/swagger-ui.html
2. Expanda **POST /api/v1/items**
3. Clique em **Try it out**
4. Cole este JSON:

```json
{
  "userId": "550e8400-e29b-41d4-a716-446655440001",
  "name": "Honda CB 500X",
  "nickname": "Motoca",
  "categoryId": "650e8400-e29b-41d4-a716-446655440002",
  "templateCode": "VEHICLE",
  "tags": ["moto", "honda", "transporte"],
  "metadata": {
    "brand": "Honda",
    "model": "CB 500X",
    "year": 2020,
    "plate": "ABC-1234",
    "color": "Vermelha"
  }
}
```

5. Clique em **Execute**
6. **Resultado esperado**: Status `201 Created` + JSON com `id` do item criado
7. **COPIE O ID** retornado - você vai usar nos próximos testes!

### Via PowerShell

```powershell
$body = @{
  userId = "550e8400-e29b-41d4-a716-446655440001"
  name = "Honda CB 500X"
  nickname = "Motoca"
  categoryId = "650e8400-e29b-41d4-a716-446655440002"
  templateCode = "VEHICLE"
  tags = @("moto", "honda", "transporte")
  metadata = @{
    brand = "Honda"
    model = "CB 500X"
    year = 2020
    plate = "ABC-1234"
    color = "Vermelha"
  }
} | ConvertTo-Json -Depth 10

$response = Invoke-RestMethod -Uri "http://localhost:8082/api/v1/items" `
  -Method POST `
  -ContentType "application/json" `
  -Body $body

Write-Host "Item criado com ID: $($response.id)"
$itemId = $response.id
```

---

## 📝 Teste 2: Registrar um Evento

**IMPORTANTE:** Substitua `{itemId}` pelo ID do item que você criou no Teste 1!

### Via Swagger UI

1. Expanda **POST /api/v1/events**
2. Clique em **Try it out**
3. Cole este JSON (substitua o `itemId`):

```json
{
  "itemId": "{COLE-O-ID-DO-ITEM-AQUI}",
  "userId": "550e8400-e29b-41d4-a716-446655440001",
  "eventType": "MAINTENANCE",
  "eventDate": "2026-01-22T19:00:00Z",
  "description": "Troca de óleo e filtro",
  "metrics": {
    "odometer": 15000,
    "cost": 350.00,
    "serviceName": "Troca de óleo completa",
    "nextMaintenanceKm": 20000
  }
}
```

4. Clique em **Execute**
5. **Resultado esperado**: Status `201 Created` + JSON com o evento

### Via PowerShell

```powershell
# Use o $itemId da etapa anterior
$eventBody = @{
  itemId = $itemId
  userId = "550e8400-e29b-41d4-a716-446655440001"
  eventType = "MAINTENANCE"
  eventDate = (Get-Date).ToUniversalTime().ToString("o")
  description = "Troca de óleo e filtro"
  metrics = @{
    odometer = 15000
    cost = 350.00
    serviceName = "Troca de óleo completa"
    nextMaintenanceKm = 20000
  }
} | ConvertTo-Json -Depth 10

$eventResponse = Invoke-RestMethod -Uri "http://localhost:8082/api/v1/events" `
  -Method POST `
  -ContentType "application/json" `
  -Body $eventBody

Write-Host "Evento registrado com ID: $($eventResponse.id)"
```

---

## 📝 Teste 3: Buscar o Item Criado

### Via Swagger UI

1. Expanda **GET /api/v1/items/{id}**
2. Clique em **Try it out**
3. Cole o ID do item no campo `id`
4. Clique em **Execute**
5. **Resultado esperado**: JSON completo do item

### Via PowerShell

```powershell
$item = Invoke-RestMethod -Uri "http://localhost:8082/api/v1/items/$itemId"
Write-Host "Nome: $($item.name)"
Write-Host "Nickname: $($item.nickname)"
Write-Host "Status: $($item.status)"
```

---

## 📝 Teste 4: Listar Eventos do Item

### Via Swagger UI

1. Expanda **GET /api/v1/events**
2. Clique em **Try it out**
3. No campo `itemId`, cole o ID do item
4. Clique em **Execute**
5. **Resultado esperado**: Array com todos os eventos do item

### Via PowerShell

```powershell
$events = Invoke-RestMethod -Uri "http://localhost:8082/api/v1/events?itemId=$itemId"
Write-Host "Total de eventos: $($events.Count)"
$events | ForEach-Object {
  Write-Host "- $($_.eventType): $($_.description)"
}
```

---

## 🗄️ Verificação no MongoDB

### Via Mongo Express (Interface Web)

1. Acesse: **http://localhost:8081**
2. Clique em **item_control_db_dev**
3. Veja as collections:
   - **items** - deve ter 1 documento (a Honda CB 500X)
   - **events** - deve ter 1 ou mais documentos (eventos registrados)
4. Clique em **items** → **View documents**
5. Você deve ver o item completo com metadata

### Via MongoDB CLI

```bash
# Entrar no container
docker exec -it item-control-mongodb mongosh

# No prompt do MongoDB:
use item_control_db_dev

# Ver items
db.items.find().pretty()

# Ver events
db.events.find().pretty()

# Contar documentos
db.items.countDocuments()
db.events.countDocuments()

# Sair
exit
```

---

## 📊 Checklist de Validação

- [ ] API iniciou sem erros
- [ ] Health endpoint retorna `status: UP`
- [ ] Swagger UI acessível
- [ ] Item criado via API (retornou 201)
- [ ] Evento registrado via API (retornou 201)
- [ ] Item buscado por ID (retornou 200)
- [ ] Eventos listados (retornou 200)
- [ ] Item visível no Mongo Express
- [ ] Evento visível no Mongo Express
- [ ] MongoDB CLI funciona

---

## 🎯 Teste Completo com Script

Execute o script automatizado:

```powershell
.\test-api.ps1
```

Este script:
1. Cria um item (Honda CB 500X)
2. Registra evento de manutenção
3. Registra evento de abastecimento
4. Busca o item criado
5. Lista eventos
6. Lista items do usuário
7. Mostra resumo com links

---

## ❌ Problemas Comuns

### Erro: "Connection refused"
```
✅ Solução:
1. Verifique se a API está rodando:
   Get-Process java
2. Verifique se está na porta correta (8082):
   netstat -ano | findstr "8082"
3. Reinicie a API
```

### Erro: "MongoTimeoutException"
```
✅ Solução:
1. Verifique se MongoDB está rodando:
   docker ps
2. Se não estiver, inicie:
   docker compose up -d
3. Aguarde 5 segundos e tente novamente
```

### Erro: "ItemNotFoundException" ao registrar evento
```
✅ Solução:
1. Certifique-se de usar o ID correto do item
2. Verifique se o item existe:
   GET /api/v1/items/{id}
```

### Erro 400 "Bad Request"
```
✅ Solução:
1. Verifique o JSON enviado (vírgulas, aspas)
2. Todos os campos obrigatórios presentes?
   - userId, name, categoryId, templateCode (para Item)
   - itemId, userId, eventType, eventDate (para Event)
```

---

## 🎉 Resultado Esperado

Se todos os testes passaram, você tem:

✅ **API funcionando perfeitamente**  
✅ **Dados sendo gravados no MongoDB**  
✅ **Endpoints REST respondendo corretamente**  
✅ **Swagger UI funcional**  
✅ **Sistema pronto para desenvolvimento de novas features**

---

## 📚 Próximos Passos

Após validar que tudo funciona:

1. **Criar mais items de teste** (conta de água, galão de água, etc.)
2. **Registrar diversos eventos** para cada item
3. **Explorar outros endpoints** (update metadata, etc.)
4. **Testar filtros e consultas**
5. **Implementar AlertRepository** (Sprint 2)

---

**Última atualização:** 22/01/2026 19:20  
**Versão:** 1.0

