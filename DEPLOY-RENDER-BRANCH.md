# 🚀 Branch Deploy - Render.com

Esta é a branch de deploy para **Render.com** (100% GRÁTIS)

## ⚡ Quick Start

### 1️⃣ MongoDB Atlas (✅ Configurado)
```
mongodb+srv://harlemclaumann:***@cluster0.69j3tzl.mongodb.net/item_control_db
```

### 2️⃣ Deploy no Render (15 minutos)

**Tutorial Completo:**
- 📖 [Passo a Passo Detalhado](docs/025-deploy-render-step-by-step.md)
- 📋 [Guia Rápido](RENDER-DEPLOY.md)

**Resumo Rápido:**
1. Acesse: https://render.com
2. Signup (use GitHub para integração automática)
3. New → Web Service
4. Connect repo: `item-control-system`
5. Branch: `deploy/render`
6. Configure variáveis (veja `.env.render`)
7. Deploy!

### 3️⃣ Configuração Render

**Build Command:**
```bash
mvn clean package -DskipTests -pl modules/api -am
```

**Start Command:**
```bash
java -Xmx512m -jar modules/api/target/item-control-api-0.1.0-SNAPSHOT.jar
```

**Environment Variables:**
```
MONGODB_URI=mongodb+srv://harlemclaumann:Harlem010101@cluster0.69j3tzl.mongodb.net/item_control_db?retryWrites=true&w=majority
SPRING_PROFILES_ACTIVE=prod
PORT=10000
```

## 📁 Arquivos Importantes

| Arquivo | Descrição |
|---------|-----------|
| `render.yaml` | Blueprint Render |
| `Procfile` | Comando alternativo de start |
| `.env.render` | Variáveis de ambiente (não comitado) |
| `RENDER-DEPLOY.md` | Guia rápido |
| `docs/025-deploy-render-step-by-step.md` | Tutorial completo |

## 💰 Custo

- **Render Free Tier:** $0/mês
- **MongoDB Atlas M0:** $0/mês (512MB)
- **Total:** $0/mês 🎉

## ⚠️ Limitações

- App hiberna após 15 min de inatividade
- Cold start: ~30-60s
- 750 horas/mês (suficiente)

## 🔗 Links Úteis

- **Render Dashboard:** https://dashboard.render.com
- **MongoDB Atlas:** https://cloud.mongodb.com
- **Swagger (após deploy):** `https://[seu-app].onrender.com/swagger-ui.html`

## 📊 Status Deploy

Após deploy, sua API estará em:
```
https://item-control-api.onrender.com
```

Health check:
```
https://item-control-api.onrender.com/actuator/health
```

---

**Desenvolvido por Harlem Claumann** 🚀
