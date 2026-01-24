# Script simples para conectar ao MongoDB Railway via mongosh
# Abre uma sessao interativa

Write-Host ""
Write-Host "Conectando ao MongoDB Railway..." -ForegroundColor Cyan
Write-Host ""

# Verificar se mongosh esta instalado
if (-not (Get-Command mongosh -ErrorAction SilentlyContinue)) {
    Write-Host "ERRO: mongosh nao encontrado!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Instale: https://www.mongodb.com/try/download/shell" -ForegroundColor Yellow
    exit 1
}

$mongoUri = "mongodb://mongo:vrzaNIBSuwNrVIMLLvKfmuiJwBFvglAG@hopper.proxy.rlwy.net:40930/item_control_db"

Write-Host "URI de Conexao configurada" -ForegroundColor Green
Write-Host "Abrindo MongoDB Shell..." -ForegroundColor Yellow
Write-Host ""
Write-Host "Comandos uteis:" -ForegroundColor Cyan
Write-Host "  show collections          - Listar colecoes" -ForegroundColor Gray
Write-Host "  db.items.find()           - Ver todos os items" -ForegroundColor Gray
Write-Host "  db.items.countDocuments() - Contar items" -ForegroundColor Gray
Write-Host "  db.events.find().limit(5) - Ver 5 eventos" -ForegroundColor Gray
Write-Host "  exit                      - Sair" -ForegroundColor Gray
Write-Host ""

# Conectar
mongosh $mongoUri
