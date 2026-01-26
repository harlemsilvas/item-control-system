# Item Control System - Docker Desktop - Diagnostico e Inicializacao
# Este script verifica e tenta iniciar o Docker Desktop automaticamente

Write-Host "========================================================" -ForegroundColor Cyan
Write-Host "DOCKER DESKTOP - DIAGNOSTICO" -ForegroundColor Cyan
Write-Host "========================================================" -ForegroundColor Cyan
Write-Host ""

# Verificar se Docker Desktop está instalado
Write-Host "[1/4] Verificando instalação do Docker Desktop..." -ForegroundColor Yellow

$dockerDesktopPath = "C:\Program Files\Docker\Docker\Docker Desktop.exe"
if (-not (Test-Path $dockerDesktopPath)) {
    Write-Host "   Docker Desktop nao encontrado!" -ForegroundColor Red
    Write-Host ""
    Write-Host "   Baixe em: https://www.docker.com/products/docker-desktop" -ForegroundColor Yellow
    Write-Host ""
    exit 1
}

Write-Host "   Docker Desktop instalado" -ForegroundColor Green
Write-Host ""

# Verificar se Docker Desktop está rodando
Write-Host "[2/4] Verificando processo Docker Desktop..." -ForegroundColor Yellow

$dockerProcess = Get-Process "Docker Desktop" -ErrorAction SilentlyContinue
if (-not $dockerProcess) {
    Write-Host "   AVISO: Docker Desktop nao esta rodando" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "   Iniciando Docker Desktop..." -ForegroundColor Cyan

    Start-Process $dockerDesktopPath
    Write-Host "   Docker Desktop iniciado" -ForegroundColor Green
    Write-Host ""
    Write-Host "   Aguardando Docker Engine inicializar..." -ForegroundColor Yellow
    Write-Host "   (Isso pode demorar 30-60 segundos)" -ForegroundColor Gray
} else {
    Write-Host "   Docker Desktop esta rodando (PID: $($dockerProcess.Id))" -ForegroundColor Green
    Write-Host ""
    Write-Host "   Verificando Docker Engine..." -ForegroundColor Yellow
}
Write-Host ""

# Aguardar Docker Engine ficar pronto
Write-Host "[3/4] Aguardando Docker Engine..." -ForegroundColor Yellow

$maxAttempts = 30
$attempt = 0
$engineReady = $false

while ($attempt -lt $maxAttempts -and -not $engineReady) {
    $attempt++

    $dockerInfo = docker info 2>&1
    if ($LASTEXITCODE -eq 0) {
        $engineReady = $true
        Write-Host "   Docker Engine esta pronto!" -ForegroundColor Green
    } else {
        Write-Host "   Tentativa $attempt/$maxAttempts..." -ForegroundColor Gray
        Start-Sleep -Seconds 2
    }
}

Write-Host ""

if (-not $engineReady) {
    Write-Host "   Docker Engine nao inicializou no tempo esperado" -ForegroundColor Red
    Write-Host ""
    Write-Host "   Possíveis causas:" -ForegroundColor Yellow
    Write-Host "   - Docker Desktop ainda esta inicializando (aguarde mais)" -ForegroundColor White
    Write-Host "   - WSL 2 nao esta configurado corretamente" -ForegroundColor White
    Write-Host "   - Virtualizacao nao esta habilitada na BIOS" -ForegroundColor White
    Write-Host ""
    Write-Host "   Soluções:" -ForegroundColor Yellow
    Write-Host "   1. Aguarde mais 1-2 minutos e execute este script novamente" -ForegroundColor White
    Write-Host "   2. Reinicie o Docker Desktop (icone -> Restart)" -ForegroundColor White
    Write-Host "   3. Verifique se WSL 2 está instalado: wsl --status" -ForegroundColor White
    Write-Host ""
    exit 1
}

# Testar conexão Docker
Write-Host "[4/4] Testando conexão Docker..." -ForegroundColor Yellow

try {
    $version = docker version --format "{{.Server.Version}}" 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "   Docker Engine versao: $version" -ForegroundColor Green
    } else {
        throw "Erro ao obter versão"
    }
} catch {
    Write-Host "   AVISO: Docker Engine respondeu mas com erro" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "========================================================" -ForegroundColor Green
Write-Host "DOCKER PRONTO PARA USO" -ForegroundColor Green
Write-Host "========================================================" -ForegroundColor Green
Write-Host ""
Write-Host "Status:" -ForegroundColor Cyan
Write-Host "   Docker Desktop: Rodando" -ForegroundColor White
Write-Host "   Docker Engine: Pronto" -ForegroundColor White
Write-Host ""
Write-Host "Proximo passo:" -ForegroundColor Cyan
Write-Host "   Execute: .\scripts\start-all-dev.ps1" -ForegroundColor White
Write-Host ""
