# 🔧 Troubleshooting - Erro de Autenticação MongoDB Railway

**Erro:** `Authentication failed` ao conectar no MongoDB Railway

**Data:** 2026-01-23

---

## 🔍 Problema Identificado

```
com.mongodb.MongoCommandException: Command failed with error 18 (AuthenticationFailed): 
'Authentication failed.' on server hopper.proxy.rlwy.net:40930
```

**URL usada:**
```
mongodb://mongo:<Password>@hopper.proxy.rlwy.net:40930/item_control_db
```

---

## ✅ Soluções Aplicadas

### 1. **Separar Database da URI**

MongoDB Railway pode exigir que o database seja especificado separadamente.

**Antes:**
```yaml
uri: mongodb://mongo:senha@host:porta/item_control_db
```

**Depois:**
```yaml
uri: mongodb://mongo:senha@host:porta
database: item_control_db
```

**Arquivo alterado:** `modules/api/src/main/resources/application-prod.yml`

---

## 🧪 Possíveis Causas e Soluções

### Causa 1: Credenciais Incorretas

**Sintomas:**
- Erro de autenticação imediato
- Mensagem "Authentication failed"

**Solução:**
1. Acesse Railway Dashboard: https://railway.app
2. Selecione seu projeto MongoDB
3. Vá em **Variables** ou **Connect**
4. Copie as credenciais EXATAS:
   - `MONGOUSER`
   - `MONGOPASSWORD`
   - `MONGOHOST`
   - `MONGOPORT`
   - `MONGO_PUBLIC_URL`

5. Compare com a URL em `application-prod.yml`

**Formato correto:**
```
mongodb://USUARIO:SENHA@HOST:PORTA
```

---

### Causa 2: Database Não Existe

**Sintomas:**
- Autenticação funciona mas falha ao acessar database
- Erro 18 (AuthenticationFailed)

**Solução:**

**Opção A: Criar database primeiro (RECOMENDADO)**

1. Conecte ao Railway SEM especificar database:
   ```
   mongodb://mongo:senha@hopper.proxy.rlwy.net:40930
   ```

2. No MongoDB Compass ou mongosh, crie o database:
   ```javascript
   use item_control_db
   db.createCollection("init")
   ```

3. Depois use a aplicação normalmente

**Opção B: Deixar a aplicação criar**

Configure para conectar sem database e deixar Spring criar:
```yaml
uri: mongodb://mongo:senha@hopper.proxy.rlwy.net:40930
database: item_control_db
```

---

### Causa 3: authSource Incorreto

**Sintomas:**
- Credenciais corretas mas ainda falha
- MongoDB requer autenticação em database admin

**Solução:**

Adicione `authSource=admin` na URI:

```yaml
uri: mongodb://mongo:senha@hopper.proxy.rlwy.net:40930?authSource=admin
database: item_control_db
```

---

### Causa 4: Formato da Senha

**Sintomas:**
- Senha contém caracteres especiais (@, :, /, etc)

**Solução:**

Encode a senha usando URL encoding:
- `@` → `%40`
- `:` → `%3A`
- `/` → `%2F`

**Exemplo:**
```
Senha: p@ss:word
Encoded: p%40ss%3Aword
```

---

## 🔧 Como Testar Conexão

### Método 1: MongoDB Compass (RECOMENDADO)

1. **Instale:** https://www.mongodb.com/try/download/compass

2. **Teste estas connection strings na ordem:**

   **Teste 1 - Sem database:**
   ```
   mongodb://mongo:<Password>@hopper.proxy.rlwy.net:40930
   ```

   **Teste 2 - Com database:**
   ```
   mongodb://mongo:<Password>@hopper.proxy.rlwy.net:40930/item_control_db
   ```

   **Teste 3 - Com authSource:**
   ```
   mongodb://mongo:<Password>@hopper.proxy.rlwy.net:40930/item_control_db?authSource=admin
   ```

3. **Se algum conectar:** Use esse formato no `application-prod.yml`

---

### Método 2: mongosh (Terminal)

```bash
# Teste 1 - Sem database
mongosh "mongodb://mongo:<Password>@hopper.proxy.rlwy.net:40930"

# Teste 2 - Com database
mongosh "mongodb://mongo:<Password>@hopper.proxy.rlwy.net:40930/item_control_db"

# Teste 3 - Com authSource
mongosh "mongodb://mongo:<Password>@hopper.proxy.rlwy.net:40930/item_control_db?authSource=admin"
```

---

### Método 3: Script de Diagnóstico

```powershell
.\scripts\diagnostico-railway.ps1
```

Este script:
- Mostra configuração atual
- Lista possíveis problemas
- Fornece strings de teste
- Guia para verificar no Railway

---

## 📋 Checklist de Verificação

Antes de testar novamente, verifique:

- [ ] Credenciais copiadas EXATAMENTE do Railway Dashboard
- [ ] Senha não contém caracteres especiais não encoded
- [ ] Database existe no MongoDB Railway (ou remova da URI)
- [ ] Porta 40930 não está bloqueada por firewall
- [ ] Conexão de internet funcionando
- [ ] Railway service está ativo (não em sleep)

---

## 🚀 Próximos Passos

### Passo 1: Testar com MongoDB Compass

Use MongoDB Compass para validar qual formato de URL funciona.

### Passo 2: Atualizar application-prod.yml

Use o formato que funcionou no Compass.

### Passo 3: Recompilar

```powershell
mvn clean package -DskipTests
```

### Passo 4: Testar

```powershell
.\scripts\quick-start-prod.ps1
```

---

## 📝 Formatos de URI Testados

| Formato | Status | Observações |
|---------|--------|-------------|
| `mongodb://user:pass@host:port/db` | ❌ Falhou | Formato original com database na URI |
| `mongodb://user:pass@host:port` + `database: db` | ✅ FUNCIONA | **Testado no Compass - OK** |
| `mongodb://user:pass@host:port/db?authSource=admin` | 🔄 Não testado | Se precisar authSource |
| `mongodb://user:pass@host:port?authSource=admin` + `database: db` | 🔄 Não testado | Combinação |

**✅ CONFIRMADO:** URL `mongodb://mongo:<Password>@hopper.proxy.rlwy.net:40930` conecta com sucesso no MongoDB Compass.

**⚠️ PRÓXIMO PASSO:** Collections serão criadas automaticamente pela aplicação Spring Boot ao salvar primeiro documento.

---

## 🆘 Se Nada Funcionar

1. **Recrie o MongoDB no Railway**
   - Delete o service atual
   - Crie novo MongoDB
   - Copie novas credenciais

2. **Use MongoDB Atlas** (alternativa ao Railway)
   - Mais estável
   - Free tier disponível
   - https://www.mongodb.com/cloud/atlas/register

3. **Use MongoDB local** (para desenvolvimento)
   ```powershell
   docker-compose up -d
   .\scripts\quick-start.ps1
   ```

---

**Última atualização:** 2026-01-23  
**Status:** Correção aplicada, aguardando teste

