# ========================================
# Script: Configurar Railway MongoDB
# ========================================
Write-Host "`n╔════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║   CONFIGURAÇÃO RAILWAY.COM - MONGODB       ║" -ForegroundColor Cyan
Write-Host "╚════════════════════════════════════════════╝`n" -ForegroundColor Cyan
Write-Host "📝 PASSO A PASSO:" -ForegroundColor Yellow
Write-Host ""
Write-Host "1️⃣  Acesse: https://railway.com/" -ForegroundColor White
Write-Host "2️⃣  Faça login (GitHub/Google/Email)" -ForegroundColor White
Write-Host "3️⃣  Clique em 'New Project'" -ForegroundColor White
Write-Host "4️⃣  Selecione '+ New' → 'Database' → 'MongoDB'" -ForegroundColor White
Write-Host "5️⃣  Aguarde provisionamento (1-2 min)" -ForegroundColor White
Write-Host ""
Write-Host "📋 OBTER STRING DE CONEXÃO:" -ForegroundColor Yellow
Write-Host ""
Write-Host "1️⃣  Clique no serviço MongoDB criado" -ForegroundColor White
Write-Host "2️⃣  Vá na aba 'Variables'" -ForegroundColor White
Write-Host "3️⃣  Copie a variável 'MONGO_URL'" -ForegroundColor White
Write-Host ""
Write-Host "🔧 FORMATO ESPERADO:" -ForegroundColor Yellow
Write-Host "mongodb://mongo:SENHA@containers-us-west-XXX.railway.app:PORTA" -ForegroundColor Gray
Write-Host ""
Write-Host "⚙️  CONFIGURAR NO PROJETO:" -ForegroundColor Yellow
Write-Host ""
Write-Host "Opção 1: Variável de Ambiente (RECOMENDADO)" -ForegroundColor Green
Write-Host "  Windows PowerShell:" -ForegroundColor White
Write-Host "  `$env:MONGODB_URI='sua-url-aqui'" -ForegroundColor Gray
Write-Host ""
Write-Host "Opção 2: Editar application-prod.yml" -ForegroundColor Green
Write-Host "  Arquivo: modules/api/src/main/resources/application-prod.yml" -ForegroundColor White
Write-Host "  Substitua: uri: mongodb://..." -ForegroundColor Gray
Write-Host ""
Write-Host "🚀 INICIAR COM RAILWAY:" -ForegroundColor Yellow
Write-Host ""
Write-Host "API:" -ForegroundColor White
Write-Host "  `$env:MONGODB_URI='sua-url'" -ForegroundColor Gray
Write-Host "  mvn spring-boot:run -Dspring-boot.run.profiles=prod" -ForegroundColor Gray
Write-Host ""
Write-Host "Worker:" -ForegroundColor White
Write-Host "  `$env:MONGODB_URI='sua-url'" -ForegroundColor Gray
Write-Host "  cd modules\worker" -ForegroundColor Gray
Write-Host "  mvn spring-boot:run -Dspring-boot.run.profiles=prod" -ForegroundColor Gray
Write-Host ""
Write-Host "📦 DATABASE E COLLECTIONS:" -ForegroundColor Yellow
Write-Host ""
Write-Host "Database: item_control_db" -ForegroundColor White
Write-Host ""
Write-Host "Collections (criadas automaticamente pelo Spring):" -ForegroundColor White
Write-Host "  • items       - Itens gerenciados" -ForegroundColor Gray
Write-Host "  • events      - Eventos registrados" -ForegroundColor Gray
Write-Host "  • alerts      - Alertas gerados" -ForegroundColor Gray
Write-Host "  • rules       - Regras configuradas" -ForegroundColor Gray
Write-Host "  • categories  - Categorias hierárquicas" -ForegroundColor Gray
Write-Host ""
Write-Host "✅ PRONTO! Após configurar, execute:" -ForegroundColor Green
Write-Host "   .\start-api-railway.ps1" -ForegroundColor Cyan
Write-Host ""
