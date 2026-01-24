# Script para executar populacao de dados
# Aguarda API e executa populate-test-data.ps1

Write-Host ""
Write-Host "Preparando para popular o sistema..." -ForegroundColor Cyan
Write-Host ""

# Verificar se MongoDB esta rodando
$mongoRunning = docker ps --filter "name=item-control-mongodb" --format "{{.Names}}"
if (-not $mongoRunning) {
    Write-Host "MongoDB nao esta rodando!" -ForegroundColor Red
    Write-Host "Execute: docker compose up -d" -ForegroundColor Yellow
    exit 1
}

Write-Host "MongoDB esta rodando" -ForegroundColor Green

# Verificar se API esta rodando
Write-Host "Verificando API..." -ForegroundColor Yellow

$maxAttempts = 30
$attempt = 0
$apiReady = $false

while ($attempt -lt $maxAttempts -and -not $apiReady) {
    $attempt++
    try {
        $health = Invoke-RestMethod -Uri "http://localhost:8080/actuator/health" -TimeoutSec 2 -ErrorAction Stop
        if ($health.status -eq "UP") {
            $apiReady = $true
            Write-Host "API esta respondendo!" -ForegroundColor Green
        }
    } catch {
        if ($attempt -eq 1) {
            Write-Host "API ja esta rodando. Continuando..." -ForegroundColor Yellow
        }
        Write-Host "  Tentativa $attempt/$maxAttempts..." -ForegroundColor Gray
        Start-Sleep -Seconds 2
    }
}

if (-not $apiReady) {
    Write-Host ""
    Write-Host "API nao respondeu apos $maxAttempts tentativas" -ForegroundColor Red
    Write-Host ""
    Write-Host "Verifique se a API esta rodando na porta 8080" -ForegroundColor Yellow
    Write-Host ""
    exit 1
}

Write-Host ""
Write-Host "Executando populacao de dados..." -ForegroundColor Cyan
Write-Host ""

# Executar script de populacao
$scriptPath = Join-Path $PSScriptRoot "populate-test-data.ps1"
& $scriptPath
