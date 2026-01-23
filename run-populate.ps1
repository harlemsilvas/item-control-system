# Script para executar população de dados
# Aguarda API e executa populate-test-data.ps1

Write-Host ""
Write-Host "🚀 Preparando para popular o sistema..." -ForegroundColor Cyan
Write-Host ""

# Verificar se MongoDB está rodando
$mongoRunning = docker ps --filter "name=item-control-mongodb" --format "{{.Names}}"
if (-not $mongoRunning) {
    Write-Host "❌ MongoDB não está rodando!" -ForegroundColor Red
    Write-Host "Execute: docker compose up -d" -ForegroundColor Yellow
    exit 1
}

Write-Host "✅ MongoDB está rodando" -ForegroundColor Green

# Verificar se API está rodando
Write-Host "🔍 Verificando API..." -ForegroundColor Yellow

$maxAttempts = 30
$attempt = 0
$apiReady = $false

while ($attempt -lt $maxAttempts -and -not $apiReady) {
    $attempt++
    try {
        $health = Invoke-RestMethod -Uri "http://localhost:8080/actuator/health" -TimeoutSec 2 -ErrorAction Stop
        if ($health.status -eq "UP") {
            $apiReady = $true
            Write-Host "✅ API está respondendo!" -ForegroundColor Green
        }
    } catch {
        if ($attempt -eq 1) {
            Write-Host "⏳ API não está rodando. Aguardando..." -ForegroundColor Yellow
        }
        Write-Host "  Tentativa $attempt/$maxAttempts..." -ForegroundColor Gray
        Start-Sleep -Seconds 2
    }
}

if (-not $apiReady) {
    Write-Host ""
    Write-Host "❌ API não respondeu após $maxAttempts tentativas" -ForegroundColor Red
    Write-Host ""
    Write-Host "Para iniciar a API manualmente:" -ForegroundColor Yellow
    Write-Host "  cd modules/api" -ForegroundColor Gray
    Write-Host "  java -jar target\item-control-api-0.1.0-SNAPSHOT.jar --spring.profiles.active=dev" -ForegroundColor Gray
    Write-Host ""
    exit 1
}

Write-Host ""
Write-Host "🎯 Executando população de dados..." -ForegroundColor Cyan
Write-Host ""

# Executar script de população
& ".\populate-test-data.ps1"
