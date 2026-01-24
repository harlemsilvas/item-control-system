# Script simplificado - executa diretamente o populate
# Use este se a API ja estiver rodando

$ErrorActionPreference = "Stop"

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "POPULANDO SISTEMA COM DADOS DE TESTE" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Verificar API
try {
    $health = Invoke-RestMethod -Uri "http://localhost:8080/actuator/health" -TimeoutSec 3
    Write-Host "API esta respondendo!" -ForegroundColor Green
    Write-Host ""
} catch {
    Write-Host "ERRO: API nao esta respondendo na porta 8080" -ForegroundColor Red
    Write-Host "Certifique-se que a API esta rodando" -ForegroundColor Yellow
    exit 1
}

# Executar populate
$scriptPath = Join-Path $PSScriptRoot "populate-test-data.ps1"
if (Test-Path $scriptPath) {
    & $scriptPath
} else {
    Write-Host "ERRO: Arquivo populate-test-data.ps1 nao encontrado" -ForegroundColor Red
    exit 1
}
