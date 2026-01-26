# 🚀 START APPLICATION LOCAL
# Inicia a aplicação Spring Boot com MongoDB local

Write-Host "=====================================================" -ForegroundColor Cyan
Write-Host "   🚀 INICIANDO APLICAÇÃO LOCAL" -ForegroundColor Cyan
Write-Host "=====================================================" -ForegroundColor Cyan
Write-Host ""

# Carregar variáveis do .env.local
Write-Host "[1/5] Carregando variáveis de ambiente (.env.local)..." -ForegroundColor Yellow
if (Test-Path ".env.local") {
    Get-Content ".env.local" | ForEach-Object {
        if ($_ -match '^\s*([^#][^=]+)\s*=\s*(.+)\s*$') {
            $name = $matches[1].Trim()
            $value = $matches[2].Trim()
            [Environment]::SetEnvironmentVariable($name, $value, "Process")
            Write-Host "   ✅ $name" -ForegroundColor Green
        }
    }
} else {
    Write-Host "   ❌ Arquivo .env.local não encontrado!" -ForegroundColor Red
    Write-Host "   Crie o arquivo .env.local na raiz do projeto." -ForegroundColor Yellow
    pause
    exit 1
}

# Verificar se MongoDB está rodando
Write-Host ""
Write-Host "[2/5] Verificando MongoDB..." -ForegroundColor Yellow
$mongoRunning = docker ps --filter "name=mongodb" --format "{{.Names}}"
if (-not $mongoRunning) {
    Write-Host "   ❌ MongoDB não está rodando!" -ForegroundColor Red
    Write-Host "   Execute primeiro: .\scripts\start-mongodb-local.ps1" -ForegroundColor Yellow
    pause
    exit 1
}
Write-Host "   ✅ MongoDB rodando" -ForegroundColor Green

# Matar processos na porta 8080
Write-Host ""
Write-Host "[3/5] Verificando porta 8080..." -ForegroundColor Yellow
$process = Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue | Select-Object -ExpandProperty OwningProcess -Unique
if ($process) {
    Write-Host "   ⚠️ Porta 8080 em uso. Finalizando processo..." -ForegroundColor Yellow
    Stop-Process -Id $process -Force
    Start-Sleep -Seconds 2
    Write-Host "   ✅ Porta liberada" -ForegroundColor Green
} else {
    Write-Host "   ✅ Porta 8080 disponível" -ForegroundColor Green
}

# Build da aplicação
Write-Host ""
Write-Host "[4/5] Compilando aplicação..." -ForegroundColor Yellow
Write-Host "   (Isso pode demorar na primeira vez...)" -ForegroundColor Gray
cd modules/api
mvn clean package -DskipTests -q
if ($LASTEXITCODE -ne 0) {
    Write-Host "   ❌ Erro ao compilar!" -ForegroundColor Red
    pause
    exit 1
}
Write-Host "   ✅ Compilação concluída" -ForegroundColor Green

# Iniciar aplicação
Write-Host ""
Write-Host "[5/5] Iniciando aplicação Spring Boot..." -ForegroundColor Yellow
Write-Host ""
Write-Host "=====================================================" -ForegroundColor Cyan
Write-Host "   🌟 APLICAÇÃO INICIANDO..." -ForegroundColor Green
Write-Host "=====================================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "📊 Configuração:" -ForegroundColor White
Write-Host "   URL: http://localhost:8080" -ForegroundColor Gray
Write-Host "   MongoDB: mongodb://localhost:27017" -ForegroundColor Gray
Write-Host "   Profile: dev" -ForegroundColor Gray
Write-Host ""
Write-Host "🛠️ Endpoints úteis:" -ForegroundColor White
Write-Host "   Health: http://localhost:8080/actuator/health" -ForegroundColor Gray
Write-Host "   Items: http://localhost:8080/api/v1/items" -ForegroundColor Gray
Write-Host "   Categories: http://localhost:8080/api/v1/categories" -ForegroundColor Gray
Write-Host ""
Write-Host "=====================================================" -ForegroundColor Cyan
Write-Host ""

java -jar target\item-control-api-0.1.0-SNAPSHOT.jar
