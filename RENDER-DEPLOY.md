# 🎯 DEPLOY RENDER - BRANCH ESPECÍFICA

Esta branch (`deploy/render`) contém configurações otimizadas para **Render.com (100% GRÁTIS)**

## 📁 Arquivos desta Branch

- `render.yaml` - Configuração Render Blueprint
- `Procfile` - Comando de start alternativo
- `.render-buildpacks` - Buildpacks Java
- `docs/024-deploy-render-tutorial.md` - Tutorial completo

## 🚀 Como Fazer Deploy

### Passo 1: MongoDB Atlas (5 min)

1. Acesse: https://www.mongodb.com/cloud/atlas/register
2. Crie conta gratuita (sem cartão)
3. Create Cluster → **M0 FREE**
4. Database Access:
   - Username: `itemcontrol`
   - Password: [gerar senha forte]
   - Role: **Atlas Admin**
5. Network Access:
   - Add IP: **0.0.0.0/0** (Allow from anywhere)
6. Connect → Application → Java
7. Copiar connection string:
   ```
   mongodb+srv://itemcontrol:PASSWORD@cluster.xxxxx.mongodb.net/item_control_db
   ```

### Passo 2: Render Deploy (10 min)

1. Acesse: https://render.com
2. Signup (GitHub/Google/Email)
3. New → **Blueprint**
4. Connect Repository: `item-control-system`
5. Branch: `deploy/render`
6. Render detectará `render.yaml`
7. Configure Environment Variables:
   - `MONGODB_URI`: [sua connection string do Atlas]
8. Apply → Deploy!

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
