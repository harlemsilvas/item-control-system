# ============================================
# Script de Populacao - Dados de Teste
# Cria 15 items, 75 eventos, 30 alertas
# ============================================

$baseUrl = "http://localhost:8080/api/v1"
$userId = "550e8400-e29b-41d4-a716-446655440001"

Write-Host "Verificando API..." -ForegroundColor Yellow
try {
    $health = Invoke-RestMethod -Uri "http://localhost:8080/actuator/health"
    Write-Host "API OK!" -ForegroundColor Green
} catch {
    Write-Host "ERRO: API nao esta respondendo" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "Criando 15 items..." -ForegroundColor Cyan

# Array com os items
$items = @(
    @{name="Honda CB 500X"; nickname="Motoca"; template="VEHICLE"; category="650e8400-e29b-41d4-a716-446655440002"; brand="Honda"; model="CB 500X"; year=2020; plate="ABC-1234"; odometer=15000},
    @{name="Toyota Corolla"; nickname="Carro"; template="VEHICLE"; category="650e8400-e29b-41d4-a716-446655440002"; brand="Toyota"; model="Corolla"; year=2021; plate="XYZ-9876"; odometer=32000},
    @{name="Chevrolet Onix"; nickname="Onix"; template="VEHICLE"; category="650e8400-e29b-41d4-a716-446655440002"; brand="Chevrolet"; model="Onix"; year=2022; plate="DEF-4567"; odometer=18500},
    @{name="Yamaha Fazer"; nickname="Fazer"; template="VEHICLE"; category="650e8400-e29b-41d4-a716-446655440002"; brand="Yamaha"; model="Fazer 250"; year=2019; plate="GHI-7890"; odometer=42000},
    @{name="Fiat Uno"; nickname="Carrinho"; template="VEHICLE"; category="650e8400-e29b-41d4-a716-446655440002"; brand="Fiat"; model="Uno"; year=2018; plate="JKL-1357"; odometer=65000},
    @{name="Conta de Agua"; nickname="Agua"; template="UTILITY_BILL"; category="750e8400-e29b-41d4-a716-446655440003"; supplier="SABESP"; dueDay=10; avgCost=85.50},
    @{name="Conta de Luz"; nickname="Luz"; template="UTILITY_BILL"; category="750e8400-e29b-41d4-a716-446655440003"; supplier="Enel"; dueDay=15; avgCost=152.30},
    @{name="Internet"; nickname="WiFi"; template="UTILITY_BILL"; category="750e8400-e29b-41d4-a716-446655440003"; supplier="Vivo"; dueDay=5; avgCost=119.90},
    @{name="Condominio"; nickname="Condo"; template="UTILITY_BILL"; category="750e8400-e29b-41d4-a716-446655440003"; supplier="Sindico"; dueDay=8; avgCost=450.00},
    @{name="Celular"; nickname="Cel"; template="UTILITY_BILL"; category="750e8400-e29b-41d4-a716-446655440003"; supplier="Claro"; dueDay=12; avgCost=89.90},
    @{name="Galao Agua"; nickname="Galao"; template="CONSUMABLE_ITEM"; category="850e8400-e29b-41d4-a716-446655440004"; unitCost=12.50},
    @{name="Botijao Gas"; nickname="Gas"; template="CONSUMABLE_ITEM"; category="850e8400-e29b-41d4-a716-446655440004"; unitCost=95.00},
    @{name="Filtro Cafe"; nickname="Filtro"; template="CONSUMABLE_ITEM"; category="850e8400-e29b-41d4-a716-446655440004"; unitCost=8.90},
    @{name="Papel Higienico"; nickname="Papel"; template="CONSUMABLE_ITEM"; category="850e8400-e29b-41d4-a716-446655440004"; unitCost=18.50},
    @{name="Detergente"; nickname="Det"; template="CONSUMABLE_ITEM"; category="850e8400-e29b-41d4-a716-446655440004"; unitCost=2.90}
)

$createdItems = @()
$itemCount = 0

foreach ($item in $items) {
    $body = @{
        userId = $userId
        name = $item.name
        nickname = $item.nickname
        categoryId = $item.category
        templateCode = $item.template
        tags = @($item.template.ToLower())
        metadata = $item
    } | ConvertTo-Json -Depth 10

    try {
        $result = Invoke-RestMethod -Uri "$baseUrl/items" -Method POST -ContentType "application/json" -Body $body
        $createdItems += $result
        $itemCount++
        Write-Host "  [$itemCount/15] $($item.name)" -ForegroundColor Green
    } catch {
        Write-Host "  ERRO: $($item.name)" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "$itemCount items criados!" -ForegroundColor Green
Write-Host ""

# Criar eventos
Write-Host "Criando eventos para cada item..." -ForegroundColor Cyan
$eventCount = 0
$eventErrors = 0

foreach ($item in $createdItems) {
    Write-Host "  Item: $($item.name) (ID: $($item.id))" -ForegroundColor Gray

    for ($i = 1; $i -le 5; $i++) {
        $daysAgo = 30 * $i
        $eventDate = (Get-Date).AddDays(-$daysAgo).ToUniversalTime().ToString("yyyy-MM-ddTHH:mm:ssZ")

        # Determinar tipo de evento baseado no template do item
        $eventType = switch ($item.templateCode) {
            "VEHICLE" { "MAINTENANCE" }
            "UTILITY_BILL" { "PAYMENT" }
            "CONSUMABLE_ITEM" { "PURCHASE" }
            default { "UPDATE" }
        }

        # Estrutura correta conforme RegisterEventRequest.java
        $eventBody = @{
            itemId = $item.id
            userId = $userId
            eventType = $eventType
            eventDate = $eventDate
            description = "Evento $i - $daysAgo dias atras ($eventType)"
            metrics = @{
                value = (Get-Random -Minimum 50 -Maximum 500)
                cost = (Get-Random -Minimum 50 -Maximum 500)
            }
        } | ConvertTo-Json -Depth 10

        try {
            $result = Invoke-RestMethod -Uri "$baseUrl/events" -Method POST -ContentType "application/json" -Body $eventBody -ErrorAction Stop
            $eventCount++
            Write-Host "    OK: Evento $i criado" -ForegroundColor Green
        } catch {
            $eventErrors++
            Write-Host "    ERRO: $($_.Exception.Message)" -ForegroundColor Red
            if ($i -eq 1) {
                # Mostrar detalhes do primeiro erro
                if ($_.ErrorDetails.Message) {
                    Write-Host "    Detalhes: $($_.ErrorDetails.Message)" -ForegroundColor Yellow
                }
            }
        }
    }
}

Write-Host "$eventCount eventos criados!" -ForegroundColor Green
Write-Host ""

# Criar alertas
Write-Host ""
Write-Host "Criando alertas para cada item..." -ForegroundColor Cyan
$alertCount = 0
$alertErrors = 0

foreach ($item in $createdItems) {
    Write-Host "  Item: $($item.name)" -ForegroundColor Gray

    for ($i = 1; $i -le 2; $i++) {
        $priority = Get-Random -Minimum 2 -Maximum 5
        $daysAhead = 7 * $i
        $dueDate = (Get-Date).AddDays($daysAhead).ToUniversalTime().ToString("yyyy-MM-ddTHH:mm:ssZ")

        # Estrutura correta conforme CreateAlertRequest.java
        $alertBody = @{
            itemId = $item.id
            userId = $userId
            ruleId = [Guid]::NewGuid().ToString()
            alertType = "SCHEDULED"
            title = "Alerta $i para $($item.name)"
            message = "Vencimento em $daysAhead dias"
            priority = [int]$priority
            dueAt = $dueDate
        } | ConvertTo-Json -Depth 10

        try {
            $result = Invoke-RestMethod -Uri "$baseUrl/alerts" -Method POST -ContentType "application/json" -Body $alertBody -ErrorAction Stop
            $alertCount++
            Write-Host "    OK: Alerta $i criado (prioridade $priority)" -ForegroundColor Green
        } catch {
            $alertErrors++
            Write-Host "    ERRO: $($_.Exception.Message)" -ForegroundColor Red
            if ($i -eq 1) {
                # Mostrar detalhes do primeiro erro
                if ($_.ErrorDetails.Message) {
                    Write-Host "    Detalhes: $($_.ErrorDetails.Message)" -ForegroundColor Yellow
                }
            }
        }
    }
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "POPULACAO CONCLUIDA!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Resumo:" -ForegroundColor Yellow
Write-Host "  Items:         $itemCount criados" -ForegroundColor Green
Write-Host "  Eventos:       $eventCount criados ($eventErrors erros)" -ForegroundColor $(if ($eventErrors -gt 0) { "Yellow" } else { "Green" })
Write-Host "  Alertas:       $alertCount criados ($alertErrors erros)" -ForegroundColor $(if ($alertErrors -gt 0) { "Yellow" } else { "Green" })
Write-Host ""
Write-Host "Acesse:" -ForegroundColor Yellow
Write-Host "  Swagger: http://localhost:8080/swagger-ui.html" -ForegroundColor Cyan
Write-Host "  MongoDB: http://localhost:8081" -ForegroundColor Cyan
Write-Host ""
