# 🧪 Script de Populate - Dados de Teste Completos
# Cria 15 itens, 75 eventos (5 por item) e alertas para testes

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "🧪 POPULANDO SISTEMA COM DADOS DE TESTE" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$baseUrl = "http://localhost:8080/api/v1"
$userId = "550e8400-e29b-41d4-a716-446655440001"
$categoryVehicle = "650e8400-e29b-41d4-a716-446655440002"
$categoryBill = "750e8400-e29b-41d4-a716-446655440003"
$categoryConsumable = "850e8400-e29b-41d4-a716-446655440004"

# Verificar se API está rodando
try {
    $health = Invoke-RestMethod -Uri "http://localhost:8080/actuator/health" -Method GET -ErrorAction Stop
    Write-Host "✅ API está rodando!" -ForegroundColor Green
    Write-Host ""
} catch {
    Write-Host "❌ API não está rodando!" -ForegroundColor Red
    Write-Host "Inicie a API primeiro: cd modules/api; java -jar target/item-control-api-0.1.0-SNAPSHOT.jar" -ForegroundColor Yellow
    exit 1
}

$itemsCreated = @()
$eventsCreated = 0
$alertsCreated = 0

# ========================================
# DEFINIÇÃO DOS 15 ITENS
# ========================================

$itemsData = @(
    # VEÍCULOS (5)
    @{
        name = "Honda CB 500X"
        nickname = "Motoca"
        categoryId = $categoryVehicle
        templateCode = "VEHICLE"
        tags = @("moto", "honda", "transporte")
        metadata = @{
            brand = "Honda"
            model = "CB 500X"
            year = 2020
            plate = "ABC-1234"
            color = "Vermelha"
            odometer = 15000
        }
    },
    @{
        name = "Toyota Corolla"
        nickname = "Carro da Família"
        categoryId = $categoryVehicle
        templateCode = "VEHICLE"
        tags = @("carro", "toyota", "sedan")
        metadata = @{
            brand = "Toyota"
            model = "Corolla XEi"
            year = 2021
            plate = "XYZ-9876"
            color = "Prata"
            odometer = 32000
        }
    },
    @{
        name = "Chevrolet Onix"
        nickname = "Onix"
        categoryId = $categoryVehicle
        templateCode = "VEHICLE"
        tags = @("carro", "chevrolet", "compacto")
        metadata = @{
            brand = "Chevrolet"
            model = "Onix LTZ"
            year = 2022
            plate = "DEF-4567"
            color = "Branco"
            odometer = 18500
        }
    },
    @{
        name = "Yamaha Fazer 250"
        nickname = "Fazer"
        categoryId = $categoryVehicle
        templateCode = "VEHICLE"
        tags = @("moto", "yamaha", "transporte")
        metadata = @{
            brand = "Yamaha"
            model = "Fazer 250"
            year = 2019
            plate = "GHI-7890"
            color = "Azul"
            odometer = 42000
        }
    },
    @{
        name = "Fiat Uno"
        nickname = "Carrinho"
        categoryId = $categoryVehicle
        templateCode = "VEHICLE"
        tags = @("carro", "fiat", "econômico")
        metadata = @{
            brand = "Fiat"
            model = "Uno Vivace"
            year = 2018
            plate = "JKL-1357"
            color = "Vermelho"
            odometer = 65000
        }
    },

    # CONTAS (5)
    @{
        name = "Conta de Água - Residência"
        nickname = "Água Casa"
        categoryId = $categoryBill
        templateCode = "UTILITY_BILL"
        tags = @("conta", "agua", "mensal", "residencial")
        metadata = @{
            supplier = "SABESP"
            accountNumber = "123456789"
            dueDay = 10
            averageCost = 85.50
        }
    },
    @{
        name = "Conta de Luz - Residência"
        nickname = "Luz Casa"
        categoryId = $categoryBill
        templateCode = "UTILITY_BILL"
        tags = @("conta", "luz", "mensal", "residencial")
        metadata = @{
            supplier = "Enel"
            accountNumber = "987654321"
            dueDay = 15
            averageCost = 152.30
        }
    },
    @{
        name = "Internet Fibra"
        nickname = "Wi-Fi"
        categoryId = $categoryBill
        templateCode = "UTILITY_BILL"
        tags = @("conta", "internet", "mensal")
        metadata = @{
            supplier = "Vivo Fibra"
            accountNumber = "555-777-999"
            dueDay = 5
            averageCost = 119.90
            speed = "300 Mbps"
        }
    },
    @{
        name = "Condomínio Residencial"
        nickname = "Condomínio"
        categoryId = $categoryBill
        templateCode = "UTILITY_BILL"
        tags = @("conta", "condominio", "mensal")
        metadata = @{
            supplier = "Síndico"
            accountNumber = "APT-401"
            dueDay = 8
            averageCost = 450.00
        }
    },
    @{
        name = "Telefone Celular"
        nickname = "Celular"
        categoryId = $categoryBill
        templateCode = "UTILITY_BILL"
        tags = @("conta", "telefone", "mensal")
        metadata = @{
            supplier = "Claro"
            accountNumber = "11-98765-4321"
            dueDay = 12
            averageCost = 89.90
            plan = "Controle 15GB"
        }
    },

    # CONSUMÍVEIS (5)
    @{
        name = "Galão de Água 20L"
        nickname = "Galão Água"
        categoryId = $categoryConsumable
        templateCode = "CONSUMABLE_ITEM"
        tags = @("consumivel", "agua", "casa")
        metadata = @{
            brand = "Água Mineral Natural"
            volume = "20L"
            supplier = "Distribuidora XYZ"
            unitCost = 12.50
            averageConsumption = "2 por semana"
        }
    },
    @{
        name = "Botijão de Gás 13kg"
        nickname = "Gás"
        categoryId = $categoryConsumable
        templateCode = "CONSUMABLE_ITEM"
        tags = @("consumivel", "gas", "cozinha")
        metadata = @{
            brand = "Ultragaz"
            weight = "13kg"
            supplier = "Revendedor ABC"
            unitCost = 95.00
            averageConsumption = "1 por mês"
        }
    },
    @{
        name = "Filtro de Café"
        nickname = "Filtro Café"
        categoryId = $categoryConsumable
        templateCode = "CONSUMABLE_ITEM"
        tags = @("consumivel", "cafe", "cozinha")
        metadata = @{
            brand = "Melitta"
            quantity = "100 unidades"
            unitCost = 8.90
            averageConsumption = "1 pacote por mês"
        }
    },
    @{
        name = "Papel Higiênico"
        nickname = "Papel"
        categoryId = $categoryConsumable
        templateCode = "CONSUMABLE_ITEM"
        tags = @("consumivel", "higiene", "casa")
        metadata = @{
            brand = "Personal"
            quantity = "12 rolos"
            unitCost = 18.50
            averageConsumption = "1 pacote quinzenal"
        }
    },
    @{
        name = "Detergente Líquido"
        nickname = "Detergente"
        categoryId = $categoryConsumable
        templateCode = "CONSUMABLE_ITEM"
        tags = @("consumivel", "limpeza", "cozinha")
        metadata = @{
            brand = "Ypê"
            quantity = "500ml"
            unitCost = 2.90
            averageConsumption = "2 por mês"
        }
    }
)

Write-Host "📦 Criando 15 itens variados..." -ForegroundColor Yellow
Write-Host ""

foreach ($itemData in $itemsData) {
    $body = @{
        userId = $userId
        name = $itemData.name
        nickname = $itemData.nickname
        categoryId = $itemData.categoryId
        templateCode = $itemData.templateCode
        tags = $itemData.tags
        metadata = $itemData.metadata
    } | ConvertTo-Json -Depth 10

    try {
        $item = Invoke-RestMethod -Uri "$baseUrl/items" -Method POST -ContentType "application/json" -Body $body
        $itemsCreated += $item
        Write-Host "  ✅ $($item.name)" -ForegroundColor Green
    } catch {
        Write-Host "  ❌ Erro ao criar: $($itemData.name)" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "✅ $($itemsCreated.Count) itens criados!" -ForegroundColor Green
Write-Host ""

# ========================================
# CRIAR 5 EVENTOS PARA CADA ITEM
# ========================================

Write-Host "📅 Criando 5 eventos retroativos para cada item..." -ForegroundColor Yellow
Write-Host ""

foreach ($item in $itemsCreated) {
    Write-Host "  📦 $($item.name):" -ForegroundColor Cyan

    # Determinar tipo de eventos baseado no template
    $events = @()

    if ($item.templateCode -eq "VEHICLE") {
        # Eventos de veículos
        $events = @(
            @{
                type = "MAINTENANCE"
                description = "Troca de óleo e filtro"
                daysAgo = 90
                metrics = @{
                    odometer = $item.metadata.odometer - 5000
                    cost = 280.00 + (Get-Random -Minimum -50 -Maximum 100)
                    serviceName = "Troca de óleo"
                }
            },
            @{
                type = "CONSUMPTION"
                description = "Abastecimento completo"
                daysAgo = 60
                metrics = @{
                    quantity = 40.0 + (Get-Random -Minimum -10 -Maximum 10)
                    unitPrice = 5.89
                    fullTank = $true
                    odometer = $item.metadata.odometer - 3000
                }
            },
            @{
                type = "MAINTENANCE"
                description = "Troca de pneus"
                daysAgo = 180
                metrics = @{
                    odometer = $item.metadata.odometer - 8000
                    cost = 1200.00 + (Get-Random -Minimum -200 -Maximum 300)
                    serviceName = "Substituição de 4 pneus"
                }
            },
            @{
                type = "INSPECTION"
                description = "Revisão periódica"
                daysAgo = 30
                metrics = @{
                    odometer = $item.metadata.odometer - 1000
                    cost = 450.00
                    approved = $true
                    notes = "Veículo em bom estado"
                }
            },
            @{
                type = "CONSUMPTION"
                description = "Abastecimento"
                daysAgo = 15
                metrics = @{
                    quantity = 35.0 + (Get-Random -Minimum -5 -Maximum 10)
                    unitPrice = 5.99
                    fullTank = $false
                    odometer = $item.metadata.odometer - 500
                }
            }
        )
    }
    elseif ($item.templateCode -eq "UTILITY_BILL") {
        # Eventos de contas
        $avgCost = $item.metadata.averageCost
        $events = @(
            @{
                type = "CONSUMPTION"
                description = "Pagamento da conta - Janeiro"
                daysAgo = 120
                metrics = @{
                    amount = $avgCost * 0.95
                    dueDate = "2025-01-$($item.metadata.dueDay)"
                    paymentDate = "2025-01-$($item.metadata.dueDay - 2)"
                    lateFee = 0
                }
            },
            @{
                type = "CONSUMPTION"
                description = "Pagamento da conta - Fevereiro"
                daysAgo = 90
                metrics = @{
                    amount = $avgCost * 1.1
                    dueDate = "2025-02-$($item.metadata.dueDay)"
                    paymentDate = "2025-02-$($item.metadata.dueDay + 3)"
                    lateFee = $avgCost * 0.02
                }
            },
            @{
                type = "CONSUMPTION"
                description = "Pagamento da conta - Março"
                daysAgo = 60
                metrics = @{
                    amount = $avgCost
                    dueDate = "2025-03-$($item.metadata.dueDay)"
                    paymentDate = "2025-03-$($item.metadata.dueDay - 1)"
                    lateFee = 0
                }
            },
            @{
                type = "CONSUMPTION"
                description = "Pagamento da conta - Novembro"
                daysAgo = 30
                metrics = @{
                    amount = $avgCost * 1.05
                    dueDate = "2025-11-$($item.metadata.dueDay)"
                    paymentDate = "2025-11-$($item.metadata.dueDay)"
                    lateFee = 0
                }
            },
            @{
                type = "CONSUMPTION"
                description = "Pagamento da conta - Dezembro"
                daysAgo = 7
                metrics = @{
                    amount = $avgCost * 0.92
                    dueDate = "2025-12-$($item.metadata.dueDay)"
                    paymentDate = "2025-12-$($item.metadata.dueDay - 3)"
                    lateFee = 0
                }
            }
        )
    }
    else {
        # Eventos de consumíveis
        $unitCost = $item.metadata.unitCost
        $events = @(
            @{
                type = "CONSUMPTION"
                description = "Compra - reposição"
                daysAgo = 120
                metrics = @{
                    quantity = Get-Random -Minimum 1 -Maximum 5
                    unitPrice = $unitCost * 0.95
                    totalCost = ($unitCost * 0.95) * (Get-Random -Minimum 1 -Maximum 5)
                }
            },
            @{
                type = "CONSUMPTION"
                description = "Compra - estoque"
                daysAgo = 90
                metrics = @{
                    quantity = Get-Random -Minimum 2 -Maximum 6
                    unitPrice = $unitCost
                    totalCost = $unitCost * (Get-Random -Minimum 2 -Maximum 6)
                }
            },
            @{
                type = "CONSUMPTION"
                description = "Compra - promoção"
                daysAgo = 60
                metrics = @{
                    quantity = Get-Random -Minimum 3 -Maximum 8
                    unitPrice = $unitCost * 0.85
                    totalCost = ($unitCost * 0.85) * (Get-Random -Minimum 3 -Maximum 8)
                    promotion = $true
                }
            },
            @{
                type = "CONSUMPTION"
                description = "Compra - emergência"
                daysAgo = 30
                metrics = @{
                    quantity = 1
                    unitPrice = $unitCost * 1.1
                    totalCost = $unitCost * 1.1
                }
            },
            @{
                type = "CONSUMPTION"
                description = "Compra - reposição mensal"
                daysAgo = 7
                metrics = @{
                    quantity = Get-Random -Minimum 1 -Maximum 3
                    unitPrice = $unitCost
                    totalCost = $unitCost * (Get-Random -Minimum 1 -Maximum 3)
                }
            }
        )
    }

    # Registrar cada evento
    foreach ($eventData in $events) {
        $eventDate = (Get-Date).AddDays(-$eventData.daysAgo).ToUniversalTime().ToString("o")

        $eventBody = @{
            itemId = $item.id
            userId = $userId
            eventType = $eventData.type
            eventDate = $eventDate
            description = $eventData.description
            metrics = $eventData.metrics
        } | ConvertTo-Json -Depth 10

        try {
            $event = Invoke-RestMethod -Uri "$baseUrl/events" -Method POST -ContentType "application/json" -Body $eventBody
            $eventsCreated++
            Write-Host "    ✅ $($eventData.description)" -ForegroundColor Green
        } catch {
            Write-Host "    ❌ Erro: $($eventData.description)" -ForegroundColor Red
        }
    }

    Write-Host ""
}

Write-Host "✅ $eventsCreated eventos criados!" -ForegroundColor Green
Write-Host ""

# ========================================
# CRIAR ALERTAS BASEADOS NOS EVENTOS
# ========================================

Write-Host "🔔 Criando alertas baseados nos eventos..." -ForegroundColor Yellow
Write-Host ""

foreach ($item in $itemsCreated) {
    Write-Host "  📦 $($item.name):" -ForegroundColor Cyan

    $alerts = @()

    if ($item.templateCode -eq "VEHICLE") {
        # Alertas de veículos
        $alerts = @(
            @{
                ruleId = [Guid]::NewGuid().ToString()
                alertType = "SCHEDULED"
                title = "Troca de óleo vencendo"
                message = "Próxima troca de óleo em 1.000 km"
                priority = 4
                daysUntilDue = 15
            },
            @{
                ruleId = [Guid]::NewGuid().ToString()
                alertType = "THRESHOLD"
                title = "Revisão periódica"
                message = "Revisão dos 50.000 km se aproximando"
                priority = 3
                daysUntilDue = 30
            }
        )
    }
    elseif ($item.templateCode -eq "UTILITY_BILL") {
        # Alertas de contas
        $alerts = @(
            @{
                ruleId = [Guid]::NewGuid().ToString()
                alertType = "SCHEDULED"
                title = "Vencimento da conta"
                message = "Conta vence em 5 dias - R$ $([math]::Round($item.metadata.averageCost, 2))"
                priority = 5
                daysUntilDue = 5
            },
            @{
                ruleId = [Guid]::NewGuid().ToString()
                alertType = "THRESHOLD"
                title = "Consumo elevado"
                message = "Consumo acima da média detectado"
                priority = 3
                daysUntilDue = 10
            }
        )
    }
    else {
        # Alertas de consumíveis
        $alerts = @(
            @{
                ruleId = [Guid]::NewGuid().ToString()
                alertType = "THRESHOLD"
                title = "Estoque baixo"
                message = "Apenas 1 unidade em estoque"
                priority = 4
                daysUntilDue = 3
            },
            @{
                ruleId = [Guid]::NewGuid().ToString()
                alertType = "SCHEDULED"
                title = "Reposição programada"
                message = "Data de reposição habitual se aproximando"
                priority = 2
                daysUntilDue = 7
            }
        )
    }

    # Criar cada alerta
    foreach ($alertData in $alerts) {
        $dueDate = (Get-Date).AddDays($alertData.daysUntilDue).ToUniversalTime().ToString("o")

        $alertBody = @{
            itemId = $item.id
            userId = $userId
            ruleId = $alertData.ruleId
            alertType = $alertData.alertType
            title = $alertData.title
            message = $alertData.message
            priority = $alertData.priority
            dueAt = $dueDate
        } | ConvertTo-Json -Depth 10

        try {
            $alert = Invoke-RestMethod -Uri "$baseUrl/alerts" -Method POST -ContentType "application/json" -Body $alertBody
            $alertsCreated++
            Write-Host "    🔔 $($alertData.title)" -ForegroundColor Green
        } catch {
            Write-Host "    ❌ Erro: $($alertData.title)" -ForegroundColor Red
        }
    }

    Write-Host ""
}

Write-Host "✅ $alertsCreated alertas criados!" -ForegroundColor Green
Write-Host ""

# ========================================
# RESUMO FINAL
# ========================================

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "✅ POPULAÇÃO DE DADOS CONCLUÍDA!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "📊 Estatísticas:" -ForegroundColor Yellow
Write-Host "  📦 Items criados:   $($itemsCreated.Count)" -ForegroundColor Gray
Write-Host "  📅 Eventos criados: $eventsCreated" -ForegroundColor Gray
Write-Host "  🔔 Alertas criados: $alertsCreated" -ForegroundColor Gray
Write-Host ""
Write-Host "📋 Distribuição de Items:" -ForegroundColor Yellow
Write-Host "  🚗 Veículos:    5 items" -ForegroundColor Gray
Write-Host "  💰 Contas:      5 items" -ForegroundColor Gray
Write-Host "  📦 Consumíveis: 5 items" -ForegroundColor Gray
Write-Host ""
Write-Host "🔍 Verificar dados:" -ForegroundColor Yellow
Write-Host "  🌐 Swagger UI:      http://localhost:8080/swagger-ui.html" -ForegroundColor Cyan
Write-Host "  🗄️  Mongo Express:   http://localhost:8081" -ForegroundColor Cyan
Write-Host ""
Write-Host "📖 Consultas úteis:" -ForegroundColor Yellow
Write-Host "  GET /api/v1/items?userId=$userId" -ForegroundColor Gray
Write-Host "  GET /api/v1/events?itemId={itemId}" -ForegroundColor Gray
Write-Host "  GET /api/v1/alerts/pending?userId=$userId" -ForegroundColor Gray
Write-Host ""
Write-Host "🎉 Sistema populado com dados realistas para testes!" -ForegroundColor Green
Write-Host ""
