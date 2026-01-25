# 🎯 DEPLOY RENDER - BRANCH ESPECÍFICA

Esta branch (`deploy/render`) contém configurações otimizadas para **Render.com (100% GRÁTIS)**

## 📁 Arquivos desta Branch

- `render.yaml` - Configuração Render Blueprint
- `Procfile` - Comando de start alternativo
- `.render-buildpacks` - Buildpacks Java
- `docs/024-deploy-render-tutorial.md` - Tutorial completo

## 🚀 Como Fazer Deploy

### Passo 1: MongoDB Atlas (✅ JÁ CONFIGURADO)

**Connection String:**
```
mongodb+srv://harlemclaumann:xAsYVqpaNzGLJq80@cluster0.69j3tzl.mongodb.net/item_control_db
```

✅ Cluster: `cluster0.69j3tzl.mongodb.net`  
✅ Database: `item_control_db`  
✅ Network Access: Configurado  

> **Nota:** Credenciais salvas em `.env.render` (não comitar!)

### Passo 2: Render Deploy (10 min)

1. **Acesse:** https://render.com
2. **Signup** (GitHub/Google/Email - **RECOMENDADO: usar GitHub**)
3. **Dashboard** → New → **Web Service**
4. **Connect Repository:**
   - Se usou GitHub: autorizar acesso ao repo `item-control-system`
   - Ou: **Public Git Repository** → colar URL do GitHub
5. **Configurar Service:**
   - **Name:** `item-control-api`
   - **Region:** Oregon (US West)
   - **Branch:** `deploy/render` ⚠️ **IMPORTANTE!**
   - **Runtime:** Docker **OU** deixar auto-detect
   - **Build Command:** 
     ```bash
     mvn clean package -DskipTests -pl modules/api -am
     ```
   - **Start Command:**
     ```bash
     java -Xmx512m -jar modules/api/target/item-control-api-0.1.0-SNAPSHOT.jar
     ```
   - **Instance Type:** Free
6. **Environment Variables** (clicar "Advanced"):
   - `MONGODB_URI` = `mongodb+srv://harlemclaumann:xAsYVqpaNzGLJq80@cluster0.69j3tzl.mongodb.net/item_control_db?retryWrites=true&w=majority`
   - `SPRING_PROFILES_ACTIVE` = `prod`
   - `PORT` = `10000` (Render auto-configura, mas pode definir)
7. **Health Check Path:** `/actuator/health`
8. Clicar **"Create Web Service"**

### Passo 3: Aguardar Deploy (5-10 min)

Render vai:
1. Detectar Java 21
2. Executar Maven build
3. Iniciar aplicação
4. Gerar URL pública

### Passo 4: Testar

```bash
# Sua URL será algo como:
# https://item-control-api.onrender.com

# Health check
curl https://item-control-api.onrender.com/actuator/health

# Swagger
# https://item-control-api.onrender.com/swagger-ui.html
```

## 💰 Custo

- **Render:** $0/mês (FREE forever)
- **MongoDB Atlas:** $0/mês (512MB grátis)
- **TOTAL:** $0/mês 🎉

## ⚠️ Limitações FREE Tier

- App hiberna após **15 minutos** de inatividade
- **Cold start:** ~30-60 segundos na primeira requisição
- **750 horas/mês** (suficiente)
- Build: até 30 minutos

## 🔄 Migração para Railway

Quando quiser migrar:

```bash
# Voltar para main
git checkout main

# Ir para branch railway
git checkout deploy/railway

# Deploy no Railway
# (arquivos otimizados já estarão lá)
```

## 📖 Tutorial Completo

Veja `docs/024-deploy-render-tutorial.md` para guia passo a passo detalhado.

## ✅ Checklist

- [ ] MongoDB Atlas criado
- [ ] Connection string copiada
- [ ] Conta Render criada
- [ ] Blueprint deploy iniciado
- [ ] MONGODB_URI configurada
- [ ] Deploy completado
- [ ] Health check OK
- [ ] Endpoints testados

---

**Deploy em 15-20 minutos! 🚀**
