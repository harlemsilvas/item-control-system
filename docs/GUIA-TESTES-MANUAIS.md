# Guia: Criar Eventos e Alertas Manualmente via Swagger

## Situacao Atual
- ✅ 15 items criados com sucesso
- ❌ Script automatico falhou para eventos e alertas
- 💡 Solucao: Criar manualmente via Swagger UI

## Passo a Passo

### 1. Abrir Swagger UI
```
http://localhost:8080/swagger-ui.html
```

### 2. Buscar ID de um Item

**Endpoint:** `GET /api/v1/items`

**Parametros:**
- userId: `550e8400-e29b-41d4-a716-446655440001`

**Clique em "Try it out" e "Execute"**

Copie o `id` de um dos items retornados (exemplo: Honda CB 500X)

---

### 3. Criar Evento para o Item

**Endpoint:** `POST /api/v1/events`

**Body exemplo:**
```json
{
  "itemId": "COLE-O-ID-DO-ITEM-AQUI",
  "userId": "550e8400-e29b-41d4-a716-446655440001",
  "eventType": "MAINTENANCE",
  "eventDate": "2025-12-23T10:00:00Z",
  "description": "Troca de oleo e filtro",
  "metrics": {
    "odometer": 14500,
    "cost": 280.00,
    "serviceName": "Troca de oleo"
  }
}
```

**Tipos de Eventos:**
- `MAINTENANCE` - Manutencao
- `CONSUMPTION` - Consumo/Abastecimento
- `INSPECTION` - Revisao/Inspecao
- `ALERT_TRIGGERED` - Alerta disparado
- `OTHER` - Outros

---

### 4. Criar Alerta para o Item

**Endpoint:** `POST /api/v1/alerts`

**Body exemplo:**
```json
{
  "itemId": "COLE-O-ID-DO-ITEM-AQUI",
  "userId": "550e8400-e29b-41d4-a716-446655440001",
  "ruleId": "750e8400-e29b-41d4-a716-446655440005",
  "alertType": "SCHEDULED",
  "title": "Troca de oleo vencendo",
  "message": "Proxima troca de oleo em 1.000 km",
  "priority": 4,
  "dueAt": "2026-02-06T00:00:00Z"
}
```

**Tipos de Alerta:**
- `SCHEDULED` - Agendado
- `THRESHOLD` - Limite atingido
- `URGENT` - Urgente
- `REMINDER` - Lembrete

**Prioridade:**
- 1 = Muito Baixa
- 2 = Baixa
- 3 = Media
- 4 = Alta
- 5 = Critica/Urgente

---

### 5. Verificar Dados Criados

#### Listar Eventos de um Item
**GET** `/api/v1/events?itemId={item-id}`

#### Listar Alertas Pendentes
**GET** `/api/v1/alerts/pending?userId=550e8400-e29b-41d4-a716-446655440001`

#### Marcar Alerta como Lido
**PUT** `/api/v1/alerts/{alert-id}/acknowledge?userId=550e8400-e29b-41d4-a716-446655440001`

#### Resolver Alerta
**PUT** `/api/v1/alerts/{alert-id}/resolve?userId=550e8400-e29b-41d4-a716-446655440001`

---

## Exemplos Completos por Tipo de Item

### Veiculo (Honda CB 500X)

**Evento 1 - Manutencao:**
```json
{
  "itemId": "{id}",
  "userId": "550e8400-e29b-41d4-a716-446655440001",
  "eventType": "MAINTENANCE",
  "eventDate": "2025-10-22T14:30:00Z",
  "description": "Troca de oleo e filtros",
  "metrics": {
    "odometer": 10000,
    "cost": 350.00,
    "serviceName": "Troca de oleo completa",
    "nextMaintenanceKm": 15000
  }
}
```

**Evento 2 - Abastecimento:**
```json
{
  "itemId": "{id}",
  "userId": "550e8400-e29b-41d4-a716-446655440001",
  "eventType": "CONSUMPTION",
  "eventDate": "2026-01-15T09:00:00Z",
  "description": "Abastecimento completo",
  "metrics": {
    "quantity": 42.5,
    "unitPrice": 5.89,
    "totalCost": 250.00,
    "fullTank": true,
    "odometer": 14800
  }
}
```

**Alerta - Manutencao:**
```json
{
  "itemId": "{id}",
  "userId": "550e8400-e29b-41d4-a716-446655440001",
  "ruleId": "750e8400-e29b-41d4-a716-446655440005",
  "alertType": "SCHEDULED",
  "title": "Revisao dos 15.000 km",
  "message": "Revisao programada se aproximando",
  "priority": 4,
  "dueAt": "2026-02-15T00:00:00Z"
}
```

---

### Conta (Conta de Luz)

**Evento - Pagamento:**
```json
{
  "itemId": "{id}",
  "userId": "550e8400-e29b-41d4-a716-446655440001",
  "eventType": "CONSUMPTION",
  "eventDate": "2026-01-15T00:00:00Z",
  "description": "Pagamento conta de luz Janeiro/2026",
  "metrics": {
    "amount": 152.30,
    "dueDate": "2026-01-15",
    "paymentDate": "2026-01-14",
    "consumption": 350,
    "lateFee": 0
  }
}
```

**Alerta - Vencimento:**
```json
{
  "itemId": "{id}",
  "userId": "550e8400-e29b-41d4-a716-446655440001",
  "ruleId": "850e8400-e29b-41d4-a716-446655440006",
  "alertType": "SCHEDULED",
  "title": "Vencimento conta de luz",
  "message": "Conta vence em 5 dias - R$ 152,30",
  "priority": 5,
  "dueAt": "2026-02-10T00:00:00Z"
}
```

---

### Consumivel (Botijao de Gas)

**Evento - Compra:**
```json
{
  "itemId": "{id}",
  "userId": "550e8400-e29b-41d4-a716-446655440001",
  "eventType": "CONSUMPTION",
  "eventDate": "2026-01-10T00:00:00Z",
  "description": "Compra de botijao de gas 13kg",
  "metrics": {
    "quantity": 1,
    "unitPrice": 95.00,
    "totalCost": 95.00,
    "supplier": "Revendedor ABC"
  }
}
```

**Alerta - Reposicao:**
```json
{
  "itemId": "{id}",
  "userId": "550e8400-e29b-41d4-a716-446655440001",
  "ruleId": "950e8400-e29b-41d4-a716-446655440007",
  "alertType": "THRESHOLD",
  "title": "Estoque baixo - Gas",
  "message": "Botijao acabando, providenciar reposicao",
  "priority": 4,
  "dueAt": "2026-02-05T00:00:00Z"
}
```

---

## Dicas

1. **Copie o ID do item** da resposta do GET /items
2. **Ajuste as datas** para serem realistas
3. **Use prioridades diferentes** para testar ordenacao
4. **Crie varios eventos** para mesmo item para ver historico
5. **Teste workflow completo:**
   - Criar alerta (PENDING)
   - Marcar como lido (READ)  
   - Resolver (COMPLETED)

---

## Ver no MongoDB

Apos criar via Swagger, acesse:
```
http://localhost:8081
```

Database: `item_control_db_dev`
Collections:
- `items` - 15 items
- `events` - Seus eventos criados
- `alerts` - Seus alertas criados

---

**Criado em:** 22/01/2026  
**Para:** Testes manuais enquanto o script automatico e corrigido
