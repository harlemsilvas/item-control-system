# 🚀 GUIA DE DEPLOY GRATUITO - OPÇÕES DISPONÍVEIS

**Data:** 24/01/2026  
**Sistema:** Item Control System (Spring Boot + MongoDB)

---

## 📊 COMPARAÇÃO RÁPIDA - MELHORES OPÇÕES GRATUITAS

| Plataforma | Backend Java | MongoDB | Deploy Fácil | Custo | Recomendação |
|------------|--------------|---------|--------------|-------|--------------|
| **Railway** ⭐ | ✅ Sim | ✅ Sim | ✅✅✅ | $5 free/mês | ⭐⭐⭐⭐⭐ |
| **Render** | ✅ Sim | ✅ Sim | ✅✅✅ | Free tier | ⭐⭐⭐⭐ |
| **Fly.io** | ✅ Sim | ⚠️ Externo | ✅✅ | Free tier | ⭐⭐⭐ |
| **Vercel** | ❌ Não* | ❌ Não | ✅✅✅ | Free | ❌ (só frontend) |
| **Heroku** | ✅ Sim | ⚠️ Pago | ✅✅ | Sem free tier | ❌ |

*Vercel é para Next.js, React, etc. Não suporta Spring Boot

---

## 🏆 OPÇÃO 1: RAILWAY (RECOMENDADO) ⭐

### **Por que Railway?**

✅ **Suporta Spring Boot nativamente**  
✅ **MongoDB incluído (add-on gratuito)**  
✅ **Deploy automático via Git**  
✅ **$5 de crédito grátis/mês** (suficiente para dev)  
✅ **URL pública HTTPS automática**  
✅ **Logs em tempo real**  
✅ **Variáveis de ambiente fáceis**  
✅ **Você já tem experiência com Railway!**

### **Limitações do Free Tier:**
- $5/mês de crédito (500h de uso)
- Após acabar o crédito, app hiberna
- Domínio personalizado requer upgrade

### **Como Fazer Deploy:**

#### **Passo 1: Preparar o Projeto**
```bash
# Criar Dockerfile na raiz do projeto
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY modules/api/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### **Passo 2: railway.json**
```json
{
  "$schema": "https://railway.app/railway.schema.json",
  "build": {
    "builder": "DOCKERFILE",
    "dockerfilePath": "Dockerfile"
  },
  "deploy": {
    "startCommand": "java -jar app.jar",
    "restartPolicyType": "ON_FAILURE",
    "restartPolicyMaxRetries": 10
  }
}
```

#### **Passo 3: Deploy**
1. Acesse https://railway.app
2. Conecte seu GitHub
3. New Project → Deploy from GitHub
4. Selecione: `item-control-system`
5. Add MongoDB (template)
6. Defina variáveis:
   - `SPRING_PROFILES_ACTIVE=prod`
   - `MONGODB_URI` (gerado automaticamente)

**Tempo estimado:** 10 minutos ⏱️

---

## 🥈 OPÇÃO 2: RENDER

### **Por que Render?**

✅ **100% Gratuito** (tier free permanente)  
✅ **Spring Boot suportado**  
✅ **MongoDB Atlas integrado** (grátis 512MB)  
✅ **Deploy via Git automático**  
✅ **SSL automático**  
✅ **Não pede cartão de crédito**

### **Limitações do Free Tier:**
- App hiberna após 15 min inatividade
- Tempo de cold start: ~30s
- 750h/mês (suficiente)

### **Como Fazer Deploy:**

#### **Passo 1: render.yaml**
```yaml
services:
  - type: web
    name: item-control-api
    env: java
    buildCommand: mvn clean package -DskipTests
    startCommand: java -jar modules/api/target/item-control-api-0.1.0-SNAPSHOT.jar
    envVars:
      - key: SPRING_PROFILES_ACTIVE
        value: prod
      - key: MONGODB_URI
        fromDatabase:
          name: mongodb
          property: connectionString

databases:
  - name: mongodb
    databaseName: item_control_db
    user: itemcontrol
```

#### **Passo 2: Deploy**
1. Acesse https://render.com
2. New → Web Service
3. Connect GitHub
4. Select Repository: `item-control-system`
5. Configure:
   - Build: `mvn clean package -DskipTests`
   - Start: `java -jar modules/api/target/*.jar`

**Tempo estimado:** 15 minutos ⏱️

---

## 🥉 OPÇÃO 3: FLY.IO

### **Por que Fly.io?**

✅ **Gratuito para 3 VMs**  
✅ **Spring Boot suportado**  
✅ **Rápido e leve**  
✅ **CLI poderosa**

⚠️ **MongoDB precisa ser externo** (MongoDB Atlas)

### **Limitações:**
- MongoDB não incluído (usar Atlas)
- Requer Dockerfile
- CLI necessária

### **Como Fazer Deploy:**

```bash
# Instalar Fly CLI
powershell -Command "iwr https://fly.io/install.ps1 -useb | iex"

# Login
fly auth login

# Iniciar projeto
fly launch --name item-control-system

# Deploy
fly deploy
```

**Tempo estimado:** 20 minutos ⏱️

---

## 🗄️ OPÇÕES DE BANCO DE DADOS MONGODB GRATUITO

### **1. Railway MongoDB** ⭐ (RECOMENDADO)
- ✅ Incluído no Railway
- ✅ Configuração automática
- ✅ Mesma plataforma
- ⚠️ Consome os $5/mês

### **2. MongoDB Atlas** (FREE TIER)
- ✅ **512MB grátis para sempre**
- ✅ Cluster compartilhado
- ✅ Backups automáticos
- ✅ Não precisa cartão de crédito
- 🌐 https://www.mongodb.com/cloud/atlas/register

**Configuração:**
1. Criar conta gratuita
2. Create Cluster (M0 Free)
3. Copiar connection string
4. Usar em `MONGODB_URI`

### **3. Railway + MongoDB Atlas** (MELHOR CUSTO/BENEFÍCIO)
- Railway para API ($5/mês)
- MongoDB Atlas para DB (grátis)
- **Total: $5/mês** com MongoDB ilimitado

---

## 💡 MINHA RECOMENDAÇÃO FINAL

### **🏆 SETUP IDEAL PARA VOCÊ:**

```
┌─────────────────────────────────────────┐
│  🚀 Railway (API)          $5/mês       │
│  🗄️  MongoDB Atlas (DB)     $0/mês       │
│  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━   │
│  💰 TOTAL:                 $5/mês       │
└─────────────────────────────────────────┘
```

**Por quê?**
1. ✅ Você já conhece Railway (MongoDB configurado)
2. ✅ Deploy mais rápido (10 min)
3. ✅ MongoDB Atlas grátis (512MB suficiente para testes)
4. ✅ Separação de responsabilidades (API ≠ DB)
5. ✅ Se acabar os $5, só a API hiberna (DB continua)

### **🎯 ALTERNATIVA 100% GRATUITA:**

```
┌─────────────────────────────────────────┐
│  🚀 Render (API)           $0/mês       │
│  🗄️  MongoDB Atlas (DB)     $0/mês       │
│  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━   │
│  💰 TOTAL:                 $0/mês       │
└─────────────────────────────────────────┘
```

**Limitações:**
- ⚠️ App hiberna após 15 min inatividade
- ⚠️ Cold start ~30s na primeira requisição
- ✅ Perfeito para desenvolvimento/testes
- ✅ Pode migrar para Railway depois

---

## 📝 PRÓXIMOS PASSOS RECOMENDADOS

### **OPÇÃO A: Railway + MongoDB Atlas** ⭐ (RECOMENDADO)

**Tempo:** 15 minutos

1. **Criar conta MongoDB Atlas** (5 min)
   - https://www.mongodb.com/cloud/atlas/register
   - Create Free Cluster (M0)
   - Copy connection string

2. **Preparar projeto** (5 min)
   - Criar Dockerfile
   - Criar railway.json
   - Commit & push

3. **Deploy Railway** (5 min)
   - New Project
   - Import from GitHub
   - Add variável MONGODB_URI
   - Deploy!

### **OPÇÃO B: Render (100% Free)** 

**Tempo:** 20 minutos

1. **Criar conta MongoDB Atlas** (5 min)
2. **Criar render.yaml** (5 min)
3. **Deploy Render** (10 min)
   - Conectar GitHub
   - Configurar build
   - Deploy!

---

## 🎁 BONUS: FRONTEND (SE QUISER)

### **Vercel (Frontend React/Next.js)** - 100% GRÁTIS

Se futuramente criar um frontend:
- ✅ Deploy automático
- ✅ HTTPS gratuito
- ✅ CDN global
- ✅ Preview de PRs

**Stack recomendada:**
- Frontend: Vercel (React/Next.js)
- Backend: Railway/Render (Spring Boot)
- Database: MongoDB Atlas

---

## ✅ CHECKLIST PARA DEPLOY

### Antes de fazer deploy:

- [ ] Dockerfile criado
- [ ] railway.json ou render.yaml criado
- [ ] application-prod.yml configurado
- [ ] Variáveis de ambiente definidas
- [ ] MongoDB Atlas cluster criado (se usar)
- [ ] Testes passando localmente
- [ ] Commit e push no GitHub

### Durante o deploy:

- [ ] Projeto importado na plataforma
- [ ] Build configurado
- [ ] Variáveis de ambiente adicionadas
- [ ] Deploy iniciado

### Após o deploy:

- [ ] Health check funcionando
- [ ] Testar endpoints principais
- [ ] Verificar logs
- [ ] Popular dados de teste
- [ ] Documentar URL pública

---

## 📚 DOCUMENTAÇÃO ÚTIL

### Railway
- 🌐 https://docs.railway.app/
- 📖 Deploy Spring Boot: https://docs.railway.app/guides/spring-boot
- 🗄️ MongoDB Plugin: https://docs.railway.app/databases/mongodb

### Render
- 🌐 https://render.com/docs
- 📖 Deploy Java: https://render.com/docs/deploy-spring-boot

### MongoDB Atlas
- 🌐 https://www.mongodb.com/docs/atlas/
- 📖 Getting Started: https://www.mongodb.com/docs/atlas/getting-started/

---

## 🎯 RESUMO EXECUTIVO

**Para desenvolvimento/testes:** Render + MongoDB Atlas ($0)  
**Para produção/portfólio:** Railway + MongoDB Atlas ($5)  
**Vercel:** Só se fizer frontend React/Next.js

**Minha recomendação:** Comece com **Railway + MongoDB Atlas** pois:
1. Você já conhece Railway ✅
2. Deploy em 10-15 minutos ✅
3. $5/mês é investimento mínimo ✅
4. Fácil de escalar depois ✅

---

**Quando voltar do café, me diga qual opção prefere e eu preparo tudo! ☕**
