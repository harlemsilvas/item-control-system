# 🔧 Guia de Setup - Ambiente Local

## ✅ Pré-requisitos

1. **Java 21** (JDK instalado)
2. **Maven 3.9+**
3. **Docker Desktop** (para MongoDB local)

---

## 🚀 Setup Rápido

### 1️⃣ Iniciar Docker Desktop

Abra o Docker Desktop e aguarde ele iniciar completamente.

### 2️⃣ Iniciar MongoDB Local

```powershell
.\scripts\start-mongodb-local.ps1
```

**Isso vai:**
- Criar container MongoDB na porta 27017
- Database: `item_control_db_dev`

### 3️⃣ Iniciar Aplicação Spring Boot

```powershell
.\scripts\start-app-local.ps1
```

**Isso vai:**
- Carregar variáveis do `.env.local`
- Compilar o projeto
- Iniciar na porta 8080

### 4️⃣ Testar API

```powershell
# Health check
curl http://localhost:8080/actuator/health

# Listar categorias
curl http://localhost:8080/api/v1/categories

# Listar items
curl http://localhost:8080/api/v1/items
```

---

## 📋 Comandos Úteis

### MongoDB

```powershell
# Ver logs
docker logs -f mongodb

# Parar
docker stop mongodb

# Iniciar novamente
docker start mongodb

# Remover
docker rm -f mongodb

# Conectar via Mongo Shell
docker exec -it mongodb mongosh
```

### Aplicação

```powershell
# Matar processo na porta 8080
Get-NetTCPConnection -LocalPort 8080 | Select-Object -ExpandProperty OwningProcess | Stop-Process -Force

# Ver logs em tempo real
# (Os logs aparecem no terminal onde você executou start-app-local.ps1)
```

### Popular Dados de Teste

```powershell
# Popular banco LOCAL
.\scripts\populate-test-data.ps1

# Popular banco RENDER (deploy)
.\scripts\populate-test-data-deploy.ps1
```

---

## 🔐 Arquivos de Ambiente

### `.env.local` (Desenvolvimento)
```properties
MONGODB_URI=mongodb://localhost:27017/item_control_db_dev
PORT=8080
SPRING_PROFILES_ACTIVE=dev
```

### `.env.render` (Produção)
```properties
MONGODB_URI=mongodb+srv://...@cluster0.69j3tzl.mongodb.net/item_control_db
PORT=10000
SPRING_PROFILES_ACTIVE=prod
```

⚠️ **Atenção:** Esses arquivos estão no `.gitignore` e não devem ser commitados!

---

## 🐛 Troubleshooting

### Docker não inicia

**Problema:** `The system cannot find the file specified`

**Solução:**
1. Abra o Docker Desktop
2. Aguarde o ícone ficar verde
3. Execute novamente `.\scripts\start-mongodb-local.ps1`

### Porta 8080 em uso

**Solução:**
```powershell
Get-NetTCPConnection -LocalPort 8080 | Select-Object -ExpandProperty OwningProcess | Stop-Process -Force
```

### Erro de conexão MongoDB

**Problema:** `Connection refused` ou `Timeout`

**Solução:**
```powershell
# Verificar se MongoDB está rodando
docker ps | findstr mongodb

# Se não estiver, iniciar
docker start mongodb

# OU criar novamente
.\scripts\start-mongodb-local.ps1
```

### Compilação falha

**Problema:** Erros de compilação Maven

**Solução:**
```powershell
# Limpar e recompilar
cd modules/api
mvn clean install -DskipTests
```

---

## 📊 Estrutura de Ambientes

| Ambiente | MongoDB | API | Profile |
|----------|---------|-----|---------|
| **Local** | `localhost:27017` | `localhost:8080` | `dev` |
| **Render** | MongoDB Atlas | `https://item-control-system.onrender.com` | `localhost:10000` |

---

## 🎯 Próximos Passos

Depois de tudo funcionando local:

1. ✅ Popular dados de teste: `.\scripts\populate-test-data.ps1`
2. ✅ Iniciar Frontend: `cd frontend; npm run dev`
3. ✅ Acessar: `http://localhost:5173`

---

## 📝 Notas

- MongoDB local usa banco `item_control_db_dev`
- MongoDB Render usa banco `item_control_db`
- Frontend pode conectar em LOCAL ou RENDER via `.env`
