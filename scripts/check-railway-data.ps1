# Script para verificar dados no Railway via API
# Pressupoe que a API esta rodando em modo producao

Write-Host ""
Write-Host "============================================================" -ForegroundColor Cyan
Write-Host " VERIFICACAO RAILWAY - Via API REST" -ForegroundColor Cyan
Write-Host "============================================================" -ForegroundColor Cyan
Write-Host ""

$baseUrl = "http://localhost:8080/api/v1"

# Verificar se API esta online
Write-Host "Verificando se API esta online..." -ForegroundColor Yellow

try {
    $health = Invoke-RestMethod -Uri "http://localhost:8080/actuator/health" -Method GET -ErrorAction Stop
    Write-Host "API Online!" -ForegroundColor Green
    Write-Host ""
} catch {
    Write-Host "ERRO: API nao esta respondendo!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Execute primeiro:" -ForegroundColor Yellow
    Write-Host "  .\scripts\quick-start-prod.ps1" -ForegroundColor Cyan
    Write-Host ""
    exit 1
}

# Funcao para contar e exibir
function Check-Collection {
    param(
        [string]$Name,
        [string]$Endpoint
    )

    Write-Host "Verificando $Name..." -ForegroundColor Yellow

    try {
        $response = Invoke-RestMethod -Uri "$baseUrl/$Endpoint" -Method GET -ErrorAction Stop

        if ($response -is [Array]) {
            $count = $response.Count
        } else {
            $count = 1
        }

        Write-Host "  Total: $count documento(s)" -ForegroundColor Green

        if ($count -gt 0 -and $count -le 3) {
            Write-Host "  Amostras:" -ForegroundColor Gray
            $response | ConvertTo-Json -Depth 2 | Write-Host -ForegroundColor Gray
        } elseif ($count -gt 3) {
            Write-Host "  Primeiros 3 documentos:" -ForegroundColor Gray
            $response[0..2] | ForEach-Object {
                $_ | ConvertTo-Json -Depth 2 | Write-Host -ForegroundColor Gray
            }
        }

    } catch {
        Write-Host "  Erro ao acessar: $($_.Exception.Message)" -ForegroundColor Red
    }

    Write-Host ""
}

# Verificar cada colecao
Write-Host "============================================================" -ForegroundColor Cyan
Write-Host ""

Check-Collection -Name "ITEMS" -Endpoint "items"
Check-Collection -Name "EVENTS" -Endpoint "events"
Check-Collection -Name "ALERTS" -Endpoint "alerts"
Check-Collection -Name "CATEGORIES" -Endpoint "categories"
Check-Collection -Name "RULES" -Endpoint "rules"

Write-Host "============================================================" -ForegroundColor Green
Write-Host " VERIFICACAO CONCLUIDA" -ForegroundColor Green
Write-Host "============================================================" -ForegroundColor Green
Write-Host ""

Write-Host "Para mais detalhes, use MongoDB Compass:" -ForegroundColor Cyan
Write-Host "  URL: mongodb://mongo:vrzaNIBSuwNrVIMLLvKfmuiJwBFvglAG@hopper.proxy.rlwy.net:40930/item_control_db" -ForegroundColor Gray
Write-Host ""

Write-Host "Pressione ENTER para sair..."
Read-Host
