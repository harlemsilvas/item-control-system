# RESUMO - Scripts de Populacao

## Status Final

### ✅ Sucesso
- **15 Items criados** com sucesso no MongoDB
  - 5 Veículos
  - 5 Contas
  - 5 Consumíveis

### ❌ Problema
- **Eventos e Alertas** - Erro 400 (Bad Request)
- Script PowerShell falhando ao criar via API REST

## Causa do Problema

Erro **400 - Bad Request** indica que a API está rejeitando os dados.

Possíveis causas investigadas:
1. ✅ EventType corrigido (MAINTENANCE, PAYMENT, PURCHASE)
2. ✅ Estrutura de DTOs verificada (RegisterEventRequest, CreateAlertRequest)
3. ⚠️ Pode ser problema de formato de data/hora
4. ⚠️ Pode ser problema de validação do UUID
5. ⚠️ Pode ser problema de serialização JSON do PowerShell

## Solucao Alternativa

### Via Swagger UI (RECOMENDADO)

1. **Abrir Swagger:**
   ```
   http://localhost:8080/swagger-ui.html
   ```

2. **Criar Evento (POST /api/v1/events):**
   ```json
   {
     "itemId": "COLE-ID-DO-ITEM-AQUI",
     "userId": "550e8400-e29b-41d4-a716-446655440001",
     "eventType": "MAINTENANCE",
     "eventDate": "2025-12-23T10:00:00Z",
     "description": "Troca de oleo",
     "metrics": {
       "odometer": 14500,
       "cost": 280.00
     }
   }
   ```

3. **Criar Alerta (POST /api/v1/alerts):**
   ```json
   {
     "itemId": "COLE-ID-DO-ITEM-AQUI",
     "userId": "550e8400-e29b-41d4-a716-446655440001",
     "ruleId": "750e8400-e29b-41d4-a716-446655440005",
     "alertType": "SCHEDULED",
     "title": "Troca de oleo vencendo",
     "message": "Proxima troca em 1.000 km",
     "priority": 4,
     "dueAt": "2026-02-06T00:00:00Z"
   }
   ```

## Arquivos Disponiveis

1. **populate-simple.ps1** - Cria items (FUNCIONA), eventos e alertas (falha)
2. **GUIA-TESTES-MANUAIS.md** - Guia completo para testes via Swagger
3. **test-event-manual.ps1** - Script de debug (criado mas não funcionou)

## Proximos Passos

1. Usar Swagger UI para criar eventos e alertas manualmente
2. Verificar logs da API para ver erro detalhado
3. Testar com Postman/Insomnia se necessário
4. Investigar se problema é do PowerShell ou da API

## Items Criados (Funcionando!)

Você pode ver os 15 items em:
- **Swagger:** GET /api/v1/items?userId=550e8400-e29b-41d4-a716-446655440001
- **MongoDB:** http://localhost:8081 → item_control_db_dev → items

---

**Conclusão:** Script de items funciona perfeitamente. Eventos e alertas precisam ser criados via Swagger UI por enquanto.

**Data:** 22/01/2026
