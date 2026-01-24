# Teste manual de criacao de evento com detalhes do erro

$itemId = "d88f2dd6-f92c-4ddf-81c1-9f3e8d6c0f7a"  # ID da Honda CB 500X
$userId = "550e8400-e29b-41d4-a716-446655440001"

$eventBody = @{
    itemId = $itemId
    userId = $userId
    eventType = "MAINTENANCE"
    eventDate = "2025-12-23T10:00:00Z"
    description = "Teste de evento"
    metrics = @{
        value = 100
        cost = 150
    }
}

$json = $eventBody | ConvertTo-Json -Depth 10

Write-Host "Testando criar evento..." -ForegroundColor Yellow
Write-Host "Body JSON:" -ForegroundColor Cyan
Write-Host $json

try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/v1/events" `
        -Method POST `
        -ContentType "application/json" `
        -Body $json `
        -UseBasicParsing

    Write-Host ""
    Write-Host "SUCESSO!" -ForegroundColor Green
    Write-Host $response.Content
} catch {
    Write-Host ""
    Write-Host "ERRO:" -ForegroundColor Red
    Write-Host "Status: $($_.Exception.Response.StatusCode.value__)" -ForegroundColor Red
    Write-Host "Message: $($_.Exception.Message)" -ForegroundColor Red

    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        Write-Host "Response Body:" -ForegroundColor Yellow
        Write-Host $responseBody
    }
}
