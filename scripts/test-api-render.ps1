# 🧪 Teste Rápido API Render
# Testa os principais endpoints

param(
    [string]$BaseUrl = "https://item-control-api.onrender.com"
)

Write-Host "`n=== 🧪 TESTE RÁPIDO API ===" -ForegroundColor Cyan
Write-Host "URL: $BaseUrl`n" -ForegroundColor White

$erros = 0
$sucessos = 0

function Test-ApiEndpoint {
    param(
        [string]$Method = "GET",
        [string]$Endpoint,
        [string]$Nome,
        [object]$Body = $null
    )

    Write-Host "[$Nome]" -ForegroundColor Yellow -NoNewline
    Write-Host " $Method $Endpoint" -ForegroundColor Gray

    try {
        $uri = "$BaseUrl$Endpoint"

        if ($Body) {
            $bodyJson = $Body | ConvertTo-Json -Depth 10
            $response = Invoke-RestMethod -Uri $uri -Method $Method -Body $bodyJson -ContentType "application/json" -TimeoutSec 30
        } else {
            $response = Invoke-RestMethod -Uri $uri -Method $Method -TimeoutSec 30
        }

        Write-Host "   ✅ Sucesso!" -ForegroundColor Green

        # Mostrar preview da resposta
        if ($response) {
            $preview = $response | ConvertTo-Json -Depth 2 -Compress
            if ($preview.Length -gt 100) {
                $preview = $preview.Substring(0, 100) + "..."
            }
            Write-Host "   📄 $preview" -ForegroundColor DarkGray
        }

        Write-Host ""
        $script:sucessos++
        return $response

    } catch {
        $errorMsg = $_.Exception.Message

        if ($errorMsg -match "503") {
            Write-Host "   ⏳ Service Unavailable - App hibernando, aguarde..." -ForegroundColor Yellow
        } elseif ($errorMsg -match "404") {
            Write-Host "   ⚠️  Not Found (404)" -ForegroundColor Yellow
        } elseif ($errorMsg -match "400") {
            Write-Host "   ⚠️  Bad Request (400) - $errorMsg" -ForegroundColor Yellow
        } else {
            Write-Host "   ❌ Erro: $errorMsg" -ForegroundColor Red
        }

        Write-Host ""
        $script:erros++
        return $null
    }
}

# Teste 1: Health Check
Write-Host "━━━ TESTE 1: Health Check ━━━" -ForegroundColor Cyan
$health = Test-ApiEndpoint -Nome "Health" -Endpoint "/actuator/health"

if (-not $health) {
    Write-Host "⚠️  API não está disponível. Possíveis causas:" -ForegroundColor Yellow
    Write-Host "   1. Deploy em andamento (aguarde 10-15 min)" -ForegroundColor White
    Write-Host "   2. Cold start (primeira req demora 30-60s)" -ForegroundColor White
    Write-Host "   3. Build falhou (ver logs no Render)" -ForegroundColor White
    Write-Host ""
    Write-Host "Tente novamente em alguns minutos." -ForegroundColor Yellow
    exit 1
}

# Teste 2: Listar Items
Write-Host "━━━ TESTE 2: Listar Items ━━━" -ForegroundColor Cyan
$items = Test-ApiEndpoint -Nome "List Items" -Endpoint "/api/v1/items"

# Teste 3: Criar Item
Write-Host "━━━ TESTE 3: Criar Item ━━━" -ForegroundColor Cyan
$newItem = @{
    name = "Item Teste - $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')"
    nickname = "test-$(Get-Date -Format 'yyyyMMddHHmmss')"
    description = "Item criado automaticamente pelo script de teste"
    template = "GENERAL"
}

$createdItem = Test-ApiEndpoint -Nome "Create Item" -Endpoint "/api/v1/items" -Method "POST" -Body $newItem

if ($createdItem) {
    Write-Host "   🎉 Item criado com ID: $($createdItem.id)" -ForegroundColor Green
    Write-Host ""

    # Teste 4: Buscar Item por ID
    Write-Host "━━━ TESTE 4: Buscar Item por ID ━━━" -ForegroundColor Cyan
    $foundItem = Test-ApiEndpoint -Nome "Get Item" -Endpoint "/api/v1/items/$($createdItem.id)"

    # Teste 5: Registrar Evento
    Write-Host "━━━ TESTE 5: Registrar Evento ━━━" -ForegroundColor Cyan
    $newEvent = @{
        itemId = $createdItem.id
        type = "MAINTENANCE"
        description = "Evento de teste"
        value = 100.50
        eventDate = (Get-Date).ToString("yyyy-MM-ddTHH:mm:ss")
    }

    $createdEvent = Test-ApiEndpoint -Nome "Create Event" -Endpoint "/api/v1/events" -Method "POST" -Body $newEvent

    # Teste 6: Listar Eventos do Item
    Write-Host "━━━ TESTE 6: Listar Eventos ━━━" -ForegroundColor Cyan
    $events = Test-ApiEndpoint -Nome "List Events" -Endpoint "/api/v1/events?itemId=$($createdItem.id)"
}

# Teste 7: Listar Categorias
Write-Host "━━━ TESTE 7: Listar Categorias ━━━" -ForegroundColor Cyan
$categories = Test-ApiEndpoint -Nome "List Categories" -Endpoint "/api/v1/categories"

# Teste 8: Database Collections
Write-Host "━━━ TESTE 8: Database Collections ━━━" -ForegroundColor Cyan
$collections = Test-ApiEndpoint -Nome "List Collections" -Endpoint "/api/v1/admin/database/collections"

# Resumo
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Cyan
Write-Host ""
Write-Host "📊 RESUMO DOS TESTES:" -ForegroundColor Cyan
Write-Host ""
Write-Host "   ✅ Sucessos: $sucessos" -ForegroundColor Green
Write-Host "   ❌ Falhas:   $erros" -ForegroundColor $(if ($erros -gt 0) { "Red" } else { "Gray" })
Write-Host ""

if ($erros -eq 0) {
    Write-Host "🎉 TODOS OS TESTES PASSARAM!" -ForegroundColor Green
} else {
    Write-Host "⚠️  Alguns testes falharam. Verifique os erros acima." -ForegroundColor Yellow
}

Write-Host ""
Write-Host "🌐 Endpoints úteis:" -ForegroundColor Cyan
Write-Host "   • Swagger:  $BaseUrl/swagger-ui.html" -ForegroundColor White
Write-Host "   • Health:   $BaseUrl/actuator/health" -ForegroundColor White
Write-Host "   • API Docs: docs/030-guia-requisicoes-api.md" -ForegroundColor White
Write-Host ""
