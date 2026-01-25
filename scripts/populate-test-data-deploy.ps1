# ====================================
# 🚀 POPULAR MONGODB PRODUÇÃO/DEPLOY
# ====================================

Write-Host "`n═══════════════════════════════════════════" -ForegroundColor Cyan
Write-Host "  📦 Item Control - Popular Dados DEPLOY" -ForegroundColor Cyan
Write-Host "═══════════════════════════════════════════`n" -ForegroundColor Cyan

# URL da API em produção (Render)
$API_URL = "https://item-control-api.onrender.com/api/v1"
$USER_ID = "550e8400-e29b-41d4-a716-446655440001" # UUID fixo para testes

Write-Host "🌐 API URL: $API_URL" -ForegroundColor Gray
Write-Host "👤 User ID: $USER_ID`n" -ForegroundColor Gray

# Função para fazer requisições
function Invoke-ApiRequest {
    param(
        [string]$Method,
        [string]$Endpoint,
        [object]$Body
    )

    try {
        $params = @{
            Uri = "$API_URL$Endpoint"
            Method = $Method
            ContentType = "application/json"
            ErrorAction = "Stop"
        }

        if ($Body) {
            $params.Body = ($Body | ConvertTo-Json -Depth 10)
        }

        $response = Invoke-RestMethod @params
        return $response
    }
    catch {
        Write-Host "   ❌ Erro: $($_.Exception.Message)" -ForegroundColor Red

        # Tentar extrair detalhes do erro
        if ($_.ErrorDetails.Message) {
            Write-Host "   Detalhes: $($_.ErrorDetails.Message)" -ForegroundColor DarkRed
        }

        return $null
    }
}

# Testar conexão primeiro
Write-Host "🔍 Testando conexão com API..." -ForegroundColor Yellow
try {
    $health = Invoke-RestMethod "$API_URL/../actuator/health" -ErrorAction Stop
    Write-Host "   ✅ API está respondendo: $($health.status)`n" -ForegroundColor Green
}
catch {
    Write-Host "   ⚠️ Aviso: Não foi possível verificar health check" -ForegroundColor Yellow
    Write-Host "   Continuando mesmo assim...`n" -ForegroundColor Gray
}

# ====================================
# 1. CRIAR CATEGORIAS
# ====================================

Write-Host "[1/4] Criando Categorias..." -ForegroundColor Yellow

$categorias = @(
    @{ name = "Veículos"; slug = "veiculos"; description = "Carros, motos e transporte" }
    @{ name = "Casa"; slug = "casa"; description = "Itens domésticos" }
    @{ name = "Eletrônicos"; slug = "eletronicos"; description = "Equipamentos eletrônicos" }
    @{ name = "Manutenção"; slug = "manutencao"; description = "Manutenção e reparos" }
)

$categoriasIds = @{}

foreach ($cat in $categorias) {
    $body = @{
        userId = $USER_ID
        name = $cat.name
        slug = $cat.slug
        description = $cat.description
    }

    $result = Invoke-ApiRequest -Method POST -Endpoint "/categories" -Body $body

    if ($result) {
        $categoriasIds[$cat.slug] = $result.id
        Write-Host "   ✅ Categoria criada: $($cat.name)" -ForegroundColor Green
    }
    Start-Sleep -Milliseconds 500
}

Start-Sleep -Seconds 2

# ====================================
# 2. CRIAR ITEMS
# ====================================

Write-Host "`n[2/4] Criando Items..." -ForegroundColor Yellow

$items = @(
    @{
        name = "Honda Civic 2020"
        nickname = "civic"
        template = "VEHICLE"
        description = "Carro principal da família"
        categoryId = $categoriasIds["veiculos"]
        metadata = @{
            placa = "ABC-1234"
            ano = 2020
            modelo = "Civic EXL"
            quilometragem = 45000
        }
        currentMetricValue = 45000
    },
    @{
        name = "Notebook Dell"
        nickname = "notebook-trabalho"
        template = "GENERAL"
        description = "Notebook para trabalho"
        categoryId = $categoriasIds["eletronicos"]
        metadata = @{
            marca = "Dell"
            modelo = "Inspiron 15"
            numeroSerie = "DL2020XYZ"
        }
    },
    @{
        name = "Geladeira Brastemp"
        nickname = "geladeira-cozinha"
        template = "GENERAL"
        description = "Geladeira da cozinha"
        categoryId = $categoriasIds["casa"]
        metadata = @{
            marca = "Brastemp"
            modelo = "Frost Free 400L"
            voltagem = "220V"
        }
    },
    @{
        name = "Conta de Luz"
        nickname = "conta-luz"
        template = "RECURRING_BILL"
        description = "Conta de energia elétrica mensal"
        categoryId = $categoriasIds["casa"]
        metadata = @{
            empresa = "Companhia de Energia"
            numeroCliente = "123456789"
            vencimento = 10
        }
    },
    @{
        name = "Ar Condicionado"
        nickname = "ar-sala"
        template = "GENERAL"
        description = "Ar condicionado da sala"
        categoryId = $categoriasIds["casa"]
        metadata = @{
            marca = "LG"
            modelo = "Dual Inverter 12000 BTU"
            potencia = "12000 BTU"
        }
    }
)

$itemsIds = @{}

foreach ($item in $items) {
    $body = @{
        userId = $USER_ID
        name = $item.name
        nickname = $item.nickname
        template = $item.template
        description = $item.description
        categoryId = $item.categoryId
        metadata = $item.metadata
    }

    if ($item.currentMetricValue) {
        $body.currentMetricValue = $item.currentMetricValue
    }

    $result = Invoke-ApiRequest -Method POST -Endpoint "/items" -Body $body

    if ($result) {
        $itemsIds[$item.nickname] = $result.id
        Write-Host "   ✅ Item criado: $($item.name)" -ForegroundColor Green
    }
    Start-Sleep -Milliseconds 500
}

Start-Sleep -Seconds 2

# ====================================
# 3. CRIAR EVENTOS
# ====================================

Write-Host "`n[3/4] Criando Eventos..." -ForegroundColor Yellow

$eventos = @(
    # Eventos do Civic
    @{
        itemNickname = "civic"
        type = "MAINTENANCE"
        description = "Troca de óleo e filtros"
        value = 350.00
        metricValue = 40000
        eventDate = (Get-Date).AddDays(-60).ToString("yyyy-MM-dd")
    },
    @{
        itemNickname = "civic"
        type = "MAINTENANCE"
        description = "Revisão dos 45 mil km"
        value = 800.00
        metricValue = 45000
        eventDate = (Get-Date).AddDays(-10).ToString("yyyy-MM-dd")
    },
    @{
        itemNickname = "civic"
        type = "CONSUMPTION"
        description = "Abastecimento - 50L"
        value = 300.00
        eventDate = (Get-Date).AddDays(-5).ToString("yyyy-MM-dd")
    },
    # Eventos da Conta de Luz
    @{
        itemNickname = "conta-luz"
        type = "PAYMENT"
        description = "Pagamento Janeiro/2026"
        value = 450.00
        eventDate = (Get-Date).AddDays(-25).ToString("yyyy-MM-dd")
    },
    @{
        itemNickname = "conta-luz"
        type = "PAYMENT"
        description = "Pagamento Dezembro/2025"
        value = 520.00
        eventDate = (Get-Date).AddDays(-55).ToString("yyyy-MM-dd")
    },
    # Eventos do Ar Condicionado
    @{
        itemNickname = "ar-sala"
        type = "MAINTENANCE"
        description = "Limpeza de filtros"
        value = 150.00
        eventDate = (Get-Date).AddDays(-30).ToString("yyyy-MM-dd")
    },
    # Eventos do Notebook
    @{
        itemNickname = "notebook-trabalho"
        type = "MAINTENANCE"
        description = "Upgrade de memória RAM"
        value = 400.00
        eventDate = (Get-Date).AddDays(-90).ToString("yyyy-MM-dd")
    }
)

foreach ($evento in $eventos) {
    $itemId = $itemsIds[$evento.itemNickname]

    if ($itemId) {
        $body = @{
            itemId = $itemId
            type = $evento.type
            description = $evento.description
            eventDate = $evento.eventDate
        }

        if ($evento.value) {
            $body.value = $evento.value
        }

        if ($evento.metricValue) {
            $body.metricValue = $evento.metricValue
        }

        $result = Invoke-ApiRequest -Method POST -Endpoint "/events" -Body $body

        if ($result) {
            Write-Host "   ✅ Evento criado: $($evento.description)" -ForegroundColor Green
        }
        Start-Sleep -Milliseconds 300
    }
}

Start-Sleep -Seconds 2

# ====================================
# 4. CRIAR ALERTAS
# ====================================

Write-Host "`n[4/4] Criando Alertas..." -ForegroundColor Yellow

$alertas = @(
    @{
        itemNickname = "civic"
        type = "WARNING"
        title = "Revisão dos 50 mil km se aproxima"
        message = "Agendar revisão para quando atingir 50.000 km"
        scheduledFor = (Get-Date).AddDays(30).ToString("yyyy-MM-dd")
    },
    @{
        itemNickname = "conta-luz"
        type = "INFO"
        title = "Vencimento da conta de luz"
        message = "Conta de luz vence dia 10 de Fevereiro"
        scheduledFor = (Get-Date).AddDays(5).ToString("yyyy-MM-dd")
    },
    @{
        itemNickname = "ar-sala"
        type = "INFO"
        title = "Limpeza de filtros"
        message = "Realizar limpeza mensal dos filtros do ar condicionado"
        scheduledFor = (Get-Date).AddDays(15).ToString("yyyy-MM-dd")
    },
    @{
        itemNickname = "geladeira-cozinha"
        type = "WARNING"
        title = "Degelo preventivo"
        message = "Realizar degelo e limpeza da geladeira"
        scheduledFor = (Get-Date).AddDays(7).ToString("yyyy-MM-dd")
    }
)

foreach ($alerta in $alertas) {
    $itemId = $itemsIds[$alerta.itemNickname]

    if ($itemId) {
        $body = @{
            itemId = $itemId
            userId = $USER_ID
            type = $alerta.type
            title = $alerta.title
            message = $alerta.message
            scheduledFor = $alerta.scheduledFor
        }

        $result = Invoke-ApiRequest -Method POST -Endpoint "/alerts" -Body $body

        if ($result) {
            Write-Host "   ✅ Alerta criado: $($alerta.title)" -ForegroundColor Green
        }
        Start-Sleep -Milliseconds 300
    }
}

# ====================================
# RESUMO
# ====================================

Write-Host "`n═══════════════════════════════════════════" -ForegroundColor Cyan
Write-Host "  ✅ POPULAÇÃO DE DADOS CONCLUÍDA!" -ForegroundColor Green
Write-Host "═══════════════════════════════════════════`n" -ForegroundColor Cyan

Write-Host "📊 Resumo:" -ForegroundColor White
Write-Host "   • Categorias: $($categorias.Count)" -ForegroundColor Gray
Write-Host "   • Items: $($items.Count)" -ForegroundColor Gray
Write-Host "   • Eventos: $($eventos.Count)" -ForegroundColor Gray
Write-Host "   • Alertas: $($alertas.Count)" -ForegroundColor Gray

Write-Host "`n🌐 API URL:" -ForegroundColor White
Write-Host "   $API_URL" -ForegroundColor Cyan

Write-Host "`n🔗 Acesse o frontend:" -ForegroundColor White
Write-Host "   http://localhost:5173" -ForegroundColor Cyan
Write-Host "   (Configure .env com a URL de produção)" -ForegroundColor Gray

Write-Host "`n💡 Dica:" -ForegroundColor Yellow
Write-Host "   Os dados foram criados no banco de produção!" -ForegroundColor Gray
Write-Host "   Faça refresh (F5) no frontend para ver os dados!" -ForegroundColor Gray

Write-Host "`n✅ Para verificar no MongoDB Atlas:" -ForegroundColor Yellow
Write-Host "   1. Acesse: https://cloud.mongodb.com" -ForegroundColor Gray
Write-Host "   2. Database → Browse Collections" -ForegroundColor Gray
Write-Host "   3. Ver items, events, alerts e categories criados" -ForegroundColor Gray

Write-Host "`n"
