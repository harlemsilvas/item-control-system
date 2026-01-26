# 🚀 DEPLOY NO RENDER - PASSO A PASSO COMPLETO

**Data:** 2026-01-25  
**Branch:** `deploy/render`  
**MongoDB:** MongoDB Atlas (já configurado)  
**Custo Total:** $0/mês (100% GRÁTIS)

---

## 📋 PRÉ-REQUISITOS CONCLUÍDOS

✅ **MongoDB Atlas configurado**
- Connection String: `mongodb+srv://harlemclaumann:Harlem010101@cluster0.69j3tzl.mongodb.net/item_control_db`
- Database: `item_control_db`
- Network Access: Liberado (0.0.0.0/0)

✅ **Código no GitHub**
- Repositório: `item-control-system`
- Branch: `deploy/render`

✅ **Arquivos de Deploy**
- `render.yaml` - Blueprint Render
- `Procfile` - Comando de start
- `.env.render` - Variáveis de ambiente (local, não comitado)

---

## 🎯 PASSO 1: CRIAR CONTA NO RENDER (2 min)

### 1.1 Acessar Render
```
🌐 URL: https://render.com
```

### 1.2 Criar Conta (escolha uma opção)

**OPÇÃO A: GitHub (RECOMENDADO)** ⭐
1. Clicar **"Get Started for Free"**
2. Escolher **"Sign up with GitHub"**
3. Autorizar Render no GitHub
4. ✅ **Vantagem:** Deploy automático via Git push

**OPÇÃO B: Google Account**
1. Clicar **"Get Started for Free"**
2. Escolher **"Sign up with Google"**
3. Selecionar conta Google

**OPÇÃO C: Email**
1. Clicar **"Get Started for Free"**
2. Preencher email + senha
3. Verificar email

> **💡 Dica:** Use GitHub para integração perfeita!

---

## 🎯 PASSO 2: CONECTAR REPOSITÓRIO (3 min)

### 2.1 Ir para Dashboard
1. Após login → Dashboard Render
2. Clicar **"New +"** (canto superior direito)
3. Escolher **"Web Service"**

### 2.2 Conectar Repo GitHub

**Se usou GitHub no login:**
1. Na tela "Create a new Web Service"
2. Seção **"You are logged in to GitHub"**
3. Clicar **"+ New Web Service"** ou **"Configure account"**
4. Render pedirá para autorizar acesso aos repositórios:
   - **Option 1:** All repositories (mais fácil)
   - **Option 2:** Only select repositories → escolher `item-control-system`
5. Clicar **"Install"**
6. Voltar para Render → repo aparecerá na lista
7. Clicar **"Connect"** ao lado de `item-control-system`

**Se NÃO usou GitHub:**
1. Escolher **"Public Git Repository"**
2. Colar URL: `https://github.com/[SEU_USER]/item-control-system.git`
3. Clicar **"Continue"**

---

## 🎯 PASSO 3: CONFIGURAR WEB SERVICE (5 min)

### ⚠️ IMPORTANTE: Render e Java

**Render NÃO tem runtime Java nativo!**
- Linguagens nativas: Node, Python, Ruby, Go, Rust, Elixir
- **Para Java/Spring Boot:** Use **Docker** (forma oficial e recomendada)
- Nosso projeto já tem `Dockerfile` otimizado para Render

### 3.1 Informações Básicas

```yaml
Name: item-control-api
Project: (deixar vazio)
Language: Docker        # ✅ DEIXAR Docker! (não existe Java nativo)
Branch: deploy/render   # ⚠️ IMPORTANTE!
Region: Oregon (US West)
```

### 3.2 Root Directory

**Root Directory:** (deixar vazio)

### 3.3 Configuração Docker

O Render detectou automaticamente o `Dockerfile`:

**Dockerfile Path:** `./Dockerfile` (já detectado - não alterar)

**Docker Command:** (deixar vazio - usaremos o ENTRYPOINT do Dockerfile)

### 3.4 Instance Type

Rolar para baixo até **"Instance Type"**

- Escolher: **"Free"** (primeira opção - "For hobby projects")
- ✅ 512MB RAM
- ✅ 750 horas/mês
- ⚠️ Sleep após 15 min de inatividade (normal no free tier)

### 3.5 Environment Variables

Rolar para baixo até **"Environment Variables"**

Clicar para adicionar 3 variáveis:

| Key | Value |
|-----|-------|
| `MONGODB_URI` | `mongodb+srv://harlemclaumann:Harlem010101@cluster0.69j3tzl.mongodb.net/item_control_db?retryWrites=true&w=majority` |
| `SPRING_PROFILES_ACTIVE` | `prod` |
| `PORT` | `10000` |

**Como adicionar:**
1. Clicar **"Add Environment Variable"** ou ícone "+"
2. Preencher **Key** e **Value**
3. Repetir 3 vezes

⚠️ **IMPORTANTE:** 
- Copie `MONGODB_URI` completo com `?retryWrites=true&w=majority`
- `PORT=10000` é padrão do Render - nosso Dockerfile usa essa variável

### 3.6 Health Check Path

Localizar **"Health Check Path"** e preencher:

```
/actuator/health
```

### 3.7 Auto-Deploy

No final da página, localizar **"Auto-Deploy"**

- ✅ Deixar marcado (habilitado)
- Render fará deploy automático a cada push na branch `deploy/render`

---

## 🎯 PASSO 4: INICIAR DEPLOY (1 min)

1. Revisar todas as configurações
2. Clicar **"Create Web Service"** (botão azul no final da página)
3. Render iniciará deploy automático

---

## 🎯 PASSO 5: ACOMPANHAR BUILD (5-10 min)

### 5.1 Logs em Tempo Real

Render mostrará logs do build:

```
==> Cloning from https://github.com/[USER]/item-control-system...
==> Checking out commit abc123... in branch deploy/render
==> Downloading buildpack... ✓
==> Detecting...
    Java detected
==> Restoring cache...
==> Building...
    [INFO] Building Item Control System - API
    [INFO] Downloading dependencies...
    [INFO] Compiling...
    [INFO] BUILD SUCCESS
==> Uploading build...
==> Starting service...
    2026-01-25 20:30:00 INFO  Starting ItemControlApplication
    2026-01-25 20:30:05 INFO  Connected to MongoDB Atlas
    2026-01-25 20:30:08 INFO  Started ItemControlApplication in 8.2 seconds
==> Your service is live 🎉
```

### 5.2 O que esperar

| Fase | Duração | Status |
|------|---------|--------|
| Clone repo | 10-30s | ⏳ |
| Download deps | 2-5 min | ⏳ |
| Maven build | 2-4 min | ⏳ |
| Upload build | 30s-1min | ⏳ |
| Start app | 30s-1min | ⏳ |
| **TOTAL** | **5-10 min** | ✅ |

### 5.3 Possíveis Problemas

**❌ Build Failed - Dependency download**
- **Causa:** Timeout baixando dependências
- **Solução:** Clicar **"Manual Deploy"** → **"Deploy latest commit"**

**❌ Application Failed to Start**
- **Causa:** MONGODB_URI incorreta
- **Solução:** 
  1. Ir em **"Environment"** (menu lateral)
  2. Verificar/corrigir `MONGODB_URI`
  3. Clicar **"Save Changes"** (redeploy automático)

**❌ Health Check Failed**
- **Causa:** Path incorreto
- **Solução:** Verificar `/actuator/health` está correto

---

## 🎯 PASSO 6: TESTAR DEPLOY (2 min)

### 6.1 Copiar URL

Após deploy concluído, Render mostrará:
```
Your service is live at https://item-control-api.onrender.com
```

### 6.2 Testar Health Check

**No navegador:**
```
https://item-control-api.onrender.com/actuator/health
```

**Resposta esperada:**
```json
{"status":"UP"}
```

### 6.3 Testar API (PowerShell)

```powershell
# Health Check
$url = "https://item-control-api.onrender.com"
Invoke-RestMethod -Uri "$url/actuator/health"

# Criar primeiro item
$body = @{
    name = "Item Deploy Render"
    nickname = "render-001"
    description = "Primeiro item criado no Render"
    template = "GENERAL"
} | ConvertTo-Json

Invoke-RestMethod -Uri "$url/api/v1/items" -Method POST -Body $body -ContentType "application/json"
```

### 6.4 Acessar Swagger

```
https://item-control-api.onrender.com/swagger-ui.html
```

---

## 🎯 PASSO 7: VERIFICAR MONGODB ATLAS (1 min)

### 7.1 Acessar Atlas

1. Ir para https://cloud.mongodb.com
2. Login
3. **Database** → **Browse Collections**
4. Cluster: `cluster0`
5. Database: `item_control_db`

### 7.2 Verificar Collections Criadas

Render criará automaticamente:
- ✅ `items`
- ✅ `categories`
- ✅ `events`
- ✅ `alerts`

---

## 🎉 DEPLOY CONCLUÍDO!

### ✅ Checklist Final

- [x] Conta Render criada
- [x] Repositório conectado
- [x] Environment variables configuradas
- [x] Build concluído com sucesso
- [x] Health check respondendo
- [x] MongoDB Atlas conectado
- [x] Collections criadas
- [x] API funcionando

### 📊 Informações do Deploy

| Item | Valor |
|------|-------|
| **URL** | `https://item-control-api.onrender.com` |
| **MongoDB** | MongoDB Atlas (cluster0.69j3tzl.mongodb.net) |
| **Database** | `item_control_db` |
| **Region** | Oregon (US West) |
| **Custo** | $0/mês |
| **Uptime** | 750h/mês |

---

## 📝 PRÓXIMOS PASSOS

### Opção A: Popular Banco de Dados

```powershell
# Via scripts locais apontando para Render
$env:API_URL = "https://item-control-api.onrender.com"
.\scripts\populate-categories.ps1
.\scripts\populate-test-data.ps1
```

### Opção B: Configurar Domínio Customizado (opcional)

1. Render Dashboard → service `item-control-api`
2. **Settings** → **Custom Domain**
3. Adicionar: `api.seudominio.com`
4. Configurar DNS conforme instruções

### Opção C: Monitorar Aplicação

1. **Metrics** (menu lateral) → gráficos CPU/RAM
2. **Logs** → logs em tempo real
3. **Events** → histórico deploys

---

## ⚠️ LIMITAÇÕES FREE TIER

### Sleep Automático
- App hiberna após **15 minutos** sem requisições
- **Cold start:** 30-60 segundos na primeira requisição após hibernar
- **Solução:** Use um cron job para "pingar" a API a cada 10 minutos

### Build Time
- Timeout: 30 minutos
- Geralmente leva 5-10 min

### Disco
- Ephemeral (dados não persistem entre deploys)
- ✅ OK para nossa app (estado no MongoDB)

---

## 🔧 TROUBLESHOOTING

### Problema 1: Build Timeout
```
Error: Build exceeded 30 minutes
```

**Solução:**
1. Verificar se Maven está baixando deps desnecessárias
2. Adicionar ao `pom.xml`:
```xml
<properties>
    <maven.test.skip>true</maven.test.skip>
</properties>
```

### Problema 2: MongoDB Connection Failed
```
MongoTimeoutException: Timed out after 30000 ms
```

**Solução:**
1. Verificar Network Access no Atlas:
   - **IP Whitelist:** `0.0.0.0/0`
2. Verificar `MONGODB_URI` tem `?retryWrites=true&w=majority`

### Problema 3: Health Check Failed
```
Health check failed after 3 attempts
```

**Solução:**
1. Verificar porta: `server.port=${PORT:8080}`
2. Verificar path: `/actuator/health`
3. Logs → verificar se app iniciou

---

## 🆘 SUPORTE

- **Render Docs:** https://render.com/docs
- **MongoDB Atlas Docs:** https://docs.atlas.mongodb.com
- **Nosso projeto:** `docs/024-deploy-render-tutorial.md`

---

**Deploy realizado com sucesso! 🎉🚀**
