# Script de teste rapido - Railway MongoDB
# Conecta e cria primeiro documento para gerar collections

Write-Host ""
Write-Host "TESTE RAPIDO - RAILWAY MONGODB" -ForegroundColor Cyan
Write-Host "===============================" -ForegroundColor Cyan
Write-Host ""

Write-Host "[1/4] Encerrando processos na porta 8080..." -ForegroundColor Yellow
Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue | ForEach-Object {
    Stop-Process -Id $_.OwningProcess -Force -ErrorAction SilentlyContinue
}
Start-Sleep -Seconds 2
Write-Host "   OK" -ForegroundColor Green
Write-Host ""

Write-Host "[2/4] Recompilando projeto..." -ForegroundColor Yellow
$compileOutput = mvn clean package -DskipTests 2>&1
if ($LASTEXITCODE -eq 0) {
    Write-Host "   Compilacao OK" -ForegroundColor Green
} else {
    Write-Host "   ERRO na compilacao!" -ForegroundColor Red
    Write-Host "   Execute manualmente: mvn clean package -DskipTests" -ForegroundColor Yellow
    exit 1
}
Write-Host ""

Write-Host "[3/4] Iniciando API em modo producao..." -ForegroundColor Yellow
Write-Host "   Aguarde 15 segundos..." -ForegroundColor Gray

# Iniciar API em background
$job = Start-Job -ScriptBlock {
    Set-Location "C:\Users\harle\Desktop\java-estudos\Projeto-Harlem\item-control-system\modules\api"
    java -jar target\item-control-api-0.1.0-SNAPSHOT.jar --spring.profiles.active=prod
}

Start-Sleep -Seconds 15

Write-Host "   API iniciada (Job ID: $($job.Id))" -ForegroundColor Green
Write-Host ""

Write-Host "[4/4] Criando primeiro item para gerar collections..." -ForegroundColor Yellow

# Gerar UUID para userId
$userId = [System.Guid]::NewGuid().ToString()

$itemData = @{
    userId = $userId
    name = "Item Teste Railway"
    nickname = "item-railway-001"
    templateCode = "GENERAL"
    tags = @("railway", "teste", "primeiro")
    metadata = @{
        ambiente = "railway"
        criado_em = (Get-Date).ToString("yyyy-MM-dd HH:mm:ss")
        teste = $true
    }
} | ConvertTo-Json -Depth 10

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/v1/items" `
        -Method POST `
        -Body $itemData `
        -ContentType "application/json" `
        -ErrorAction Stop

    Write-Host "   Item criado com sucesso!" -ForegroundColor Green
    Write-Host "   ID: $($response.id)" -ForegroundColor Gray
    Write-Host ""
    Write-Host "Collections criadas no Railway:" -ForegroundColor Cyan
    Write-Host "   - items" -ForegroundColor White
    Write-Host ""
    Write-Host "Verifique no MongoDB Compass!" -ForegroundColor Yellow

} catch {
    Write-Host "   ERRO ao criar item: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host ""
    Write-Host "Verifique logs da aplicacao" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "Processo API ainda em execucao (Job ID: $($job.Id))" -ForegroundColor Gray
Write-Host "Para parar: Stop-Job -Id $($job.Id); Remove-Job -Id $($job.Id)" -ForegroundColor Gray
Write-Host ""

Read-Host "Pressione ENTER para encerrar API e sair"

# Encerrar API
Stop-Job -Id $job.Id -ErrorAction SilentlyContinue
Remove-Job -Id $job.Id -ErrorAction SilentlyContinue

Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue | ForEach-Object {
    Stop-Process -Id $_.OwningProcess -Force -ErrorAction SilentlyContinue
}

Write-Host "API encerrada." -ForegroundColor Green
