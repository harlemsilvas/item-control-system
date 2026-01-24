# Teste simples - Criar primeiro item no Railway
# Pressupoe que a API ja esta rodando

Write-Host ""
Write-Host "CRIAR PRIMEIRO ITEM - RAILWAY" -ForegroundColor Cyan
Write-Host "=============================" -ForegroundColor Cyan
Write-Host ""

# Verificar se API esta online
Write-Host "Verificando API..." -ForegroundColor Yellow
try {
    $health = Invoke-RestMethod -Uri "http://localhost:8080/actuator/health" -Method GET -ErrorAction Stop
    Write-Host "   API Online!" -ForegroundColor Green
} catch {
    Write-Host "   ERRO: API nao esta rodando!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Execute primeiro:" -ForegroundColor Yellow
    Write-Host "   .\scripts\quick-start-prod.ps1" -ForegroundColor Cyan
    Write-Host ""
    exit 1
}

Write-Host ""

# Gerar UUID para userId
$userId = [System.Guid]::NewGuid().ToString()

Write-Host "Criando item..." -ForegroundColor Yellow
Write-Host "   userId: $userId" -ForegroundColor Gray

$itemData = @{
    userId = $userId
    name = "Primeiro Item Railway"
    nickname = "item-railway-001"
    templateCode = "GENERAL"
    tags = @("railway", "teste", "primeiro")
    metadata = @{
        ambiente = "railway"
        criado_em = (Get-Date).ToString("yyyy-MM-dd HH:mm:ss")
        descricao = "Item de teste para criar collection no Railway"
    }
} | ConvertTo-Json -Depth 10

Write-Host ""
Write-Host "JSON enviado:" -ForegroundColor Gray
Write-Host $itemData -ForegroundColor DarkGray
Write-Host ""

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/v1/items" `
        -Method POST `
        -Body $itemData `
        -ContentType "application/json; charset=utf-8" `
        -ErrorAction Stop

    Write-Host "SUCESSO! Item criado!" -ForegroundColor Green
    Write-Host ""
    Write-Host "Detalhes do item:" -ForegroundColor Cyan
    Write-Host "   ID: $($response.id)" -ForegroundColor White
    Write-Host "   Nome: $($response.name)" -ForegroundColor White
    Write-Host "   Nickname: $($response.nickname)" -ForegroundColor White
    Write-Host "   Template: $($response.templateCode)" -ForegroundColor White
    Write-Host "   Criado em: $($response.createdAt)" -ForegroundColor White
    Write-Host ""

    Write-Host "Collection criada no Railway:" -ForegroundColor Green
    Write-Host "   Database: item_control_db" -ForegroundColor White
    Write-Host "   Collection: items" -ForegroundColor White
    Write-Host ""

    Write-Host "Verifique no MongoDB Compass!" -ForegroundColor Yellow
    Write-Host "   Atualize (F5) e veja a collection 'items'" -ForegroundColor Gray
    Write-Host ""

    # Listar todos os items
    Write-Host "Listando todos os items..." -ForegroundColor Yellow
    $allItems = Invoke-RestMethod -Uri "http://localhost:8080/api/v1/items" -Method GET
    Write-Host "   Total de items: $($allItems.Count)" -ForegroundColor White

} catch {
    Write-Host "ERRO ao criar item:" -ForegroundColor Red
    Write-Host "   $($_.Exception.Message)" -ForegroundColor Red

    if ($_.ErrorDetails.Message) {
        Write-Host ""
        Write-Host "Detalhes do erro:" -ForegroundColor Yellow
        Write-Host $_.ErrorDetails.Message -ForegroundColor Gray
    }

    Write-Host ""
    Write-Host "Verifique:" -ForegroundColor Yellow
    Write-Host "   1. API esta rodando em modo producao?" -ForegroundColor Gray
    Write-Host "   2. MongoDB Railway esta acessivel?" -ForegroundColor Gray
    Write-Host "   3. Credenciais corretas?" -ForegroundColor Gray
}

# Se criacao foi sucesso, tentar listar items
if ($response) {
    Write-Host ""
    Write-Host "Tentando listar todos os items..." -ForegroundColor Yellow

    try {
        # Tentar GET simples
        $allItems = Invoke-RestMethod -Uri "http://localhost:8080/api/v1/items" -Method GET -ErrorAction Stop

        if ($allItems -is [Array]) {
            Write-Host "   Total de items: $($allItems.Count)" -ForegroundColor White
        } else {
            Write-Host "   Total de items: 1" -ForegroundColor White
        }

    } catch {
        # Se falhar, pode ser que precise de userId
        Write-Host "   Endpoint requer parametros. Tentando com userId..." -ForegroundColor Gray

        try {
            $allItemsUser = Invoke-RestMethod -Uri "http://localhost:8080/api/v1/items?userId=$userId" -Method GET -ErrorAction Stop
            Write-Host "   Items do usuario: $($allItemsUser.Count)" -ForegroundColor White
        } catch {
            Write-Host "   Nao foi possivel listar (endpoint pode requerer autenticacao)" -ForegroundColor Gray
            Write-Host "   Mas o item FOI CRIADO com sucesso!" -ForegroundColor Green
        }
    }
}

Write-Host ""
Write-Host "Pressione ENTER para sair..."
Read-Host
