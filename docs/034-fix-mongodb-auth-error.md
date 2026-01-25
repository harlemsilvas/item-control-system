# 🔒 ERRO DE AUTENTICAÇÃO MONGODB ATLAS - SOLUÇÃO

**Data:** 2026-01-25  
**Erro:** `bad auth : authentication failed` (code 8000)  
**Causa:** Credenciais MongoDB Atlas incorretas ou expiradas

---

## ❌ ERRO IDENTIFICADO

```
com.mongodb.MongoCommandException: Command failed with error 8000 (AtlasError): 
'bad auth : authentication failed' on server 
ac-rgxa7yr-shard-00-02.69j3tzl.mongodb.net:27017
```

**Significado:**
- ❌ Usuário ou senha incorretos
- ❌ Credenciais expiradas
- ❌ Usuário não tem permissão no database

---

## 🔧 SOLUÇÃO: RESETAR SENHA MONGODB ATLAS

### Passo 1: Acessar MongoDB Atlas

```
https://cloud.mongodb.com
```

1. **Login** com sua conta
2. Selecionar projeto (se tiver múltiplos)

### Passo 2: Ir para Database Access

**No menu lateral esquerdo:**
1. Clicar em **"Database Access"**
2. Ver lista de usuários

### Passo 3: Editar Usuário

**Localizar usuário:** `harlemclaumann`

**Opções:**
1. Clicar em **"Edit"** (ícone lápis)
2. Ou clicar nos 3 pontinhos → **"Edit"**

### Passo 4: Resetar Senha

**Na tela de edição:**

1. Seção **"Password"**
2. Clicar em **"Edit Password"**
3. **IMPORTANTE:** Escolher uma das opções:

**OPÇÃO A: Auto-Generate (Recomendado)**
- Clicar em **"Autogenerate Secure Password"**
- MongoDB cria senha segura
- **COPIAR** a senha gerada ⚠️ (você não verá novamente!)
- Anotar em local seguro

**OPÇÃO B: Senha Manual**
- Digitar nova senha
- **Requisitos:**
  - Mínimo 8 caracteres
  - Letras e números
  - **SEM** caracteres especiais (`@`, `:`, `/`, `?`, `#`, `[`, `]`)
- **Exemplo bom:** `Harlem2026Pass`
- **Exemplo ruim:** `Harlem@2026!` (tem @ e !)

5. Clicar em **"Update User"**

### Passo 5: Criar Nova Connection String

**Com a nova senha:**

**Formato:**
```
mongodb+srv://harlemclaumann:NOVA_SENHA@cluster0.69j3tzl.mongodb.net/item_control_db?retryWrites=true&w=majority
```

**Substituir:**
- `NOVA_SENHA` pela senha que você acabou de criar
- **SEM espaços**
- **SEM caracteres especiais na senha**

**Exemplo:**
```
mongodb+srv://harlemclaumann:Harlem2026Pass@cluster0.69j3tzl.mongodb.net/item_control_db?retryWrites=true&w=majority
```

---

## 📝 ATUALIZAR CREDENCIAIS

### 1. Atualizar Arquivo Local (.env.render)

**Arquivo:**
```
C:\Users\harle\Desktop\java-estudos\Projeto-Harlem\item-control-system\.env.render
```

**Editar linha:**
```dotenv
MONGODB_URI=mongodb+srv://harlemclaumann:NOVA_SENHA_AQUI@cluster0.69j3tzl.mongodb.net/item_control_db?retryWrites=true&w=majority
```

**⚠️ IMPORTANTE:** NÃO comitar este arquivo (já está no .gitignore)

### 2. Atualizar no Render Dashboard

**Acessar:**
```
https://dashboard.render.com
```

1. Service: **`item-control-api`**
2. Menu lateral: **"Environment"**
3. Localizar: **`MONGODB_URI`**
4. Clicar em **"Edit"** (ícone lápis)
5. Colar nova connection string completa
6. Clicar em **"Save Changes"**

**Render irá:**
- Reiniciar o service automaticamente
- Aplicar nova variável
- Deploy novamente

### 3. Atualizar Frontend (.env)

**Arquivo:**
```
C:\Users\harle\Desktop\java-estudos\Projeto-Harlem\item-control-frontend\.env
```

**Não precisa alterar!** (Frontend não conecta direto no MongoDB)

---

## 🧪 TESTAR CONEXÃO

### Testar Localmente (Antes de Deploy)

**1. Testar Connection String:**

```powershell
# Usar mongosh (se instalado)
mongosh "mongodb+srv://harlemclaumann:NOVA_SENHA@cluster0.69j3tzl.mongodb.net/item_control_db"

# Deve conectar sem erro
```

**2. Testar com aplicação local:**

```powershell
# Atualizar variável de ambiente
$env:MONGODB_URI = "mongodb+srv://harlemclaumann:NOVA_SENHA@cluster0.69j3tzl.mongodb.net/item_control_db?retryWrites=true&w=majority"
$env:SPRING_PROFILES_ACTIVE = "prod"

# Rodar aplicação
cd C:\Users\harle\Desktop\java-estudos\Projeto-Harlem\item-control-system\modules\api
mvn spring-boot:run
```

**Deve iniciar sem erro de autenticação!**

### Testar no Render (Após Atualizar)

**Aguardar restart do service (2-3 minutos)**

```powershell
# Health Check
Invoke-RestMethod "https://item-control-api.onrender.com/actuator/health"

# Deve retornar: {"status":"UP"}
```

---

## 🔍 VERIFICAR PERMISSÕES DO USUÁRIO

### No MongoDB Atlas

**Database Access → harlemclaumann → Database User Privileges:**

**Deve ter:**
- ✅ **Role:** `readWrite` ou `dbAdmin`
- ✅ **Database:** `item_control_db` ou `admin`

**Se não tiver:**
1. Edit User
2. Database User Privileges
3. Adicionar:
   - **Built-in Role:** `readWrite`
   - **Database:** `item_control_db`
4. Update User

---

## 🌐 VERIFICAR NETWORK ACCESS

### No MongoDB Atlas

**Network Access → IP Access List:**

**Deve ter:**
- ✅ **0.0.0.0/0** (Allow access from anywhere)
- Ou IP do Render especificamente

**Se não tiver:**
1. Network Access
2. Add IP Address
3. **ALLOW ACCESS FROM ANYWHERE** (recomendado para Render)
4. IP Address: `0.0.0.0/0`
5. Comment: `Render Deploy`
6. Confirm

---

## 📋 CHECKLIST COMPLETO

### MongoDB Atlas

- [ ] Acessar MongoDB Atlas
- [ ] Database Access
- [ ] Editar usuário `harlemclaumann`
- [ ] Resetar senha (anotar nova senha!)
- [ ] Verificar permissões (readWrite em item_control_db)
- [ ] Network Access (0.0.0.0/0 permitido)

### Atualizar Credenciais

- [ ] Atualizar `.env.render` local
- [ ] Atualizar `MONGODB_URI` no Render Environment
- [ ] Aguardar restart do Render (2-3 min)

### Testar

- [ ] Testar localmente (opcional)
- [ ] Testar health check no Render
- [ ] Criar item de teste
- [ ] Verificar MongoDB Atlas (collections criadas)

---

## 🆘 TROUBLESHOOTING

### Erro Persiste Após Trocar Senha

**Verificar:**

1. **Senha tem caracteres especiais?**
   - ❌ Evite: `@`, `:`, `/`, `?`, `#`, `%`, `&`
   - ✅ Use: letras, números, underscore `_`

2. **Connection string está correta?**
   ```
   mongodb+srv://usuario:senha@cluster.mongodb.net/database?params
   ```
   - Verificar: usuário, senha, cluster, database

3. **Variável foi atualizada no Render?**
   - Environment → MONGODB_URI → Edit → Save

4. **Service foi reiniciado?**
   - Manual Deploy → Deploy latest commit

### Erro "User not found"

**Recriar usuário:**
1. Database Access → Add New Database User
2. Username: `harlemclaumann` (ou outro)
3. Password: escolher senha segura
4. User Privileges: readWrite @ item_control_db
5. Add User
6. Atualizar connection string com novo usuário/senha

---

## 💡 DICAS DE SEGURANÇA

### Boas Práticas

**Senha:**
- ✅ Mínimo 12 caracteres
- ✅ Letras maiúsculas e minúsculas
- ✅ Números
- ❌ Evitar caracteres especiais (causam problemas em URLs)
- ✅ Usar auto-generate do Atlas

**Usuário:**
- ✅ Criar usuário específico para cada app
- ✅ Dar apenas permissões necessárias (readWrite)
- ✅ Não usar usuário admin para apps

**Network:**
- ✅ 0.0.0.0/0 para serviços cloud (Render, Vercel)
- ✅ IP específico para dev local (mais seguro)

---

## 🎯 RESUMO EXECUTIVO

**Problema:** Senha MongoDB Atlas incorreta ou expirada

**Solução em 3 passos:**
1. ✅ MongoDB Atlas → Database Access → Edit User → Reset Password
2. ✅ Copiar nova senha e criar nova connection string
3. ✅ Render → Environment → Edit MONGODB_URI → Save

**Tempo:** 5-10 minutos

**Resultado:** Backend conectando no MongoDB com sucesso!

---

## 📞 PRÓXIMOS PASSOS

**Após resolver autenticação:**

1. ✅ Clear build cache no Render (se ainda necessário)
2. ✅ Aguardar deploy completar
3. ✅ Testar health check
4. ✅ Criar item de teste
5. 🎉 Sistema funcionando!

---

**AÇÃO IMEDIATA:**

1. **Resetar senha MongoDB Atlas**
2. **Anotar nova senha**
3. **Atualizar MONGODB_URI no Render**
4. **Aguardar restart**
5. **Testar!**

---

**Após fazer isso, me avise para testarmos juntos! 🚀**
