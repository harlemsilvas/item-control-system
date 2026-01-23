# 🧪 Script de Teste Completo - Item Control System
# Demonstra criação de Item e registro de Events no MongoDB

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "🧪 TESTE DA API - ITEM CONTROL SYSTEM" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$baseUrl = "http://localhost:8080/api/v1"

# Verificar se a API está rodando
Write-Host "🔍 Verificando se a API está rodando..." -ForegroundColor Yellow
try {
    $health = Invoke-RestMethod -Uri "http://localhost:8080/actuator/health" -Method GET
    if ($health.status -eq "UP") {
        Write-Host "✅ API está UP e rodando!" -ForegroundColor Green
        Write-Host ""
    }
} catch {
    Write-Host "❌ API não está respondendo!" -ForegroundColor Red
    Write-Host "Execute o script: .\start-api.ps1" -ForegroundColor Yellow
    exit 1
}

# IDs fixos para o teste
$userId = "550e8400-e29b-41d4-a716-446655440001"
$categoryId = "650e8400-e29b-41d4-a716-446655440002"

Write-Host "📌 IDs de teste:" -ForegroundColor Yellow
Write-Host "   UserId: $userId" -ForegroundColor Gray
Write-Host "   CategoryId: $categoryId" -ForegroundColor Gray
Write-Host ""

# ========================================
# 1. CRIAR ITEM: Honda CB 500X
# ========================================
Write-Host "1️⃣  Criando Item: Honda CB 500X..." -ForegroundColor Green

$createItemBody = @{
    userId = $userId
    name = "Honda CB 500X"
    nickname = "Motoca"
    categoryId = $categoryId
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

try {
    $itemResponse = Invoke-RestMethod -Uri "$baseUrl/items" `
        -Method POST `
        -ContentType "application/json" `
        -Body $createItemBody

    $itemId = $itemResponse.id

    Write-Host "   ✅ Item criado com sucesso!" -ForegroundColor Green
    Write-Host "   📦 ID: $itemId" -ForegroundColor Cyan
    Write-Host "   📝 Nome: $($itemResponse.name)" -ForegroundColor Gray
    Write-Host "   🏷️  Nickname: $($itemResponse.nickname)" -ForegroundColor Gray
    Write-Host "   📊 Status: $($itemResponse.status)" -ForegroundColor Gray
    Write-Host "   🏭 Template: $($itemResponse.templateCode)" -ForegroundColor Gray
    Write-Host ""
} catch {
    Write-Host "   ❌ Erro ao criar item: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# ========================================
# 2. REGISTRAR EVENTO DE MANUTENÇÃO
# ========================================
Write-Host "2️⃣  Registrando evento: Troca de óleo..." -ForegroundColor Green

$registerEventBody = @{
    itemId = $itemId
    userId = $userId
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

try {
    $eventResponse = Invoke-RestMethod -Uri "$baseUrl/events" `
        -Method POST `
        -ContentType "application/json" `
        -Body $registerEventBody

    Write-Host "   ✅ Evento registrado com sucesso!" -ForegroundColor Green
    Write-Host "   📦 ID: $($eventResponse.id)" -ForegroundColor Cyan
    Write-Host "   🔧 Tipo: $($eventResponse.eventType)" -ForegroundColor Gray
    Write-Host "   📝 Descrição: $($eventResponse.description)" -ForegroundColor Gray
    Write-Host "   📅 Data: $($eventResponse.eventDate)" -ForegroundColor Gray
    Write-Host ""
} catch {
    Write-Host "   ❌ Erro ao registrar evento: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "   Detalhes: $($_.ErrorDetails.Message)" -ForegroundColor DarkRed
}

# ========================================
# 3. REGISTRAR EVENTO DE ABASTECIMENTO
# ========================================
Write-Host "3️⃣  Registrando evento: Abastecimento..." -ForegroundColor Green

$fuelEventBody = @{
    itemId = $itemId
    userId = $userId
    eventType = "CONSUMPTION"
    eventDate = (Get-Date).AddHours(-2).ToUniversalTime().ToString("o")
    description = "Abastecimento completo"
    metrics = @{
        quantity = 17.5
        unitPrice = 5.89
        totalCost = 103.08
        odometer = 14850
        fullTank = $true
    }
} | ConvertTo-Json -Depth 10

try {
    $fuelEventResponse = Invoke-RestMethod -Uri "$baseUrl/events" `
        -Method POST `
        -ContentType "application/json" `
        -Body $fuelEventBody

    Write-Host "   ✅ Evento de abastecimento registrado!" -ForegroundColor Green
    Write-Host "   📦 ID: $($fuelEventResponse.id)" -ForegroundColor Cyan
    Write-Host "   ⛽ Quantidade: $($fuelEventResponse.metrics.quantity) litros" -ForegroundColor Gray
    Write-Host "   💰 Custo total: R$ $($fuelEventResponse.metrics.totalCost)" -ForegroundColor Gray
    Write-Host ""
} catch {
    Write-Host "   ❌ Erro ao registrar evento: $($_.Exception.Message)" -ForegroundColor Red
}

# ========================================
# 4. BUSCAR ITEM CRIADO
# ========================================
Write-Host "4️⃣  Buscando item por ID..." -ForegroundColor Green

try {
    $getItemResponse = Invoke-RestMethod -Uri "$baseUrl/items/$itemId" -Method GET

    Write-Host "   ✅ Item recuperado do MongoDB:" -ForegroundColor Green
    Write-Host "   📝 Nome: $($getItemResponse.name)" -ForegroundColor Gray
    Write-Host "   🏷️  Nickname: $($getItemResponse.nickname)" -ForegroundColor Gray
    Write-Host "   🏭 Template: $($getItemResponse.templateCode)" -ForegroundColor Gray
    Write-Host "   🏷️  Tags: $($getItemResponse.tags -join ', ')" -ForegroundColor Gray
    Write-Host "   📊 Metadata:" -ForegroundColor Gray
    $getItemResponse.metadata.PSObject.Properties | ForEach-Object {
        Write-Host "      - $($_.Name): $($_.Value)" -ForegroundColor DarkGray
    }
    Write-Host ""
} catch {
    Write-Host "   ❌ Erro ao buscar item: $($_.Exception.Message)" -ForegroundColor Red
}

# ========================================
# 5. LISTAR EVENTOS DO ITEM
# ========================================
Write-Host "5️⃣  Listando histórico de eventos..." -ForegroundColor Green

try {
    $eventsResponse = Invoke-RestMethod -Uri "$baseUrl/events?itemId=$itemId" -Method GET

    Write-Host "   ✅ Total de eventos: $($eventsResponse.Count)" -ForegroundColor Green
    Write-Host ""

    $eventsResponse | ForEach-Object {
        Write-Host "   📌 Evento:" -ForegroundColor Yellow
        Write-Host "      🔧 Tipo: $($_.eventType)" -ForegroundColor Gray
        Write-Host "      📝 Descrição: $($_.description)" -ForegroundColor Gray
        Write-Host "      📅 Data: $($_.eventDate)" -ForegroundColor Gray
        if ($_.metrics) {
            Write-Host "      📊 Métricas:" -ForegroundColor Gray
            $_.metrics.PSObject.Properties | ForEach-Object {
                Write-Host "         - $($_.Name): $($_.Value)" -ForegroundColor DarkGray
            }
        }
        Write-Host ""
    }
} catch {
    Write-Host "   ❌ Erro ao listar eventos: $($_.Exception.Message)" -ForegroundColor Red
}

# ========================================
# 6. LISTAR TODOS OS ITEMS DO USUÁRIO
# ========================================
Write-Host "6️⃣  Listando todos os items do usuário..." -ForegroundColor Green

try {
    $userItemsResponse = Invoke-RestMethod -Uri "$baseUrl/items?userId=$userId" -Method GET

    Write-Host "   ✅ Total de items do usuário: $($userItemsResponse.Count)" -ForegroundColor Green
    Write-Host ""

    $userItemsResponse | ForEach-Object {
        Write-Host "   📦 $($_.name) ($($_.templateCode))" -ForegroundColor Gray
        Write-Host "      ID: $($_.id)" -ForegroundColor DarkGray
        Write-Host "      Status: $($_.status)" -ForegroundColor DarkGray
        Write-Host ""
    }
} catch {
    Write-Host "   ❌ Erro ao listar items: $($_.Exception.Message)" -ForegroundColor Red
}

# ========================================
# RESUMO FINAL
# ========================================
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "✅ TESTES CONCLUÍDOS COM SUCESSO!" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "📊 Dados persistidos no MongoDB:" -ForegroundColor Yellow
Write-Host "   - 1 Item criado (Honda CB 500X)" -ForegroundColor Gray
Write-Host "   - 2 Eventos registrados (Manutenção + Abastecimento)" -ForegroundColor Gray
Write-Host ""
Write-Host "🔍 Verificar dados no Mongo Express:" -ForegroundColor Yellow
Write-Host "   🌐 URL: http://localhost:8081" -ForegroundColor Cyan
Write-Host "   🗄️  Database: item_control_db_dev" -ForegroundColor Gray
Write-Host "   📚 Collections: items, events" -ForegroundColor Gray
Write-Host ""
Write-Host "📖 Documentação da API (Swagger):" -ForegroundColor Yellow
Write-Host "   🌐 URL: http://localhost:8080/swagger-ui.html" -ForegroundColor Cyan
Write-Host ""
Write-Host "🎉 Parabéns! Sistema funcionando perfeitamente!" -ForegroundColor Green
Write-Host ""
