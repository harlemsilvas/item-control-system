# 📦 POPULAR MONGODB COM DADOS DE TESTE

**Data:** 2026-01-25  
**Script:** `scripts/populate-test-data-local.ps1`  
**Status:** ✅ Pronto para uso

---

## 🎯 O QUE O SCRIPT FAZ

### Cria Dados Completos para Teste

**1. Categorias (4):**
- Veículos
- Casa
- Eletrônicos
- Manutenção

**2. Items (5):**
- Honda Civic 2020 (veículo com quilometragem)
- Notebook Dell (eletrônico)
- Geladeira Brastemp (casa)
- Conta de Luz (conta recorrente)
- Ar Condicionado (casa)

**3. Eventos (7):**
- Manutenções do carro (troca de óleo, revisão)
- Abastecimento
- Pagamentos de contas
- Limpeza de equipamentos
- Upgrade de notebook

**4. Alertas (4):**
- Revisão dos 50 mil km (carro)
- Vencimento conta de luz
- Limpeza de filtros (ar condicionado)
- Degelo geladeira

---

## 🚀 COMO USAR

### Opção 1: Executar Script

```powershell
cd C:\Users\harle\Desktop\java-estudos\Projeto-Harlem\item-control-system\scripts
.\populate-test-data-local.ps1
```

### Opção 2: Via Raiz do Projeto

```powershell
cd C:\Users\harle\Desktop\java-estudos\Projeto-Harlem\item-control-system
.\scripts\populate-test-data-local.ps1
```

---

## ⚙️ PRÉ-REQUISITOS

### Antes de executar:

1. **Backend rodando:**
   ```powershell
   cd modules/api
   mvn spring-boot:run
   # Ou: java -jar target/*.jar
   ```
   
   **Verificar:** http://localhost:8080/actuator/health

2. **MongoDB conectado:**
   - Docker local, ou
   - MongoDB Atlas configurado

3. **API acessível:**
   - Porta 8080 livre
   - Spring Boot iniciado sem erros

---

## 📊 DADOS CRIADOS

### UUID Fixo para Testes

**User ID:** `550e8400-e29b-41d4-a716-446655440001`

Todos os dados são criados com este UUID para facilitar os testes.

### Estrutura Completa

```
Categorias (4)
├── Veículos
├── Casa
├── Eletrônicos
└── Manutenção

Items (5)
├── Honda Civic 2020 (45.000 km)
├── Notebook Dell
├── Geladeira Brastemp
├── Conta de Luz
└── Ar Condicionado

Eventos (7)
├── Civic: Troca de óleo (-60 dias)
├── Civic: Revisão 45k (-10 dias)
├── Civic: Abastecimento (-5 dias)
├── Conta Luz: Pagamento Jan (-25 dias)
├── Conta Luz: Pagamento Dez (-55 dias)
├── Ar: Limpeza filtros (-30 dias)
└── Notebook: Upgrade RAM (-90 dias)

Alertas (4)
├── Civic: Revisão 50k (+30 dias)
├── Conta Luz: Vencimento (+5 dias)
├── Ar: Limpeza (+15 dias)
└── Geladeira: Degelo (+7 dias)
```

---

## ✅ RESULTADO ESPERADO

### No Terminal

```
═══════════════════════════════════════════
  📦 Item Control - Popular Dados de Teste
═══════════════════════════════════════════

[1/4] Criando Categorias...
   ✅ Categoria criada: Veículos
   ✅ Categoria criada: Casa
   ✅ Categoria criada: Eletrônicos
   ✅ Categoria criada: Manutenção

[2/4] Criando Items...
   ✅ Item criado: Honda Civic 2020
   ✅ Item criado: Notebook Dell
   ✅ Item criado: Geladeira Brastemp
   ✅ Item criado: Conta de Luz
   ✅ Item criado: Ar Condicionado

[3/4] Criando Eventos...
   ✅ Evento criado: Troca de óleo e filtros
   ✅ Evento criado: Revisão dos 45 mil km
   (...)

[4/4] Criando Alertas...
   ✅ Alerta criado: Revisão dos 50 mil km se aproxima
   (...)

═══════════════════════════════════════════
  ✅ POPULAÇÃO DE DADOS CONCLUÍDA!
═══════════════════════════════════════════

📊 Resumo:
   • Categorias: 4
   • Items: 5
   • Eventos: 7
   • Alertas: 4
```

### No Frontend (http://localhost:5173)

**Dashboard:**
- ✅ Total de Items: 5
- ✅ Items Ativos: 5
- ✅ Alertas Pendentes: 4
- ✅ Eventos do Mês: 7

**Items Recentes:**
- Honda Civic 2020
- Notebook Dell
- Geladeira Brastemp
- Conta de Luz
- Ar Condicionado

**Alertas Recentes:**
- Revisão dos 50 mil km se aproxima
- Vencimento da conta de luz
- Limpeza de filtros
- Degelo preventivo

---

## 🔧 TROUBLESHOOTING

### Erro: "Cannot connect to localhost:8080"

**Solução:**
```powershell
# Verificar se backend está rodando
Invoke-RestMethod http://localhost:8080/actuator/health

# Se não estiver, iniciar:
cd modules/api
mvn spring-boot:run
```

### Erro: "Required request parameter 'userId'"

**Causa:** Endpoint requer userId mas script envia no body

**Solução:** Script já corrigido - userId sempre enviado como `550e8400-e29b-41d4-a716-446655440001`

### Erro: "Invalid UUID string"

**Causa:** ID mal formatado

**Solução:** Script usa UUIDs válidos gerados pela API

### Dados Duplicados

**Limpar banco:**
```powershell
# Via MongoDB Compass ou mongosh
use item_control_db_dev
db.items.deleteMany({})
db.events.deleteMany({})
db.alerts.deleteMany({})
db.categories.deleteMany({})

# Executar script novamente
.\populate-test-data-local.ps1
```

---

## 🧪 TESTAR DADOS CRIADOS

### Via PowerShell

```powershell
# Listar items
Invoke-RestMethod "http://localhost:8080/api/v1/items?userId=550e8400-e29b-41d4-a716-446655440001"

# Listar categorias
Invoke-RestMethod "http://localhost:8080/api/v1/categories?userId=550e8400-e29b-41d4-a716-446655440001"

# Listar alertas pendentes
Invoke-RestMethod "http://localhost:8080/api/v1/alerts/pending?userId=550e8400-e29b-41d4-a716-446655440001"
```

### Via Frontend

1. Abrir: http://localhost:5173
2. Ver Dashboard com dados populados
3. Clicar em "Items" - ver 5 items
4. Ver widgets com números reais
5. Ver alertas pendentes

### Via Swagger

1. Abrir: http://localhost:8080/swagger-ui.html
2. Testar endpoints
3. Usar userId: `550e8400-e29b-41d4-a716-446655440001`

---

## 📝 CUSTOMIZAR DADOS

### Adicionar Mais Items

Editar `populate-test-data-local.ps1`:

```powershell
$items = @(
    # ... items existentes ...
    @{
        name = "Meu Novo Item"
        nickname = "novo-item"
        template = "GENERAL"
        description = "Descrição"
        categoryId = $categoriasIds["casa"]
        metadata = @{
            campo1 = "valor1"
        }
    }
)
```

### Adicionar Mais Eventos

```powershell
$eventos = @(
    # ... eventos existentes ...
    @{
        itemNickname = "civic"
        type = "MAINTENANCE"
        description = "Nova manutenção"
        value = 500.00
        eventDate = (Get-Date).ToString("yyyy-MM-dd")
    }
)
```

### Mudar Datas

```powershell
# Evento de hoje
eventDate = (Get-Date).ToString("yyyy-MM-dd")

# Evento de ontem
eventDate = (Get-Date).AddDays(-1).ToString("yyyy-MM-dd")

# Evento em 1 mês
scheduledFor = (Get-Date).AddDays(30).ToString("yyyy-MM-dd")
```

---

## 🎯 USAR EM PRODUÇÃO/RENDER

### Adaptar para Render

**Criar:** `populate-test-data-render.ps1`

```powershell
# Mudar URL
$API_URL = "https://item-control-api.onrender.com/api/v1"

# Resto igual!
```

**Executar:**
```powershell
.\populate-test-data-render.ps1
```

---

## 📚 SCRIPTS RELACIONADOS

**Outros scripts úteis:**

- `start-api.ps1` - Iniciar backend local
- `test-api.ps1` - Testar endpoints
- `check-railway-mongodb.ps1` - Verificar MongoDB
- `quick-start.ps1` - Iniciar tudo de uma vez

---

## ✅ RESUMO

**Script:** `populate-test-data-local.ps1`

**Cria:**
- 4 Categorias
- 5 Items variados
- 7 Eventos com datas reais
- 4 Alertas futuros

**User ID:** `550e8400-e29b-41d4-a716-446655440001`

**Uso:**
```powershell
.\scripts\populate-test-data-local.ps1
```

**Resultado:**
- Dashboard populado
- Dados realistas
- Pronto para desenvolvimento e testes

---

**Script pronto! Execute e veja o frontend ganhar vida! 🚀**
