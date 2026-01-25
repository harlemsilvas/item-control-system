# 🚀 Script de Preparação para Deploy no Render
# Verifica configurações e prepara ambiente

Write-Host "`n=== 🚀 PREPARAÇÃO DEPLOY RENDER ===" -ForegroundColor Cyan
Write-Host ""

# 1. Verificar branch
Write-Host "[1/6] Verificando branch..." -ForegroundColor Yellow
$branch = git rev-parse --abbrev-ref HEAD
if ($branch -ne "deploy/render") {
    Write-Host "   ❌ ERRO: Você está na branch '$branch'" -ForegroundColor Red
    Write-Host "   Switch para branch deploy/render:" -ForegroundColor Yellow
    Write-Host "   git checkout deploy/render`n" -ForegroundColor White
    exit 1
}
Write-Host "   ✅ Branch correta: deploy/render`n" -ForegroundColor Green

# 2. Verificar arquivo .env.render
Write-Host "[2/6] Verificando .env.render..." -ForegroundColor Yellow
if (Test-Path ".env.render") {
    Write-Host "   ✅ Arquivo .env.render existe" -ForegroundColor Green

    # Ler e validar MONGODB_URI
    $envContent = Get-Content ".env.render" -Raw
    if ($envContent -match "MONGODB_URI=mongodb\+srv://") {
        Write-Host "   ✅ MONGODB_URI configurada`n" -ForegroundColor Green
    } else {
        Write-Host "   ⚠️  MONGODB_URI pode estar incorreta`n" -ForegroundColor Yellow
    }
} else {
    Write-Host "   ❌ Arquivo .env.render não encontrado!" -ForegroundColor Red
    Write-Host "   Crie o arquivo com as variáveis de ambiente`n" -ForegroundColor Yellow
    exit 1
}

# 3. Verificar arquivos de configuração
Write-Host "[3/6] Verificando arquivos de deploy..." -ForegroundColor Yellow
$arquivos = @("render.yaml", "Procfile", "RENDER-DEPLOY.md")
$todosOk = $true
foreach ($arquivo in $arquivos) {
    if (Test-Path $arquivo) {
        Write-Host "   ✅ $arquivo" -ForegroundColor Green
    } else {
        Write-Host "   ❌ $arquivo não encontrado!" -ForegroundColor Red
        $todosOk = $false
    }
}
if (!$todosOk) {
    Write-Host "`n   Arquivos de deploy faltando!`n" -ForegroundColor Red
    exit 1
}
Write-Host ""

# 4. Verificar application-prod.yml
Write-Host "[4/6] Verificando application-prod.yml..." -ForegroundColor Yellow
$prodYml = "modules/api/src/main/resources/application-prod.yml"
if (Test-Path $prodYml) {
    $content = Get-Content $prodYml -Raw
    if ($content -match 'MONGODB_URI') {
        Write-Host "   ✅ application-prod.yml configurado`n" -ForegroundColor Green
    } else {
        Write-Host "   ⚠️  MONGODB_URI pode não estar configurada`n" -ForegroundColor Yellow
    }
} else {
    Write-Host "   ❌ application-prod.yml não encontrado!`n" -ForegroundColor Red
    exit 1
}

# 5. Testar build local (opcional)
Write-Host "[5/6] Deseja testar build local? (s/n): " -ForegroundColor Yellow -NoNewline
$resposta = Read-Host
if ($resposta -eq "s" -or $resposta -eq "S") {
    Write-Host "   Executando: mvn clean package -DskipTests..." -ForegroundColor Cyan
    $buildResult = mvn clean package -DskipTests -pl modules/api -am 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "   ✅ Build local OK!`n" -ForegroundColor Green
    } else {
        Write-Host "   ❌ Build falhou!`n" -ForegroundColor Red
        Write-Host "   Corrija os erros antes de fazer deploy`n" -ForegroundColor Yellow
        exit 1
    }
} else {
    Write-Host "   ⏭️  Build local pulado`n" -ForegroundColor Gray
}

# 6. Verificar status Git
Write-Host "[6/6] Verificando Git status..." -ForegroundColor Yellow
$gitStatus = git status --porcelain
if ($gitStatus) {
    Write-Host "   ⚠️  Há mudanças não commitadas:" -ForegroundColor Yellow
    git status --short
    Write-Host ""
    Write-Host "   Deseja fazer commit agora? (s/n): " -ForegroundColor Yellow -NoNewline
    $resposta = Read-Host
    if ($resposta -eq "s" -or $resposta -eq "S") {
        Write-Host "   Mensagem do commit:" -ForegroundColor Yellow -NoNewline
        $mensagem = Read-Host
        git add .
        git commit -m $mensagem
        Write-Host "   ✅ Commit realizado`n" -ForegroundColor Green
    }
} else {
    Write-Host "   ✅ Nenhuma mudança pendente`n" -ForegroundColor Green
}

# Resumo
Write-Host "=== ✅ CHECKLIST DEPLOY RENDER ===" -ForegroundColor Green
Write-Host ""
Write-Host "Próximos passos:" -ForegroundColor Cyan
Write-Host ""
Write-Host "1. Push para GitHub:" -ForegroundColor White
Write-Host "   git push origin deploy/render" -ForegroundColor Gray
Write-Host ""
Write-Host "2. Acesse Render.com:" -ForegroundColor White
Write-Host "   https://render.com" -ForegroundColor Gray
Write-Host ""
Write-Host "3. Siga o tutorial:" -ForegroundColor White
Write-Host "   docs/025-deploy-render-step-by-step.md" -ForegroundColor Gray
Write-Host ""
Write-Host "4. Configure variáveis (copiar de .env.render):" -ForegroundColor White
Write-Host "   MONGODB_URI" -ForegroundColor Gray
Write-Host "   SPRING_PROFILES_ACTIVE=prod" -ForegroundColor Gray
Write-Host "   PORT=10000" -ForegroundColor Gray
Write-Host ""
Write-Host "5. Aguarde deploy (5-10 min)" -ForegroundColor White
Write-Host ""
Write-Host "=== 🎉 BOA SORTE COM O DEPLOY! ===" -ForegroundColor Green
Write-Host ""
