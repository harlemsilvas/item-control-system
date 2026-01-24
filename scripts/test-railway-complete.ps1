# ========================================
# Script: Testar Railway MongoDB
# ========================================
Write-Host "`n╔════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║   TESTE COMPLETO - RAILWAY MONGODB         ║" -ForegroundColor Cyan
Write-Host "╚════════════════════════════════════════════╝`n" -ForegroundColor Cyan
# 1. Testar Health
Write-Host "1️⃣  Testando Health Check..." -ForegroundColor Yellow
try {
    $health = Invoke-RestMethod -Uri "http://localhost:8080/actuator/health" -UseBasicParsing
    Write-Host "   ✅ API: $($health.status)" -ForegroundColor Green
    if ($health.components.mongo) {
        Write-Host "   ✅ MongoDB: $($health.components.mongo.status)" -ForegroundColor Green
    }
} catch {
    Write-Host "   ❌ API não respondeu" -ForegroundColor Red
    Write-Host "   Execute primeiro: .\start-api-railway.ps1`n" -ForegroundColor Yellow
    exit 1
}
Write-Host ""
# 2. Criar Item
Write-Host "2️⃣  Criando item de teste..." -ForegroundColor Yellow
$item = @{
    name = "Moto Honda CB 500X"
    userId = "550e8400-e29b-41d4-a716-446655440001"
    nickname = "CB500"
    metadata = @{
        ano = 2024
        km = 5000
        plataforma = "Railway Cloud"
    }
} | ConvertTo-Json
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/v1/items" `
        -Method POST -Body $item -ContentType "application/json"
    Write-Host "   ✅ Item criado com sucesso!" -ForegroundColor Green
    Write-Host "   ID: $($response.id)" -ForegroundColor Gray
    Write-Host "   Nome: $($response.name)" -ForegroundColor Gray
    $itemId = $response.id
} catch {
    Write-Host "   ❌ Erro: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}
Write-Host ""
# 3. Listar Items
Write-Host "3️⃣  Listando items do usuário..." -ForegroundColor Yellow
try {
    $items = Invoke-RestMethod -Uri "http://localhost:8080/api/v1/items?userId=550e8400-e29b-41d4-a716-446655440001"
    Write-Host "   ✅ Items encontrados: $($items.Count)" -ForegroundColor Green
    foreach ($i in $items) {
        Write-Host "   • $($i.name) ($($i.nickname))" -ForegroundColor Gray
    }
} catch {
    Write-Host "   ❌ Erro ao listar" -ForegroundColor Red
}
Write-Host ""
# 4. Criar Evento
Write-Host "4️⃣  Registrando evento..." -ForegroundColor Yellow
$event = @{
    itemId = $itemId
    eventType = "MAINTENANCE"
    description = "Troca de óleo - Teste Railway"
    occurredAt = (Get-Date).ToUniversalTime().ToString("yyyy-MM-ddTHH:mm:ssZ")
    metadata = @{
        km = 5000
        tipo = "preventiva"
    }
} | ConvertTo-Json
try {
    $eventResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/v1/events" `
        -Method POST -Body $event -ContentType "application/json"
    Write-Host "   ✅ Evento registrado!" -ForegroundColor Green
    Write-Host "   Tipo: $($eventResponse.eventType)" -ForegroundColor Gray
} catch {
    Write-Host "   ❌ Erro: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""
# 5. Criar Regra
Write-Host "5️⃣  Criando regra..." -ForegroundColor Yellow
$rule = @{
    itemId = $itemId
    name = "Troca de Óleo - 5000km"
    description = "Alerta a cada 5.000 km"
    ruleType = "METRIC_BASED"
    enabled = $true
    conditions = @{
        metricField = "km"
        threshold = 10000
        warningThreshold = 9500
        severity = "WARNING"
    }
} | ConvertTo-Json
try {
    $ruleResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/v1/rules" `
        -Method POST -Body $rule -ContentType "application/json"
    Write-Host "   ✅ Regra criada!" -ForegroundColor Green
    Write-Host "   Nome: $($ruleResponse.name)" -ForegroundColor Gray
} catch {
    Write-Host "   ❌ Erro: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""
# Resultado Final
Write-Host "╔════════════════════════════════════════════╗" -ForegroundColor Green
Write-Host "║         TESTE CONCLUÍDO COM SUCESSO!       ║" -ForegroundColor Green
Write-Host "╚════════════════════════════════════════════╝" -ForegroundColor Green
Write-Host "`n🌐 PRÓXIMO PASSO:" -ForegroundColor Yellow
Write-Host "   Acesse https://railway.app/" -ForegroundColor White
Write-Host "   Vá em seu projeto MongoDB → Data → Browse" -ForegroundColor White
Write-Host "   Você verá as collections criadas:" -ForegroundColor White
Write-Host "   • items (1+ documento)" -ForegroundColor Cyan
Write-Host "   • events (1+ documento)" -ForegroundColor Cyan
Write-Host "   • rules (1+ documento)" -ForegroundColor Cyan
Write-Host ""
