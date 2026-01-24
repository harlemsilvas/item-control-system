# Script para popular dados no MongoDB Railway
# Nao depende de Docker - usa conexao direta

param(
    [string]$MongoUri = $env:MONGODB_URI
)

Write-Host ""
Write-Host "Preparando para popular MongoDB Railway..." -ForegroundColor Cyan
Write-Host ""

# Verificar se URI do MongoDB foi fornecida
if (-not $MongoUri) {
    Write-Host "MongoDB URI nao encontrada!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Execute com:" -ForegroundColor Yellow
    Write-Host '  .\scripts\run-populate-railway.ps1 -MongoUri "mongodb://usuario:senha@host:porta/database"' -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Ou defina a variavel de ambiente:" -ForegroundColor Yellow
    Write-Host '  $env:MONGODB_URI = "mongodb://usuario:senha@host:porta/database"' -ForegroundColor Yellow
    Write-Host ""
    exit 1
}

Write-Host "MongoDB URI configurada" -ForegroundColor Green

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
            Write-Host "Aguardando API iniciar..." -ForegroundColor Yellow
        }
        Write-Host "  Tentativa $attempt/$maxAttempts..." -ForegroundColor Gray
        Start-Sleep -Seconds 2
    }
}

if (-not $apiReady) {
    Write-Host ""
    Write-Host "API nao respondeu apos $maxAttempts tentativas" -ForegroundColor Red
    Write-Host ""
    Write-Host "Inicie a API com:" -ForegroundColor Yellow
    Write-Host "  cd modules\api" -ForegroundColor Yellow
    Write-Host '  java -jar target\item-control-api-0.1.0-SNAPSHOT.jar --spring.profiles.active=prod --spring.data.mongodb.uri="' + $MongoUri + '"' -ForegroundColor Yellow
    Write-Host ""
    exit 1
}

Write-Host ""
Write-Host "Executando populacao de dados..." -ForegroundColor Cyan
Write-Host ""

# Executar script de populacao
$scriptPath = Join-Path $PSScriptRoot "populate-test-data.ps1"
& $scriptPath

Write-Host ""
Write-Host "Populacao concluida!" -ForegroundColor Green
Write-Host ""