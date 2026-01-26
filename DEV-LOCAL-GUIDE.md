# 🚀 Guia Rápido - Desenvolvimento Local

## 📋 Arquitetura

```
┌─────────────────────────────────────────────┐
│  🎨 FRONTEND (React + Vite)                 │
│  http://localhost:5173                      │
│  ✅ Rodando LOCALMENTE (fora do Docker)     │
└─────────────────┬───────────────────────────┘
                  │ HTTP
                  ▼
┌─────────────────────────────────────────────┐
│  🚀 BACKEND API (Spring Boot)               │
│  http://localhost:8080                      │
│  ✅ Rodando LOCALMENTE (fora do Docker)     │
└─────────────────┬───────────────────────────┘
                  │ MongoDB Driver
                  ▼
┌─────────────────────────────────────────────┐
│  🐳 MONGODB (Docker Container)              │
│  mongodb://localhost:27017                  │
│  ✅ Rodando em DOCKER                       │
└─────────────────────────────────────────────┘
```

---

## 🎯 Início Rápido

### Opção 1: Iniciar TUDO de uma vez

```powershell
.\scripts\start-all-dev.ps1
```

✅ Isso inicia automaticamente:
1. MongoDB Docker
2. Backend API (nova janela)
3. Frontend (nova janela)

---

### Opção 2: Iniciar componentes separadamente

#### 1️⃣ MongoDB Docker (obrigatório primeiro)

```powershell
.\scripts\start-mongodb-docker.ps1
```

**Resultado:**
- ✅ Container `item-control-mongo-dev` rodando
- 📍 Disponível em: `mongodb://localhost:27017`
- 👤 User: `admin` / Senha: `admin123`
- 💾 Database: `item_control_db_dev`

---

#### 2️⃣ Backend API (local)

```powershell
.\scripts\start-backend-dev.ps1
```

**Resultado:**
- ✅ Spring Boot rodando localmente
- 📍 API: http://localhost:8080
- 📊 Swagger: http://localhost:8080/swagger-ui.html
- ❤️ Health: http://localhost:8080/actuator/health

**Opções:**
```powershell
# Pular compilação (mais rápido)
.\scripts\start-backend-dev.ps1 -SkipBuild
```

---

#### 3️⃣ Frontend (local)

```powershell
.\scripts\start-frontend-dev.ps1
```

**Resultado:**
- ✅ Vite Dev Server rodando
- 📍 Frontend: http://localhost:5173
- 🔥 Hot Module Replacement ativo
- 🔗 Conectado à API: http://localhost:8080

---

## 🛑 Parar Ambiente

### Opção 1: Parar TUDO de uma vez

```powershell
.\scripts\stop-all-dev.ps1
```

---

### Opção 2: Parar componentes manualmente

**Frontend/Backend:**
- Pressione `Ctrl + C` na janela do PowerShell

**MongoDB Docker:**
```powershell
docker-compose -f docker-compose.mongodb.yml down
```

---

## 🔍 Verificar Status

### MongoDB Docker

```powershell
docker ps --filter "name=item-control-mongo-dev"
```

### Backend API

```powershell
# Via browser
http://localhost:8080/actuator/health

# Via PowerShell
Invoke-WebRequest -Uri "http://localhost:8080/actuator/health"
```

### Frontend

```powershell
# Via browser
http://localhost:5173
```

---

## 📊 URLs Importantes

| Serviço | URL | Descrição |
|---------|-----|-----------|
| 🎨 Frontend | http://localhost:5173 | Interface React |
| 🚀 Backend API | http://localhost:8080 | REST API |
| 📚 Swagger UI | http://localhost:8080/swagger-ui.html | Documentação API |
| ❤️ Health Check | http://localhost:8080/actuator/health | Status da aplicação |
| 🐳 MongoDB | mongodb://localhost:27017 | Database |
| 🗄️ Collections | http://localhost:8080/api/v1/admin/database/collections | Admin endpoint |

---

## 🔧 Comandos Úteis MongoDB

### Acessar MongoDB Shell

```powershell
docker exec -it item-control-mongo-dev mongosh -u admin -p admin123
```

### Ver Collections

```javascript
use item_control_db_dev
show collections
```

### Query de teste

```javascript
db.items.find().pretty()
db.categories.find().pretty()
```

### Limpar dados

```javascript
db.items.deleteMany({})
db.events.deleteMany({})
db.alerts.deleteMany({})
db.categories.deleteMany({})
```

---

## 🐛 Troubleshooting

### Porta 8080 em uso

```powershell
# Ver o que está usando a porta
Get-NetTCPConnection -LocalPort 8080

# Matar processo
$process = Get-NetTCPConnection -LocalPort 8080
Stop-Process -Id $process.OwningProcess -Force
```

### MongoDB não conecta

```powershell
# Reiniciar MongoDB
docker-compose -f docker-compose.mongodb.yml restart

# Ver logs
docker logs item-control-mongo-dev
```

### Backend não compila

```powershell
# Limpar e rebuild
cd modules\api
mvn clean install -DskipTests
```

### Frontend não carrega

```powershell
cd ..\item-control-frontend

# Reinstalar dependências
Remove-Item -Recurse -Force node_modules
npm install

# Limpar cache
npm run dev -- --force
```

---

## 📁 Estrutura de Arquivos

```
item-control-system/
├── docker-compose.mongodb.yml       ← MongoDB Docker config
├── scripts/
│   ├── start-all-dev.ps1           ← Inicia TUDO
│   ├── stop-all-dev.ps1            ← Para TUDO
│   ├── start-mongodb-docker.ps1    ← Só MongoDB
│   ├── start-backend-dev.ps1       ← Só Backend
│   └── start-frontend-dev.ps1      ← Só Frontend
└── modules/
    └── api/
        └── src/main/resources/
            └── application-dev.yml  ← Config dev

item-control-frontend/
├── .env.development                ← Config Vite dev
└── src/
```

---

## ⚡ Workflow Recomendado

### 1. Primeira vez (setup completo)

```powershell
# Iniciar tudo
.\scripts\start-all-dev.ps1

# Aguardar tudo subir (~1-2 minutos)
# Acessar http://localhost:5173
```

---

### 2. Desenvolvimento diário

```powershell
# Manhã: Iniciar MongoDB
.\scripts\start-mongodb-docker.ps1

# Iniciar Backend (em nova janela)
.\scripts\start-backend-dev.ps1 -SkipBuild

# Iniciar Frontend (em nova janela)
.\scripts\start-frontend-dev.ps1

# Trabalhar...

# Fim do dia: Parar tudo
.\scripts\stop-all-dev.ps1
```

---

### 3. Fazer mudanças no código

**Backend:**
- ✅ Hot reload ativo (Spring DevTools)
- 🔄 Mudanças em `.java` recarregam automaticamente
- 📦 Se adicionar dependência: rebuild com `mvn clean install`

**Frontend:**
- ✅ Hot Module Replacement (HMR) do Vite
- 🔥 Mudanças em `.tsx/.ts/.css` refletem instantaneamente
- 🔄 Se adicionar dependência: `npm install <package>`

---

## 🎓 Próximos Passos

1. ✅ Ambiente dev configurado
2. ✅ MongoDB + Backend + Frontend rodando
3. 🔜 Popular dados de teste
4. 🔜 Desenvolver features
5. 🔜 Deploy em produção

---

## 📞 Ajuda

**Ver logs detalhados:**

```powershell
# MongoDB
docker logs -f item-control-mongo-dev

# Backend
# (já aparece no console onde rodou)

# Frontend
# (já aparece no console onde rodou)
```

**Resetar ambiente completamente:**

```powershell
# Parar tudo
.\scripts\stop-all-dev.ps1

# Remover volumes MongoDB
docker-compose -f docker-compose.mongodb.yml down -v

# Limpar builds
cd modules\api
mvn clean

cd ..\..\item-control-frontend
Remove-Item -Recurse -Force node_modules

# Recomeçar
cd ..\item-control-system
.\scripts\start-all-dev.ps1
```

---

✅ **Ambiente pronto para desenvolvimento!** 🚀
