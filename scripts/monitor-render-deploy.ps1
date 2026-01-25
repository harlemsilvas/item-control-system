# 🔍 Monitorar Deploy Render
# Testa se API está online e funcionando

param(
    [string]$Url = "https://item-control-api.onrender.com"
)

Write-Host "`n=== 🔍 MONITORAR DEPLOY RENDER ===" -ForegroundColor Cyan
Write-Host ""
Write-Host "URL: $Url" -ForegroundColor White
Write-Host ""

function Test-Endpoint {
    param(
        [string]$Uri,
        [string]$Nome
    )

    Write-Host "[$Nome]" -ForegroundColor Yellow -NoNewline
    Write-Host " Testando: $Uri" -ForegroundColor Gray

    try {
        $response = Invoke-WebRequest -Uri $Uri -UseBasicParsing -TimeoutSec 10

        if ($response.StatusCode -eq 200) {
            Write-Host "   ✅ Status: $($response.StatusCode)" -ForegroundColor Green

            # Tentar parsear JSON
            try {
                $json = $response.Content | ConvertFrom-Json
                Write-Host "   📄 Response:" -ForegroundColor Cyan
                $json | ConvertTo-Json -Depth 2 | Write-Host -ForegroundColor Gray
            } catch {
                Write-Host "   📄 Response: $($response.Content)" -ForegroundColor Gray
            }
            Write-Host ""
            return $true
        } else {
            Write-Host "   ⚠️  Status: $($response.StatusCode)" -ForegroundColor Yellow
            Write-Host ""
            return $false
        }
    } catch {
        $errorMsg = $_.Exception.Message

        if ($errorMsg -match "503") {
            Write-Host "   ⏳ Service Unavailable (503) - App ainda está inicializando..." -ForegroundColor Yellow
        } elseif ($errorMsg -match "404") {
            Write-Host "   ❌ Not Found (404)" -ForegroundColor Red
        } elseif ($errorMsg -match "timeout") {
            Write-Host "   ⏱️  Timeout - App pode estar em cold start..." -ForegroundColor Yellow
        } else {
            Write-Host "   ❌ Erro: $errorMsg" -ForegroundColor Red
        }
        Write-Host ""
        return $false
    }
}

# Teste 1: Health Check
$healthOk = Test-Endpoint -Uri "$Url/actuator/health" -Nome "Health Check"

if ($healthOk) {
    Write-Host "🎉 API ESTÁ ONLINE!" -ForegroundColor Green
    Write-Host ""

    # Teste 2: Swagger
    Write-Host "[Swagger UI]" -ForegroundColor Yellow
    Write-Host "   🌐 Acesse: $Url/swagger-ui.html" -ForegroundColor Cyan
    Write-Host ""

    # Teste 3: Endpoints principais
    Write-Host "[Endpoints Disponíveis]" -ForegroundColor Yellow
    Write-Host "   📋 Items:      GET  $Url/api/v1/items" -ForegroundColor Gray
    Write-Host "   ➕ Create Item: POST $Url/api/v1/items" -ForegroundColor Gray
    Write-Host "   📊 Categories: GET  $Url/api/v1/categories" -ForegroundColor Gray
    Write-Host "   📝 Events:     GET  $Url/api/v1/events" -ForegroundColor Gray
    Write-Host "   🔔 Alerts:     GET  $Url/api/v1/alerts/pending" -ForegroundColor Gray
    Write-Host "   🗄️  Database:   GET  $Url/api/v1/admin/database/collections" -ForegroundColor Gray
    Write-Host ""

    # Oferecer criar item de teste
    Write-Host "Deseja criar um item de teste? (s/n): " -ForegroundColor Yellow -NoNewline
    $resposta = Read-Host

    if ($resposta -eq "s" -or $resposta -eq "S") {
        Write-Host ""
        Write-Host "[Criando Item de Teste]" -ForegroundColor Yellow

        $body = @{
            name = "Item Deploy Render - $(Get-Date -Format 'yyyy-MM-dd HH:mm')"
            nickname = "render-test-$(Get-Date -Format 'yyyyMMddHHmm')"
            description = "Item criado para testar deploy no Render"
            template = "GENERAL"
        } | ConvertTo-Json

        try {
            $item = Invoke-RestMethod -Uri "$Url/api/v1/items" `
                -Method POST `
                -Body $body `
                -ContentType "application/json"

            Write-Host "   ✅ Item criado com sucesso!" -ForegroundColor Green
            Write-Host "   📄 ID: $($item.id)" -ForegroundColor Cyan
            Write-Host "   📄 Name: $($item.name)" -ForegroundColor Cyan
            Write-Host "   📄 Nickname: $($item.nickname)" -ForegroundColor Cyan
            Write-Host ""
            Write-Host "   💡 Collection 'items' foi criada automaticamente no MongoDB Atlas!" -ForegroundColor Green
            Write-Host ""
        } catch {
            Write-Host "   ❌ Erro ao criar item: $($_.Exception.Message)" -ForegroundColor Red
            Write-Host ""
        }
    }

    Write-Host "=== ✅ DEPLOY BEM-SUCEDIDO! ===" -ForegroundColor Green
    Write-Host ""

} else {
    Write-Host "⏳ API AINDA NÃO ESTÁ DISPONÍVEL" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Possíveis causas:" -ForegroundColor Yellow
    Write-Host "  1. Deploy ainda em andamento (aguarde 10-15 min)" -ForegroundColor White
    Write-Host "  2. Cold start (primeira requisição demora ~30-60s)" -ForegroundColor White
    Write-Host "  3. Build falhou (verificar logs no Render)" -ForegroundColor White
    Write-Host ""
    Write-Host "Como verificar:" -ForegroundColor Cyan
    Write-Host "  1. Acesse: https://dashboard.render.com" -ForegroundColor White
    Write-Host "  2. Selecione service: item-control-api" -ForegroundColor White
    Write-Host "  3. Ver Logs e Status" -ForegroundColor White
    Write-Host ""
    Write-Host "Aguarde alguns minutos e execute novamente:" -ForegroundColor Yellow
    Write-Host "  .\scripts\monitor-render-deploy.ps1" -ForegroundColor Gray
    Write-Host ""
}

# Informações adicionais
Write-Host "📊 Informações do Deploy:" -ForegroundColor Cyan
Write-Host "  • URL: $Url" -ForegroundColor White
Write-Host "  • Health Check: $Url/actuator/health" -ForegroundColor White
Write-Host "  • Swagger: $Url/swagger-ui.html" -ForegroundColor White
Write-Host "  • MongoDB: Atlas (cluster0.69j3tzl.mongodb.net)" -ForegroundColor White
Write-Host ""
