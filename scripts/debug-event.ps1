# Script de debug para testar criacao de eventos

$baseUrl = "http://localhost:8080/api/v1"
$userId = "550e8400-e29b-41d4-a716-446655440001"

Write-Host "1. Buscando items..." -ForegroundColor Yellow
$items = Invoke-RestMethod -Uri "$baseUrl/items?userId=$userId"
Write-Host "   Total: $($items.Count) items" -ForegroundColor Green

if ($items.Count -eq 0) {
    Write-Host "   Nenhum item encontrado!" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "2. Primeiro item:" -ForegroundColor Yellow
Write-Host "   Nome: $($items[0].name)" -ForegroundColor Gray
Write-Host "   ID: $($items[0].id)" -ForegroundColor Gray

Write-Host ""
Write-Host "3. Criando evento de teste..." -ForegroundColor Yellow

$eventBody = @{
    itemId = $items[0].id
    userId = $userId
    eventType = "CONSUMPTION"
    eventDate = (Get-Date).AddDays(-30).ToUniversalTime().ToString("o")
    description = "Teste de evento"
    metrics = @{
        value = 100
        cost = 150
    }
} | ConvertTo-Json -Depth 10

Write-Host "   Request Body:" -ForegroundColor Gray
Write-Host $eventBody -ForegroundColor DarkGray

try {
    $result = Invoke-RestMethod -Uri "$baseUrl/events" -Method POST -ContentType "application/json" -Body $eventBody
    Write-Host ""
    Write-Host "   SUCESSO!" -ForegroundColor Green
    Write-Host "   Event ID: $($result.id)" -ForegroundColor Green
} catch {
    Write-Host ""
    Write-Host "   ERRO!" -ForegroundColor Red
    Write-Host "   Message: $($_.Exception.Message)" -ForegroundColor Red

    if ($_.ErrorDetails -and $_.ErrorDetails.Message) {
        Write-Host "   Details: $($_.ErrorDetails.Message)" -ForegroundColor Red
    }

    $statusCode = $_.Exception.Response.StatusCode.value__
    if ($statusCode) {
        Write-Host "   Status Code: $statusCode" -ForegroundColor Red
    }
}
