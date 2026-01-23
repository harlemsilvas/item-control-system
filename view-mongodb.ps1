# 🗄️ Script para visualizar coleções MongoDB
# Item Control System

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "🗄️  MONGODB - VISUALIZAR COLEÇÕES" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Verificar se MongoDB está rodando
Write-Host "🔍 Verificando MongoDB..." -ForegroundColor Yellow
$mongoContainer = docker ps --filter "name=item-control-mongodb" --format "{{.Names}}"

if ($mongoContainer) {
    Write-Host "✅ MongoDB está rodando!" -ForegroundColor Green
    Write-Host ""
} else {
    Write-Host "❌ MongoDB não está rodando!" -ForegroundColor Red
    Write-Host "Execute: docker compose up -d" -ForegroundColor Yellow
    exit 1
}

# Menu de opções
Write-Host "📋 Escolha uma opção:" -ForegroundColor Cyan
Write-Host ""
Write-Host "1. Listar todas as coleções" -ForegroundColor Yellow
Write-Host "2. Ver documentos de ITEMS" -ForegroundColor Yellow
Write-Host "3. Ver documentos de EVENTS" -ForegroundColor Yellow
Write-Host "4. Contar documentos em cada coleção" -ForegroundColor Yellow
Write-Host "5. Ver últimos 5 items criados" -ForegroundColor Yellow
Write-Host "6. Ver últimos 5 eventos registrados" -ForegroundColor Yellow
Write-Host "7. Abrir shell interativo do MongoDB" -ForegroundColor Yellow
Write-Host ""

$opcao = Read-Host "Digite o número da opção"

switch ($opcao) {
    "1" {
        Write-Host ""
        Write-Host "📚 Listando todas as coleções..." -ForegroundColor Green
        docker exec -it item-control-mongodb mongosh --quiet --eval "use item_control_db_dev; db.getCollectionNames()"
    }

    "2" {
        Write-Host ""
        Write-Host "📦 Documentos da coleção ITEMS:" -ForegroundColor Green
        docker exec -it item-control-mongodb mongosh --quiet --eval "use item_control_db_dev; db.items.find().pretty()"
    }

    "3" {
        Write-Host ""
        Write-Host "📅 Documentos da coleção EVENTS:" -ForegroundColor Green
        docker exec -it item-control-mongodb mongosh --quiet --eval "use item_control_db_dev; db.events.find().pretty()"
    }

    "4" {
        Write-Host ""
        Write-Host "📊 Contagem de documentos:" -ForegroundColor Green
        Write-Host ""
        Write-Host "Items:" -ForegroundColor Yellow
        docker exec -it item-control-mongodb mongosh --quiet --eval "use item_control_db_dev; db.items.countDocuments()"
        Write-Host ""
        Write-Host "Events:" -ForegroundColor Yellow
        docker exec -it item-control-mongodb mongosh --quiet --eval "use item_control_db_dev; db.events.countDocuments()"
    }

    "5" {
        Write-Host ""
        Write-Host "📦 Últimos 5 items criados:" -ForegroundColor Green
        docker exec -it item-control-mongodb mongosh --quiet --eval "use item_control_db_dev; db.items.find().sort({createdAt: -1}).limit(5).pretty()"
    }

    "6" {
        Write-Host ""
        Write-Host "📅 Últimos 5 eventos registrados:" -ForegroundColor Green
        docker exec -it item-control-mongodb mongosh --quiet --eval "use item_control_db_dev; db.events.find().sort({eventDate: -1}).limit(5).pretty()"
    }

    "7" {
        Write-Host ""
        Write-Host "🐚 Abrindo shell interativo do MongoDB..." -ForegroundColor Green
        Write-Host ""
        Write-Host "Comandos úteis:" -ForegroundColor Yellow
        Write-Host "  use item_control_db_dev        - Selecionar database" -ForegroundColor Gray
        Write-Host "  db.getCollectionNames()        - Listar coleções" -ForegroundColor Gray
        Write-Host "  db.items.find().pretty()       - Ver items" -ForegroundColor Gray
        Write-Host "  db.events.find().pretty()      - Ver eventos" -ForegroundColor Gray
        Write-Host "  db.items.countDocuments()      - Contar items" -ForegroundColor Gray
        Write-Host "  exit                           - Sair" -ForegroundColor Gray
        Write-Host ""
        docker exec -it item-control-mongodb mongosh
    }

    default {
        Write-Host ""
        Write-Host "❌ Opção inválida!" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "✅ Concluído!" -ForegroundColor Green
Write-Host ""
