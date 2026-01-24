# Script para iniciar a API
Write-Host "🚀 Iniciando Item Control System API..." -ForegroundColor Cyan
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

$apiJar = "C:\Users\harle\Desktop\java-estudos\Projeto-Harlem\item-control-system\modules\api\target\item-control-api-0.1.0-SNAPSHOT.jar"

if (-Not (Test-Path $apiJar)) {
    Write-Host "❌ JAR não encontrado: $apiJar" -ForegroundColor Red
    Write-Host "Execute: mvn clean package -DskipTests" -ForegroundColor Yellow
    exit 1
}

Write-Host "✅ JAR encontrado" -ForegroundColor Green
Write-Host "📦 Arquivo: $apiJar" -ForegroundColor Gray
Write-Host ""
Write-Host "🔧 Iniciando na porta 8082 (perfil: dev)..." -ForegroundColor Yellow
Write-Host ""

java -jar $apiJar --spring.profiles.active=dev
