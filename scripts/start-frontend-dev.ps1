# Item Control System - Start Frontend (DEV LOCAL)
# Inicia o frontend React + Vite localmente (fora do Docker)
# Requer: Backend API rodando (use start-backend-dev.ps1)

Write-Host "========================================================" -ForegroundColor Cyan
Write-Host "START FRONTEND - DEV LOCAL" -ForegroundColor Cyan
Write-Host "========================================================" -ForegroundColor Cyan
Write-Host ""

# Verificar se Backend está rodando
Write-Host "[1/5] Verificando Backend API..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/actuator/health" -UseBasicParsing -TimeoutSec 3 -ErrorAction Stop
    if ($response.StatusCode -eq 200) {
        Write-Host "   Backend API esta rodando (localhost:8080)" -ForegroundColor Green
    }
} catch {
    Write-Host "   AVISO: Backend API nao esta respondendo!" -ForegroundColor Yellow
    Write-Host ""
    $continue = Read-Host "   Deseja continuar mesmo assim? (S/N)"

    if ($continue -ne "S" -and $continue -ne "s") {
        Write-Host "   Execute: .\scripts\start-backend-dev.ps1" -ForegroundColor Yellow
        exit 1
    }
}
Write-Host ""

# Verificar Node.js
Write-Host "[2/5] Verificando Node.js..." -ForegroundColor Yellow
$nodeVersion = node --version 2>$null
if ($nodeVersion) {
    Write-Host "   Node.js instalado: $nodeVersion" -ForegroundColor Green
} else {
    Write-Host "   ❌ Node.js não encontrado!" -ForegroundColor Red
    exit 1
}
Write-Host ""

# Navegar para pasta do frontend
# OBS: o frontend fica dentro do repo em item-control-system\item-control-frontend
$repoRoot = (Resolve-Path (Join-Path $PSScriptRoot ".."))
$frontendPath = Join-Path $repoRoot "item-control-frontend"

if (-not (Test-Path $frontendPath)) {
    Write-Host "   ❌ Pasta do frontend não encontrada!" -ForegroundColor Red
    Write-Host "   Esperado: $frontendPath" -ForegroundColor Yellow
    exit 1
}

Set-Location $frontendPath

# Verificar se node_modules existe
Write-Host "[3/5] Verificando dependências..." -ForegroundColor Yellow
if (-not (Test-Path "node_modules")) {
    Write-Host "   ⚠️  node_modules não encontrado" -ForegroundColor Yellow
    Write-Host "   Instalando dependências..." -ForegroundColor Gray
    npm install

    if ($LASTEXITCODE -ne 0) {
        Write-Host "   ❌ Erro ao instalar dependências!" -ForegroundColor Red
        exit 1
    }
    Write-Host "   ✅ Dependências instaladas" -ForegroundColor Green
} else {
    Write-Host "   ✅ Dependências já instaladas" -ForegroundColor Green
}
Write-Host ""

# Verificar .env
Write-Host "[4/5] Configurando ambiente..." -ForegroundColor Yellow
if (-not (Test-Path ".env.development")) {
    Write-Host "   Criando .env.development..." -ForegroundColor Gray
    @"
VITE_API_URL=http://localhost:8080
"@ | Out-File -FilePath ".env.development" -Encoding UTF8
    Write-Host "   ✅ .env.development criado" -ForegroundColor Green
} else {
    Write-Host "   ✅ .env.development já existe" -ForegroundColor Green
}
Write-Host ""

# Iniciar Vite Dev Server
Write-Host "[5/5] Iniciando Vite Dev Server..." -ForegroundColor Yellow
Write-Host "   (Isso pode demorar ~10 segundos)" -ForegroundColor Gray
Write-Host ""
Write-Host "========================================================" -ForegroundColor Green
Write-Host "INICIANDO FRONTEND..." -ForegroundColor Green
Write-Host "========================================================" -ForegroundColor Green
Write-Host ""
Write-Host "Frontend: http://localhost:5173" -ForegroundColor Cyan
Write-Host "Backend:  http://localhost:8080" -ForegroundColor Cyan
Write-Host ""
Write-Host "Para parar: Ctrl + C" -ForegroundColor Yellow
Write-Host ""
Write-Host "========================================================" -ForegroundColor Cyan
Write-Host ""

# Executar npm run dev
npm run dev
