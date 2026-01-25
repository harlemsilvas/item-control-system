# 🔥 PROBLEMA RENDER - CÓDIGO ANTIGO EM CACHE

**Data:** 2026-01-25  
**Status:** ⚠️ RENDER USANDO CÓDIGO DESATUALIZADO

---

## ❌ SITUAÇÃO ATUAL

**Erro no Render:**
```
Failed looking up SRV record for '_mongodb._tcp.cluster0.69j3tzl.mongodb.net'
Error creating bean with name 'databaseAdminController': Invocation of init method failed
```

**Causa:**
- Render está usando **código antigo** (com `@PostConstruct`)
- Mesmo após commit e push da correção
- Build cache do Render não foi limpo

---

## ✅ CÓDIGO LOCAL ESTÁ CORRETO

**Arquivo:** `DatabaseAdminController.java`

```java
// ✅ CORRETO - Sem @PostConstruct
public class DatabaseAdminController {
    private final MongoTemplate mongoTemplate;
    
    // Collections são criadas automaticamente pelo Spring Data MongoDB
    // quando o primeiro documento é salvo. Não é necessário criar manualmente.
    
    @GetMapping("/collections")
    // ... endpoints funcionam normalmente
}
```

**Commits realizados:**
- ✅ `fix: Remover @PostConstruct que causava falha no deploy Render`
- ✅ Push para `deploy/render`
- ✅ Código no GitHub está correto

---

## 🔧 SOLUÇÃO: CLEAR BUILD CACHE NO RENDER

### Passo 1: Acessar Render Dashboard

```
https://dashboard.render.com
```

### Passo 2: Selecionar Service

1. Clicar em **"item-control-api"** (seu service)
2. Ir para aba **"Manual Deploy"** (menu lateral)

### Passo 3: Clear Build Cache e Redeploy

**OPÇÃO A: Manual Deploy com Clear Cache** ⭐ (RECOMENDADO)

1. No menu lateral, clicar em **"Manual Deploy"**
2. Clicar em **"Clear build cache & deploy"**
3. Aguardar novo build (10-15 min)

**OPÇÃO B: Redeploy Normal** (se opção A não funcionar)

1. Clicar em **"Deploy latest commit"**
2. Se falhar novamente, repetir com "Clear build cache"

### Passo 4: Acompanhar Logs

1. Ir para aba **"Logs"**
2. Procurar por:
   - ✅ `Started ItemControlApplication` ← BOM!
   - ❌ `Invocation of init method failed` ← RUIM (código antigo)

---

## 🔍 COMO VERIFICAR SE CORRIGIU

### No Render Logs, após deploy:

**✅ SUCESSO - Deve aparecer:**
```
==> Building Docker image
==> Image built successfully
==> Starting service
2026-01-25 ... Started ItemControlApplication in 8.2 seconds
==> Your service is live 🎉
```

**❌ FALHA - NÃO deve aparecer:**
```
Exception while resolving SRV records
Failed looking up SRV record
Invocation of init method failed
```

### Testar Health Check:

```powershell
Invoke-RestMethod -Uri "https://item-control-api.onrender.com/actuator/health"
```

Deve retornar: `{"status":"UP"}`

---

## 📋 ALTERNATIVA: DELETAR E RECRIAR SERVICE

Se "Clear build cache" não funcionar:

### 1. Deletar Service Atual

1. Render Dashboard → service `item-control-api`
2. Settings (menu lateral) → rolar até final
3. Clicar **"Delete Web Service"**
4. Confirmar exclusão

### 2. Criar Novo Service

Seguir tutorial: `docs/027-render-form-quick-guide.md`

**Configuração:**
- Name: `item-control-api`
- Language: `Docker`
- Branch: `deploy/render`
- Region: Oregon
- Instance: Free

**Environment Variables:**
```
MONGODB_URI=mongodb+srv://harlemclaumann:xAsYVqpaNzGLJq80@cluster0.69j3tzl.mongodb.net/item_control_db?retryWrites=true&w=majority
SPRING_PROFILES_ACTIVE=prod
PORT=10000
```

**Health Check Path:** `/actuator/health`

---

## 🎯 VERIFICAÇÃO FINAL

Após redeploy bem-sucedido:

### 1. Health Check
```powershell
Invoke-RestMethod "https://item-control-api.onrender.com/actuator/health"
# Retorno: {"status":"UP"}
```

### 2. Criar Item
```powershell
$body = @{
    name = "Teste Render Fix"
    nickname = "fix-001"
    template = "GENERAL"
} | ConvertTo-Json

Invoke-RestMethod "https://item-control-api.onrender.com/api/v1/items" `
    -Method POST -Body $body -ContentType "application/json"
```

### 3. Verificar MongoDB Atlas
```
1. https://cloud.mongodb.com
2. Database → Browse Collections
3. Database: item_control_db
4. Collection: items ← deve aparecer!
```

---

## 📊 DIAGNÓSTICO

| Item | Status Local | Status Render |
|------|--------------|---------------|
| **Código** | ✅ Sem @PostConstruct | ❌ Com @PostConstruct (cache) |
| **GitHub** | ✅ Correto | ✅ Correto |
| **Build** | ✅ OK | ❌ Usando cache antigo |
| **Deploy** | N/A | ❌ Falhando |

**Conclusão:** Render precisa fazer **clean build** para pegar código novo!

---

## 🔄 TIMELINE ESPERADA

```
1. Clear build cache & deploy ⏱️ 0 min
   ↓
2. Render clona código novo ⏱️ 1 min
   ↓
3. Build Docker (sem cache) ⏱️ 10-12 min
   ↓
4. Start container ⏱️ 1 min
   ↓
5. App inicializa SEM erro ⏱️ 30s
   ↓
6. Health check OK ✅
   ↓
7. Service LIVE! 🎉
```

**Total:** ~12-15 minutos

---

## ⚠️ IMPORTANTE

**NÃO fazer:**
- ❌ Novos commits sem necessidade
- ❌ Alterar variáveis de ambiente (já estão corretas)
- ❌ Trocar branch

**FAZER:**
- ✅ Clear build cache & deploy
- ✅ Aguardar build completo
- ✅ Ver logs até "Started ItemControlApplication"

---

## 📞 SE AINDA FALHAR

### Opção 1: Verificar Connection String

No Render, Environment Variables, verificar se `MONGODB_URI` está:
```
mongodb+srv://harlemclaumann:xAsYVqpaNzGLJq80@cluster0.69j3tzl.mongodb.net/item_control_db?retryWrites=true&w=majority
```

### Opção 2: Testar Connection String Localmente

```powershell
# Testar se connection string funciona
$env:MONGODB_URI = "mongodb+srv://harlemclaumann:xAsYVqpaNzGLJq80@cluster0.69j3tzl.mongodb.net/item_control_db?retryWrites=true&w=majority"
$env:SPRING_PROFILES_ACTIVE = "prod"

cd modules/api
mvn spring-boot:run
```

Se funcionar local, problema é 100% cache do Render.

### Opção 3: Criar Issue no Render

Se nada funcionar:
1. Render Dashboard → Help
2. Reportar: "Build cache não está sendo limpo"
3. Mencionar commit hash do fix

---

## ✅ AÇÃO IMEDIATA

**Execute agora no Render:**

```
1. Dashboard → item-control-api
2. Manual Deploy → "Clear build cache & deploy"
3. Aguardar 12-15 min
4. Verificar logs: "Started ItemControlApplication"
```

---

**Render precisa fazer clean build para aplicar o fix! 🔧**
