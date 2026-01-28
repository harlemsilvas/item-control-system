# ✅ Checklist de Validação - Setup Dev Local

Use este checklist para validar que tudo está funcionando corretamente.

---

## 📋 Pré-requisitos

### Softwares Instalados

- [ ] **Docker Desktop** instalado e rodando
  ```powershell
  docker --version
  # Deve retornar algo como: Docker version 24.x.x
  ```

- [ ] **Java 21** instalado
  ```powershell
  java -version
  # Deve retornar: openjdk version "21.x.x"
  ```

- [ ] **Maven** instalado
  ```powershell
  mvn -version
  # Deve retornar: Apache Maven 3.x.x
  ```

- [ ] **Node.js** instalado (v20+)
  ```powershell
  node --version
  # Deve retornar: v20.x.x ou superior
  ```

---

## 🐳 Validar MongoDB Docker

### 1. Iniciar MongoDB

```powershell
cd C:\Users\harle\Desktop\java-estudos\Projeto-Harlem\item-control-system
.\scripts\start-mongodb-docker.ps1
```

**Esperado:**
- [ ] ✅ Docker está rodando
- [ ] ✅ Porta 27017 disponível
- [ ] ✅ Container iniciado
- [ ] ✅ Healthcheck OK
- [ ] ✅ Mensagem: "MONGODB DOCKER PRONTO!"

### 2. Validar Container

```powershell
docker ps --filter "name=item-control-mongo-dev"
```

**Esperado:**
- [ ] Container `item-control-mongo-dev` aparece como `Up`
- [ ] Status: `healthy`

### 3. Testar Conexão

```powershell
docker exec -it item-control-mongo-dev mongosh -u admin -p admin123 --eval "db.version()"
```

**Esperado:**
- [ ] Retorna versão do MongoDB (ex: 7.0.x)
- [ ] Sem erros de autenticação

---

## 🚀 Validar Backend API

### 1. Iniciar Backend

**Opção A: Via script individual**
```powershell
.\scripts\start-backend-dev.ps1
```

**Opção B: Via start-all (abrir nova janela)**

**Esperado:**
- [ ] ✅ MongoDB Docker está rodando (ou oferece iniciar)
- [ ] ✅ Porta 8080 disponível
- [ ] ✅ Java instalado
- [ ] ✅ Compilação Maven sucesso
- [ ] ✅ Spring Boot inicia
- [ ] ✅ Mensagem: "Started ApiApplication in X seconds"

### 2. Validar Endpoints

**Health Check:**
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/actuator/health" | Select-Object StatusCode,Content
```

**Esperado:**
- [ ] StatusCode: 200
- [ ] Content contém: `{"status":"UP"}`

**Swagger UI:**
```powershell
Start-Process "http://localhost:8080/swagger-ui.html"
```

**Esperado:**
- [ ] Página Swagger abre no browser
- [ ] Lista de endpoints visível

### 3. Testar Endpoint Real

**Via PowerShell:**
```powershell
$response = Invoke-WebRequest -Uri "http://localhost:8080/api/v1/categories" -Method GET
$response.StatusCode
$response.Content
```

**Esperado:**
- [ ] StatusCode: 200
- [ ] Content: Array JSON (mesmo que vazio: `[]`)

**Via Swagger:**
- [ ] GET /api/v1/categories → Try it out → Execute
- [ ] Response: 200 OK

---

## 🎨 Validar Frontend

### 1. Iniciar Frontend

**Opção A: Via script individual**
```powershell
.\scripts\start-frontend-dev.ps1
```

**Opção B: Via start-all (abrir nova janela)**

**Esperado:**
- [ ] ✅ Backend está rodando (ou avisa)
- [ ] ✅ Node.js instalado
- [ ] ✅ Dependências instaladas (ou instala automático)
- [ ] ✅ .env.development criado
- [ ] ✅ Vite dev server inicia
- [ ] ✅ Mensagem: "Local: http://localhost:5173"

### 2. Validar Página

**Abrir no browser:**
```powershell
Start-Process "http://localhost:5173"
```

**Esperado:**
- [ ] Página carrega sem erros
- [ ] CSS Tailwind aplicado (visual correto)
- [ ] Sem erros no Console (F12)

### 3. Testar Integração API

**No Console do browser (F12):**
```javascript
fetch('http://localhost:8080/api/v1/categories')
  .then(r => r.json())
  .then(console.log)
```

**Esperado:**
- [ ] Sem erro CORS
- [ ] Retorna array JSON

---

## 🚀 Validar Start All

### 1. Parar Tudo Primeiro

```powershell
.\scripts\stop-all-dev.ps1
```

**Esperado:**
- [ ] Backend parado (porta 8080 livre)
- [ ] Frontend parado (porta 5173 livre)
- [ ] MongoDB Docker parado

### 2. Iniciar Tudo de Uma Vez

```powershell
.\scripts\start-all-dev.ps1
```

**Esperado:**
- [ ] Etapa 1: MongoDB sobe
- [ ] Etapa 2: Backend abre em nova janela
- [ ] Etapa 3: Frontend abre em nova janela
- [ ] ✅ Mensagem: "AMBIENTE COMPLETO INICIADO!"
- [ ] 3 janelas PowerShell abertas (esta + backend + frontend)

### 3. Validar URLs

**Frontend:**
- [ ] http://localhost:5173 → Página carrega

**Backend:**
- [ ] http://localhost:8080/actuator/health → `{"status":"UP"}`
- [ ] http://localhost:8080/swagger-ui.html → Documentação

**MongoDB:**
```powershell
docker ps --filter "name=item-control-mongo-dev"
```
- [ ] Container rodando

---

## 🔥 Validar Hot Reload

### Backend (Spring DevTools)

1. **Fazer mudança em código Java:**
   - [ ] Abrir: `modules/api/src/main/java/br/com/harlemsilvas/itemcontrol/api/web/controller/CategoryController.java`
   - [ ] Adicionar comentário qualquer
   - [ ] Salvar (Ctrl+S)

2. **Validar reload:**
   - [ ] Console do Backend mostra: "Restarting..."
   - [ ] Aplicação reinicia automaticamente (~5 segundos)

### Frontend (Vite HMR)

1. **Fazer mudança em código React:**
   - [ ] Abrir: `frontend/src/App.tsx`
   - [ ] Mudar texto qualquer
   - [ ] Salvar (Ctrl+S)

2. **Validar hot reload:**
   - [ ] Browser atualiza INSTANTANEAMENTE
   - [ ] Sem reload completo da página
   - [ ] Console: `[vite] hot updated: ...`

---

## 🛑 Validar Stop All

### 1. Executar Stop

```powershell
.\scripts\stop-all-dev.ps1
```

**Esperado:**
- [ ] Backend parado (porta 8080 livre)
- [ ] Frontend parado (porta 5173 livre)
- [ ] MongoDB Docker parado
- [ ] Mensagem: "AMBIENTE PARADO COM SUCESSO!"

### 2. Validar Portas Livres

```powershell
Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue
# Deve retornar nada (erro)

Get-NetTCPConnection -LocalPort 5173 -ErrorAction SilentlyContinue
# Deve retornar nada (erro)
```

### 3. Validar Docker Parado

```powershell
docker ps --filter "name=item-control-mongo-dev"
# Deve retornar cabeçalho vazio (sem containers)
```

---

## 📊 Validar Persistência MongoDB

### 1. Popular Dados

```powershell
# Iniciar ambiente
.\scripts\start-all-dev.ps1

# Aguardar tudo subir
# Criar categoria via Swagger ou PowerShell
```

**Via PowerShell:**
```powershell
$body = @{
    name = "Teste Persistência"
    description = "Validar volume Docker"
} | ConvertTo-Json

Invoke-WebRequest -Uri "http://localhost:8080/api/v1/categories" `
  -Method POST `
  -ContentType "application/json" `
  -Body $body
```

**Esperado:**
- [ ] StatusCode: 201 Created
- [ ] Response contém ID da categoria

### 2. Parar Ambiente

```powershell
.\scripts\stop-all-dev.ps1
```

### 3. Reiniciar e Validar

```powershell
.\scripts\start-all-dev.ps1

# Aguardar tudo subir
# Buscar categorias
$response = Invoke-WebRequest -Uri "http://localhost:8080/api/v1/categories" -Method GET
$response.Content
```

**Esperado:**
- [ ] Categoria "Teste Persistência" ainda existe
- [ ] Volume Docker manteve os dados

---

## 🎯 Checklist Final

### Funcionalidades Básicas
- [ ] MongoDB sobe em Docker
- [ ] Backend conecta ao MongoDB
- [ ] Backend API responde (8080)
- [ ] Frontend sobe localmente
- [ ] Frontend acessa Backend (sem CORS)

### Hot Reload
- [ ] Backend recarrega ao mudar `.java`
- [ ] Frontend atualiza ao mudar `.tsx`

### Scripts
- [ ] start-mongodb-docker.ps1 funciona
- [ ] start-backend-dev.ps1 funciona
- [ ] start-frontend-dev.ps1 funciona
- [ ] start-all-dev.ps1 funciona (inicia tudo)
- [ ] stop-all-dev.ps1 funciona (para tudo)

### Persistência
- [ ] Dados MongoDB persistem entre restarts
- [ ] Volume Docker funciona

### Documentação
- [ ] QUICK-START-DEV.md criado
- [ ] DEV-LOCAL-GUIDE.md criado
- [ ] docs/043-setup-dev-local-completo.md criado
- [ ] docs/042-commits-git-atualizados.md atualizado
- [ ] docs/INDEX.md atualizado

---

## ✅ Status Final

**Se TODOS os checkboxes estão marcados:**

🎉 **SETUP VALIDADO E FUNCIONANDO 100%!**

**Próximo passo:**  
Popular dados de teste e começar desenvolvimento!

---

**Se ALGUM checkbox falhou:**

1. Ver seção específica que falhou
2. Consultar: `DEV-LOCAL-GUIDE.md` → Troubleshooting
3. Verificar logs:
   - MongoDB: `docker logs item-control-mongo-dev`
   - Backend: Console da janela
   - Frontend: Console da janela + Browser F12

---

## 📞 Comandos de Debug

### Ver Logs MongoDB
```powershell
docker logs item-control-mongo-dev
docker logs -f item-control-mongo-dev  # follow
```

### Acessar MongoDB Shell
```powershell
docker exec -it item-control-mongo-dev mongosh -u admin -p admin123
```

### Ver Collections
```javascript
use item_control_db_dev
show collections
db.categories.find().pretty()
```

### Limpar Dados (se necessário)
```javascript
db.categories.deleteMany({})
db.items.deleteMany({})
db.events.deleteMany({})
db.alerts.deleteMany({})
```

### Verificar Processos Porta 8080
```powershell
Get-NetTCPConnection -LocalPort 8080 | 
  Select-Object OwningProcess | 
  ForEach-Object { Get-Process -Id $_.OwningProcess }
```

### Matar Processo Porta 8080
```powershell
$proc = Get-NetTCPConnection -LocalPort 8080
Stop-Process -Id $proc.OwningProcess -Force
```

---

**Data:** 2026-01-26  
**Versão:** 1.0  
**Status:** ✅ Setup Completo
