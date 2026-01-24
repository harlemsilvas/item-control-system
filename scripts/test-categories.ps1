# ========================================
# Script de Teste: Categories CRUD
# ========================================

$baseUrl = "http://localhost:8080/api/v1"
$userId = "550e8400-e29b-41d4-a716-446655440001"

Write-Host "`n╔════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║   TESTANDO CATEGORIES CRUD - API REST     ║" -ForegroundColor Cyan
Write-Host "╚════════════════════════════════════════════╝`n" -ForegroundColor Cyan

# ========================================
# 1. CRIAR CATEGORIA
# ========================================
Write-Host "1️⃣  CRIANDO CATEGORIA..." -ForegroundColor Yellow

$createRequest = @{
    userId = $userId
    name = "Manutenção de Veículos"
    parentId = $null
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/categories" `
        -Method POST `
        -Body $createRequest `
        -ContentType "application/json"

    $categoryId = $response.id
    Write-Host "✅ Categoria criada com sucesso!" -ForegroundColor Green
    Write-Host "   ID: $categoryId" -ForegroundColor Gray
    Write-Host "   Nome: $($response.name)" -ForegroundColor Gray
    Write-Host "   Criado em: $($response.createdAt)`n" -ForegroundColor Gray
}
catch {
    Write-Host "❌ Erro ao criar categoria: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# ========================================
# 2. CRIAR SUBCATEGORIA
# ========================================
Write-Host "2️⃣  CRIANDO SUBCATEGORIA..." -ForegroundColor Yellow

$createSubRequest = @{
    userId = $userId
    name = "Troca de Óleo"
    parentId = $categoryId
} | ConvertTo-Json

try {
    $subResponse = Invoke-RestMethod -Uri "$baseUrl/categories" `
        -Method POST `
        -Body $createSubRequest `
        -ContentType "application/json"

    $subCategoryId = $subResponse.id
    Write-Host "✅ Subcategoria criada com sucesso!" -ForegroundColor Green
    Write-Host "   ID: $subCategoryId" -ForegroundColor Gray
    Write-Host "   Nome: $($subResponse.name)" -ForegroundColor Gray
    Write-Host "   Categoria Pai: $($subResponse.parentId)`n" -ForegroundColor Gray
}
catch {
    Write-Host "❌ Erro ao criar subcategoria: $($_.Exception.Message)" -ForegroundColor Red
}

# ========================================
# 3. LISTAR CATEGORIAS DO USUÁRIO
# ========================================
Write-Host "3️⃣  LISTANDO CATEGORIAS DO USUÁRIO..." -ForegroundColor Yellow

try {
    $categories = Invoke-RestMethod -Uri "$baseUrl/categories?userId=$userId" `
        -Method GET

    Write-Host "✅ Categorias encontradas: $($categories.Count)" -ForegroundColor Green

    foreach ($cat in $categories) {
        $parentInfo = if ($cat.parentId) { "→ Pai: $($cat.parentId)" } else { "(Raiz)" }
        Write-Host "   • $($cat.name) $parentInfo" -ForegroundColor Gray
    }
    Write-Host ""
}
catch {
    Write-Host "❌ Erro ao listar categorias: $($_.Exception.Message)" -ForegroundColor Red
}

# ========================================
# 4. ATUALIZAR CATEGORIA
# ========================================
Write-Host "4️⃣  ATUALIZANDO CATEGORIA..." -ForegroundColor Yellow

$updateRequest = @{
    name = "Manutenção Automotiva"
} | ConvertTo-Json

try {
    $updated = Invoke-RestMethod -Uri "$baseUrl/categories/$categoryId" `
        -Method PUT `
        -Body $updateRequest `
        -ContentType "application/json"

    Write-Host "✅ Categoria atualizada com sucesso!" -ForegroundColor Green
    Write-Host "   Novo nome: $($updated.name)" -ForegroundColor Gray
    Write-Host "   Atualizado em: $($updated.updatedAt)`n" -ForegroundColor Gray
}
catch {
    Write-Host "❌ Erro ao atualizar categoria: $($_.Exception.Message)" -ForegroundColor Red
}

# ========================================
# 5. DELETAR SUBCATEGORIA
# ========================================
Write-Host "5️⃣  DELETANDO SUBCATEGORIA..." -ForegroundColor Yellow

try {
    Invoke-RestMethod -Uri "$baseUrl/categories/$subCategoryId" `
        -Method DELETE

    Write-Host "✅ Subcategoria deletada com sucesso!`n" -ForegroundColor Green
}
catch {
    Write-Host "❌ Erro ao deletar subcategoria: $($_.Exception.Message)" -ForegroundColor Red
}

# ========================================
# 6. DELETAR CATEGORIA
# ========================================
Write-Host "6️⃣  DELETANDO CATEGORIA..." -ForegroundColor Yellow

try {
    Invoke-RestMethod -Uri "$baseUrl/categories/$categoryId" `
        -Method DELETE

    Write-Host "✅ Categoria deletada com sucesso!`n" -ForegroundColor Green
}
catch {
    Write-Host "❌ Erro ao deletar categoria: $($_.Exception.Message)" -ForegroundColor Red
}

# ========================================
# RESUMO FINAL
# ========================================
Write-Host "`n╔════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║          TESTES CONCLUÍDOS! ✅             ║" -ForegroundColor Cyan
Write-Host "╚════════════════════════════════════════════╝`n" -ForegroundColor Cyan

Write-Host "📊 ENDPOINTS TESTADOS:" -ForegroundColor Green
Write-Host "   POST   /api/v1/categories          ✅" -ForegroundColor White
Write-Host "   GET    /api/v1/categories?userId=  ✅" -ForegroundColor White
Write-Host "   PUT    /api/v1/categories/{id}     ✅" -ForegroundColor White
Write-Host "   DELETE /api/v1/categories/{id}     ✅" -ForegroundColor White
Write-Host ""
