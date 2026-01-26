# 🎯 GUIA RÁPIDO - CONFIGURAR RENDER (Tela Atual)

**Você está na tela:** `https://dashboard.render.com/web/new`

---

## ⚠️ IMPORTANTE: RENDER NÃO TEM JAVA NATIVO!

**Render só oferece runtime nativo para:** Node/Bun, Python, Ruby, Go, Rust, Elixir

**Para Java/Spring Boot:** Use **Docker** (é o jeito correto!)

---

## ✅ PASSO A PASSO - PREENCHER FORMULÁRIO

### 1️⃣ Source Code
```
✅ JÁ PREENCHIDO: harlemsilvas / item-control-system
```

### 2️⃣ Name
```
item-control-api
```

### 3️⃣ Project (Optional)
```
(deixar vazio)
```

### 4️⃣ Language ✅ **DEIXAR DOCKER!**
```
✅ MANTER: Docker
```

**Por quê Docker?**
- Render NÃO tem runtime Java nativo
- Docker é a forma oficial/recomendada para Java/Spring Boot
- Nosso projeto já tem Dockerfile pronto!

### 5️⃣ Branch
```
deploy/render
```
⚠️ **MUITO IMPORTANTE:** Usar `deploy/render`!

### 6️⃣ Region
```
Oregon (US West)
```

### 7️⃣ Root Directory (Optional)
```
(deixar vazio)
```

---

## 🐳 CONFIGURAÇÃO DOCKER

Como estamos usando Docker, o Render mostrará campos específicos:

### 8️⃣ Dockerfile Path (já detectado automaticamente)
```
./Dockerfile
```
✅ Render detecta automaticamente - não precisa alterar

### 9️⃣ Docker Command (Opcional)
```
(deixar vazio - usaremos o CMD do Dockerfile)
```

---

## 💰 INSTANCE TYPE

### 10️⃣ Escolher Free Tier
```
☑️ Free (primeira opção - "For hobby projects")
```

**Características:**
- 512MB RAM
- 750 horas/mês
- Sleep após inatividade (normal)

---

## 🔐 ENVIRONMENT VARIABLES

### 11️⃣ Adicionar 3 Variáveis

Rolar até "Environment Variables" e adicionar:

**Variável 1:**
```
Key:   MONGODB_URI
Value: mongodb+srv://harlemclaumann:Harlem010101@cluster0.69j3tzl.mongodb.net/item_control_db?retryWrites=true&w=majority
```

**Variável 2:**
```
Key:   SPRING_PROFILES_ACTIVE
Value: prod
```

**Variável 3:**
```
Key:   PORT
Value: 10000
```

**Como adicionar:**
- Clicar em "Add Environment Variable" ou ícone "+"
- Preencher Key e Value
- Repetir 3 vezes

---

## 🏥 HEALTH CHECK

### 12️⃣ Health Check Path

Rolar até encontrar "Health Check Path" e preencher:

```
/actuator/health
```

---

## 🔄 AUTO-DEPLOY

### 13️⃣ Auto-Deploy

```
☑️ Deixar marcado (habilitado)
```

Render fará deploy automático quando você fizer push na branch.

---

## 🚀 FINALIZAR

### 14️⃣ Criar Web Service

Rolar até o final da página e clicar:

```
[Create Web Service] (botão azul)
```

---

## ✅ CHECKLIST RÁPIDO

Antes de clicar em "Create Web Service", confirme:

- [ ] **Name:** `item-control-api`
- [ ] **Language:** `Docker` ✅ (NÃO existe Java nativo!)
- [ ] **Branch:** `deploy/render`
- [ ] **Dockerfile Path:** `./Dockerfile` (detectado automaticamente)
- [ ] **Instance Type:** `Free`
- [ ] **MONGODB_URI:** adicionada (com `?retryWrites=true&w=majority`)
- [ ] **SPRING_PROFILES_ACTIVE:** `prod`
- [ ] **PORT:** `10000`
- [ ] **Health Check Path:** `/actuator/health`
- [ ] **Auto-Deploy:** marcado

---

## 🎯 O QUE NÃO PREENCHER

**Campos que podem aparecer mas NÃO precisam ser preenchidos:**

- ❌ **Secret Files** (não precisa)
- ❌ **Registry Credential** (não precisa - não usamos registry privado)
- ❌ **Docker Build Context Directory** (Render detecta automaticamente)
- ❌ **Docker Command** (já definido no Dockerfile)
- ❌ **Pre-Deploy Command** (opcional - não precisa agora)
- ❌ **Build Filters** (opcional - não precisa)

---

## ⏭️ PRÓXIMO PASSO

Após clicar em "Create Web Service":

1. ⏳ Render iniciará o build Docker (10-15 minutos na primeira vez)
2. 📊 Acompanhe os logs em tempo real
3. ✅ Aguarde mensagem "Your service is live"
4. 🧪 Teste a URL gerada

**Tempo de build esperado:**
- ✅ Primeira vez: 10-15 min (Maven baixa dependências)
- ✅ Rebuilds: 5-8 min (com cache)

---

**Boa sorte! 🚀**
