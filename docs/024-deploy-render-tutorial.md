# 🎯 TUTORIAL COMPLETO - DEPLOY NO RENDER (100% GRÁTIS)

**Plataforma:** Render.com  
**Custo:** $0/mês (FREE forever)  
**Tempo:** 15-20 minutos  
**Branch:** `deploy/render`

---

## 📋 VISÃO GERAL

Vamos fazer deploy de:
- ✅ **API Spring Boot** → Render (FREE tier)
- ✅ **MongoDB** → MongoDB Atlas (FREE 512MB)
- ✅ **TOTAL:** $0/mês 🎉

---

## 🗄️ PASSO 1: CRIAR MONGODB ATLAS (5 min)

### 1.1 Criar Conta

```
🌐 URL: https://www.mongodb.com/cloud/atlas/register
```

1. Clicar **"Try Free"**
2. Escolher método:
   - Google Account (recomendado)
   - GitHub Account
   - ou Email + Password
3. **NÃO precisa cartão de crédito!**

### 1.2 Criar Cluster Gratuito

1. Após login, clicar **"Build a Database"**
2. Escolher **"M0 FREE"** (Shared)
3. Configurar:
   - **Provider:** AWS
   - **Region:** US East (N. Virginia) ou mais próximo
   - **Cluster Name:** `item-control-cluster`
4. Clicar **"Create"**
5. Aguardar 1-3 minutos (cluster sendo criado)

### 1.3 Configurar Database Access

1. Menu lateral → **Database Access**
2. Clicar **"Add New Database User"**
3. Authentication Method: **Password**
4. Configurar:
   ```
   Username: itemcontrol
   Password: [clicar em Auto-Generate ou criar senha forte]
   
   ⚠️ COPIAR A SENHA GERADA!
   ```
5. Built-in Role: **Atlas Admin**
6. Clicar **"Add User"**

### 1.4 Configurar Network Access

1. Menu lateral → **Network Access**
2. Clicar **"Add IP Address"**
3. Escolher **"Allow Access from Anywhere"**
   ```
   IP Address: 0.0.0.0/0
   Comment: Render App Access
   ```
4. Clicar **"Confirm"**

### 1.5 Obter Connection String

1. Menu lateral → **Database** (Deployments)
2. No cluster, clicar **"Connect"**
3. Escolher **"Connect your application"**
4. Driver: **Java**
5. Version: **4.11 or later**
6. Copiar a connection string:

```
mongodb+srv://itemcontrol:<password>@item-control-cluster.xxxxx.mongodb.net/?retryWrites=true&w=majority
```

7. **IMPORTANTE:** Substituir `<password>` pela senha real!
8. **Adicionar database name:**

```
mongodb+srv://itemcontrol:SUA_SENHA@item-control-cluster.xxxxx.mongodb.net/item_control_db?retryWrites=true&w=majority
```

**✅ MongoDB Atlas configurado!**

---

## 🚀 PASSO 2: DEPLOY NO RENDER (10 min)

### 2.1 Criar Conta Render

```
🌐 URL: https://render.com
```

1. Clicar **"Get Started"**
2. Signup com:
   - GitHub (recomendado) ⭐
   - GitLab
   - Google
   - ou Email
3. **NÃO precisa cartão de crédito!**

### 2.2 Conectar GitHub

1. Após login, clicar **"New +"**
2. Escolher **"Blueprint"**
3. Clicar **"Connect account"** (GitHub)
4. Autorizar Render no GitHub
5. Escolher opção:
   - **All repositories** (mais fácil)
   - ou **Only select repositories** (mais seguro)
6. Se escolher select: marcar `item-control-system`
7. Clicar **"Install"**

### 2.3 Criar Blueprint

1. Na página Blueprints, clicar **"New Blueprint Instance"**
2. Connect a repository:
   - Buscar: `item-control-system`
   - Clicar no repositório
3. Branch: escolher **`deploy/render`**
4. Blueprint Name: `item-control-system`
5. Render vai detectar `render.yaml` automaticamente
6. Clicar **"Apply"**

### 2.4 Configurar Variáveis de Ambiente

1. Render vai mostrar os serviços detectados
2. Clicar no serviço **"item-control-api"**
3. Ir em **Environment**
4. Adicionar variáveis:

```bash
# 1. Spring Profile
SPRING_PROFILES_ACTIVE = prod

# 2. MongoDB Connection String (COPIAR DO ATLAS!)
MONGODB_URI = mongodb+srv://itemcontrol:SUA_SENHA@cluster.xxxxx.mongodb.net/item_control_db?retryWrites=true&w=majority

# 3. Porta (Render define automaticamente, mas pode confirmar)
PORT = 10000
```

**⚠️ ATENÇÃO:**
- Substituir `SUA_SENHA` pela senha do MongoDB Atlas
- Incluir `/item_control_db` no final da URL
- Não usar aspas nas variáveis

5. Clicar **"Save Changes"**

### 2.5 Iniciar Deploy

1. Render iniciará deploy automaticamente
2. Você verá logs em tempo real:

```
==> Downloading Maven...
==> Running mvn clean package...
==> Building JAR...
==> Starting application...
==> Your service is live 🎉
```

3. **Aguardar 5-10 minutos** (primeira vez demora mais)

### 2.6 Obter URL Pública

1. Após deploy completar, Render mostrará:
   ```
   Your service is live at:
   https://item-control-api.onrender.com
   ```

2. Copiar essa URL!

**✅ Deploy completado!**

---

## 🧪 PASSO 3: TESTAR A APLICAÇÃO (5 min)

### 3.1 Health Check

Abrir no navegador:
```
https://item-control-api.onrender.com/actuator/health
```

Deve retornar:
```json
{"status":"UP"}
```

### 3.2 Testar com PowerShell

```powershell
# Definir URL
$baseUrl = "https://item-control-api.onrender.com"

# Health check
Invoke-RestMethod -Uri "$baseUrl/actuator/health"

# Deve retornar: status=UP
```

### 3.3 Acessar Swagger UI

Abrir no navegador:
```
https://item-control-api.onrender.com/swagger-ui.html
```

### 3.4 Criar Primeira Categoria

```powershell
$baseUrl = "https://item-control-api.onrender.com"
$userId = "550e8400-e29b-41d4-a716-446655440001"

$body = @{
    userId = $userId
    name = "Veículos"
} | ConvertTo-Json

Invoke-RestMethod -Uri "$baseUrl/api/v1/categories" `
    -Method POST `
    -ContentType "application/json" `
    -Body $body
```

### 3.5 Listar Categorias

```powershell
Invoke-RestMethod -Uri "$baseUrl/api/v1/categories?userId=$userId"
```

**✅ Aplicação funcionando!**

---

## 📊 PASSO 4: POPULAR DADOS DE TESTE (Opcional)

### 4.1 Atualizar Script Local

Editar `scripts/populate-categories.ps1`:

```powershell
# Mudar apenas a primeira linha
$baseUrl = "https://item-control-api.onrender.com/api/v1"

# Resto do script igual
```

### 4.2 Executar

```powershell
.\scripts\populate-categories.ps1
```

Deve criar 5 categorias!

---

## 🔍 PASSO 5: MONITORAR (Opcional)

### 5.1 Ver Logs

1. Render Dashboard
2. Serviço `item-control-api`
3. Aba **"Logs"**
4. Ver logs em tempo real

### 5.2 Métricas

1. Aba **"Metrics"**
2. Ver:
   - Response times
   - Memory usage
   - Request count

### 5.3 MongoDB Atlas Metrics

1. Atlas Dashboard
2. Cluster → **Metrics**
3. Ver:
   - Connections
   - Operations/sec
   - Network I/O

---

## ⚙️ CONFIGURAÇÕES RENDER

### Auto-Deploy

Por padrão, Render faz auto-deploy quando você faz push:

```bash
git push origin deploy/render
# Render automaticamente faz novo deploy
```

Para desabilitar:
1. Service Settings
2. Auto-Deploy: **Off**

### Custom Domain (Opcional)

1. Settings → **Custom Domain**
2. Add custom domain
3. Configurar DNS conforme instruções

### Environment Groups

Criar grupos de variáveis reutilizáveis:
1. Dashboard → **Environment Groups**
2. Create → Adicionar variáveis
3. Linkar ao serviço

---

## 🐛 TROUBLESHOOTING

### Erro: "Build Failed"

**Problema:** Maven não consegue baixar dependências

**Solução:**
```yaml
# Em render.yaml, adicionar:
buildCommand: |
  mvn clean package -DskipTests -pl modules/api -am -U
```

### Erro: "Application Failed to Start"

**Verificar:**
1. Logs do Render (aba Logs)
2. Variável `MONGODB_URI` está correta?
3. Senha sem caracteres especiais problemáticos?

**Solução:**
```bash
# Testar connection string localmente:
mongosh "mongodb+srv://itemcontrol:SENHA@cluster.mongodb.net/item_control_db"
```

### Erro: "Connection Timeout" ou "MongoTimeoutException"

**Problema:** MongoDB Atlas não aceita conexão do Render

**Verificar:**
1. Network Access tem 0.0.0.0/0?
2. Usuario/senha corretos?
3. Database name na URL?

**Solução:**
1. Atlas → Network Access
2. Garantir **0.0.0.0/0** está permitido
3. Restartar serviço no Render

### Cold Start Lento

**Problema:** App demora 30-60s para responder após inatividade

**Isso é normal no FREE tier!**

**Solução (opcional):**
- Usar serviço de ping (https://uptimerobot.com) FREE
- Pingar app a cada 14 min
- Mantém app "acordado"

### Build Timeout

**Problema:** Build demora mais de 30 min

**Solução:**
```yaml
buildCommand: mvn clean package -DskipTests -T 1C -pl modules/api -am
# -T 1C = build paralelo (mais rápido)
```

---

## 💰 CUSTOS E LIMITES FREE TIER

### Render FREE

```
✅ 750 horas/mês (mais que suficiente)
✅ 512MB RAM
✅ Shared CPU
✅ Auto-deploy via Git
✅ HTTPS automático
✅ Logs 7 dias
⚠️ App hiberna após 15 min inatividade
⚠️ Cold start ~30-60s
⚠️ Build máximo 30 min
```

### MongoDB Atlas FREE

```
✅ 512MB storage (suficiente para testes)
✅ Shared cluster
✅ Backups automáticos
✅ Monitoramento básico
✅ Sem limite de tempo
⚠️ Conexões limitadas (100 simultâneas)
⚠️ Performance limitada
```

---

## 🔄 MIGRAÇÃO PARA RAILWAY

Quando quiser migrar para Railway ($5/mês):

### Passo 1: Ir para branch Railway

```bash
git checkout main
git checkout deploy/railway
```

### Passo 2: Deploy no Railway

1. https://railway.app
2. New Project → item-control-system
3. Branch: `deploy/railway`
4. Adicionar variável `MONGODB_URI` (mesma do Atlas)
5. Deploy!

### Passo 3: Atualizar URLs

Trocar URL do Render pela URL do Railway nos scripts.

**Vantagem Railway:**
- Sem cold start
- App sempre disponível
- Build mais rápido

---

## ✅ CHECKLIST FINAL

### MongoDB Atlas
- [ ] Conta criada
- [ ] Cluster M0 FREE criado
- [ ] Database user criado
- [ ] Network access 0.0.0.0/0
- [ ] Connection string copiada

### Render
- [ ] Conta criada
- [ ] GitHub conectado
- [ ] Blueprint criado
- [ ] Variáveis configuradas
- [ ] Deploy completado
- [ ] URL pública gerada

### Testes
- [ ] Health check OK
- [ ] Swagger acessível
- [ ] Categoria criada
- [ ] Endpoints funcionando
- [ ] Scripts testados

---

## 🎉 CONCLUSÃO

Você agora tem:

```
✅ API Spring Boot rodando no Render (FREE)
✅ MongoDB Atlas (FREE 512MB)
✅ URL pública HTTPS
✅ Swagger documentado
✅ Auto-deploy configurado
✅ Custo total: $0/mês 🎉
```

**Endpoints:**
- API: https://item-control-api.onrender.com
- Health: https://item-control-api.onrender.com/actuator/health
- Swagger: https://item-control-api.onrender.com/swagger-ui.html

**Próximos passos:**
1. Popular dados de teste
2. Testar todos os endpoints
3. Adicionar URL no README
4. Configurar ping (evitar cold start)
5. Migrar para Railway quando quiser

---

**🚀 Sistema em produção 100% GRÁTIS!**
