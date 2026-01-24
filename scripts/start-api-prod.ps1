# ====================================================================
# Script para iniciar a API em modo PRODUÇÃO (Railway MongoDB)
# ====================================================================

Write-Host "🚀 INICIANDO API EM MODO PRODUÇÃO (Railway)" -ForegroundColor Cyan
Write-Host "=============================================" -ForegroundColor Cyan
Write-Host ""

# Verificar e encerrar processos na porta 8080
Write-Host "🔍 Verificando processos na porta 8080..." -ForegroundColor Yellow
$port8080 = Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue

if ($port8080) {
    Write-Host "⚠️  Porta 8080 em uso! Encerrando processos..." -ForegroundColor Yellow

    foreach ($connection in $port8080) {
        $processId = $connection.OwningProcess
        $process = Get-Process -Id $processId -ErrorAction SilentlyContinue

        if ($process) {
            Write-Host "   ❌ Encerrando: $($process.ProcessName) (PID: $processId)" -ForegroundColor Red
            Stop-Process -Id $processId -Force -ErrorAction SilentlyContinue
            Start-Sleep -Seconds 2
        }
    }

    Write-Host "✅ Porta 8080 liberada!" -ForegroundColor Green
    Write-Host ""
} else {
    Write-Host "✅ Porta 8080 disponível!" -ForegroundColor Green
    Write-Host ""
}

$apiPath = "$PSScriptRoot\..\modules\api"
$jarFile = "$apiPath\target\item-control-api-0.1.0-SNAPSHOT.jar"

# Verificar se o JAR existe
if (-not (Test-Path $jarFile)) {
    Write-Host "❌ JAR não encontrado!" -ForegroundColor Red
    Write-Host "   Execute: mvn clean install -DskipTests" -ForegroundColor Yellow
    exit 1
}

Write-Host "📦 JAR encontrado: item-control-api-0.1.0-SNAPSHOT.jar" -ForegroundColor Green
Write-Host "🔗 MongoDB: Railway (hopper.proxy.rlwy.net:40930)" -ForegroundColor Green
Write-Host "🌐 API: http://localhost:8080" -ForegroundColor Green
Write-Host ""
Write-Host "⚙️  Iniciando aplicação..." -ForegroundColor Yellow
Write-Host ""

# Iniciar a aplicação
Set-Location $apiPath
java -jar target\item-control-api-0.1.0-SNAPSHOT.jar --spring.profiles.active=prod
