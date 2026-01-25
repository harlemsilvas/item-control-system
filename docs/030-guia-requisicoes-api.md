# 🌐 GUIA DE REQUISIÇÕES - API RENDER

**URL Base:** `https://item-control-api.onrender.com`

---

## 📋 ENDPOINTS DISPONÍVEIS

### 1️⃣ HEALTH CHECK

**Verificar se API está online:**

```powershell
# PowerShell
Invoke-RestMethod -Uri "https://item-control-api.onrender.com/actuator/health"

# Ou com Invoke-WebRequest
$response = Invoke-WebRequest -Uri "https://item-control-api.onrender.com/actuator/health"
$response.Content
```

**cURL (Git Bash/Linux/Mac):**
```bash
curl https://item-control-api.onrender.com/actuator/health
```

**Resposta esperada:**
```json
{"status":"UP"}
```

---

## 📦 ITEMS (Itens)

### Criar Item (POST)

```powershell
# PowerShell
$body = @{
    name = "Meu Carro"
    nickname = "car-001"
    description = "Toyota Corolla 2020"
    template = "VEHICLE"
} | ConvertTo-Json

$item = Invoke-RestMethod -Uri "https://item-control-api.onrender.com/api/v1/items" `
    -Method POST `
    -Body $body `
    -ContentType "application/json"

# Ver resultado
$item | ConvertTo-Json
```

**cURL:**
```bash
curl -X POST https://item-control-api.onrender.com/api/v1/items \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Meu Carro",
    "nickname": "car-001",
    "description": "Toyota Corolla 2020",
    "template": "VEHICLE"
  }'
```

**Templates disponíveis:**
- `GENERAL` - Item genérico
- `VEHICLE` - Veículo (carro, moto)
- `RECURRING_BILL` - Conta recorrente (água, luz)
- `CONSUMABLE` - Consumível (botijão de gás)

### Listar Todos os Items (GET)

```powershell
# PowerShell
$items = Invoke-RestMethod -Uri "https://item-control-api.onrender.com/api/v1/items"
$items | ConvertTo-Json -Depth 3
```

**cURL:**
```bash
curl https://item-control-api.onrender.com/api/v1/items
```

### Buscar Item por ID (GET)

```powershell
# PowerShell
$id = "seu-item-id-aqui"
$item = Invoke-RestMethod -Uri "https://item-control-api.onrender.com/api/v1/items/$id"
$item | ConvertTo-Json
```

**cURL:**
```bash
curl https://item-control-api.onrender.com/api/v1/items/{id}
```

### Listar Items por Usuário (GET)

```powershell
# PowerShell
$userId = "550e8400-e29b-41d4-a716-446655440001"
$items = Invoke-RestMethod -Uri "https://item-control-api.onrender.com/api/v1/items?userId=$userId"
$items | ConvertTo-Json -Depth 3
```

**cURL:**
```bash
curl "https://item-control-api.onrender.com/api/v1/items?userId=550e8400-e29b-41d4-a716-446655440001"
```

---

## 📊 CATEGORIES (Categorias)

### Criar Categoria (POST)

```powershell
# PowerShell
$body = @{
    name = "Veículos"
    slug = "veiculos"
    description = "Categoria para veículos"
} | ConvertTo-Json

$category = Invoke-RestMethod -Uri "https://item-control-api.onrender.com/api/v1/categories" `
    -Method POST `
    -Body $body `
    -ContentType "application/json"

$category | ConvertTo-Json
```

**cURL:**
```bash
curl -X POST https://item-control-api.onrender.com/api/v1/categories \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Veículos",
    "slug": "veiculos",
    "description": "Categoria para veículos"
  }'
```

### Listar Todas as Categorias (GET)

```powershell
# PowerShell
$categories = Invoke-RestMethod -Uri "https://item-control-api.onrender.com/api/v1/categories"
$categories | ConvertTo-Json -Depth 3
```

**cURL:**
```bash
curl https://item-control-api.onrender.com/api/v1/categories
```

### Buscar Categorias Raiz (GET)

```powershell
# PowerShell
$rootCategories = Invoke-RestMethod -Uri "https://item-control-api.onrender.com/api/v1/categories/roots"
$rootCategories | ConvertTo-Json -Depth 3
```

**cURL:**
```bash
curl https://item-control-api.onrender.com/api/v1/categories/roots
```

---

## 📝 EVENTS (Eventos)

### Registrar Evento (POST)

```powershell
# PowerShell
$body = @{
    itemId = "seu-item-id"
    type = "MAINTENANCE"
    description = "Troca de óleo"
    value = 150.00
    metricValue = 85000  # km rodados
    eventDate = (Get-Date).ToString("yyyy-MM-ddTHH:mm:ss")
} | ConvertTo-Json

$event = Invoke-RestMethod -Uri "https://item-control-api.onrender.com/api/v1/events" `
    -Method POST `
    -Body $body `
    -ContentType "application/json"

$event | ConvertTo-Json
```

**cURL:**
```bash
curl -X POST https://item-control-api.onrender.com/api/v1/events \
  -H "Content-Type: application/json" \
  -d '{
    "itemId": "seu-item-id",
    "type": "MAINTENANCE",
    "description": "Troca de óleo",
    "value": 150.00,
    "metricValue": 85000,
    "eventDate": "2026-01-25T12:00:00"
  }'
```

**Tipos de evento:**
- `MAINTENANCE` - Manutenção
- `CONSUMPTION` - Consumo
- `PAYMENT` - Pagamento
- `REPLACEMENT` - Substituição
- `OTHER` - Outro

### Listar Eventos de um Item (GET)

```powershell
# PowerShell
$itemId = "seu-item-id"
$events = Invoke-RestMethod -Uri "https://item-control-api.onrender.com/api/v1/events?itemId=$itemId"
$events | ConvertTo-Json -Depth 3
```

**cURL:**
```bash
curl "https://item-control-api.onrender.com/api/v1/events?itemId={itemId}"
```

---

## 🔔 ALERTS (Alertas)

### Listar Alertas Pendentes (GET)

```powershell
# PowerShell
$userId = "550e8400-e29b-41d4-a716-446655440001"
$alerts = Invoke-RestMethod -Uri "https://item-control-api.onrender.com/api/v1/alerts/pending?userId=$userId"
$alerts | ConvertTo-Json -Depth 3
```

**cURL:**
```bash
curl "https://item-control-api.onrender.com/api/v1/alerts/pending?userId=550e8400-e29b-41d4-a716-446655440001"
```

### Listar Alertas de um Item (GET)

```powershell
# PowerShell
$itemId = "seu-item-id"
$alerts = Invoke-RestMethod -Uri "https://item-control-api.onrender.com/api/v1/alerts/item/$itemId"
$alerts | ConvertTo-Json -Depth 3
```

**cURL:**
```bash
curl https://item-control-api.onrender.com/api/v1/alerts/item/{itemId}
```

### Marcar Alerta como Lido (PUT)

```powershell
# PowerShell
$alertId = "seu-alert-id"
Invoke-RestMethod -Uri "https://item-control-api.onrender.com/api/v1/alerts/$alertId/read" `
    -Method PUT
```

**cURL:**
```bash
curl -X PUT https://item-control-api.onrender.com/api/v1/alerts/{alertId}/read
```

---

## 🗄️ DATABASE ADMIN

### Listar Collections (GET)

```powershell
# PowerShell
$collections = Invoke-RestMethod -Uri "https://item-control-api.onrender.com/api/v1/admin/database/collections"
$collections | ConvertTo-Json
```

**cURL:**
```bash
curl https://item-control-api.onrender.com/api/v1/admin/database/collections
```

### Criar Collections Manualmente (POST)

```powershell
# PowerShell
$result = Invoke-RestMethod -Uri "https://item-control-api.onrender.com/api/v1/admin/database/collections/create" `
    -Method POST
$result | ConvertTo-Json
```

**cURL:**
```bash
curl -X POST https://item-control-api.onrender.com/api/v1/admin/database/collections/create
```

### Health Check MongoDB (GET)

```powershell
# PowerShell
$health = Invoke-RestMethod -Uri "https://item-control-api.onrender.com/api/v1/admin/database/health"
$health | ConvertTo-Json
```

**cURL:**
```bash
curl https://item-control-api.onrender.com/api/v1/admin/database/health
```

---

## 🧪 EXEMPLO COMPLETO: Criar Item e Registrar Evento

```powershell
# 1. Criar um item
$itemBody = @{
    name = "Meu Carro"
    nickname = "car-001"
    description = "Toyota Corolla 2020"
    template = "VEHICLE"
} | ConvertTo-Json

$item = Invoke-RestMethod -Uri "https://item-control-api.onrender.com/api/v1/items" `
    -Method POST `
    -Body $itemBody `
    -ContentType "application/json"

Write-Host "✅ Item criado: $($item.id)" -ForegroundColor Green

# 2. Registrar evento de manutenção
$eventBody = @{
    itemId = $item.id
    type = "MAINTENANCE"
    description = "Troca de óleo e filtro"
    value = 180.00
    metricValue = 85000
    eventDate = (Get-Date).ToString("yyyy-MM-ddTHH:mm:ss")
} | ConvertTo-Json

$event = Invoke-RestMethod -Uri "https://item-control-api.onrender.com/api/v1/events" `
    -Method POST `
    -Body $eventBody `
    -ContentType "application/json"

Write-Host "✅ Evento registrado: $($event.id)" -ForegroundColor Green

# 3. Listar eventos do item
$events = Invoke-RestMethod -Uri "https://item-control-api.onrender.com/api/v1/events?itemId=$($item.id)"
Write-Host "📝 Total de eventos: $($events.Count)" -ForegroundColor Cyan

# 4. Ver alertas (se houver)
$alerts = Invoke-RestMethod -Uri "https://item-control-api.onrender.com/api/v1/alerts/item/$($item.id)"
Write-Host "🔔 Total de alertas: $($alerts.Count)" -ForegroundColor Yellow
```

---

## 🌐 SWAGGER UI (Interface Visual)

**URL:** https://item-control-api.onrender.com/swagger-ui.html

No Swagger você pode:
- ✅ Ver todos os endpoints
- ✅ Testar requisições diretamente no navegador
- ✅ Ver exemplos de request/response
- ✅ Gerar código de exemplo

---

## 🔧 TROUBLESHOOTING

### Erro 503 (Service Unavailable)

```
API está em "cold start" (primeira requisição após hibernar)
Aguarde 30-60 segundos e tente novamente
```

### Erro 400 (Bad Request)

```
Corpo da requisição inválido
Verifique se JSON está correto
Campos obrigatórios: name, nickname, template (para items)
```

### Erro 404 (Not Found)

```
Endpoint ou ID não encontrado
Verifique URL e IDs usados
```

### Timeout

```
App pode estar iniciando
Execute: .\scripts\monitor-render-deploy.ps1
Aguarde deploy completar
```

---

## 📱 TESTAR NO POSTMAN

### 1. Importar Collection

Criar nova Collection no Postman com:
- **Base URL:** `https://item-control-api.onrender.com`
- **Headers:** `Content-Type: application/json`

### 2. Requests Básicos

**Health Check:**
- Method: GET
- URL: `{{baseUrl}}/actuator/health`

**Create Item:**
- Method: POST
- URL: `{{baseUrl}}/api/v1/items`
- Body (raw JSON):
```json
{
  "name": "Meu Item",
  "nickname": "item-001",
  "template": "GENERAL"
}
```

---

## ⚡ SCRIPT RÁPIDO DE TESTE

```powershell
# Salvar como: test-api.ps1

$baseUrl = "https://item-control-api.onrender.com"

Write-Host "🧪 Testando API..." -ForegroundColor Cyan

# 1. Health
try {
    $health = Invoke-RestMethod "$baseUrl/actuator/health"
    Write-Host "✅ Health: $($health.status)" -ForegroundColor Green
} catch {
    Write-Host "❌ Health falhou" -ForegroundColor Red
    exit
}

# 2. Criar item
$itemBody = @{
    name = "Item Teste $(Get-Date -Format 'HH:mm:ss')"
    nickname = "test-$(Get-Random)"
    template = "GENERAL"
} | ConvertTo-Json

try {
    $item = Invoke-RestMethod "$baseUrl/api/v1/items" -Method POST -Body $itemBody -ContentType "application/json"
    Write-Host "✅ Item criado: $($item.nickname)" -ForegroundColor Green
} catch {
    Write-Host "❌ Falha ao criar item: $($_.Exception.Message)" -ForegroundColor Red
}

# 3. Listar items
try {
    $items = Invoke-RestMethod "$baseUrl/api/v1/items"
    Write-Host "✅ Total de items: $($items.Count)" -ForegroundColor Green
} catch {
    Write-Host "❌ Falha ao listar items" -ForegroundColor Red
}

Write-Host "`n🎉 Testes concluídos!" -ForegroundColor Cyan
```

---

**Pronto para testar a API! 🚀**
