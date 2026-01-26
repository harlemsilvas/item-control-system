# 🚀 START MONGODB LOCAL (Docker)
# Inicia MongoDB local para desenvolvimento

Write-Host "=====================================================" -ForegroundColor Cyan
Write-Host "   🍃 INICIANDO MONGODB LOCAL (Docker)" -ForegroundColor Cyan
Write-Host "=====================================================" -ForegroundColor Cyan
Write-Host ""

# Verificar se Docker está rodando
try {
    docker --version | Out-Null
} catch {
    Write-Host "❌ Docker não está instalado ou não está rodando!" -ForegroundColor Red
    Write-Host "   Instale o Docker Desktop e tente novamente." -ForegroundColor Yellow
    pause
    exit 1
}

# Parar container existente (se houver)
Write-Host "[1/3] Verificando containers existentes..." -ForegroundColor Yellow
$existing = docker ps -a --filter "name=mongodb" --format "{{.Names}}"
if ($existing) {
    Write-Host "   ⚠️ Container 'mongodb' já existe. Removendo..." -ForegroundColor Yellow
    docker stop mongodb 2>$null
    docker rm mongodb 2>$null
    Write-Host "   ✅ Container removido" -ForegroundColor Green
}

# Criar e iniciar MongoDB
Write-Host "[2/3] Criando container MongoDB..." -ForegroundColor Yellow
docker run -d `
    --name mongodb `
    -p 27017:27017 `
    -e MONGO_INITDB_DATABASE=item_control_db_dev `
    mongo:latest

if ($LASTEXITCODE -eq 0) {
    Write-Host "   ✅ MongoDB iniciado com sucesso!" -ForegroundColor Green
} else {
    Write-Host "   ❌ Erro ao iniciar MongoDB" -ForegroundColor Red
    pause
    exit 1
}

# Aguardar MongoDB ficar pronto
Write-Host "[3/3] Aguardando MongoDB ficar pronto..." -ForegroundColor Yellow
Start-Sleep -Seconds 3

# Testar conexão
Write-Host ""
Write-Host "=====================================================" -ForegroundColor Cyan
Write-Host "   ✅ MONGODB RODANDO!" -ForegroundColor Green
Write-Host "=====================================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "📊 Informações da conexão:" -ForegroundColor White
Write-Host "   URI: mongodb://localhost:27017" -ForegroundColor Gray
Write-Host "   Database: item_control_db_dev" -ForegroundColor Gray
Write-Host "   Porta: 27017" -ForegroundColor Gray
Write-Host ""
Write-Host "🛠️ Comandos úteis:" -ForegroundColor White
Write-Host "   Ver logs:     docker logs -f mongodb" -ForegroundColor Gray
Write-Host "   Parar:        docker stop mongodb" -ForegroundColor Gray
Write-Host "   Iniciar:      docker start mongodb" -ForegroundColor Gray
Write-Host "   Remover:      docker rm -f mongodb" -ForegroundColor Gray
Write-Host ""
Write-Host "📌 Próximo passo:" -ForegroundColor Yellow
Write-Host "   Execute: .\start-app-local.ps1" -ForegroundColor Cyan
Write-Host ""
