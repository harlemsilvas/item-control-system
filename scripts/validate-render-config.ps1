# 🔍 VALIDAÇÃO PRÉ-DEPLOY RENDER (Docker)

Write-Host "`n=== 🔍 VALIDAÇÃO PRÉ-DEPLOY RENDER ===" -ForegroundColor Cyan
Write-Host ""

$erros = 0

# 1. Verificar Dockerfile
Write-Host "[1/5] Verificando Dockerfile..." -ForegroundColor Yellow
if (Test-Path "Dockerfile") {
    $dockerContent = Get-Content "Dockerfile" -Raw

    # Verificar se usa PORT
    if ($dockerContent -match '\$\{?PORT\}?') {
        Write-Host "   ✅ Dockerfile usa variável PORT" -ForegroundColor Green
    } else {
        Write-Host "   ❌ Dockerfile NÃO usa PORT do Render!" -ForegroundColor Red
        $erros++
    }

    # Verificar multi-stage
    if ($dockerContent -match 'AS build') {
        Write-Host "   ✅ Multi-stage build configurado" -ForegroundColor Green
    } else {
        Write-Host "   ⚠️  Dockerfile não usa multi-stage" -ForegroundColor Yellow
    }

    # Verificar health check
    if ($dockerContent -match 'HEALTHCHECK') {
        Write-Host "   ✅ Health check configurado`n" -ForegroundColor Green
    } else {
        Write-Host "   ⚠️  Health check não configurado`n" -ForegroundColor Yellow
    }
} else {
    Write-Host "   ❌ Dockerfile não encontrado na raiz!`n" -ForegroundColor Red
    $erros++
}

# 2. Verificar branch
Write-Host "[2/5] Verificando branch..." -ForegroundColor Yellow
$branch = git rev-parse --abbrev-ref HEAD
if ($branch -eq "deploy/render") {
    Write-Host "   ✅ Branch correta: deploy/render`n" -ForegroundColor Green
} else {
    Write-Host "   ❌ Branch incorreta: $branch" -ForegroundColor Red
    Write-Host "   Execute: git checkout deploy/render`n" -ForegroundColor Yellow
    $erros++
}

# 3. Verificar .env.render
Write-Host "[3/5] Verificando variáveis de ambiente..." -ForegroundColor Yellow
if (Test-Path ".env.render") {
    $envContent = Get-Content ".env.render" -Raw

    # Verificar MONGODB_URI
    if ($envContent -match "MONGODB_URI=mongodb\+srv://") {
        Write-Host "   ✅ MONGODB_URI configurada" -ForegroundColor Green

        # Verificar query params
        if ($envContent -match "\?retryWrites=true") {
            Write-Host "   ✅ retryWrites=true presente" -ForegroundColor Green
        } else {
            Write-Host "   ⚠️  retryWrites=true ausente na URI" -ForegroundColor Yellow
        }
    } else {
        Write-Host "   ❌ MONGODB_URI não encontrada ou incorreta" -ForegroundColor Red
        $erros++
    }

    # Verificar SPRING_PROFILES_ACTIVE
    if ($envContent -match "SPRING_PROFILES_ACTIVE=prod") {
        Write-Host "   ✅ SPRING_PROFILES_ACTIVE=prod" -ForegroundColor Green
    } else {
        Write-Host "   ⚠️  SPRING_PROFILES_ACTIVE não configurado" -ForegroundColor Yellow
    }

    # Verificar PORT
    if ($envContent -match "PORT=10000") {
        Write-Host "   ✅ PORT=10000 definido`n" -ForegroundColor Green
    } else {
        Write-Host "   ⚠️  PORT não definido`n" -ForegroundColor Yellow
    }
} else {
    Write-Host "   ❌ Arquivo .env.render não encontrado!`n" -ForegroundColor Red
    $erros++
}

# 4. Verificar application-prod.yml
Write-Host "[4/5] Verificando application-prod.yml..." -ForegroundColor Yellow
$prodYml = "modules/api/src/main/resources/application-prod.yml"
if (Test-Path $prodYml) {
    $prodContent = Get-Content $prodYml -Raw

    if ($prodContent -match 'MONGODB_URI') {
        Write-Host "   ✅ application-prod.yml usa MONGODB_URI" -ForegroundColor Green
    } else {
        Write-Host "   ⚠️  MONGODB_URI pode não estar configurada" -ForegroundColor Yellow
    }

    if ($prodContent -match '\$\{PORT') {
        Write-Host "   ✅ server.port usa variável PORT`n" -ForegroundColor Green
    } else {
        Write-Host "   ⚠️  server.port pode estar fixo`n" -ForegroundColor Yellow
    }
} else {
    Write-Host "   ❌ application-prod.yml não encontrado!`n" -ForegroundColor Red
    $erros++
}

# 5. Verificar documentação
Write-Host "[5/5] Verificando documentação..." -ForegroundColor Yellow
$docs = @(
    "docs/027-render-form-quick-guide.md",
    "docs/028-dockerfile-render-explicado.md",
    "RENDER-DEPLOY.md"
)

foreach ($doc in $docs) {
    if (Test-Path $doc) {
        Write-Host "   ✅ $doc" -ForegroundColor Green
    } else {
        Write-Host "   ⚠️  $doc não encontrado" -ForegroundColor Yellow
    }
}
Write-Host ""

# Resumo
if ($erros -eq 0) {
    Write-Host "=== ✅ VALIDAÇÃO OK! ===" -ForegroundColor Green
    Write-Host ""
    Write-Host "Tudo pronto para deploy no Render!" -ForegroundColor Green
    Write-Host ""
    Write-Host "📋 CHECKLIST RENDER:" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "No formulário Render (https://dashboard.render.com/web/new):" -ForegroundColor White
    Write-Host ""
    Write-Host "  ☐ Name: item-control-api" -ForegroundColor Gray
    Write-Host "  ☐ Language: Docker (NÃO trocar!)" -ForegroundColor Gray
    Write-Host "  ☐ Branch: deploy/render" -ForegroundColor Gray
    Write-Host "  ☐ Region: Oregon (US West)" -ForegroundColor Gray
    Write-Host "  ☐ Instance Type: Free" -ForegroundColor Gray
    Write-Host ""
    Write-Host "  Environment Variables:" -ForegroundColor Gray
    Write-Host "    ☐ MONGODB_URI (copiar de .env.render)" -ForegroundColor Gray
    Write-Host "    ☐ SPRING_PROFILES_ACTIVE=prod" -ForegroundColor Gray
    Write-Host "    ☐ PORT=10000" -ForegroundColor Gray
    Write-Host ""
    Write-Host "  ☐ Health Check Path: /actuator/health" -ForegroundColor Gray
    Write-Host "  ☐ Auto-Deploy: marcado" -ForegroundColor Gray
    Write-Host ""
    Write-Host "🚀 Após preencher, clicar em 'Create Web Service'" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "⏱️  Tempo estimado: 10-15 minutos (primeira vez)" -ForegroundColor White
    Write-Host ""
} else {
    Write-Host "=== ❌ VALIDAÇÃO FALHOU! ===" -ForegroundColor Red
    Write-Host ""
    Write-Host "Encontrados $erros erro(s)." -ForegroundColor Red
    Write-Host "Corrija os problemas antes de fazer deploy." -ForegroundColor Yellow
    Write-Host ""
    exit 1
}

# Mostrar variáveis para copiar
Write-Host "💡 VARIÁVEIS PARA COPIAR:" -ForegroundColor Yellow
Write-Host ""
if (Test-Path ".env.render") {
    Write-Host "Abrir arquivo .env.render e copiar as 3 variáveis para o Render:" -ForegroundColor White
    Write-Host ""
    Get-Content ".env.render" | Where-Object { $_ -match "^[A-Z]" } | ForEach-Object {
        Write-Host "  $_" -ForegroundColor Cyan
    }
    Write-Host ""
}

Write-Host "📖 Documentação:" -ForegroundColor Yellow
Write-Host "  - Guia rápido: docs/027-render-form-quick-guide.md" -ForegroundColor White
Write-Host "  - Tutorial completo: docs/025-deploy-render-step-by-step.md" -ForegroundColor White
Write-Host "  - Dockerfile explicado: docs/028-dockerfile-render-explicado.md" -ForegroundColor White
Write-Host ""
