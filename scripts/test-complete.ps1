# Script de Teste Completo - Item Control System
# Demonstra criacao de Item e registro de Events no MongoDB
Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "TESTE DA API - ITEM CONTROL SYSTEM" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
$baseUrl = "http://localhost:8080/api/v1"
# Verificar se a API esta rodando
Write-Host "Verificando se a API esta rodando..." -ForegroundColor Yellow
try {
    $health = Invoke-RestMethod -Uri "http://localhost:8080/actuator/health" -Method GET
    if ($health.status -eq "UP") {
        Write-Host "API esta UP e rodando!" -ForegroundColor Green
        Write-Host ""
    }
} catch {
    Write-Host "API nao esta respondendo!" -ForegroundColor Red
    Write-Host "Execute o script: .\start-api.ps1" -ForegroundColor Yellow
    exit 1
}
# IDs fixos para o teste
$userId = "550e8400-e29b-41d4-a716-446655440001"
$categoryId = "650e8400-e29b-41d4-a716-446655440002"
Write-Host "IDs de teste:" -ForegroundColor Yellow
Write-Host "   UserId: $userId" -ForegroundColor Gray
Write-Host "   CategoryId: $categoryId" -ForegroundColor Gray
Write-Host ""
# ========================================
# 1. CRIAR ITEM: Honda CB 500X
# ========================================
Write-Host "1. Criando Item: Honda CB 500X..." -ForegroundColor Green
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
    $itemResponse = Invoke-RestMethod -Uri "$baseUrl/items" -Method POST -ContentType "application/json" -Body $createItemBody
    $itemId = $itemResponse.id
    Write-Host "   Item criado com sucesso!" -ForegroundColor Green
    Write-Host "   ID: $itemId" -ForegroundColor Cyan
    Write-Host "   Nome: $($itemResponse.name)" -ForegroundColor Gray
    Write-Host "   Nickname: $($itemResponse.nickname)" -ForegroundColor Gray
    Write-Host "   Status: $($itemResponse.status)" -ForegroundColor Gray
    Write-Host "   Template: $($itemResponse.templateCode)" -ForegroundColor Gray
    Write-Host ""
} catch {
    Write-Host "   Erro ao criar item: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}
Write-Host "Parabens! Sistema funcionando!" -ForegroundColor Green
