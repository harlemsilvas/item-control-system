# ✅ CHECKLIST - Retomada do Trabalho

**Data:** 22/01/2026  
**Hora:** Quando você voltar do café ☕

---

## 🔍 VERIFICAÇÕES RÁPIDAS (2 minutos)

### 1. Containers Docker
```powershell
docker ps
```
**Esperado:** 2 containers rodando
- ✅ `item-control-mongodb`
- ✅ `item-control-mongo-express`

**Se não estiver rodando:**
```powershell
cd C:\Users\harle\Desktop\java-estudos\Projeto-Harlem\item-control-system
docker compose up -d
```

---

### 2. API Spring Boot
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/actuator/health"
```
**Esperado:** `status: UP`

**Se não estiver rodando:**
```powershell
cd C:\Users\harle\Desktop\java-estudos\Projeto-Harlem\item-control-system\modules\api
java -jar target/item-control-api-0.1.0-SNAPSHOT.jar --spring.profiles.active=dev
```

---

### 3. Acessos Web (abrir navegador)
- [ ] **Swagger UI:** http://localhost:8080/swagger-ui.html
- [ ] **Mongo Express:** http://localhost:8081

---

## 🎯 OPÇÕES DE TRABALHO

Escolha UMA das opções abaixo:

---

### 📋 OPÇÃO A: VALIDAR SISTEMA (Recomendado - 1h)

**Objetivo:** Testar tudo que foi construído

#### Passo 1: Testar Eventos via Swagger
1. Abrir http://localhost:8080/swagger-ui.html
2. Expandir **POST /api/v1/events**
3. Clicar **Try it out**
4. Usar este JSON (substitua o itemId):

```json
{
  "itemId": "COPIAR-ID-DO-ITEM-CRIADO",
  "userId": "550e8400-e29b-41d4-a716-446655440001",
  "eventType": "MAINTENANCE",
  "eventDate": "2026-01-22T22:30:00Z",
  "description": "Troca de óleo e filtro",
  "metrics": {
    "odometer": 15000,
    "cost": 350.00,
    "serviceName": "Troca de óleo completa"
  }
}
```

5. Clicar **Execute**
6. Verificar response **201 Created**

#### Passo 2: Listar Eventos
1. Expandir **GET /api/v1/events**
2. Clicar **Try it out**
3. Colar o itemId no campo
4. Clicar **Execute**
5. Verificar lista de eventos

#### Passo 3: Ver no MongoDB
1. Abrir http://localhost:8081
2. Clicar em **item_control_db_dev**
3. Ver collections:
   - **items** (deve ter 1 item)
   - **events** (deve ter N eventos)

#### Passo 4: Testar Mais Casos
- [ ] Criar outro item (conta de água)
- [ ] Registrar evento de consumo
- [ ] Listar items do usuário
- [ ] Buscar item por ID

---

### 🐙 OPÇÃO B: PUBLICAR NO GITHUB (30min)

**Objetivo:** Versionar o código

#### Passo 1: Criar Repositório
1. Ir para https://github.com/new
2. Repository name: `item-control-system`
3. Description: `Sistema de controle de itens com alertas`
4. Visibility: **Public** ou **Private**
5. NÃO marcar "Initialize with README"
6. Clicar **Create repository**

#### Passo 2: Conectar e Push
```bash
cd C:\Users\harle\Desktop\java-estudos\Projeto-Harlem\item-control-system

# Verificar status
git status

# Adicionar todos os arquivos
git add .

# Commit
git commit -m "feat: Sprint 1 - Fundação do sistema (Item + Event)"

# Conectar ao GitHub (use SUA URL)
git remote add origin https://github.com/harlemsilvas/item-control-system.git

# Push
git push -u origin main

# Criar tag de release
git tag -a v0.1.0 -m "Sprint 1 - MVP Fundação"
git push origin v0.1.0
```

#### Passo 3: Verificar
- Acessar repositório no GitHub
- Verificar arquivos
- Ver releases

---

### 🚀 OPÇÃO C: COMEÇAR SPRINT 2 - ALERTS (4h)

**Objetivo:** Implementar AlertRepository

Ver arquivo: **PROXIMO-PASSO.md** (seção "Opção C")

---

## 📝 ANOTAÇÕES RÁPIDAS

### Item Criado Anteriormente
```
ID: (buscar no Swagger ou Mongo Express)
Nome: Honda CB 500X
UserId: 550e8400-e29b-41d4-a716-446655440001
```

### Comandos Úteis
```powershell
# Ver items do usuário
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/items?userId=550e8400-e29b-41d4-a716-446655440001"

# Criar novo item (exemplo: conta de água)
$body = @{
  userId = "550e8400-e29b-41d4-a716-446655440001"
  name = "Conta de Água - Residência"
  nickname = "Água Casa"
  categoryId = "750e8400-e29b-41d4-a716-446655440003"
  templateCode = "UTILITY_BILL"
  tags = @("conta", "agua", "mensal")
  metadata = @{
    supplier = "SABESP"
    accountNumber = "123456789"
    dueDay = 10
  }
} | ConvertTo-Json -Depth 10

Invoke-RestMethod -Uri "http://localhost:8080/api/v1/items" `
  -Method POST -ContentType "application/json" -Body $body
```

---

## 🎯 MINHA ESCOLHA

**Marque aqui sua decisão:**
- [ ] OPÇÃO A - Validar Sistema
- [ ] OPÇÃO B - GitHub
- [ ] OPÇÃO C - Sprint 2

**Tempo disponível:** ________

**Objetivo da sessão:** ____________________________

---

## 📚 Referências Rápidas

| Documento | Onde Está | Para Quê |
|-----------|-----------|----------|
| RESUMO-EXECUTIVO.md | Raiz do projeto | Visão geral |
| GUIA-TESTES.md | Raiz do projeto | Como testar |
| PROXIMO-PASSO.md | Raiz do projeto | Detalhes Sprint 2 |
| docs/GITHUB-SETUP.md | docs/ | Setup GitHub |
| docs/003-roadmap-implementacao.md | docs/ | Planejamento |

---

## ✅ CHECKLIST DE FIM DE SESSÃO

Quando terminar a sessão de hoje:

- [ ] Fazer commit das mudanças
- [ ] Atualizar documentação (se necessário)
- [ ] Parar containers (opcional):
  ```powershell
  docker compose down
  ```
- [ ] Anotar próximos passos
- [ ] Fechar IDE e navegador

---

**BOA CONTINUAÇÃO! 🚀**

Tudo está pronto e organizado para você continuar de onde parou!

