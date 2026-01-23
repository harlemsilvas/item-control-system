# Script para iniciar a API
Write-Host "🚀 Iniciando Item Control System API..." -ForegroundColor Cyan
Write-Host ""

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
