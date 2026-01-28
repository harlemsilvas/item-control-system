# 🚀 DEPLOY NO RAILWAY - PASSO A PASSO

**Sistema:** Item Control System  
**Stack:** Spring Boot + MongoDB  
**Custo:** $5/mês (Railway) + $0/mês (MongoDB Atlas)

---

## ✅ PRÉ-REQUISITOS

- [x] Conta GitHub (já tem)
- [x] Repositório no GitHub (já tem)
- [x] Código commitado e com push
- [x] Dockerfile criado ✅
- [x] railway.json criado ✅

---

## 📋 PASSO 1: CRIAR CONTA MONGODB ATLAS (5 min)

### 1.1 Acessar MongoDB Atlas
```
🌐 https://www.mongodb.com/cloud/atlas/register
```

### 1.2 Criar Conta Gratuita
- Usar email ou Google/GitHub
- **NÃO precisa cartão de crédito**

### 1.3 Criar Cluster Gratuito
1. Clicar em "Build a Database"
2. Escolher **M0 FREE**
3. Provider: **AWS**
4. Region: **US East (N. Virginia)** ou mais próximo
5. Cluster Name: `item-control-cluster`
6. Clicar "Create"

### 1.4 Configurar Acesso
1. **Database Access:**
   - Username: `itemcontrol`
   - Password: `gerar senha forte` (copiar!)
   - Built-in Role: **Atlas Admin**

2. **Network Access:**
   - Clicar "Add IP Address"
   - Escolher **"Allow Access from Anywhere"** (0.0.0.0/0)
   - Confirm

### 1.5 Copiar Connection String
1. Clicar "Connect" no cluster
2. Escolher "Connect your application"
3. Driver: **Java**, Version: **4.11 or later**
4. Copiar a string:
```
mongodb+srv://itemcontrol:<password>@item-control-cluster.xxxxx.mongodb.net/?retryWrites=true&w=majority
```

5. **IMPORTANTE:** Substituir `<password>` pela senha real!

**✅ MongoDB Atlas configurado!**

---

## 📋 PASSO 2: FAZER DEPLOY NO RAILWAY (10 min)

### 2.1 Acessar Railway
```
🌐 https://railway.app
```

### 2.2 Login com GitHub
- Clicar "Login with GitHub"
- Autorizar Railway

### 2.3 Criar Novo Projeto
1. Clicar "New Project"
2. Escolher "Deploy from GitHub repo"
3. Selecionar: **item-control-system**
4. Aguardar Railway detectar o Dockerfile

### 2.4 Configurar Variáveis de Ambiente

Clicar na aba **"Variables"** e adicionar:

```bash
# Profile Spring Boot
SPRING_PROFILES_ACTIVE=prod

# MongoDB Connection (copiar do Atlas)
MONGODB_URI=mongodb+srv://itemcontrol:SUA_SENHA_AQUI@item-control-cluster.xxxxx.mongodb.net/item_control_db?retryWrites=true&w=majority

# Porta (Railway define automaticamente, mas pode setar)
PORT=8080
```

**⚠️ IMPORTANTE:** 
- Substituir `SUA_SENHA_AQUI` pela senha do MongoDB Atlas
- Adicionar `/item_control_db` no final da URL

### 2.5 Fazer Deploy

1. Railway vai detectar mudanças automaticamente
2. Clicar em "Deploy"
3. Aguardar build (5-10 minutos)
4. Acompanhar logs em tempo real

### 2.6 Obter URL Pública

1. Ir em **Settings**
2. Clicar em "Generate Domain"
3. Railway vai gerar algo como:
```
https://item-control-system-production.up.railway.app
```

**✅ Deploy completo!**

---

## 🧪 PASSO 3: TESTAR A APLICAÇÃO (5 min)

### 3.1 Health Check

Abrir no navegador ou usar curl:

```bash
# Health check
curl https://sua-url.railway.app/actuator/health

# Deve retornar:
{"status":"UP"}
```

### 3.2 Testar Endpoints

```powershell
# Definir URL base
$baseUrl = "https://sua-url.railway.app"

# 1. Criar uma categoria
$body = @{
    userId = "550e8400-e29b-41d4-a716-446655440001"
    name = "Veículos"
} | ConvertTo-Json

Invoke-RestMethod -Uri "$baseUrl/api/v1/categories" `
    -Method POST `
    -ContentType "application/json" `
    -Body $body

# 2. Listar categorias
Invoke-RestMethod -Uri "$baseUrl/api/v1/categories?userId=550e8400-e29b-41d4-a716-446655440001"

# 3. Criar um item
$item = @{
    userId = "550e8400-e29b-41d4-a716-446655440001"
    name = "Meu Carro"
    nickname = "corolla-2020"
    templateCode = "VEHICLE"
    metadata = @{
        brand = "Toyota"
        model = "Corolla"
        year = 2020
    }
} | ConvertTo-Json

Invoke-RestMethod -Uri "$baseUrl/api/v1/items" `
    -Method POST `
    -ContentType "application/json" `
    -Body $item

# 4. Listar items
Invoke-RestMethod -Uri "$baseUrl/api/v1/items?userId=550e8400-e29b-41d4-a716-446655440001"
```

### 3.3 Acessar Swagger UI

```
🌐 https://sua-url.railway.app/swagger-ui.html
```

**✅ Aplicação funcionando em produção!**

---

## 📊 PASSO 4: POPULAR DADOS DE TESTE (Opcional)

### 4.1 Atualizar Script de População

Editar `scripts/populate-categories.ps1`:

```powershell
# Mudar URL base
$baseUrl = "https://sua-url.railway.app/api/v1"

# Resto do script igual
```

### 4.2 Executar Script

```powershell
.\scripts\populate-categories.ps1
```

**✅ Dados de teste populados!**

---

## 🔍 PASSO 5: MONITORAR E LOGS

### 5.1 Ver Logs no Railway

1. Abrir projeto no Railway
2. Clicar na aba "Deployments"
3. Selecionar deployment ativo
4. Ver logs em tempo real

### 5.2 Verificar Métricas

1. Aba "Metrics"
2. Ver:
   - CPU usage
   - Memory usage
   - Network traffic

### 5.3 MongoDB Atlas Monitoring

1. Abrir MongoDB Atlas
2. Ir em "Metrics"
3. Ver:
   - Connections
   - Operations
   - Storage

---

## ⚙️ CONFIGURAÇÕES AVANÇADAS

### Domínio Personalizado (Opcional - Pago)

1. Railway Settings
2. Custom Domain
3. Adicionar seu domínio
4. Configurar DNS

### Escalar Aplicação

1. Railway Settings
2. Replicas: aumentar número
3. Save

### Backups MongoDB

1. MongoDB Atlas
2. Backups (automático no free tier)
3. Restore quando necessário

---

## 🐛 TROUBLESHOOTING

### Erro: "Application failed to start"

**Verificar:**
1. Logs no Railway
2. Variável `MONGODB_URI` correta
3. MongoDB Atlas permite conexão (Network Access)
4. Senha correta na connection string

**Solução:**
```bash
# Testar connection string localmente primeiro
# Adicionar em application-prod.yml:
spring:
  data:
    mongodb:
      uri: ${MONGODB_URI}
```

### Erro: "Connection timeout"

**Verificar:**
1. MongoDB Atlas → Network Access
2. Deve ter 0.0.0.0/0 permitido
3. Ou adicionar IPs do Railway

### Erro: "Build failed"

**Verificar:**
1. Dockerfile correto
2. Código compila localmente: `mvn clean package`
3. Java 21 especificado

**Solução:**
```bash
# Testar build local com Docker:
docker build -t item-control-test .
docker run -p 8080:8080 item-control-test
```

---

## 📝 CHECKLIST FINAL

### Antes do Deploy
- [x] MongoDB Atlas criado
- [x] Connection string copiada
- [x] Código commitado no GitHub
- [x] Dockerfile criado
- [x] railway.json criado

### Durante o Deploy
- [ ] Projeto criado no Railway
- [ ] Variáveis de ambiente configuradas
- [ ] Deploy iniciado
- [ ] Build completado com sucesso

### Após o Deploy
- [ ] Health check funcionando
- [ ] Endpoints testados
- [ ] Swagger acessível
- [ ] Dados de teste populados
- [ ] URL documentada

---

## 🎉 PRONTO!

Sua aplicação está rodando em produção:

```
✅ Backend: https://sua-url.railway.app
✅ MongoDB: Atlas (512MB grátis)
✅ Swagger: https://sua-url.railway.app/swagger-ui.html
✅ Health: https://sua-url.railway.app/actuator/health
```

**Custos:**
- Railway: $5/mês
- MongoDB Atlas: $0/mês
- **Total: $5/mês**

**Próximos passos:**
1. Adicionar URL no README.md
2. Testar todos os endpoints
3. Configurar CI/CD (GitHub Actions)
4. Adicionar monitoramento
5. Criar frontend (opcional)

---

**🚀 Sistema em produção e funcionando!**
