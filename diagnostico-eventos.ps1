# ===================================================
# DIAGNOSTICO COMPLETO - Criacao de Eventos
# ===================================================

$ErrorActionPreference = "Continue"
$baseUrl = "http://localhost:8080/api/v1"
$userId = "550e8400-e29b-41d4-a716-446655440001"

Write-Host ""
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "DIAGNOSTICO - Criacao de Eventos" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""

# ===================================================
# PASSO 1: Verificar API
# ===================================================
Write-Host "PASSO 1: Verificando API..." -ForegroundColor Yellow
try {
    $health = Invoke-RestMethod -Uri "http://localhost:8080/actuator/health" -ErrorAction Stop
    Write-Host "  Status: $($health.status)" -ForegroundColor Green
} catch {
    Write-Host "  ERRO: API nao esta respondendo!" -ForegroundColor Red
    exit 1
}

# ===================================================
# PASSO 2: Buscar Items
# ===================================================
Write-Host ""
Write-Host "PASSO 2: Buscando items..." -ForegroundColor Yellow
try {
    $items = Invoke-RestMethod -Uri "$baseUrl/items?userId=$userId" -ErrorAction Stop
    Write-Host "  Total de items: $($items.Count)" -ForegroundColor Green

    if ($items.Count -eq 0) {
        Write-Host "  ERRO: Nenhum item encontrado!" -ForegroundColor Red
        exit 1
    }

    $testItem = $items[0]
    Write-Host "  Item de teste: $($testItem.name)" -ForegroundColor Cyan
    Write-Host "  ID: $($testItem.id)" -ForegroundColor Gray
    Write-Host "  Template: $($testItem.templateCode)" -ForegroundColor Gray

} catch {
    Write-Host "  ERRO ao buscar items: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# ===================================================
# PASSO 3: Testar criacao de evento - Versao 1
# ===================================================
Write-Host ""
Write-Host "PASSO 3: Testando criacao de evento (JSON basico)..." -ForegroundColor Yellow

$eventBody1 = @{
    itemId = $testItem.id
    userId = $userId
    eventType = "MAINTENANCE"
    eventDate = "2025-12-23T10:00:00Z"
    description = "Teste de evento - versao 1"
    metrics = @{
        value = 100
        cost = 150.50
    }
}

$json1 = $eventBody1 | ConvertTo-Json -Depth 10 -Compress:$false
Write-Host "  JSON gerado:" -ForegroundColor Gray
Write-Host $json1 -ForegroundColor DarkGray

try {
    $response1 = Invoke-WebRequest -Uri "$baseUrl/events" `
        -Method POST `
        -ContentType "application/json; charset=utf-8" `
        -Body $json1 `
        -UseBasicParsing `
        -ErrorAction Stop

    Write-Host "  SUCESSO! Status: $($response1.StatusCode)" -ForegroundColor Green
    $event1 = $response1.Content | ConvertFrom-Json
    Write-Host "  Event ID: $($event1.id)" -ForegroundColor Green

} catch {
    Write-Host "  FALHOU!" -ForegroundColor Red
    Write-Host "  Status Code: $($_.Exception.Response.StatusCode.value__)" -ForegroundColor Red
    Write-Host "  Mensagem: $($_.Exception.Message)" -ForegroundColor Red

    if ($_.Exception.Response) {
        try {
            $stream = $_.Exception.Response.GetResponseStream()
            $reader = New-Object System.IO.StreamReader($stream)
            $errorBody = $reader.ReadToEnd()
            Write-Host "  Resposta da API:" -ForegroundColor Yellow
            Write-Host $errorBody -ForegroundColor Red
        } catch {
            Write-Host "  Nao foi possivel ler resposta de erro" -ForegroundColor Gray
        }
    }
}

# ===================================================
# PASSO 4: Testar com Invoke-RestMethod
# ===================================================
Write-Host ""
Write-Host "PASSO 4: Testando com Invoke-RestMethod..." -ForegroundColor Yellow

$eventBody2 = @{
    itemId = $testItem.id
    userId = $userId
    eventType = "PAYMENT"
    eventDate = (Get-Date).AddDays(-10).ToUniversalTime().ToString("yyyy-MM-ddTHH:mm:ssZ")
    description = "Teste de evento - versao 2"
    metrics = @{
        amount = 250.00
        paid = $true
    }
}

$json2 = $eventBody2 | ConvertTo-Json -Depth 10
Write-Host "  JSON gerado:" -ForegroundColor Gray
Write-Host $json2 -ForegroundColor DarkGray

try {
    $event2 = Invoke-RestMethod -Uri "$baseUrl/events" `
        -Method POST `
        -ContentType "application/json" `
        -Body $json2 `
        -ErrorAction Stop

    Write-Host "  SUCESSO!" -ForegroundColor Green
    Write-Host "  Event ID: $($event2.id)" -ForegroundColor Green

} catch {
    Write-Host "  FALHOU!" -ForegroundColor Red

    if ($_.ErrorDetails -and $_.ErrorDetails.Message) {
        Write-Host "  Detalhes do erro:" -ForegroundColor Yellow
        Write-Host $_.ErrorDetails.Message -ForegroundColor Red
    } else {
        Write-Host "  Mensagem: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# ===================================================
# PASSO 5: Testar formato de data diferente
# ===================================================
Write-Host ""
Write-Host "PASSO 5: Testando formatos de data..." -ForegroundColor Yellow

$dates = @{
    "ISO 8601 com Z" = "2025-12-23T10:00:00Z"
    "ISO 8601 com offset" = "2025-12-23T10:00:00-03:00"
    "ISO 8601 sem timezone" = "2025-12-23T10:00:00"
    "PowerShell ToString(o)" = (Get-Date).AddDays(-5).ToUniversalTime().ToString("o")
}

foreach ($format in $dates.Keys) {
    $dateValue = $dates[$format]
    Write-Host "  Testando: $format" -ForegroundColor Cyan
    Write-Host "  Valor: $dateValue" -ForegroundColor Gray

    $eventBody = @{
        itemId = $testItem.id
        userId = $userId
        eventType = "UPDATE"
        eventDate = $dateValue
        description = "Teste de formato de data: $format"
    } | ConvertTo-Json -Depth 10

    try {
        $result = Invoke-RestMethod -Uri "$baseUrl/events" `
            -Method POST `
            -ContentType "application/json" `
            -Body $eventBody `
            -ErrorAction Stop

        Write-Host "    OK! Formato aceito" -ForegroundColor Green
        break  # Se funcionar, parar aqui

    } catch {
        Write-Host "    FALHOU com este formato" -ForegroundColor Red
        if ($_.ErrorDetails.Message) {
            Write-Host "    Erro: $($_.ErrorDetails.Message)" -ForegroundColor DarkRed
        }
    }
}

# ===================================================
# PASSO 6: Verificar eventos criados
# ===================================================
Write-Host ""
Write-Host "PASSO 6: Verificando eventos criados..." -ForegroundColor Yellow
try {
    $events = Invoke-RestMethod -Uri "$baseUrl/events?itemId=$($testItem.id)" -ErrorAction Stop
    Write-Host "  Total de eventos para este item: $($events.Count)" -ForegroundColor $(if ($events.Count -gt 0) { "Green" } else { "Yellow" })

    if ($events.Count -gt 0) {
        Write-Host "  Ultimo evento criado:" -ForegroundColor Cyan
        $lastEvent = $events[-1]
        Write-Host "    ID: $($lastEvent.id)" -ForegroundColor Gray
        Write-Host "    Tipo: $($lastEvent.eventType)" -ForegroundColor Gray
        Write-Host "    Descricao: $($lastEvent.description)" -ForegroundColor Gray
    }
} catch {
    Write-Host "  ERRO ao buscar eventos: $($_.Exception.Message)" -ForegroundColor Red
}

# ===================================================
# RESUMO
# ===================================================
Write-Host ""
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "FIM DO DIAGNOSTICO" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Proximos passos:" -ForegroundColor Yellow
Write-Host "  1. Verifique os logs acima para ver qual formato funcionou" -ForegroundColor Gray
Write-Host "  2. Se todos falharam, verifique logs da API Java" -ForegroundColor Gray
Write-Host "  3. Teste manualmente via Swagger UI" -ForegroundColor Gray
Write-Host ""
