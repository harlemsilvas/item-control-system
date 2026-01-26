# Item Control System - START ALL DEV (Completo)
# Inicia TODO o ambiente de desenvolvimento:
# 1. MongoDB (Docker)
# 2. Backend API (Local)
# 3. Frontend (Local)

Write-Host "========================================================" -ForegroundColor Magenta
Write-Host "START DEV LOCAL - Item Control System" -ForegroundColor Magenta
Write-Host "========================================================" -ForegroundColor Magenta
Write-Host ""
Write-Host "Componentes que serao iniciados:" -ForegroundColor Cyan
Write-Host "   1) MongoDB (Docker)" -ForegroundColor White
Write-Host "   2) Backend API (Local - Spring Boot)" -ForegroundColor White
Write-Host "   3) Frontend (Local - React + Vite)" -ForegroundColor White
Write-Host ""
Read-Host "Pressione ENTER para continuar"
Write-Host ""

# ========================================
# ETAPA 0: Verificar Docker Desktop
# ========================================
Write-Host "========================================================" -ForegroundColor Cyan
Write-Host "ETAPA 0/3: VERIFICANDO DOCKER DESKTOP" -ForegroundColor Cyan
Write-Host "========================================================" -ForegroundColor Cyan
Write-Host ""

# Verificar se Docker Engine está respondendo
Write-Host "Verificando Docker Engine..." -ForegroundColor Yellow
$dockerInfo = docker info 2>&1
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERRO: Docker Engine nao esta respondendo!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Tentando iniciar Docker Desktop..." -ForegroundColor Yellow

    # Executar script de verificação
    & "$PSScriptRoot\check-docker.ps1"

    if ($LASTEXITCODE -ne 0) {
        Write-Host "ERRO: Nao foi possivel iniciar Docker. Abortando..." -ForegroundColor Red
        exit 1
    }
} else {
    Write-Host "OK: Docker Engine esta pronto!" -ForegroundColor Green
}

Write-Host ""
Start-Sleep -Seconds 2

# ========================================
# ETAPA 1: MongoDB Docker
# ========================================
Write-Host "========================================================" -ForegroundColor Cyan
Write-Host "ETAPA 1/3: MONGODB DOCKER" -ForegroundColor Cyan
Write-Host "========================================================" -ForegroundColor Cyan
Write-Host ""

& "$PSScriptRoot\start-mongodb-docker.ps1"

if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "ERRO: Erro ao iniciar MongoDB. Abortando..." -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "OK: MongoDB iniciado com sucesso!" -ForegroundColor Green
Write-Host ""
Start-Sleep -Seconds 3

# ========================================
# ETAPA 2: Backend API
# ========================================
Write-Host "========================================================" -ForegroundColor Cyan
Write-Host "ETAPA 2/3: BACKEND API (SPRING BOOT)" -ForegroundColor Cyan
Write-Host "========================================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "AVISO: O Backend sera iniciado em uma nova janela..." -ForegroundColor Yellow
Write-Host ""

$backendScript = "$PSScriptRoot\start-backend-dev.ps1"
Start-Process powershell -ArgumentList "-NoExit", "-File", $backendScript

Write-Host "OK: Backend iniciado em nova janela!" -ForegroundColor Green
Write-Host "   Aguardando API ficar pronta..." -ForegroundColor Gray
Write-Host ""

# Aguardar backend ficar pronto
$maxAttempts = 60
$attempt = 0
$ready = $false

while ($attempt -lt $maxAttempts -and -not $ready) {
    $attempt++
    Write-Host "   Tentativa $attempt/$maxAttempts..." -ForegroundColor Gray

    try {
        $response = Invoke-WebRequest -Uri "http://localhost:8080/actuator/health" -UseBasicParsing -TimeoutSec 2 -ErrorAction Stop
        if ($response.StatusCode -eq 200) {
            $ready = $true
        }
    } catch {
        Start-Sleep -Seconds 2
    }
}

Write-Host ""
if ($ready) {
    Write-Host "OK: Backend API esta respondendo!" -ForegroundColor Green
} else {
    Write-Host "AVISO: Backend ainda nao respondeu, mas continuando..." -ForegroundColor Yellow
}
Write-Host ""
Start-Sleep -Seconds 2

# ========================================
# ETAPA 3: Frontend
# ========================================
Write-Host "========================================================" -ForegroundColor Cyan
Write-Host "ETAPA 3/3: FRONTEND (REACT + VITE)" -ForegroundColor Cyan
Write-Host "========================================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "AVISO: O Frontend sera iniciado em uma nova janela..." -ForegroundColor Yellow
Write-Host ""

$frontendScript = "$PSScriptRoot\start-frontend-dev.ps1"
Start-Process powershell -ArgumentList "-NoExit", "-File", $frontendScript

Write-Host "OK: Frontend iniciado em nova janela!" -ForegroundColor Green
Write-Host ""
Start-Sleep -Seconds 3

# ========================================
# RESUMO FINAL
# ========================================
Write-Host ""
Write-Host "========================================================" -ForegroundColor Green
Write-Host "AMBIENTE COMPLETO INICIADO" -ForegroundColor Green
Write-Host "========================================================" -ForegroundColor Green
Write-Host ""
Write-Host "URLs:" -ForegroundColor Cyan
Write-Host "   Frontend: http://localhost:5173" -ForegroundColor White
Write-Host "   Backend:  http://localhost:8080" -ForegroundColor White
Write-Host "   Swagger:  http://localhost:8080/swagger-ui.html" -ForegroundColor White
Write-Host "   MongoDB:  mongodb://localhost:27017" -ForegroundColor White
Write-Host ""
Write-Host "Janelas abertas:" -ForegroundColor Cyan
Write-Host "   Backend: nova janela PowerShell (Spring Boot)" -ForegroundColor Gray
Write-Host "   Frontend: nova janela PowerShell (Vite)" -ForegroundColor Gray
Write-Host ""
Write-Host "Para parar tudo:" -ForegroundColor Yellow
Write-Host "   1. Feche as janelas do Backend e Frontend (Ctrl+C)" -ForegroundColor Gray
Write-Host "   2. Execute: docker-compose -f docker-compose.mongodb.yml down" -ForegroundColor Gray
Write-Host "   OU use: .\scripts\stop-all-dev.ps1" -ForegroundColor Gray
Write-Host ""
Write-Host "========================================================" -ForegroundColor Green
Write-Host ""
Write-Host "Pressione ENTER para fechar esta janela..." -ForegroundColor Gray
Read-Host
