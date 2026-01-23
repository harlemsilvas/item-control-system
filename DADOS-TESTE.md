# 🧪 Dados de Teste - Documentação

**Script:** `populate-test-data.ps1`  
**Executor:** `run-populate.ps1`

---

## 📋 O QUE SERÁ CRIADO

### Total:
- **15 Items** (5 veículos, 5 contas, 5 consumíveis)
- **75 Eventos** (5 eventos para cada item)
- **~30 Alertas** (2 alertas por item em média)

---

## 📦 ITENS CRIADOS

### 🚗 Veículos (5 items)

1. **Honda CB 500X** (Motoca)
   - Moto Honda 2020, vermelha
   - Placa: ABC-1234
   - Odômetro: 15.000 km

2. **Toyota Corolla** (Carro da Família)
   - Sedan Toyota 2021, prata
   - Placa: XYZ-9876
   - Odômetro: 32.000 km

3. **Chevrolet Onix** (Onix)
   - Compacto Chevrolet 2022, branco
   - Placa: DEF-4567
   - Odômetro: 18.500 km

4. **Yamaha Fazer 250** (Fazer)
   - Moto Yamaha 2019, azul
   - Placa: GHI-7890
   - Odômetro: 42.000 km

5. **Fiat Uno** (Carrinho)
   - Econômico Fiat 2018, vermelho
   - Placa: JKL-1357
   - Odômetro: 65.000 km

**Eventos de Veículos:**
- Troca de óleo (90 dias atrás)
- Abastecimento (60 dias atrás)
- Troca de pneus (180 dias atrás)
- Revisão periódica (30 dias atrás)
- Abastecimento recente (15 dias atrás)

**Alertas de Veículos:**
- Troca de óleo vencendo (prioridade 4)
- Revisão periódica (prioridade 3)

---

### 💰 Contas (5 items)

1. **Conta de Água** (Água Casa)
   - SABESP
   - Vencimento: dia 10
   - Média: R$ 85,50

2. **Conta de Luz** (Luz Casa)
   - Enel
   - Vencimento: dia 15
   - Média: R$ 152,30

3. **Internet Fibra** (Wi-Fi)
   - Vivo Fibra 300 Mbps
   - Vencimento: dia 5
   - Média: R$ 119,90

4. **Condomínio** (Condomínio)
   - APT-401
   - Vencimento: dia 8
   - Média: R$ 450,00

5. **Telefone Celular** (Celular)
   - Claro Controle 15GB
   - Vencimento: dia 12
   - Média: R$ 89,90

**Eventos de Contas:**
- Pagamento Janeiro (120 dias atrás)
- Pagamento Fevereiro com atraso (90 dias atrás)
- Pagamento Março (60 dias atrás)
- Pagamento Novembro (30 dias atrás)
- Pagamento Dezembro (7 dias atrás)

**Alertas de Contas:**
- Vencimento próximo (prioridade 5)
- Consumo elevado (prioridade 3)

---

### 📦 Consumíveis (5 items)

1. **Galão de Água 20L** (Galão Água)
   - Água Mineral Natural
   - Custo: R$ 12,50
   - Consumo: 2 por semana

2. **Botijão de Gás 13kg** (Gás)
   - Ultragaz
   - Custo: R$ 95,00
   - Consumo: 1 por mês

3. **Filtro de Café** (Filtro Café)
   - Melitta - 100 unidades
   - Custo: R$ 8,90
   - Consumo: 1 pacote/mês

4. **Papel Higiênico** (Papel)
   - Personal - 12 rolos
   - Custo: R$ 18,50
   - Consumo: 1 pacote quinzenal

5. **Detergente Líquido** (Detergente)
   - Ypê 500ml
   - Custo: R$ 2,90
   - Consumo: 2 por mês

**Eventos de Consumíveis:**
- Compra reposição (120 dias atrás)
- Compra estoque (90 dias atrás)
- Compra em promoção (60 dias atrás)
- Compra emergência (30 dias atrás)
- Compra mensal (7 dias atrás)

**Alertas de Consumíveis:**
- Estoque baixo (prioridade 4)
- Reposição programada (prioridade 2)

---

## 🚀 COMO USAR

### 1. Pré-requisitos

```powershell
# MongoDB rodando
docker compose up -d

# API compilada
mvn clean install -DskipTests
```

### 2. Executar População

**Opção A - Com aguardar API:**
```powershell
.\run-populate.ps1
```

**Opção B - Direto (API já rodando):**
```powershell
.\populate-test-data.ps1
```

### 3. Iniciar API (se necessário)

```powershell
cd modules/api
java -jar target/item-control-api-0.1.0-SNAPSHOT.jar --spring.profiles.active=dev
```

---

## 📊 ESTRUTURA DOS DADOS

### Item
```json
{
  "userId": "550e8400-e29b-41d4-a716-446655440001",
  "name": "Honda CB 500X",
  "nickname": "Motoca",
  "categoryId": "650e8400-e29b-41d4-a716-446655440002",
  "templateCode": "VEHICLE",
  "tags": ["moto", "honda", "transporte"],
  "metadata": {
    "brand": "Honda",
    "model": "CB 500X",
    "year": 2020,
    "plate": "ABC-1234",
    "odometer": 15000
  }
}
```

### Event
```json
{
  "itemId": "{item-id}",
  "userId": "550e8400-e29b-41d4-a716-446655440001",
  "eventType": "MAINTENANCE",
  "eventDate": "2025-10-23T10:00:00Z",
  "description": "Troca de óleo e filtro",
  "metrics": {
    "odometer": 10000,
    "cost": 280.00,
    "serviceName": "Troca de óleo"
  }
}
```

### Alert
```json
{
  "itemId": "{item-id}",
  "userId": "550e8400-e29b-41d4-a716-446655440001",
  "ruleId": "{rule-id}",
  "alertType": "SCHEDULED",
  "title": "Troca de óleo vencendo",
  "message": "Próxima troca de óleo em 1.000 km",
  "priority": 4,
  "dueAt": "2026-02-06T00:00:00Z"
}
```

---

## 🔍 VERIFICAR DADOS CRIADOS

### Via Swagger UI
```
http://localhost:8080/swagger-ui.html
```

Endpoints para testar:
- `GET /api/v1/items?userId=550e8400-e29b-41d4-a716-446655440001`
- `GET /api/v1/events?itemId={item-id}`
- `GET /api/v1/alerts/pending?userId=550e8400-e29b-41d4-a716-446655440001`

### Via Mongo Express
```
http://localhost:8081
```

Collections:
- `item_control_db_dev.items`
- `item_control_db_dev.events`
- `item_control_db_dev.alerts`

### Via MongoDB CLI
```bash
docker exec -it item-control-mongodb mongosh

use item_control_db_dev

# Ver items
db.items.find().pretty()
db.items.countDocuments()

# Ver eventos
db.events.find().pretty()
db.events.countDocuments()

# Ver alertas
db.alerts.find().pretty()
db.alerts.countDocuments()

# Estatísticas
db.items.aggregate([
  { $group: { _id: "$templateCode", count: { $sum: 1 } } }
])
```

---

## 📈 CASOS DE USO PARA TESTES

### 1. Dashboard de Alertas
```
GET /api/v1/alerts/pending?userId=550e8400-e29b-41d4-a716-446655440001
```
Mostra todos os alertas pendentes ordenados por prioridade.

### 2. Histórico de Veículo
```
GET /api/v1/events?itemId={honda-cb-id}
```
Ver todas as manutenções e abastecimentos da moto.

### 3. Análise de Contas
```
GET /api/v1/events?itemId={conta-luz-id}
```
Verificar histórico de pagamentos e consumo.

### 4. Controle de Estoque
```
GET /api/v1/items?userId={user-id}
```
Filtrar por `templateCode=CONSUMABLE_ITEM` para ver consumíveis.

### 5. Marcar Alert como Lido
```
PUT /api/v1/alerts/{alert-id}/acknowledge?userId={user-id}
```

### 6. Resolver Alert
```
PUT /api/v1/alerts/{alert-id}/resolve?userId={user-id}
```

---

## 🎯 CENÁRIOS DE TESTE

### Cenário 1: Manutenção de Veículo
1. Ver alertas de troca de óleo
2. Registrar nova manutenção
3. Resolver alerta

### Cenário 2: Pagamento de Contas
1. Ver alertas de vencimento
2. Registrar pagamento
3. Marcar alerta como resolvido

### Cenário 3: Reposição de Consumíveis
1. Ver alerta de estoque baixo
2. Registrar compra
3. Atualizar alerta

---

## 🧹 LIMPAR DADOS

Para remover todos os dados de teste:

```bash
docker exec -it item-control-mongodb mongosh

use item_control_db_dev

db.items.deleteMany({})
db.events.deleteMany({})
db.alerts.deleteMany({})
```

Ou reiniciar o container:
```bash
docker compose down -v
docker compose up -d
```

---

## 📝 OBSERVAÇÕES

- Todos os dados usam o mesmo `userId` para facilitar testes
- Eventos têm datas retroativas realistas (7 a 180 dias atrás)
- Alertas têm datas futuras (3 a 30 dias à frente)
- Valores monetários variam para simular realidade
- Prioridades distribuídas entre 2 e 5

---

**Criado em:** 22/01/2026  
**Versão:** 1.0

