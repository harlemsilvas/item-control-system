# Script de inicializacao rapida - API em modo DESENVOLVIMENTO
Write-Host ""
Write-Host "ITEM CONTROL SYSTEM - INICIALIZACAO RAPIDA (DESENVOLVIMENTO)" -ForegroundColor Cyan
Write-Host ""
Write-Host "[1/3] Verificando porta 8080..." -ForegroundColor Yellow
$port8080 = Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue
if ($port8080) {
    foreach ($connection in $port8080) {
        Stop-Process -Id $connection.OwningProcess -Force -ErrorAction SilentlyContinue
    }
    Start-Sleep -Seconds 2
    Write-Host "   Porta 8080 liberada!" -ForegroundColor Green
} else {
    Write-Host "   Porta 8080 disponivel!" -ForegroundColor Green
}
Write-Host "[2/3] Verificando JAR..." -ForegroundColor Yellow
$apiJar = "$PSScriptRoot\..\modules\api\target\item-control-api-0.1.0-SNAPSHOT.jar"
if (-Not (Test-Path $apiJar)) {
    Write-Host "   JAR nao encontrado! Execute: mvn clean install -DskipTests" -ForegroundColor Red
    exit 1
}
Write-Host "   JAR encontrado!" -ForegroundColor Green
Write-Host "[3/3] Iniciando aplicacao..." -ForegroundColor Yellow
Write-Host "   Modo: DESENVOLVIMENTO | MongoDB: Docker Local | Porta: 8080" -ForegroundColor White
Set-Location "$PSScriptRoot\..\modules\api"
java -jar target\item-control-api-0.1.0-SNAPSHOT.jar --spring.profiles.active=dev
