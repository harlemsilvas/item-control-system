# Item Control System - Start MongoDB Docker
# Inicia apenas o MongoDB em Docker para desenvolvimento local

Write-Host "========================================================" -ForegroundColor Cyan
Write-Host "STARTING MONGODB DOCKER - DEV LOCAL" -ForegroundColor Cyan
Write-Host "========================================================" -ForegroundColor Cyan
Write-Host ""

# Verificar se Docker está rodando
Write-Host "[1/5] Verificando Docker..." -ForegroundColor Yellow

# Tentar várias vezes (Docker Desktop pode estar iniciando)
$maxAttempts = 5
$attempt = 0
$dockerOk = $false

while ($attempt -lt $maxAttempts -and -not $dockerOk) {
    $attempt++

    $dockerInfo = docker info 2>&1
    if ($LASTEXITCODE -eq 0) {
        $dockerOk = $true
    } else {
        if ($attempt -lt $maxAttempts) {
            Write-Host "   Tentativa $attempt/$maxAttempts... Aguardando Docker Desktop iniciar..." -ForegroundColor Gray
            Start-Sleep -Seconds 3
        }
    }
}

if (-not $dockerOk) {
    Write-Host "   ❌ Docker não está respondendo!" -ForegroundColor Red
    Write-Host "" -ForegroundColor Yellow
    Write-Host "   Possíveis soluções:" -ForegroundColor Yellow
    Write-Host "   1. Abra o Docker Desktop" -ForegroundColor White
    Write-Host "   2. Aguarde ele iniciar completamente (ícone fica sem animação)" -ForegroundColor White
    Write-Host "   3. Execute este script novamente" -ForegroundColor White
    Write-Host ""
    Write-Host "   Se o Docker Desktop está aberto, pode estar travado." -ForegroundColor Gray
    Write-Host "   Tente fechar e abrir novamente." -ForegroundColor Gray
    Write-Host ""
    Read-Host "Pressione ENTER para sair"
    exit 1
}

Write-Host "   ✅ Docker está rodando" -ForegroundColor Green
Write-Host ""

# Verificar se porta 27017 está livre
Write-Host "[2/5] Verificando porta 27017..." -ForegroundColor Yellow
$portInUse = Get-NetTCPConnection -LocalPort 27017 -ErrorAction SilentlyContinue
if ($portInUse) {
    Write-Host "   ⚠️  Porta 27017 em uso. Tentando parar container existente..." -ForegroundColor Yellow
    docker-compose -f docker-compose.mongodb.yml down 2>$null
    Start-Sleep -Seconds 2
}
Write-Host "   ✅ Porta 27017 disponível" -ForegroundColor Green
Write-Host ""

# Parar containers antigos
Write-Host "[3/5] Parando containers MongoDB antigos..." -ForegroundColor Yellow
docker-compose -f docker-compose.mongodb.yml down 2>$null
Write-Host "   ✅ Containers antigos parados" -ForegroundColor Green
Write-Host ""

# Iniciar MongoDB
Write-Host "[4/5] Iniciando MongoDB Docker..." -ForegroundColor Yellow
Write-Host "   (Isso pode demorar na primeira vez...)" -ForegroundColor Gray
docker-compose -f docker-compose.mongodb.yml up -d

if ($LASTEXITCODE -ne 0) {
    Write-Host "   ❌ Erro ao iniciar MongoDB" -ForegroundColor Red
    exit 1
}
Write-Host "   ✅ MongoDB Docker iniciado" -ForegroundColor Green
Write-Host ""

# Aguardar MongoDB ficar pronto
Write-Host "[5/5] Aguardando MongoDB ficar pronto..." -ForegroundColor Yellow
$maxAttempts = 30
$attempt = 0
$ready = $false

while ($attempt -lt $maxAttempts -and -not $ready) {
    $attempt++
    Write-Host "   Tentativa $attempt/$maxAttempts..." -ForegroundColor Gray

    $health = docker inspect item-control-mongo-dev --format='{{.State.Health.Status}}' 2>$null

    if ($health -eq "healthy") {
        $ready = $true
    } else {
        Start-Sleep -Seconds 2
    }
}

Write-Host ""
if ($ready) {
    Write-Host "========================================================" -ForegroundColor Green
    Write-Host "MONGODB DOCKER PRONTO!" -ForegroundColor Green
    Write-Host "========================================================" -ForegroundColor Green
} else {
    Write-Host "========================================================" -ForegroundColor Yellow
    Write-Host "MONGODB INICIADO (aguarde mais alguns segundos)" -ForegroundColor Yellow
    Write-Host "========================================================" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "Connection info:" -ForegroundColor Cyan
Write-Host "   Host: localhost" -ForegroundColor White
Write-Host "   Port: 27017" -ForegroundColor White
Write-Host "   User: admin" -ForegroundColor White
Write-Host "   Password: admin123" -ForegroundColor White
Write-Host "   Database: item_control_db_dev" -ForegroundColor White
Write-Host ""
Write-Host "Connection string:" -ForegroundColor Cyan
Write-Host "   mongodb://admin:admin123@localhost:27017/item_control_db_dev?authSource=admin" -ForegroundColor White
Write-Host ""
Write-Host "Useful commands:" -ForegroundColor Cyan
Write-Host "   Logs: docker logs -f item-control-mongo-dev" -ForegroundColor Gray
Write-Host "   Stop: docker-compose -f docker-compose.mongodb.yml down" -ForegroundColor Gray
Write-Host "   Shell: docker exec -it item-control-mongo-dev mongosh -u admin -p admin123" -ForegroundColor Gray
Write-Host ""
Write-Host "========================================================" -ForegroundColor Cyan
Write-Host ""
