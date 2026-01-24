# ===================================================
# DIAGNOSTICO COMPLETO - Criacao de Alertas
# ===================================================

$ErrorActionPreference = "Continue"
$baseUrl = "http://localhost:8080/api/v1"
$userId = "550e8400-e29b-41d4-a716-446655440001"

Write-Host ""
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "DIAGNOSTICO - Criacao de Alertas" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""

# ===================================================
# PASSO 1: Buscar item de teste
# ===================================================
Write-Host "PASSO 1: Buscando item de teste..." -ForegroundColor Yellow
try {
    $items = Invoke-RestMethod -Uri "$baseUrl/items?userId=$userId" -ErrorAction Stop
    $testItem = $items[0]
    Write-Host "  Item: $($testItem.name)" -ForegroundColor Green
    Write-Host "  ID: $($testItem.id)" -ForegroundColor Cyan
} catch {
    Write-Host "  ERRO ao buscar items!" -ForegroundColor Red
    exit 1
}

# ===================================================
# PASSO 2: Testar formato de data que funcionou para eventos
# ===================================================
Write-Host ""
Write-Host "PASSO 2: Testando com formato que funcionou (yyyy-MM-ddTHH:mm:ssZ)..." -ForegroundColor Yellow

$dueDate1 = (Get-Date).AddDays(7).ToUniversalTime().ToString("yyyy-MM-ddTHH:mm:ssZ")

$alertBody1 = @{
    itemId = $testItem.id
    userId = $userId
    ruleId = [Guid]::NewGuid().ToString()
    alertType = "SCHEDULED"
    title = "Teste 1 - Formato eventos"
    message = "Testando formato yyyy-MM-ddTHH:mm:ssZ"
    priority = 4
    dueAt = $dueDate1
}

$json1 = $alertBody1 | ConvertTo-Json -Depth 10
Write-Host "  JSON gerado:" -ForegroundColor Gray
Write-Host $json1 -ForegroundColor DarkGray

try {
    $response1 = Invoke-WebRequest -Uri "$baseUrl/alerts" `
        -Method POST `
        -ContentType "application/json; charset=utf-8" `
        -Body $json1 `
        -UseBasicParsing

    Write-Host "  SUCESSO! Status: $($response1.StatusCode)" -ForegroundColor Green
    $alert1 = $response1.Content | ConvertFrom-Json
    Write-Host "  Alert ID: $($alert1.id)" -ForegroundColor Green

} catch {
    Write-Host "  FALHOU!" -ForegroundColor Red
    Write-Host "  Status: $($_.Exception.Response.StatusCode.value__)" -ForegroundColor Red

    if ($_.Exception.Response) {
        $stream = $_.Exception.Response.GetResponseStream()
        $reader = New-Object System.IO.StreamReader($stream)
        $errorBody = $reader.ReadToEnd()
        Write-Host "  Erro detalhado:" -ForegroundColor Yellow
        Write-Host $errorBody -ForegroundColor Red
    }
}

# ===================================================
# PASSO 3: Testar sem campo opcional triggeredAt
# ===================================================
Write-Host ""
Write-Host "PASSO 3: Testando SEM triggeredAt (campo opcional)..." -ForegroundColor Yellow

$alertBody2 = @{
    itemId = $testItem.id
    userId = $userId
    ruleId = [Guid]::NewGuid().ToString()
    alertType = "SCHEDULED"
    title = "Teste 2 - Sem triggeredAt"
    message = "Campo triggeredAt omitido"
    priority = 3
    dueAt = (Get-Date).AddDays(10).ToUniversalTime().ToString("yyyy-MM-ddTHH:mm:ssZ")
}

$json2 = $alertBody2 | ConvertTo-Json -Depth 10
Write-Host "  JSON:" -ForegroundColor Gray
Write-Host $json2 -ForegroundColor DarkGray

try {
    $result2 = Invoke-RestMethod -Uri "$baseUrl/alerts" `
        -Method POST `
        -ContentType "application/json" `
        -Body $json2

    Write-Host "  SUCESSO!" -ForegroundColor Green
    Write-Host "  Alert ID: $($result2.id)" -ForegroundColor Green

} catch {
    Write-Host "  FALHOU!" -ForegroundColor Red
    if ($_.ErrorDetails.Message) {
        Write-Host "  Erro: $($_.ErrorDetails.Message)" -ForegroundColor Red
    }
}

# ===================================================
# PASSO 4: Testar diferentes tipos de AlertType
# ===================================================
Write-Host ""
Write-Host "PASSO 4: Testando diferentes AlertTypes..." -ForegroundColor Yellow

$alertTypes = @("SCHEDULED", "THRESHOLD", "URGENT", "REMINDER")

foreach ($type in $alertTypes) {
    Write-Host "  Testando: $type" -ForegroundColor Cyan

    $alertBody = @{
        itemId = $testItem.id
        userId = $userId
        ruleId = [Guid]::NewGuid().ToString()
        alertType = $type
        title = "Teste tipo $type"
        message = "Testando AlertType $type"
        priority = 3
        dueAt = (Get-Date).AddDays(5).ToUniversalTime().ToString("yyyy-MM-ddTHH:mm:ssZ")
    } | ConvertTo-Json -Depth 10

    try {
        $result = Invoke-RestMethod -Uri "$baseUrl/alerts" `
            -Method POST `
            -ContentType "application/json" `
            -Body $alertBody

        Write-Host "    OK! $type aceito" -ForegroundColor Green

    } catch {
        Write-Host "    FALHOU com $type" -ForegroundColor Red
        if ($_.ErrorDetails.Message) {
            Write-Host "    Erro: $($_.ErrorDetails.Message)" -ForegroundColor DarkRed
        }
    }
}

# ===================================================
# PASSO 5: Testar diferentes valores de prioridade
# ===================================================
Write-Host ""
Write-Host "PASSO 5: Testando diferentes prioridades (1-5)..." -ForegroundColor Yellow

for ($priority = 1; $priority -le 5; $priority++) {
    Write-Host "  Testando prioridade: $priority" -ForegroundColor Cyan

    $alertBody = @{
        itemId = $testItem.id
        userId = $userId
        ruleId = [Guid]::NewGuid().ToString()
        alertType = "SCHEDULED"
        title = "Teste prioridade $priority"
        message = "Testando prioridade $priority"
        priority = $priority
        dueAt = (Get-Date).AddDays(3).ToUniversalTime().ToString("yyyy-MM-ddTHH:mm:ssZ")
    } | ConvertTo-Json -Depth 10

    try {
        $result = Invoke-RestMethod -Uri "$baseUrl/alerts" `
            -Method POST `
            -ContentType "application/json" `
            -Body $alertBody

        Write-Host "    OK! Prioridade $priority aceita" -ForegroundColor Green

    } catch {
        Write-Host "    FALHOU com prioridade $priority" -ForegroundColor Red
    }
}

# ===================================================
# PASSO 6: Testar formatos de data diferentes
# ===================================================
Write-Host ""
Write-Host "PASSO 6: Testando formatos de data..." -ForegroundColor Yellow

$dateFormats = @{
    "yyyy-MM-ddTHH:mm:ssZ" = (Get-Date).AddDays(7).ToUniversalTime().ToString("yyyy-MM-ddTHH:mm:ssZ")
    "yyyy-MM-dd'T'HH:mm:ss'Z'" = (Get-Date).AddDays(7).ToUniversalTime().ToString("yyyy-MM-dd'T'HH:mm:ss'Z'")
    "Data fixa ISO" = "2026-02-15T00:00:00Z"
    "Data fixa sem segundos" = "2026-02-15T00:00Z"
}

foreach ($formatName in $dateFormats.Keys) {
    $dateValue = $dateFormats[$formatName]
    Write-Host "  Testando: $formatName" -ForegroundColor Cyan
    Write-Host "  Valor: $dateValue" -ForegroundColor Gray

    $alertBody = @{
        itemId = $testItem.id
        userId = $userId
        ruleId = [Guid]::NewGuid().ToString()
        alertType = "SCHEDULED"
        title = "Teste data - $formatName"
        message = "Formato: $formatName"
        priority = 3
        dueAt = $dateValue
    } | ConvertTo-Json -Depth 10

    try {
        $result = Invoke-RestMethod -Uri "$baseUrl/alerts" `
            -Method POST `
            -ContentType "application/json" `
            -Body $alertBody

        Write-Host "    OK! Formato aceito: $formatName" -ForegroundColor Green

    } catch {
        Write-Host "    FALHOU com $formatName" -ForegroundColor Red
        if ($_.ErrorDetails.Message) {
            Write-Host "    Detalhe: $($_.ErrorDetails.Message)" -ForegroundColor DarkRed
        }
    }
}

# ===================================================
# PASSO 7: Testar com priority como inteiro explícito
# ===================================================
Write-Host ""
Write-Host "PASSO 7: Testando priority como [int] explicito..." -ForegroundColor Yellow

$alertBody7 = @{
    itemId = $testItem.id
    userId = $userId
    ruleId = [Guid]::NewGuid().ToString()
    alertType = "SCHEDULED"
    title = "Teste int explicito"
    message = "Priority como [int]"
    priority = [int]4
    dueAt = (Get-Date).AddDays(7).ToUniversalTime().ToString("yyyy-MM-ddTHH:mm:ssZ")
}

$json7 = $alertBody7 | ConvertTo-Json -Depth 10
Write-Host "  JSON:" -ForegroundColor Gray
Write-Host $json7 -ForegroundColor DarkGray

try {
    $result7 = Invoke-RestMethod -Uri "$baseUrl/alerts" `
        -Method POST `
        -ContentType "application/json" `
        -Body $json7

    Write-Host "  SUCESSO com [int] explicito!" -ForegroundColor Green

} catch {
    Write-Host "  FALHOU!" -ForegroundColor Red
    if ($_.ErrorDetails.Message) {
        Write-Host "  Erro: $($_.ErrorDetails.Message)" -ForegroundColor Red
    }
}

# ===================================================
# PASSO 8: Verificar alertas criados
# ===================================================
Write-Host ""
Write-Host "PASSO 8: Verificando alertas criados..." -ForegroundColor Yellow
try {
    $alerts = Invoke-RestMethod -Uri "$baseUrl/alerts/pending?userId=$userId"
    Write-Host "  Total de alertas pendentes: $($alerts.Count)" -ForegroundColor $(if ($alerts.Count -gt 0) { "Green" } else { "Yellow" })

    if ($alerts.Count -gt 0) {
        Write-Host "  Ultimo alerta criado:" -ForegroundColor Cyan
        $lastAlert = $alerts[-1]
        Write-Host "    ID: $($lastAlert.id)" -ForegroundColor Gray
        Write-Host "    Tipo: $($lastAlert.alertType)" -ForegroundColor Gray
        Write-Host "    Titulo: $($lastAlert.title)" -ForegroundColor Gray
        Write-Host "    Prioridade: $($lastAlert.priority)" -ForegroundColor Gray
    }
} catch {
    Write-Host "  ERRO ao buscar alertas: $($_.Exception.Message)" -ForegroundColor Red
}

# ===================================================
# RESUMO
# ===================================================
Write-Host ""
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "FIM DO DIAGNOSTICO" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Analise os resultados acima para identificar:" -ForegroundColor Yellow
Write-Host "  1. Qual formato de data funciona" -ForegroundColor Gray
Write-Host "  2. Se algum AlertType especifico falha" -ForegroundColor Gray
Write-Host "  3. Se prioridade precisa ser int ou pode ser number" -ForegroundColor Gray
Write-Host "  4. Se ha outro campo causando o erro" -ForegroundColor Gray
Write-Host ""
