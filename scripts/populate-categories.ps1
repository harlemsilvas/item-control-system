# ============================================
# Script de Populacao - CATEGORIAS
# Cria as categorias base do sistema
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
Write-Host "Criando categorias..." -ForegroundColor Cyan

# Categorias base
$categories = @(
    @{
        name = "Veiculos"
    },
    @{
        name = "Contas e Servicos"
    },
    @{
        name = "Itens Consumiveis"
    },
    @{
        name = "Eletronicos"
    },
    @{
        name = "Imoveis"
    }
)

$categoryCount = 0

foreach ($category in $categories) {
    $body = @{
        userId = $userId
        name = $category.name
        parentId = $null
    } | ConvertTo-Json -Depth 10

    try {
        $result = Invoke-RestMethod -Uri "$baseUrl/categories" -Method POST -ContentType "application/json" -Body $body
        $categoryCount++
        Write-Host "  OK: $($category.name)" -ForegroundColor Green
    } catch {
        Write-Host "  ERRO: $($category.name) - $($_.Exception.Message)" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "$categoryCount categorias criadas!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan