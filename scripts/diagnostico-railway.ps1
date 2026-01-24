# Script de diagnostico de conexao MongoDB Railway

Write-Host ""
Write-Host "============================================================" -ForegroundColor Cyan
Write-Host " DIAGNOSTICO MONGODB RAILWAY" -ForegroundColor Cyan
Write-Host "============================================================" -ForegroundColor Cyan
Write-Host ""

# Credenciais atuais
$currentUri = "mongodb://mongo:vrzaNIBSuwNrVIMLLvKfmuiJwBFvglAG@hopper.proxy.rlwy.net:40930"
$currentDb = "item_control_db"

Write-Host "CONFIGURACAO ATUAL:" -ForegroundColor Yellow
Write-Host "  URI: $currentUri" -ForegroundColor Gray
Write-Host "  Database: $currentDb" -ForegroundColor Gray
Write-Host ""

Write-Host "IMPORTANTE: Verifique no Railway Dashboard:" -ForegroundColor Yellow
Write-Host ""
Write-Host "1. Acesse: https://railway.app" -ForegroundColor White
Write-Host "2. Selecione seu projeto MongoDB" -ForegroundColor White
Write-Host "3. Va em 'Variables' ou 'Connect'" -ForegroundColor White
Write-Host "4. Copie as variaveis:" -ForegroundColor White
Write-Host ""

Write-Host "Variaveis esperadas:" -ForegroundColor Cyan
Write-Host "  MONGO_URL (URL interna)" -ForegroundColor Gray
Write-Host "  MONGO_PUBLIC_URL (URL publica)" -ForegroundColor Gray
Write-Host "  MONGOUSER" -ForegroundColor Gray
Write-Host "  MONGOPASSWORD" -ForegroundColor Gray
Write-Host "  MONGOHOST" -ForegroundColor Gray
Write-Host "  MONGOPORT" -ForegroundColor Gray
Write-Host ""

Write-Host "============================================================" -ForegroundColor Cyan
Write-Host " OPCOES PARA TESTAR" -ForegroundColor Cyan
Write-Host "============================================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "OPCAO 1: Testar com MongoDB Compass (RECOMENDADO)" -ForegroundColor Green
Write-Host ""
Write-Host "1. Instale MongoDB Compass:" -ForegroundColor White
Write-Host "   https://www.mongodb.com/try/download/compass" -ForegroundColor Cyan
Write-Host ""
Write-Host "2. Use estas connection strings para testar:" -ForegroundColor White
Write-Host ""
Write-Host "   Formato 1 (sem database na URI):" -ForegroundColor Yellow
Write-Host "   mongodb://mongo:vrzaNIBSuwNrVIMLLvKfmuiJwBFvglAG@hopper.proxy.rlwy.net:40930" -ForegroundColor Gray
Write-Host ""
Write-Host "   Formato 2 (com database na URI):" -ForegroundColor Yellow
Write-Host "   mongodb://mongo:vrzaNIBSuwNrVIMLLvKfmuiJwBFvglAG@hopper.proxy.rlwy.net:40930/item_control_db" -ForegroundColor Gray
Write-Host ""
Write-Host "   Formato 3 (com authSource):" -ForegroundColor Yellow
Write-Host "   mongodb://mongo:vrzaNIBSuwNrVIMLLvKfmuiJwBFvglAG@hopper.proxy.rlwy.net:40930/item_control_db?authSource=admin" -ForegroundColor Gray
Write-Host ""

Write-Host "OPCAO 2: Verificar credenciais no Railway" -ForegroundColor Green
Write-Host ""
Write-Host "Execute no Railway Dashboard > Variables:" -ForegroundColor White
Write-Host "  Copie EXATAMENTE as credenciais mostradas" -ForegroundColor Yellow
Write-Host "  Verifique se usuario e senha estao corretos" -ForegroundColor Yellow
Write-Host ""

Write-Host "OPCAO 3: Criar novo banco de dados" -ForegroundColor Green
Write-Host ""
Write-Host "Se o banco 'item_control_db' nao existe:" -ForegroundColor White
Write-Host "  1. Conecte ao Railway sem especificar database" -ForegroundColor Yellow
Write-Host "  2. Use mongosh ou Compass para criar o banco" -ForegroundColor Yellow
Write-Host "  3. Comando: use item_control_db" -ForegroundColor Gray
Write-Host ""

Write-Host "============================================================" -ForegroundColor Cyan
Write-Host " POSSIVELS PROBLEMAS" -ForegroundColor Cyan
Write-Host "============================================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "1. Senha ou usuario incorreto" -ForegroundColor Red
Write-Host "   Solucao: Copie do Railway Dashboard" -ForegroundColor Yellow
Write-Host ""

Write-Host "2. Database nao existe" -ForegroundColor Red
Write-Host "   Solucao: Crie o banco primeiro ou conecte sem database" -ForegroundColor Yellow
Write-Host ""

Write-Host "3. authSource incorreto" -ForegroundColor Red
Write-Host "   Solucao: Adicione ?authSource=admin na URI" -ForegroundColor Yellow
Write-Host ""

Write-Host "4. IP bloqueado/Firewall" -ForegroundColor Red
Write-Host "   Solucao: Verifique firewall local" -ForegroundColor Yellow
Write-Host ""

Write-Host "============================================================" -ForegroundColor Green
Write-Host ""

Write-Host "PROXIMO PASSO:" -ForegroundColor Cyan
Write-Host ""
Write-Host "1. Acesse Railway Dashboard e copie MONGO_PUBLIC_URL" -ForegroundColor White
Write-Host "2. Cole a URL completa aqui:" -ForegroundColor White
Write-Host ""

$newUrl = Read-Host "Cole a URL do Railway (ou ENTER para pular)"

if ($newUrl -and $newUrl.Trim() -ne "") {
    Write-Host ""
    Write-Host "URL fornecida:" -ForegroundColor Green
    Write-Host "  $newUrl" -ForegroundColor Gray
    Write-Host ""
    Write-Host "Atualize application-prod.yml com esta URL!" -ForegroundColor Yellow
    Write-Host ""
}

Write-Host "Pressione ENTER para sair..."
Read-Host
