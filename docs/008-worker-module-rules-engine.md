# 🤖 WORKER MODULE - RULES ENGINE COMPLETO
**Data:** 2026-01-23  
**Status:** ✅ **IMPLEMENTADO**
---
## 📋 RESUMO EXECUTIVO
O **Worker Module** foi implementado com sucesso, completando o **Rules Engine** do sistema!
### ✅ O QUE FOI IMPLEMENTADO
#### **1. USE CASE DE PROCESSAMENTO (Core)**
- ✅ `ProcessRulesUseCase` - Processar todas as regras e gerar alertas
#### **2. SCHEDULER (Worker)**
- ✅ `RuleProcessorScheduler` - Scheduler automático
  - Processamento completo: **a cada hora**
  - Processamento urgente: **a cada 15 minutos**
#### **3. CONFIGURAÇÕES**
- ✅ `application.yml` - Configurações do Worker
- ✅ `WorkerApplication` - Aplicação Spring Boot
- ✅ Bean `ProcessRulesUseCase` no `UseCaseConfig`
---
## 🏗️ ARQUITETURA DO RULES ENGINE
```
┌─────────────────────────────────────────────┐
│           WORKER MODULE (Port 8081)         │
├─────────────────────────────────────────────┤
│                                             │
│  ┌───────────────────────────────────────┐ │
│  │  RuleProcessorScheduler               │ │
│  │  ├─ Cron: 0 0 * * * * (1 hora)        │ │
│  │  └─ Cron: 0 */15 * * * * (15 min)     │ │
│  └───────────┬───────────────────────────┘ │
│              │                              │
│  ┌───────────▼───────────────────────────┐ │
│  │  ProcessRulesUseCase                  │ │
│  │  ├─ findAllEnabled()                  │ │
│  │  ├─ evaluateRule()                    │ │
│  │  └─ generateAlert()                   │ │
│  └───────────┬───────────────────────────┘ │
│              │                              │
└──────────────┼──────────────────────────────┘
               │
               │ Usa
               ▼
┌─────────────────────────────────────────────┐
│         CORE MODULE - USE CASE              │
├─────────────────────────────────────────────┤
│  ProcessRulesUseCase.execute()              │
│  ├─ Busca regras ativas                     │
│  ├─ Avalia cada regra:                      │
│  │  ├─ TIME_BASED                           │
│  │  ├─ METRIC_BASED                         │
│  │  ├─ EVENT_COUNT                          │
│  │  └─ COMPOSITE                            │
│  ├─ Gera alertas quando necessário          │
│  └─ Retorna ProcessingResult                │
└─────────────┬───────────────────────────────┘
              │
              │ Acessa
              ▼
┌─────────────────────────────────────────────┐
│         REPOSITORIES (MongoDB)              │
├─────────────────────────────────────────────┤
│  • RuleRepository                           │
│  • ItemRepository                           │
│  • EventRepository                          │
│  • AlertRepository                          │
└─────────────────────────────────────────────┘
```
---
## 🔧 TIPOS DE REGRAS SUPORTADAS
### **1. TIME_BASED - Regras Temporais**
Avalia tempo decorrido desde último evento ou criação do item.
**Exemplo:** Troca de óleo a cada 6 meses
```json
{
  "ruleType": "TIME_BASED",
  "conditions": {
    "intervalDays": 180,
    "warningDaysBefore": 15,
    "severity": "WARNING"
  }
}
```
**Lógica:**
- Calcula dias desde último evento
- Gera alerta quando `daysSinceEvent >= (interval - warning)`
- No exemplo: alerta 15 dias antes dos 180 dias
---
### **2. METRIC_BASED - Regras por Métrica**
Avalia valores numéricos no metadata do item.
**Exemplo:** Troca de óleo a cada 5.000 km
```json
{
  "ruleType": "METRIC_BASED",
  "conditions": {
    "metricField": "currentKm",
    "threshold": 15000,
    "warningThreshold": 14500,
    "severity": "URGENT"
  }
}
```
**Lógica:**
- Compara valor atual com threshold
- Gera alerta quando `current >= warningThreshold`
- No exemplo: alerta quando km >= 14.500
---
### **3. EVENT_COUNT - Regras por Contagem**
Avalia quantidade de eventos em um período.
**Exemplo:** Alerta após 3 falhas em 30 dias
```json
{
  "ruleType": "EVENT_COUNT",
  "conditions": {
    "countThreshold": 3,
    "periodDays": 30,
    "severity": "URGENT"
  }
}
```
**Lógica:**
- Conta eventos nos últimos N dias
- Gera alerta quando `eventCount >= threshold`
---
### **4. COMPOSITE - Regras Compostas**
Combina TIME_BASED + METRIC_BASED com operadores AND/OR.
**Exemplo:** Alerta por tempo OU km
```json
{
  "ruleType": "COMPOSITE",
  "conditions": {
    "intervalDays": 180,
    "warningDaysBefore": 15,
    "metricField": "currentKm",
    "threshold": 15000,
    "warningThreshold": 14500,
    "operator": "OR",
    "severity": "WARNING"
  }
}
```
**Lógica:**
- Avalia condição temporal
- Avalia condição métrica
- Combina com operador (AND/OR)
---
## ⚙️ PROCESSAMENTO DE REGRAS
### **Fluxo Completo**
```
1️⃣  Scheduler dispara (hora cheia ou 15 min)
           ↓
2️⃣  Busca TODAS as regras ativas
           ↓
3️⃣  Para cada regra:
    ├─ Busca o Item
    ├─ Busca os Events
    ├─ Avalia condições da regra
    └─ Decide se gera alerta
           ↓
4️⃣  Se deve alertar:
    ├─ Verifica se já existe alerta recente
    ├─ Gera novo Alert
    └─ Salva no MongoDB
           ↓
5️⃣  Retorna estatísticas:
    ├─ Total de regras
    ├─ Regras processadas
    ├─ Alertas gerados
    └─ Erros encontrados
```
### **Prevenção de Duplicatas**
O sistema **NÃO gera alertas duplicados**:
- Verifica se existe alerta PENDING dos últimos 24h
- Compara mensagem com nome da regra
- Se existe, **pula** a geração
---
## 📊 LOGS DO WORKER
### **Exemplo de Log (Processamento)**
```
╔════════════════════════════════════════════════════════════╗
║          INICIANDO PROCESSAMENTO DE REGRAS                ║
╚════════════════════════════════════════════════════════════╝
⏰ Início: 2026-01-23 16:00:00
┌────────────────────────────────────────────────────────┐
│                RESULTADO DO PROCESSAMENTO              │
├────────────────────────────────────────────────────────┤
│  📊 Total de Regras:           5                       │
│  ✅ Regras Processadas:        5                       │
│  🚨 Alertas Gerados:           2                       │
│  ❌ Erros:                     0                       │
│  ⏱️  Tempo de Execução:       127 ms                    │
└────────────────────────────────────────────────────────┘
✅ Processamento concluído com sucesso!
════════════════════════════════════════════════════════════
```
---
## 🚀 COMO USAR
### **1. Iniciar Worker**
```powershell
.\scripts\start-worker.ps1
```
O Worker inicia na porta **8081**.
### **2. Verificar Logs**
Os logs aparecem em tempo real no console:
- **DEBUG**: Detalhes de cada regra processada
- **INFO**: Estatísticas gerais
- **WARN**: Alertas gerados
- **ERROR**: Erros no processamento
### **3. Configurar Horários**
Edite `modules/worker/src/main/resources/application.yml`:
```yaml
scheduler:
  rule-processor:
    cron: "0 0 * * * *"        # A cada hora
    quick-cron: "0 */15 * * * *"  # A cada 15 min
```
**Exemplos de Cron:**
- `0 0 * * * *` → A cada hora (00 min)
- `0 */15 * * * *` → A cada 15 minutos
- `0 0 */2 * * *` → A cada 2 horas
- `0 0 8 * * *` → Todos os dias às 8h
- `0 0 0 * * MON` → Segundas às 00h
---
## 🧪 TESTE MANUAL
### **Cenário: Troca de Óleo**
#### **1. Criar Item (Veículo)**
```bash
POST /api/v1/items
{
  "name": "Honda CB 500X",
  "userId": "550e8400-e29b-41d4-a716-446655440001",
  "metadata": {
    "currentKm": 14500,
    "lastOilChangeKm": 10000
  }
}
```
#### **2. Criar Regra**
```bash
POST /api/v1/rules
{
  "itemId": "<ITEM_ID>",
  "name": "Troca de Óleo - 5000km",
  "ruleType": "METRIC_BASED",
  "enabled": true,
  "conditions": {
    "metricField": "currentKm",
    "threshold": 15000,
    "warningThreshold": 14500,
    "severity": "WARNING"
  }
}
```
#### **3. Aguardar Processamento**
O Worker processará a regra automaticamente:
- No próximo ciclo de 15 minutos (urgente)
- Ou na próxima hora cheia (completo)
#### **4. Verificar Alerta Gerado**
```bash
GET /api/v1/alerts?userId=550e8400-e29b-41d4-a716-446655440001
```
**Response esperado:**
```json
[
  {
    "id": "...",
    "itemId": "...",
    "type": "WARNING",
    "message": "⚠️ Alerta: Troca de Óleo - 5000km - ...",
    "status": "PENDING",
    "createdAt": "2026-01-23T16:15:00Z"
  }
]
```
---
## 📈 ESTATÍSTICAS
### **ProcessingResult**
Classe que retorna estatísticas do processamento:
```java
ProcessingResult result = processRulesUseCase.execute();
result.getTotalRules();        // Total de regras ativas
result.getProcessedRules();    // Regras processadas com sucesso
result.getAlertsGenerated();   // Alertas gerados
result.getErrors();            // Lista de erros
result.hasErrors();            // true se houve erros
```
---
## 🔍 TROUBLESHOOTING
### **Worker não inicia**
```
Erro: Could not find or load main class
```
**Solução:**
```bash
mvn clean install
```
---
### **Regras não são processadas**
**Verificar:**
1. Regras estão `enabled: true`?
2. MongoDB está rodando?
3. Scheduler está ativado no `application.yml`?
---
### **Alertas duplicados**
O sistema **previne** duplicatas automaticamente:
- Verifica alertas PENDING dos últimos 24h
- Compara mensagem com nome da regra
Se ainda ocorrer, verificar:
- Múltiplas regras com mesmo nome
- Scheduler executando em duplicata
---
## ✅ CONCLUSÃO
O **Worker Module está 100% funcional**!
🎯 **Objetivos Alcançados:**
- ✅ `ProcessRulesUseCase` implementado
- ✅ Scheduler automático (hora + 15 min)
- ✅ 4 tipos de regras suportadas
- ✅ Geração automática de alertas
- ✅ Logs detalhados
- ✅ Prevenção de duplicatas
- ✅ Documentação completa
---
**Desenvolvido por:** Harlem Silva  
**Data:** 23/01/2026  
**Versão:** 0.1.0-SNAPSHOT
