# 🔍 RESUMO - Investigação de Alertas

## Status Atual

### ✅ **EVENTOS: 100% RESOLVIDO!**
- **75/75 eventos criados com sucesso**
- Problema era formato de data
- Solução: `ToString("yyyy-MM-ddTHH:mm:ssZ")`

### ⚠️ **ALERTAS: Em Investigação**
- **0/30 alertas criados**
- Erro 400 (Bad Request) persistente
- Mesmo com formato de data corrigido

---

## 🛠️ Ferramentas Criadas para Investigação

### 1. **diagnostico-alertas.ps1**
Script PowerShell completo que testa:
- ✅ 8 passos diferentes
- ✅ Múltiplos formatos de data
- ✅ Todos os AlertTypes (SCHEDULED, THRESHOLD, URGENT, REMINDER)
- ✅ Todas as prioridades (1-5)
- ✅ Priority como int explícito

### 2. **diagnostico-alertas.py**
Script Python alternativo:
- ✅ Mais confiável para debug HTTP
- ✅ Mostra resposta completa da API
- ✅ Teste de formatos de data
- ✅ Verificação de alertas criados

### 3. **test-alert.bat**
Script CMD/Batch com curl:
- ✅ Teste direto via HTTP
- ✅ Mostra headers completos
- ✅ Bypass de problemas do PowerShell

---

## 🔎 Próximas Ações Recomendadas

### OPÇÃO 1: Via Swagger UI (MAIS RÁPIDO) ⭐

**Por que:** Interface visual, mostra erros de validação claramente

**Como fazer:**
1. Abrir: http://localhost:8080/swagger-ui.html
2. Ir em: POST /api/v1/alerts
3. Clicar em "Try it out"
4. Usar este JSON:
```json
{
  "itemId": "COLE-UM-ID-VALIDO-AQUI",
  "userId": "550e8400-e29b-41d4-a716-446655440001",
  "ruleId": "750e8400-e29b-41d4-a716-446655440005",
  "alertType": "SCHEDULED",
  "title": "Teste via Swagger",
  "message": "Criando alerta manual",
  "priority": 4,
  "dueAt": "2026-02-15T00:00:00Z"
}
```
5. Clicar "Execute"
6. **Verificar resposta:**
   - Se SUCESSO (201): Comparar JSON com o do PowerShell
   - Se ERRO (400): Ver mensagem de erro detalhada

### OPÇÃO 2: Verificar Logs da API Java

**Como:**
1. Ver console onde a API está rodando
2. Procurar por:
   - `MethodArgumentNotValidException`
   - `ConstraintViolationException`
   - Stack traces com "Alert"
3. Identificar qual campo está falhando validação

### OPÇÃO 3: Executar Scripts de Diagnóstico

**PowerShell:**
```powershell
.\diagnostico-alertas.ps1
```

**Python (se tiver instalado):**
```powershell
python diagnostico-alertas.py
```

**CMD/Batch:**
```
test-alert.bat
```

---

## 🤔 Possíveis Causas do Erro 400

### 1. **Validação de Campos**
- ✅ itemId: Testado, item existe
- ✅ userId: Correto
- ✅ ruleId: UUID válido gerado
- ✅ alertType: SCHEDULED é válido (enum)
- ✅ title: String não nula
- ✅ message: String não nula
- ⚠️ **priority**: Pode estar sendo enviado como string em vez de int
- ⚠️ **dueAt**: Formato pode não ser aceito

### 2. **Diferenças PowerShell vs Java/Jackson**
- PowerShell pode estar serializando números como strings
- JSON pode ter encoding diferente
- Headers podem estar faltando

### 3. **Validação de Negócio**
- CreateAlertUseCase verifica se item existe
- Se item não existir, retorna 400
- Pode ser problema de timing (item criado mas não commitado?)

---

## 📋 Checklist de Investigação

- [ ] **Testar via Swagger UI** (mais importante!)
- [ ] Verificar logs da API Java
- [ ] Executar diagnostico-alertas.ps1
- [ ] Comparar JSON que funciona vs JSON que falha
- [ ] Verificar se priority está como int ou string
- [ ] Tentar sem campo priority (usar default 3)
- [ ] Tentar sem campo triggeredAt
- [ ] Testar com data fixa: "2026-02-15T00:00:00Z"

---

## 💾 Estado dos Dados

### MongoDB (http://localhost:8081)

```
items:   15 documentos ✅
events:  75 documentos ✅
alerts:   0 documentos ⚠️
```

### Via API:

**Items funcionando:**
```
GET /api/v1/items?userId=550e8400-e29b-41d4-a716-446655440001
→ Retorna 15 items
```

**Eventos funcionando:**
```
GET /api/v1/events?itemId={id}
→ Retorna 5 eventos por item
```

**Alertas:**
```
GET /api/v1/alerts/pending?userId=550e8400-e29b-41d4-a716-446655440001
→ Retorna [] (vazio)
```

---

## 🎯 Objetivo

**Identificar POR QUE o endpoint POST /api/v1/alerts retorna 400**

Quando descobrir, o ajuste no `populate-simple.ps1` será simples,
assim como foi para os eventos (apenas trocar formato de data).

---

## 📝 Anotações

- Eventos funcionaram com `yyyy-MM-ddTHH:mm:ssZ`
- Alertas usam mesmo formato mas falham
- Não é problema de data (já testado)
- Provavelmente é outro campo
- **Swagger UI vai mostrar o erro exato!**

---

**Data:** 22/01/2026  
**Status:** Ferramentas de diagnóstico criadas, aguardando teste manual  
**Próximo passo:** Testar via Swagger UI

