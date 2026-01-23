# Teste de criacao de alerta

$items = Invoke-RestMethod -Uri "http://localhost:8080/api/v1/items?userId=550e8400-e29b-41d4-a716-446655440001"
$testItem = $items[0]

Write-Host "Testando alerta para: $($testItem.name)" -ForegroundColor Cyan
Write-Host "Item ID: $($testItem.id)" -ForegroundColor Gray

$alertBody = @{
    itemId = $testItem.id
    userId = "550e8400-e29b-41d4-a716-446655440001"
    ruleId = [Guid]::NewGuid().ToString()
    alertType = "SCHEDULED"
    title = "Teste de alerta"
    message = "Mensagem de teste"
    priority = 4
    dueAt = (Get-Date).AddDays(7).ToUniversalTime().ToString("yyyy-MM-ddTHH:mm:ssZ")
}

$json = $alertBody | ConvertTo-Json -Depth 10
Write-Host "JSON:" -ForegroundColor Yellow
Write-Host $json

try {
    $result = Invoke-WebRequest -Uri "http://localhost:8080/api/v1/alerts" `
        -Method POST `
        -ContentType "application/json" `
        -Body $json `
        -UseBasicParsing

    Write-Host "SUCESSO!" -ForegroundColor Green
    Write-Host $result.Content
} catch {
    Write-Host "ERRO: $($_.Exception.Message)" -ForegroundColor Red

    if ($_.Exception.Response) {
        $stream = $_.Exception.Response.GetResponseStream()
        $reader = New-Object System.IO.StreamReader($stream)
        $errorBody = $reader.ReadToEnd()
        Write-Host "Detalhe do erro:" -ForegroundColor Yellow
        Write-Host $errorBody
    }
}
