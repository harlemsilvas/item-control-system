# ============================================
# Script de População - TEMPLATES
# Cria templates padrão (GLOBAL) para testes
# ============================================

param(
    [string]$BaseUrl = "http://localhost:8080/api/v1",
    [string]$UserId = "550e8400-e29b-41d4-a716-446655440001",
    [switch]$Force
)

$ErrorActionPreference = "Stop"

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "POPULANDO TEMPLATES (GLOBAL)" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "BaseUrl: $BaseUrl" -ForegroundColor Gray
Write-Host "UserId:  $UserId" -ForegroundColor Gray

# ------------------------------------------------------------
# Helpers
# ------------------------------------------------------------
function Ensure-ApiUp {
    try {
        $apiRoot = $BaseUrl -replace "/api/v1$", ""
        $health = Invoke-RestMethod -Uri "$apiRoot/actuator/health" -Method GET -TimeoutSec 5
        Write-Host "✅ API está rodando" -ForegroundColor Green
    } catch {
        Write-Host "❌ API não está respondendo em $BaseUrl" -ForegroundColor Red
        throw
    }
}

function Normalize-Code([string]$code) {
    if ([string]::IsNullOrWhiteSpace($code)) { return $code }
    return $code.Trim().ToUpperInvariant().Replace(" ", "_")
}

function Create-Template($template) {
    $template.code = Normalize-Code($template.code)

    $json = $template | ConvertTo-Json -Depth 20

    try {
        $resp = Invoke-RestMethod -Uri "$BaseUrl/templates" -Method POST -ContentType "application/json" -Body $json -Headers @{"Accept-Language"="pt-BR"}
        Write-Host "  ✅ Template criado: $($template.code)" -ForegroundColor Green
        return $resp
    }
    catch {
        $msg = $_.Exception.Message
        Write-Host "  ⚠️ Não criado: $($template.code) -> $msg" -ForegroundColor Yellow
        return $null
    }
}

# ------------------------------------------------------------
# Execução
# ------------------------------------------------------------
Ensure-ApiUp

Write-Host ""
Write-Host "Obtendo categorias do usuário..." -ForegroundColor Cyan
$categories = @()
try {
    $categories = Invoke-RestMethod -Uri "$BaseUrl/categories?userId=$UserId" -Method GET -TimeoutSec 10
} catch {
    Write-Host "⚠️ Não foi possível listar categorias. Vou usar um conjunto padrão." -ForegroundColor Yellow
}

# Fallback (mantém coerência com populate-categories.ps1 e docs):
if (-not $categories -or $categories.Count -eq 0) {
    $categories = @(
        @{ name = "Veiculos" },
        @{ name = "Contas e Servicos" },
        @{ name = "Itens Consumiveis" },
        @{ name = "Eletronicos" },
        @{ name = "Imoveis" }
    )
}

# Para cada categoria existente, cria no mínimo 10 templates.
# Observação: no backend o Template não tem categoryId, então o vínculo é conceitual.

$templatesToCreate = @()

foreach ($cat in $categories) {
    $catName = $cat.name
    $catCode = (Normalize-Code($catName))

    switch -Regex ($catName) {
        "(?i)veicul" {
            $templatesToCreate += @(
                @{ scope="GLOBAL"; code="VEHICLE"; nameByLocale=@{"pt-BR"="Veículo";"en-US"="Vehicle"}; descriptionByLocale=@{"pt-BR"="Template geral para veículos"}; metadataSchema=@{ brand=@{type="string";required=$true}; model=@{type="string";required=$true}; year=@{type="number";required=$false}; plate=@{type="string";required=$false}; odometer=@{type="number";required=$false} } },
                @{ scope="GLOBAL"; code="MOTORCYCLE"; nameByLocale=@{"pt-BR"="Moto";"en-US"="Motorcycle"}; descriptionByLocale=@{"pt-BR"="Template para motos"}; metadataSchema=@{ brand=@{type="string";required=$true}; model=@{type="string";required=$true}; year=@{type="number"}; displacement=@{type="number"}; plate=@{type="string"} } },
                @{ scope="GLOBAL"; code="CAR"; nameByLocale=@{"pt-BR"="Carro";"en-US"="Car"}; descriptionByLocale=@{"pt-BR"="Template para carros"}; metadataSchema=@{ brand=@{type="string";required=$true}; model=@{type="string";required=$true}; year=@{type="number"}; plate=@{type="string"}; color=@{type="string"} } },
                @{ scope="GLOBAL"; code="TRUCK"; nameByLocale=@{"pt-BR"="Caminhão";"en-US"="Truck"}; descriptionByLocale=@{"pt-BR"="Template para caminhões"}; metadataSchema=@{ brand=@{type="string"}; model=@{type="string"}; year=@{type="number"}; capacityTons=@{type="number"} } },
                @{ scope="GLOBAL"; code="BICYCLE"; nameByLocale=@{"pt-BR"="Bicicleta";"en-US"="Bicycle"}; descriptionByLocale=@{"pt-BR"="Template para bicicletas"}; metadataSchema=@{ brand=@{type="string"}; type=@{type="string"}; size=@{type="string"} } },
                @{ scope="GLOBAL"; code="VEHICLE_INSURANCE"; nameByLocale=@{"pt-BR"="Seguro do Veículo";"en-US"="Vehicle Insurance"}; descriptionByLocale=@{"pt-BR"="Acompanhamento de seguro"}; metadataSchema=@{ insurer=@{type="string";required=$true}; policyNumber=@{type="string"}; expiresAt=@{type="string"} } },
                @{ scope="GLOBAL"; code="VEHICLE_MAINTENANCE"; nameByLocale=@{"pt-BR"="Manutenção do Veículo";"en-US"="Vehicle Maintenance"}; descriptionByLocale=@{"pt-BR"="Manutenções e revisões"}; metadataSchema=@{ lastServiceDate=@{type="string"}; nextServiceDate=@{type="string"}; kmInterval=@{type="number"} } },
                @{ scope="GLOBAL"; code="TIRE_CHANGE"; nameByLocale=@{"pt-BR"="Troca de Pneus";"en-US"="Tire Change"}; descriptionByLocale=@{"pt-BR"="Controle de pneus"}; metadataSchema=@{ tireBrand=@{type="string"}; installedAtKm=@{type="number"}; recommendedChangeAtKm=@{type="number"} } },
                @{ scope="GLOBAL"; code="OIL_CHANGE"; nameByLocale=@{"pt-BR"="Troca de Óleo";"en-US"="Oil Change"}; descriptionByLocale=@{"pt-BR"="Controle de óleo e filtro"}; metadataSchema=@{ oilType=@{type="string"}; lastChangeAtKm=@{type="number"}; nextChangeAtKm=@{type="number"} } },
                @{ scope="GLOBAL"; code="VEHICLE_DOCUMENTS"; nameByLocale=@{"pt-BR"="Documentos do Veículo";"en-US"="Vehicle Documents"}; descriptionByLocale=@{"pt-BR"="Licenciamento, IPVA, etc."}; metadataSchema=@{ documentType=@{type="string"}; dueDate=@{type="string"}; amount=@{type="number"} } }
            )
        }
        "(?i)conta|servic" {
            $templatesToCreate += @(
                @{ scope="GLOBAL"; code="UTILITY_BILL"; nameByLocale=@{"pt-BR"="Conta de Serviço";"en-US"="Utility Bill"}; descriptionByLocale=@{"pt-BR"="Água, luz, internet, etc."}; metadataSchema=@{ supplier=@{type="string";required=$true}; accountNumber=@{type="string"}; dueDay=@{type="number"}; averageCost=@{type="number"} } },
                @{ scope="GLOBAL"; code="WATER_BILL"; nameByLocale=@{"pt-BR"="Conta de Água";"en-US"="Water Bill"}; descriptionByLocale=@{"pt-BR"="Controle de água"}; metadataSchema=@{ supplier=@{type="string"}; dueDay=@{type="number"}; averageCost=@{type="number"} } },
                @{ scope="GLOBAL"; code="ELECTRICITY_BILL"; nameByLocale=@{"pt-BR"="Conta de Luz";"en-US"="Electricity Bill"}; descriptionByLocale=@{"pt-BR"="Controle de energia"}; metadataSchema=@{ supplier=@{type="string"}; dueDay=@{type="number"}; averageCost=@{type="number"} } },
                @{ scope="GLOBAL"; code="INTERNET_BILL"; nameByLocale=@{"pt-BR"="Internet";"en-US"="Internet"}; descriptionByLocale=@{"pt-BR"="Fibra, banda larga"}; metadataSchema=@{ supplier=@{type="string"}; speed=@{type="string"}; dueDay=@{type="number"}; averageCost=@{type="number"} } },
                @{ scope="GLOBAL"; code="PHONE_PLAN"; nameByLocale=@{"pt-BR"="Plano de Celular";"en-US"="Phone Plan"}; descriptionByLocale=@{"pt-BR"="Plano móvel"}; metadataSchema=@{ supplier=@{type="string"}; phoneNumber=@{type="string"}; dueDay=@{type="number"}; averageCost=@{type="number"} } },
                @{ scope="GLOBAL"; code="SUBSCRIPTION"; nameByLocale=@{"pt-BR"="Assinatura";"en-US"="Subscription"}; descriptionByLocale=@{"pt-BR"="Serviços recorrentes"}; metadataSchema=@{ service=@{type="string";required=$true}; dueDay=@{type="number"}; amount=@{type="number"} } },
                @{ scope="GLOBAL"; code="INSURANCE"; nameByLocale=@{"pt-BR"="Seguro";"en-US"="Insurance"}; descriptionByLocale=@{"pt-BR"="Seguros em geral"}; metadataSchema=@{ insurer=@{type="string"}; policyNumber=@{type="string"}; expiresAt=@{type="string"}; amount=@{type="number"} } },
                @{ scope="GLOBAL"; code="CONDO_FEE"; nameByLocale=@{"pt-BR"="Condomínio";"en-US"="Condominium Fee"}; descriptionByLocale=@{"pt-BR"="Taxa de condomínio"}; metadataSchema=@{ building=@{type="string"}; unit=@{type="string"}; dueDay=@{type="number"}; amount=@{type="number"} } },
                @{ scope="GLOBAL"; code="RENT"; nameByLocale=@{"pt-BR"="Aluguel";"en-US"="Rent"}; descriptionByLocale=@{"pt-BR"="Pagamento de aluguel"}; metadataSchema=@{ landlord=@{type="string"}; dueDay=@{type="number"}; amount=@{type="number"} } },
                @{ scope="GLOBAL"; code="TAX"; nameByLocale=@{"pt-BR"="Imposto";"en-US"="Tax"}; descriptionByLocale=@{"pt-BR"="Impostos e tributos"}; metadataSchema=@{ taxType=@{type="string"}; dueDate=@{type="string"}; amount=@{type="number"} } }
            )
        }
        "(?i)consum" {
            $templatesToCreate += @(
                @{ scope="GLOBAL"; code="CONSUMABLE_ITEM"; nameByLocale=@{"pt-BR"="Consumível";"en-US"="Consumable"}; descriptionByLocale=@{"pt-BR"="Itens consumíveis"}; metadataSchema=@{ brand=@{type="string"}; quantity=@{type="string"}; unitCost=@{type="number"}; averageConsumption=@{type="string"} } },
                @{ scope="GLOBAL"; code="GAS_CYLINDER"; nameByLocale=@{"pt-BR"="Botijão de Gás";"en-US"="Gas Cylinder"}; descriptionByLocale=@{"pt-BR"="Gás de cozinha"}; metadataSchema=@{ brand=@{type="string"}; weight=@{type="string"}; unitCost=@{type="number"} } },
                @{ scope="GLOBAL"; code="WATER_GALLON"; nameByLocale=@{"pt-BR"="Galão de Água";"en-US"="Water Gallon"}; descriptionByLocale=@{"pt-BR"="Água mineral"}; metadataSchema=@{ volume=@{type="string"}; supplier=@{type="string"}; unitCost=@{type="number"} } },
                @{ scope="GLOBAL"; code="CLEANING_SUPPLY"; nameByLocale=@{"pt-BR"="Produtos de Limpeza";"en-US"="Cleaning Supply"}; descriptionByLocale=@{"pt-BR"="Limpeza doméstica"}; metadataSchema=@{ brand=@{type="string"}; quantity=@{type="string"}; unitCost=@{type="number"} } },
                @{ scope="GLOBAL"; code="PERSONAL_CARE"; nameByLocale=@{"pt-BR"="Higiene Pessoal";"en-US"="Personal Care"}; descriptionByLocale=@{"pt-BR"="Itens de higiene"}; metadataSchema=@{ brand=@{type="string"}; quantity=@{type="string"}; unitCost=@{type="number"} } },
                @{ scope="GLOBAL"; code="FOOD"; nameByLocale=@{"pt-BR"="Alimentos";"en-US"="Food"}; descriptionByLocale=@{"pt-BR"="Itens alimentícios"}; metadataSchema=@{ brand=@{type="string"}; quantity=@{type="string"}; expiresAt=@{type="string"} } },
                @{ scope="GLOBAL"; code="PET_SUPPLY"; nameByLocale=@{"pt-BR"="Pet";"en-US"="Pet Supply"}; descriptionByLocale=@{"pt-BR"="Itens para pets"}; metadataSchema=@{ petType=@{type="string"}; brand=@{type="string"}; quantity=@{type="string"} } },
                @{ scope="GLOBAL"; code="BABY_SUPPLY"; nameByLocale=@{"pt-BR"="Bebê";"en-US"="Baby Supply"}; descriptionByLocale=@{"pt-BR"="Fraldas e afins"}; metadataSchema=@{ brand=@{type="string"}; size=@{type="string"}; quantity=@{type="string"} } },
                @{ scope="GLOBAL"; code="COFFEE"; nameByLocale=@{"pt-BR"="Café";"en-US"="Coffee"}; descriptionByLocale=@{"pt-BR"="Café e filtros"}; metadataSchema=@{ brand=@{type="string"}; type=@{type="string"}; quantity=@{type="string"} } },
                @{ scope="GLOBAL"; code="BATHROOM_SUPPLY"; nameByLocale=@{"pt-BR"="Banheiro";"en-US"="Bathroom Supply"}; descriptionByLocale=@{"pt-BR"="Papel, detergente, etc"}; metadataSchema=@{ brand=@{type="string"}; quantity=@{type="string"} } }
            )
        }
        "(?i)eletron" {
            $templatesToCreate += @(
                @{ scope="GLOBAL"; code="ELECTRONIC_DEVICE"; nameByLocale=@{"pt-BR"="Dispositivo Eletrônico";"en-US"="Electronic Device"}; descriptionByLocale=@{"pt-BR"="Eletrônicos em geral"}; metadataSchema=@{ brand=@{type="string"}; model=@{type="string"}; serialNumber=@{type="string"}; purchaseDate=@{type="string"} } },
                @{ scope="GLOBAL"; code="NOTEBOOK"; nameByLocale=@{"pt-BR"="Notebook";"en-US"="Laptop"}; descriptionByLocale=@{"pt-BR"="Notebook/PC portátil"}; metadataSchema=@{ brand=@{type="string"}; model=@{type="string"}; cpu=@{type="string"}; ram=@{type="string"}; storage=@{type="string"} } },
                @{ scope="GLOBAL"; code="SMARTPHONE"; nameByLocale=@{"pt-BR"="Smartphone";"en-US"="Smartphone"}; descriptionByLocale=@{"pt-BR"="Celular"}; metadataSchema=@{ brand=@{type="string"}; model=@{type="string"}; imei=@{type="string"}; storage=@{type="string"} } },
                @{ scope="GLOBAL"; code="TV"; nameByLocale=@{"pt-BR"="Televisão";"en-US"="TV"}; descriptionByLocale=@{"pt-BR"="Televisores"}; metadataSchema=@{ brand=@{type="string"}; model=@{type="string"}; inches=@{type="number"} } },
                @{ scope="GLOBAL"; code="ROUTER"; nameByLocale=@{"pt-BR"="Roteador";"en-US"="Router"}; descriptionByLocale=@{"pt-BR"="Equipamentos de rede"}; metadataSchema=@{ brand=@{type="string"}; model=@{type="string"}; mac=@{type="string"} } },
                @{ scope="GLOBAL"; code="GAME_CONSOLE"; nameByLocale=@{"pt-BR"="Console";"en-US"="Game Console"}; descriptionByLocale=@{"pt-BR"="Video game"}; metadataSchema=@{ brand=@{type="string"}; model=@{type="string"}; storage=@{type="string"} } },
                @{ scope="GLOBAL"; code="PRINTER"; nameByLocale=@{"pt-BR"="Impressora";"en-US"="Printer"}; descriptionByLocale=@{"pt-BR"="Impressoras"}; metadataSchema=@{ brand=@{type="string"}; model=@{type="string"}; type=@{type="string"} } },
                @{ scope="GLOBAL"; code="CAMERA"; nameByLocale=@{"pt-BR"="Câmera";"en-US"="Camera"}; descriptionByLocale=@{"pt-BR"="Câmeras"}; metadataSchema=@{ brand=@{type="string"}; model=@{type="string"}; lens=@{type="string"} } },
                @{ scope="GLOBAL"; code="SMARTWATCH"; nameByLocale=@{"pt-BR"="Smartwatch";"en-US"="Smartwatch"}; descriptionByLocale=@{"pt-BR"="Relógio inteligente"}; metadataSchema=@{ brand=@{type="string"}; model=@{type="string"}; size=@{type="string"} } },
                @{ scope="GLOBAL"; code="HEADPHONES"; nameByLocale=@{"pt-BR"="Fone de Ouvido";"en-US"="Headphones"}; descriptionByLocale=@{"pt-BR"="Áudio"}; metadataSchema=@{ brand=@{type="string"}; model=@{type="string"}; type=@{type="string"} } }
            )
        }
        "(?i)imove" {
            $templatesToCreate += @(
                @{ scope="GLOBAL"; code="PROPERTY"; nameByLocale=@{"pt-BR"="Imóvel";"en-US"="Property"}; descriptionByLocale=@{"pt-BR"="Imóveis em geral"}; metadataSchema=@{ address=@{type="string";required=$true}; type=@{type="string"}; area=@{type="number"} } },
                @{ scope="GLOBAL"; code="APARTMENT"; nameByLocale=@{"pt-BR"="Apartamento";"en-US"="Apartment"}; descriptionByLocale=@{"pt-BR"="Apartamento"}; metadataSchema=@{ address=@{type="string"}; building=@{type="string"}; unit=@{type="string"}; area=@{type="number"} } },
                @{ scope="GLOBAL"; code="HOUSE"; nameByLocale=@{"pt-BR"="Casa";"en-US"="House"}; descriptionByLocale=@{"pt-BR"="Casa"}; metadataSchema=@{ address=@{type="string"}; rooms=@{type="number"}; area=@{type="number"} } },
                @{ scope="GLOBAL"; code="PROPERTY_TAX"; nameByLocale=@{"pt-BR"="IPTU";"en-US"="Property Tax"}; descriptionByLocale=@{"pt-BR"="Imposto do imóvel"}; metadataSchema=@{ dueDate=@{type="string"}; amount=@{type="number"} } },
                @{ scope="GLOBAL"; code="MAINTENANCE_HOME"; nameByLocale=@{"pt-BR"="Manutenção Residencial";"en-US"="Home Maintenance"}; descriptionByLocale=@{"pt-BR"="Reparos e manutenções"}; metadataSchema=@{ lastServiceDate=@{type="string"}; nextServiceDate=@{type="string"}; notes=@{type="string"} } },
                @{ scope="GLOBAL"; code="FURNITURE"; nameByLocale=@{"pt-BR"="Móveis";"en-US"="Furniture"}; descriptionByLocale=@{"pt-BR"="Móveis"}; metadataSchema=@{ type=@{type="string"}; brand=@{type="string"}; purchaseDate=@{type="string"} } },
                @{ scope="GLOBAL"; code="APPLIANCE"; nameByLocale=@{"pt-BR"="Eletrodoméstico";"en-US"="Appliance"}; descriptionByLocale=@{"pt-BR"="Eletrodomésticos"}; metadataSchema=@{ type=@{type="string"}; brand=@{type="string"}; model=@{type="string"} } },
                @{ scope="GLOBAL"; code="RENOVATION"; nameByLocale=@{"pt-BR"="Reforma";"en-US"="Renovation"}; descriptionByLocale=@{"pt-BR"="Obras e reformas"}; metadataSchema=@{ startedAt=@{type="string"}; budget=@{type="number"}; status=@{type="string"} } },
                @{ scope="GLOBAL"; code="MORTGAGE"; nameByLocale=@{"pt-BR"="Financiamento";"en-US"="Mortgage"}; descriptionByLocale=@{"pt-BR"="Financiamento imobiliário"}; metadataSchema=@{ bank=@{type="string"}; amount=@{type="number"}; dueDay=@{type="number"} } },
                @{ scope="GLOBAL"; code="PROPERTY_INSURANCE"; nameByLocale=@{"pt-BR"="Seguro Residencial";"en-US"="Home Insurance"}; descriptionByLocale=@{"pt-BR"="Seguro do imóvel"}; metadataSchema=@{ insurer=@{type="string"}; policyNumber=@{type="string"}; expiresAt=@{type="string"} } }
            )
        }
        default {
            # Categoria desconhecida: cria 10 templates genéricos prefixados pela categoria
            for ($i = 1; $i -le 10; $i++) {
                $templatesToCreate += @{
                    scope = "GLOBAL"
                    code = "$catCode" + "_TEMPLATE_" + $i
                    nameByLocale = @{
                        "pt-BR" = "Template $i - $catName"
                        "en-US" = "Template $i - $catName"
                    }
                    descriptionByLocale = @{
                        "pt-BR" = "Template gerado automaticamente para a categoria: $catName"
                    }
                    metadataSchema = @{
                        notes = @{ type = "string"; required = $false }
                    }
                }
            }
        }
    }
}

# Garantia adicional: remover duplicados por code (mantendo primeira ocorrência)
$unique = @{}
foreach ($t in $templatesToCreate) {
    $k = (Normalize-Code($t.code))
    if (-not $unique.ContainsKey($k)) {
        $unique[$k] = $t
    }
}

Write-Host ""
Write-Host "Criando templates..." -ForegroundColor Cyan

$createdCount = 0
$skippedCount = 0

foreach ($key in $unique.Keys) {
    $res = Create-Template $unique[$key]
    if ($null -ne $res) {
        $createdCount++
    } else {
        $skippedCount++
    }
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Templates criados: $createdCount" -ForegroundColor Green
Write-Host "Templates ignorados/duplicados: $skippedCount" -ForegroundColor Yellow
Write-Host "Total tentativas: $($unique.Count)" -ForegroundColor White
Write-Host "========================================" -ForegroundColor Cyan
