# Iniciar Worker Module
Write-Host "Iniciando Worker Module..." -ForegroundColor Cyan
Set-Location ..\modules\worker
mvn spring-boot:run
