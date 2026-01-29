# Guia para visualizar MongoDB Railway

## 🎯 Opções para Visualizar MongoDB Railway

### ✅ **OPÇÃO 1: MongoDB Compass (RECOMENDADO - Interface Gráfica)**

#### Passo 1: Instalar MongoDB Compass
- Download: https://www.mongodb.com/try/download/compass
- Instale normalmente (Next, Next, Install)

#### Passo 2: Conectar ao Railway

**String de Conexão:**
```
mongodb://mongo:<Password>@hopper.proxy.rlwy.net:40930/item_control_db
```

**No MongoDB Compass:**
1. Abra MongoDB Compass
2. Cole a string de conexão
3. Clique em "Connect"
4. Navegue pelas coleções graficamente

**Vantagens:**
- ✅ Interface gráfica intuitiva
- ✅ Visualização de dados em tabela
- ✅ Editor de queries visual
- ✅ Não precisa de linha de comando

---

### ✅ **OPÇÃO 2: MongoDB Shell (mongosh) - Via Script**

#### Passo 1: Instalar mongosh
- Download: https://www.mongodb.com/try/download/shell
- Extrair e adicionar ao PATH (ou usar instalador)

#### Passo 2: Usar Scripts Criados

**Verificação Completa (lista coleções + amostras):**
```powershell
.\scripts\check-railway-mongodb.ps1
```

**Conexão Interativa:**
```powershell
.\scripts\connect-railway-mongodb.ps1
```

---

### ✅ **OPÇÃO 3: Via API (Teste HTTP)**

**Testar se há dados via API do projeto:**

```powershell
# 1. Iniciar API em modo produção
.\scripts\quick-start-prod.ps1

# 2. Em outro terminal, testar endpoints
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/items" -Method GET
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/categories" -Method GET
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/alerts" -Method GET
```

---

### ✅ **OPÇÃO 4: Via Railway Dashboard**

1. Acesse: https://railway.app
2. Login na sua conta
3. Selecione o projeto MongoDB
4. Clique em "Data" ou "MongoDB"
5. Visualize coleções e dados

---

## 📊 Comandos Úteis no mongosh

Após conectar com `connect-railway-mongodb.ps1`:

### Listar Coleções
```javascript
show collections
```

### Contar Documentos
```javascript
db.items.countDocuments()
db.events.countDocuments()
db.alerts.countDocuments()
db.categories.countDocuments()
db.rules.countDocuments()
```

### Ver Todos os Documentos
```javascript
db.items.find()
db.events.find()
```

### Ver com Formatação
```javascript
db.items.find().pretty()
```

### Ver Primeiros 5
```javascript
db.items.find().limit(5)
```

### Buscar por Campo
```javascript
db.items.find({ userId: "user123" })
```

### Ver Estrutura de um Documento
```javascript
db.items.findOne()
```

---

## 🔍 Verificar se Dados Foram Criados

### Script Rápido de Verificação
```powershell
# Testar via API (com API rodando)
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/items" -Method GET | ConvertTo-Json

# OU usar script de verificação
.\scripts\check-railway-mongodb.ps1
```

---

## 📝 Estrutura Esperada

Coleções que devem existir após usar a API:

- ✅ `items` - Itens cadastrados
- ✅ `events` - Eventos registrados
- ✅ `alerts` - Alertas gerados
- ✅ `categories` - Categorias
- ✅ `rules` - Regras configuradas

---

## 🆘 Troubleshooting

### "mongosh not found"
```powershell
# Instale MongoDB Shell
# Download: https://www.mongodb.com/try/download/shell
```

### "Connection refused"
- Verifique se a URL está correta
- Verifique firewall/proxy
- Teste conectividade de rede

### "Authentication failed"
- Verifique usuário e senha
- Verifique se banco existe no Railway

---

## 🎯 Recomendação

Para visualização rápida e fácil:
1. ✅ **MongoDB Compass** (interface gráfica)

Para scripts e automação:
2. ✅ **Scripts PowerShell** (check-railway-mongodb.ps1)

Para desenvolvimento:
3. ✅ **Via API** (testar endpoints REST)

