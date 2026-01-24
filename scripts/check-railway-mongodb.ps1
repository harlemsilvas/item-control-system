# Script para verificar MongoDB Railway
# Conecta ao Railway e lista as colecoes e documentos

Write-Host ""
Write-Host "============================================================" -ForegroundColor Cyan
Write-Host " VERIFICACAO MONGODB RAILWAY" -ForegroundColor Cyan
Write-Host "============================================================" -ForegroundColor Cyan
Write-Host ""

# Verificar se mongosh esta instalado
$mongoshPath = Get-Command mongosh -ErrorAction SilentlyContinue

if (-not $mongoshPath) {
    Write-Host "ERRO: mongosh nao encontrado!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Instale o MongoDB Shell:" -ForegroundColor Yellow
    Write-Host "  https://www.mongodb.com/try/download/shell" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "OU use MongoDB Compass (GUI):" -ForegroundColor Yellow
    Write-Host "  https://www.mongodb.com/try/download/compass" -ForegroundColor Cyan
    Write-Host ""
    exit 1
}

Write-Host "MongoDB Shell encontrado!" -ForegroundColor Green
Write-Host ""

# Credenciais Railway
$mongoUri = "mongodb://mongo:vrzaNIBSuwNrVIMLLvKfmuiJwBFvglAG@hopper.proxy.rlwy.net:40930/item_control_db"

Write-Host "Conectando ao Railway..." -ForegroundColor Yellow
Write-Host "  Host: hopper.proxy.rlwy.net:40930" -ForegroundColor Gray
Write-Host "  Database: item_control_db" -ForegroundColor Gray
Write-Host ""

# Criar script JavaScript para executar no mongosh
$scriptContent = @"
// Listar todas as colecoes
print('\n========================================');
print('COLECOES NO BANCO item_control_db');
print('========================================\n');

const collections = db.getCollectionNames();

if (collections.length === 0) {
    print('Nenhuma colecao encontrada.');
} else {
    collections.forEach(function(collName) {
        const count = db.getCollection(collName).countDocuments();
        print(collName + ': ' + count + ' documento(s)');
    });
}

print('\n========================================');
print('DETALHES DAS COLECOES');
print('========================================\n');

// Mostrar amostras de cada colecao
collections.forEach(function(collName) {
    print('\n--- Colecao: ' + collName + ' ---');
    const count = db.getCollection(collName).countDocuments();

    if (count > 0) {
        print('Total de documentos: ' + count);
        print('Amostra (primeiros 3):');
        db.getCollection(collName).find().limit(3).forEach(printjson);
    } else {
        print('Colecao vazia');
    }
});

print('\n========================================');
print('VERIFICACAO CONCLUIDA');
print('========================================\n');
"@

# Salvar script temporario
$tempScript = "$env:TEMP\mongodb-check.js"
Set-Content -Path $tempScript -Value $scriptContent

try {
    # Executar mongosh
    mongosh $mongoUri --quiet --file $tempScript

    Write-Host ""
    Write-Host "============================================================" -ForegroundColor Green
    Write-Host " CONEXAO BEM-SUCEDIDA!" -ForegroundColor Green
    Write-Host "============================================================" -ForegroundColor Green

} catch {
    Write-Host ""
    Write-Host "ERRO ao conectar ao MongoDB Railway:" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    Write-Host ""
    Write-Host "Verifique:" -ForegroundColor Yellow
    Write-Host "  1. Credenciais corretas" -ForegroundColor Gray
    Write-Host "  2. Conectividade de rede" -ForegroundColor Gray
    Write-Host "  3. Firewall/Proxy" -ForegroundColor Gray

} finally {
    # Limpar arquivo temporario
    Remove-Item -Path $tempScript -ErrorAction SilentlyContinue
}

Write-Host ""
Write-Host "Pressione ENTER para sair..."
Read-Host
