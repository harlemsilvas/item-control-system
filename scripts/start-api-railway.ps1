# ========================================
# Script: Iniciar API com Railway MongoDB
# ========================================
Write-Host "`n╔════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║     INICIANDO API - RAILWAY MONGODB        ║" -ForegroundColor Cyan
Write-Host "╚════════════════════════════════════════════╝`n" -ForegroundColor Cyan
# Verificar se variável de ambiente está configurada
if (-not $env:MONGODB_URI) {
    Write-Host "⚠️  ATENÇÃO: Variável MONGODB_URI não configurada!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Configure agora:" -ForegroundColor Yellow
    Write-Host "  `$env:MONGODB_URI = 'mongodb://mongo:SENHA@containers-us-west-XXX.railway.app:PORTA/item_control_db'" -ForegroundColor Gray
    Write-Host ""
    Write-Host "Ou execute primeiro:" -ForegroundColor Yellow
    Write-Host "  .\config-railway.ps1" -ForegroundColor Cyan
    Write-Host ""
    exit 1
}
Write-Host "✅ MongoDB URI configurado!" -ForegroundColor Green
Write-Host "   Host: Railway.com" -ForegroundColor Gray
Write-Host ""
Write-Host "🚀 Iniciando API na porta 8080..." -ForegroundColor Yellow
Write-Host ""
Set-Location ..\modules\api
mvn spring-boot:run "-Dspring-boot.run.profiles=prod"
