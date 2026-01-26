# Item Control System - Start Backend API (DEV LOCAL)
# Inicia a API Spring Boot localmente (fora do Docker)
# Requer: MongoDB Docker rodando (use start-mongodb-docker.ps1)
param(
    [switch]$SkipBuild = $false
)
Write-Host "========================================================" -ForegroundColor Cyan
Write-Host "INICIANDO BACKEND API - DEV LOCAL" -ForegroundColor Cyan
Write-Host "========================================================" -ForegroundColor Cyan
Write-Host ""
# Verificar se MongoDB Docker esta rodando
Write-Host "[1/6] Verificando MongoDB Docker..." -ForegroundColor Yellow
$mongoRunning = docker ps --filter "name=item-control-mongo-dev" --filter "status=running" --format "{{.Names}}" 2>$null
if (-not $mongoRunning) {
    Write-Host "   [ERRO] MongoDB Docker nao esta rodando!" -ForegroundColor Red
    Write-Host ""
    $start = Read-Host "   Deseja iniciar o MongoDB agora? (S/N)"
    if ($start -eq "S" -or $start -eq "s") {
        Write-Host ""
        & "$PSScriptRoot\start-mongodb-docker.ps1"
        Write-Host ""
    } else {
        Write-Host "   Execute: .\scripts\start-mongodb-docker.ps1" -ForegroundColor Yellow
        exit 1
    }
} else {
    Write-Host "   [OK] MongoDB Docker esta rodando" -ForegroundColor Green
}
Write-Host ""
# Verificar porta 8080
Write-Host "[2/6] Verificando porta 8080..." -ForegroundColor Yellow
$portInUse = Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue
if ($portInUse) {
    Write-Host "   [AVISO] Porta 8080 em uso!" -ForegroundColor Yellow
    Write-Host "   Tentando parar processo..." -ForegroundColor Gray
    $process = Get-Process -Id $portInUse.OwningProcess -ErrorAction SilentlyContinue
    if ($process) {
        Write-Host "   Processo encontrado: $($process.ProcessName) (PID: $($process.Id))" -ForegroundColor Gray
        Stop-Process -Id $process.Id -Force
        Start-Sleep -Seconds 2
        Write-Host "   [OK] Processo parado" -ForegroundColor Green
    }
}
Write-Host "   [OK] Porta 8080 disponivel" -ForegroundColor Green
Write-Host ""
# Verificar Java
Write-Host "[3/6] Verificando Java..." -ForegroundColor Yellow
$javaVersion = java -version 2>&1 | Select-String "version"
if ($javaVersion) {
    Write-Host "   [OK] Java instalado: $javaVersion" -ForegroundColor Green
} else {
    Write-Host "   [ERRO] Java nao encontrado!" -ForegroundColor Red
    exit 1
}
Write-Host ""
# Navegar para modulo API
$repoRoot = (Resolve-Path (Join-Path $PSScriptRoot ".."))
$apiPath = Join-Path $repoRoot "modules\api"

if (-not (Test-Path $apiPath)) {
    Write-Host "   [ERRO] Pasta do modulo API nao encontrada: $apiPath" -ForegroundColor Red
    exit 1
}

Push-Location $apiPath
try {
    # Build (opcional)
    if (-not $SkipBuild) {
        Write-Host "[4/6] Compilando projeto (Maven)..." -ForegroundColor Yellow
        Write-Host "   (Use -SkipBuild para pular esta etapa)" -ForegroundColor Gray
        mvn clean package -DskipTests
        if ($LASTEXITCODE -ne 0) {
            Write-Host "   [ERRO] Erro na compilacao!" -ForegroundColor Red
            exit 1
        }
        Write-Host "   [OK] Compilacao concluida" -ForegroundColor Green
    } else {
        Write-Host "[4/6] Build pulado (-SkipBuild)" -ForegroundColor Gray
    }
    Write-Host ""

    # Configurar variaveis de ambiente
    Write-Host "[5/6] Configurando ambiente DEV..." -ForegroundColor Yellow
    $env:SPRING_PROFILES_ACTIVE = "dev"
    $env:SPRING_DATA_MONGODB_URI = "mongodb://admin:admin123@localhost:27017/item_control_db_dev?authSource=admin"
    $env:SPRING_DATA_MONGODB_DATABASE = "item_control_db_dev"
    Write-Host "   [OK] Profile: dev" -ForegroundColor Green
    Write-Host "   [OK] MongoDB: localhost:27017" -ForegroundColor Green
    Write-Host ""

    # Iniciar aplicacao
    Write-Host "[6/6] Iniciando Spring Boot..." -ForegroundColor Yellow
    Write-Host "   (Isso pode demorar ~30 segundos)" -ForegroundColor Gray
    Write-Host ""

    Write-Host "========================================================" -ForegroundColor Green
    Write-Host "INICIANDO APLICACAO..." -ForegroundColor Green
    Write-Host "========================================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "API rodara em: http://localhost:8080" -ForegroundColor Cyan
    Write-Host "Swagger UI: http://localhost:8080/swagger-ui.html" -ForegroundColor Cyan
    Write-Host "Health: http://localhost:8080/actuator/health" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Para parar: Ctrl + C" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "========================================================" -ForegroundColor Cyan
    Write-Host ""

    # Executar JAR
    $jarFile = Get-ChildItem -Path "target" -Filter "item-control-api-*.jar" | Select-Object -First 1
    if ($jarFile) {
        Write-Host "Executando: $($jarFile.Name)" -ForegroundColor Gray
        Write-Host ""
        java -jar "target\$($jarFile.Name)"
    } else {
        Write-Host "[ERRO] JAR nao encontrado em target/" -ForegroundColor Red
        Write-Host "Execute sem -SkipBuild para compilar primeiro" -ForegroundColor Yellow
        exit 1
    }
}
finally {
    Pop-Location
}
