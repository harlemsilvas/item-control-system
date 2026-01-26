# Item Control System - STOP ALL DEV
# Para TODO o ambiente de desenvolvimento:
# 1. Backend API (mata processo Java)
# 2. Frontend (mata processo Node)
# 3. MongoDB Docker (para container)

Write-Host "========================================================" -ForegroundColor Red
Write-Host "STOP DEV LOCAL - Item Control System" -ForegroundColor Red
Write-Host "========================================================" -ForegroundColor Red
Write-Host ""

# ========================================
# ETAPA 1: Parar Backend (Porta 8080)
# ========================================
Write-Host "[1/3] Parando Backend API (porta 8080)..." -ForegroundColor Yellow

$port8080 = Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue
if ($port8080) {
    $process = Get-Process -Id $port8080.OwningProcess -ErrorAction SilentlyContinue
    if ($process) {
        Write-Host "   Processo encontrado: $($process.ProcessName) (PID: $($process.Id))" -ForegroundColor Gray
        Stop-Process -Id $process.Id -Force
        Write-Host "   Backend parado" -ForegroundColor Green
    }
} else {
    Write-Host "   Backend nao esta rodando" -ForegroundColor Gray
}
Write-Host ""

# ========================================
# ETAPA 2: Parar Frontend (Porta 5173)
# ========================================
Write-Host "[2/3] Parando Frontend (porta 5173)..." -ForegroundColor Yellow

$port5173 = Get-NetTCPConnection -LocalPort 5173 -ErrorAction SilentlyContinue
if ($port5173) {
    $process = Get-Process -Id $port5173.OwningProcess -ErrorAction SilentlyContinue
    if ($process) {
        Write-Host "   Processo encontrado: $($process.ProcessName) (PID: $($process.Id))" -ForegroundColor Gray
        Stop-Process -Id $process.Id -Force
        Write-Host "   Frontend parado" -ForegroundColor Green
    }
} else {
    Write-Host "   Frontend nao esta rodando" -ForegroundColor Gray
}
Write-Host ""

# ========================================
# ETAPA 3: Parar MongoDB Docker
# ========================================
Write-Host "[3/3] Parando MongoDB Docker..." -ForegroundColor Yellow

$dockerRunning = docker ps --filter "name=item-control-mongo-dev" --filter "status=running" --format "{{.Names}}" 2>$null

if ($dockerRunning) {
    docker-compose -f docker-compose.mongodb.yml down
    Write-Host "   MongoDB Docker parado" -ForegroundColor Green
} else {
    Write-Host "   MongoDB Docker nao esta rodando" -ForegroundColor Gray
}
Write-Host ""

# ========================================
# RESUMO FINAL
# ========================================
Write-Host "========================================================" -ForegroundColor Green
Write-Host "AMBIENTE PARADO COM SUCESSO" -ForegroundColor Green
Write-Host "========================================================" -ForegroundColor Green
Write-Host ""
Write-Host "Status:" -ForegroundColor Cyan
Write-Host "   Backend (8080): PARADO" -ForegroundColor Gray
Write-Host "   Frontend (5173): PARADO" -ForegroundColor Gray
Write-Host "   MongoDB Docker: PARADO" -ForegroundColor Gray
Write-Host ""
Write-Host "Para iniciar novamente: .\scripts\start-all-dev.ps1" -ForegroundColor Yellow
Write-Host ""
