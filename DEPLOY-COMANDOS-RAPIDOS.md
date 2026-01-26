# ⚡ DEPLOY RENDER - COMANDOS RÁPIDOS

## 🔍 Verificar Status

```powershell
# Ver branch atual
git branch

# Ver status
git status

# Ver último commit
git log -1
```

## 🚀 Push para GitHub

```powershell
# Se fizer mudanças
git add .
git commit -m "sua mensagem"
git push origin deploy/render
```

## 🧪 Validar Antes de Deploy

```powershell
# Executar script de validação
.\scripts\prepare-deploy-render.ps1

# Ou validar manualmente:
# 1. Verificar branch
git rev-parse --abbrev-ref HEAD  # deve retornar: deploy/render

# 2. Testar build
mvn clean package -DskipTests -pl modules/api -am

# 3. Ver arquivo .env.render
Get-Content .env.render
```

## 📋 Variáveis para Copiar no Render

Abra `.env.render` e copie para Render Dashboard:

```bash
MONGODB_URI=mongodb+srv://harlemclaumann:Harlem010101@cluster0.69j3tzl.mongodb.net/item_control_db?retryWrites=true&w=majority
SPRING_PROFILES_ACTIVE=prod
PORT=10000
```

## 🧪 Testar Após Deploy

```powershell
# Substituir URL pela sua do Render
$url = "https://item-control-api.onrender.com"

# Health check
Invoke-RestMethod -Uri "$url/actuator/health"

# Criar item teste
$body = @{
    name = "Item Teste Render"
    nickname = "render-test-001"
    template = "GENERAL"
} | ConvertTo-Json

Invoke-RestMethod -Uri "$url/api/v1/items" `
    -Method POST `
    -Body $body `
    -ContentType "application/json"

# Listar itens
Invoke-RestMethod -Uri "$url/api/v1/items"

# Abrir Swagger no navegador
Start-Process "$url/swagger-ui.html"
```

## 🔄 Redeploy Manual

No Render Dashboard:
1. Ir em "Manual Deploy"
2. Clicar "Deploy latest commit"
3. Aguardar build

## 📊 Ver Logs

No Render Dashboard:
1. Menu lateral → "Logs"
2. Ver logs em tempo real
3. Filtrar por erro: digitar "ERROR"

## 🗄️ Verificar MongoDB

```powershell
# Abrir MongoDB Compass
# Conectar com URL:
mongodb+srv://harlemclaumann:Harlem010101@cluster0.69j3tzl.mongodb.net/

# Database: item_control_db
# Collections: items, categories, events, alerts
```

## 🆘 Troubleshooting Rápido

### Build falhou
```powershell
# Testar localmente
mvn clean package -DskipTests -pl modules/api -am

# Se OK local, tentar redeploy no Render
```

### MongoDB connection failed
```
1. Verificar no Atlas:
   - Network Access → IP: 0.0.0.0/0
   - Database Access → user existe

2. Verificar no Render:
   - Environment → MONGODB_URI está correta
   - Não tem espaços no começo/fim
   - Tem ?retryWrites=true&w=majority no final
```

### Health check failed
```
1. Ver logs no Render
2. Procurar por "Started ItemControlApplication"
3. Verificar porta: deve usar $PORT do Render
```

## 📚 Documentação

```powershell
# Abrir documentos
code docs/025-deploy-render-step-by-step.md
code docs/026-resumo-config-deploy-render.md
code RENDER-DEPLOY.md
```

## 🔗 Links Úteis

- **Render Dashboard:** https://dashboard.render.com
- **MongoDB Atlas:** https://cloud.mongodb.com
- **GitHub Repo:** https://github.com/harlemsilvas/item-control-system
- **Branch Deploy:** https://github.com/harlemsilvas/item-control-system/tree/deploy/render

---

**Comandos salvos para referência rápida! ⚡**
