# 🚀 GUIA RÁPIDO - TESTE LOCAL

**Data:** 2026-01-25  
**Opção escolhida:** B - Testar Local Primeiro ⭐

---

## ✅ PASSO A PASSO

### 1. Backend (EM EXECUÇÃO) ✅

**Status:** Backend iniciando...

**Comando executado:**
```powershell
cd C:\Users\harle\Desktop\java-estudos\Projeto-Harlem\item-control-system\modules\api
mvn spring-boot:run
```

**Aguardar mensagem:**
```
Started ItemControlApplication in X.XXX seconds
```

**Testar quando pronto:**
```powershell
Invoke-RestMethod http://localhost:8080/actuator/health
# Deve retornar: {"status":"UP"}
```

---

### 2. Popular Dados (AGUARDANDO BACKEND) ⏳

**Executar APÓS backend iniciar (aguarde ~1-2 minutos):**

```powershell
cd C:\Users\harle\Desktop\java-estudos\Projeto-Harlem\item-control-system\scripts
.\populate-test-data-local.ps1
```

**Resultado esperado:**
```
[1/4] Criando Categorias... ✅ 4 criadas
[2/4] Criando Items... ✅ 5 criados
[3/4] Criando Eventos... ✅ 7 criados
[4/4] Criando Alertas... ✅ 4 criados
```

---

### 3. Frontend (DEPOIS DOS DADOS)

**Verificar .env:**
```
VITE_API_URL=http://localhost:8080/api/v1
```

**Iniciar frontend:**
```powershell
cd C:\Users\harle\Desktop\java-estudos\Projeto-Harlem\item-control-frontend
npm run dev
```

**Abrir navegador:**
```
http://localhost:5173
```

---

## 📊 TIMELINE COMPLETA

```
[0 min] Backend iniciando (Spring Boot)
   ↓
[1-2 min] Backend pronto (aguardar logs)
   ↓
[2 min] Testar health check
   ↓
[3 min] Executar populate-test-data-local.ps1
   ↓
[5 min] Dados populados (4 categorias, 5 items, 7 eventos, 4 alertas)
   ↓
[6 min] Iniciar frontend (npm run dev)
   ↓
[7 min] Abrir http://localhost:5173
   ↓
[8 min] ✅ VER TUDO FUNCIONANDO!
```

**Tempo total:** ~8 minutos

---

## 🧪 VERIFICAÇÕES

### Backend Pronto?

```powershell
# Teste 1: Health check
Invoke-RestMethod http://localhost:8080/actuator/health

# Teste 2: Swagger UI
Start-Process http://localhost:8080/swagger-ui.html
```

### MongoDB Rodando?

```powershell
docker ps
# Deve mostrar: item-control-system-mongodb
```

### Dados Populados?

```powershell
# Listar items
Invoke-RestMethod "http://localhost:8080/api/v1/items?userId=550e8400-e29b-41d4-a716-446655440001"

# Listar categorias
Invoke-RestMethod "http://localhost:8080/api/v1/categories?userId=550e8400-e29b-41d4-a716-446655440001"
```

### Frontend Funcionando?

**Abrir:** http://localhost:5173

**Verificar:**
- Dashboard com dados reais
- Total de Items: 5
- Items Ativos: 5
- Alertas: 4
- Eventos: 7

---

## 🔧 SE DER PROBLEMA

### Backend não inicia

```powershell
# Ver erros
cd modules/api
mvn clean install
mvn spring-boot:run
```

### MongoDB não conecta

```powershell
# Reiniciar Docker
docker-compose down
docker-compose up -d

# Aguardar 10 segundos
Start-Sleep -Seconds 10
```

### Script populate falha

```powershell
# Verificar se backend está respondendo
Invoke-RestMethod http://localhost:8080/actuator/health

# Se não responder, aguardar mais
Start-Sleep -Seconds 30

# Tentar novamente
.\populate-test-data-local.ps1
```

### Frontend sem dados

```powershell
# Verificar .env
Get-Content C:\Users\harle\Desktop\java-estudos\Projeto-Harlem\item-control-frontend\.env

# Deve ter:
# VITE_API_URL=http://localhost:8080/api/v1

# Restart frontend
# Ctrl+C no terminal do frontend
npm run dev

# Refresh navegador (F5)
```

---

## ✅ COMANDOS RÁPIDOS (COPIAR E COLAR)

### Terminal 1: Backend (JÁ RODANDO)
```powershell
cd C:\Users\harle\Desktop\java-estudos\Projeto-Harlem\item-control-system\modules\api
mvn spring-boot:run
```

### Terminal 2: Popular Dados (EXECUTAR DEPOIS)
```powershell
# Aguardar backend iniciar (1-2 min)
# Testar health:
Invoke-RestMethod http://localhost:8080/actuator/health

# Se retornar {"status":"UP"}, executar:
cd C:\Users\harle\Desktop\java-estudos\Projeto-Harlem\item-control-system\scripts
.\populate-test-data-local.ps1
```

### Terminal 3: Frontend (EXECUTAR POR ÚLTIMO)
```powershell
cd C:\Users\harle\Desktop\java-estudos\Projeto-Harlem\item-control-frontend
npm run dev
```

### Navegador
```
http://localhost:5173
```

---

## 🎯 CHECKLIST EXECUÇÃO

### Preparação
- [x] MongoDB Docker iniciado (docker-compose up -d)
- [x] Backend Spring Boot iniciando (mvn spring-boot:run)

### Execução (VOCÊ FAZ)
- [ ] Aguardar backend iniciar (1-2 min)
- [ ] Testar health check ({"status":"UP"})
- [ ] Executar populate-test-data-local.ps1
- [ ] Verificar: 4 categorias, 5 items, 7 eventos, 4 alertas
- [ ] Iniciar frontend (npm run dev)
- [ ] Abrir http://localhost:5173
- [ ] Verificar dashboard com dados
- [ ] Navegar entre páginas
- [ ] ✅ Celebrar! 🎉

### Depois
- [ ] Parar tudo (Ctrl+C nos terminais)
- [ ] docker-compose down (parar MongoDB)
- [ ] Resolver Render quando quiser
- [ ] Popular dados produção
- [ ] Deploy completo!

---

## 📚 LOGS IMPORTANTES

### Backend iniciando
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.1)
```

### Backend pronto
```
Started ItemControlApplication in 12.345 seconds
```

### Populate sucesso
```
✅ POPULAÇÃO DE DADOS CONCLUÍDA!

📊 Resumo:
   • Categorias: 4
   • Items: 5
   • Eventos: 7
   • Alertas: 4
```

---

## 💡 DICAS

1. **Aguarde o backend iniciar completamente** antes de popular dados
2. **Use health check** para confirmar que backend está pronto
3. **Refresh no navegador** (F5) se dados não aparecerem
4. **Ver logs** do backend para entender erros
5. **Docker Desktop** deve estar rodando

---

## 🎉 SUCESSO ESPERADO

**Ao final, você terá:**
- ✅ Backend rodando localmente (porta 8080)
- ✅ MongoDB com dados populados
- ✅ Frontend rodando (porta 5173)
- ✅ Dashboard com dados reais
- ✅ Sistema completo funcionando!

**Tempo total:** ~8 minutos

---

**AGORA:**
1. Aguarde backend iniciar (~1-2 min)
2. Execute os comandos do Terminal 2 (popular dados)
3. Execute os comandos do Terminal 3 (frontend)
4. Abra http://localhost:5173
5. VER MAGIA ACONTECER! ✨🚀

**Status atual:** Backend iniciando... aguarde logs aparecerem!
