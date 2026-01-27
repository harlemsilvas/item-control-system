param(
    [switch]$NoPrompt,
    [switch]$NoPause
)

# ============================================
# Script para Executar Testes
# ============================================

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  EXECUTANDO TESTES AUTOMATIZADOS" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$ErrorActionPreference = "Stop"

# Mudar para diretório do projeto
Set-Location "C:\Users\harle\Desktop\java-estudos\Projeto-Harlem\item-control-system"

Write-Host "[1/3] Executando testes unitários (Core)..." -ForegroundColor Yellow
mvn test -pl modules/core -q

if ($LASTEXITCODE -eq 0) {
    Write-Host "  ✅ Testes Core: SUCESSO" -ForegroundColor Green
} else {
    Write-Host "  ❌ Testes Core: FALHOU" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "[2/3] Executando testes do módulo API..." -ForegroundColor Yellow

# Integração (Mongo real / Testcontainers) está desabilitada por padrão.
# Para rodar os testes de integração, defina a env var:
#   $env:RUN_INTEGRATION_TESTS='true'
# e garanta Mongo acessível.
if ($env:RUN_INTEGRATION_TESTS -eq "true") {
    Write-Host "  RUN_INTEGRATION_TESTS=true -> rodando suíte completa da API" -ForegroundColor Gray
} else {
    Write-Host "  RUN_INTEGRATION_TESTS != true -> testes de integração da API ficam desabilitados" -ForegroundColor Gray
}

mvn test -pl modules/api -am -q

if ($LASTEXITCODE -eq 0) {
    Write-Host "   Testes API: SUCESSO" -ForegroundColor Green
} else {
    Write-Host "   Testes API: FALHOU" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "[3/3] Gerando relatório de cobertura (JaCoCo)..." -ForegroundColor Yellow

# Gera relatório agregado (e também por módulo, conforme configuração do pom pai)
mvn -q test jacoco:report

if ($LASTEXITCODE -eq 0) {
    Write-Host "   Relatório gerado" -ForegroundColor Green
} else {
    Write-Host "   Erro ao gerar relatório" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  RELATÓRIOS DE COBERTURA" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host " Core:  modules\core\target\site\jacoco\index.html" -ForegroundColor White
Write-Host " API:   modules\api\target\site\jacoco\index.html" -ForegroundColor White
Write-Host ""

$openReports = "N"
if (-not $NoPrompt) {
    $openReports = Read-Host "Abrir relatórios no navegador? (S/N)"
}

if ($openReports -eq "S" -or $openReports -eq "s") {
    if (Test-Path "modules\core\target\site\jacoco\index.html") {
        Start-Process "modules\core\target\site\jacoco\index.html"
    }
    if (Test-Path "modules\api\target\site\jacoco\index.html") {
        Start-Process "modules\api\target\site\jacoco\index.html"
    }
}

Write-Host ""
Write-Host "Pressione ENTER para sair..." -ForegroundColor Gray
if (-not $NoPause) {
    Read-Host
}
