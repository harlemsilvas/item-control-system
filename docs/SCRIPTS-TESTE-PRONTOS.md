# ✅ SCRIPTS DE TESTE CRIADOS!

**Data:** 22/01/2026  
**Status:** ✅ **PRONTO PARA USO**

---

## 📦 ARQUIVOS CRIADOS

### 1. `populate-test-data.ps1` ⭐
**Script principal de população**
- Cria 15 itens variados
- Registra 75 eventos com datas retroativas
- Gera ~30 alertas baseados nos eventos
- Dados realistas e prontos para testes

### 2. `run-populate.ps1`
**Executor inteligente**
- Verifica se MongoDB está rodando
- Aguarda API ficar pronta (até 60 segundos)
- Executa populate-test-data.ps1
- Tratamento de erros completo

### 3. `DADOS-TESTE.md`
**Documentação completa**
- Lista todos os 15 itens que serão criados
- Detalha os eventos de cada tipo
- Explica os alertas gerados
- Casos de uso para testes
- Como verificar e limpar dados

---

## 🎯 DADOS QUE SERÃO CRIADOS

### 📊 Resumo:
```
15 Items:
  ├── 5 Veículos (motos e carros)
  ├── 5 Contas (água, luz, internet, etc.)
  └── 5 Consumíveis (galão água, gás, etc.)

75 Eventos (5 por item):
  ├── MAINTENANCE (manutenções)
  ├── CONSUMPTION (consumos/pagamentos)
  └── INSPECTION (revisões)

~30 Alertas (2 por item):
  ├── Prioridade 5: Urgente
  ├── Prioridade 4: Alta
  ├── Prioridade 3: Média
  └── Prioridade 2: Baixa
```

---

## 🚀 COMO EXECUTAR

### Passo 1: Certifique-se que MongoDB está rodando
```powershell
docker compose up -d
```

### Passo 2: Inicie a API (em outra janela)
```powershell
cd modules/api
java -jar target/item-control-api-0.1.0-SNAPSHOT.jar --spring.profiles.active=dev
```

### Passo 3: Execute o script
```powershell
# Opção A - Com espera automática pela API
.\run-populate.ps1

# Opção B - Direto (se API já estiver rodando)
.\populate-test-data.ps1
```

---

## 📋 EXEMPLOS DE DADOS

### Veículos Criados:
1. **Honda CB 500X** - Moto vermelha, 15.000 km
2. **Toyota Corolla** - Sedan prata, 32.000 km
3. **Chevrolet Onix** - Compacto branco, 18.500 km
4. **Yamaha Fazer 250** - Moto azul, 42.000 km
5. **Fiat Uno** - Econômico vermelho, 65.000 km

### Contas Criadas:
1. **Conta de Água** - SABESP, R$ 85,50
2. **Conta de Luz** - Enel, R$ 152,30
3. **Internet Fibra** - Vivo 300 Mbps, R$ 119,90
4. **Condomínio** - APT-401, R$ 450,00
5. **Telefone Celular** - Claro, R$ 89,90

### Consumíveis Criados:
1. **Galão de Água 20L** - R$ 12,50
2. **Botijão de Gás 13kg** - R$ 95,00
3. **Filtro de Café** - R$ 8,90
4. **Papel Higiênico** - R$ 18,50
5. **Detergente Líquido** - R$ 2,90

---

## 🔍 VERIFICAR DADOS APÓS EXECUÇÃO

### Via Swagger UI:
```
http://localhost:8080/swagger-ui.html
```

**Endpoints para testar:**
```
GET /api/v1/items?userId=550e8400-e29b-41d4-a716-446655440001
GET /api/v1/events?itemId={item-id}
GET /api/v1/alerts/pending?userId=550e8400-e29b-41d4-a716-446655440001
```

### Via Mongo Express:
```
http://localhost:8081
```

**Collections:**
- `items` - 15 documentos
- `events` - 75 documentos
- `alerts` - ~30 documentos

### Via MongoDB CLI:
```bash
docker exec -it item-control-mongodb mongosh
use item_control_db_dev
db.items.countDocuments()    # Deve retornar 15
db.events.countDocuments()   # Deve retornar 75
db.alerts.countDocuments()   # Deve retornar ~30
```

---

## 💡 CASOS DE USO PARA TESTAR

### 1. Dashboard de Alertas
Listar todos os alertas pendentes ordenados por prioridade.

### 2. Histórico de Veículo
Ver todas as manutenções e abastecimentos de um veículo específico.

### 3. Análise de Contas
Verificar histórico de pagamentos e detectar atrasos.

### 4. Controle de Estoque
Monitorar níveis de consumíveis e reposições.

### 5. Workflow de Alerta
Marcar alertas como lidos → Resolver → Verificar status.

---

## 🧹 LIMPAR DADOS DE TESTE

```bash
# Via MongoDB CLI
docker exec -it item-control-mongodb mongosh
use item_control_db_dev
db.items.deleteMany({})
db.events.deleteMany({})
db.alerts.deleteMany({})
exit

# Ou reiniciar tudo
docker compose down -v
docker compose up -d
```

---

## 📊 EXEMPLO DE SAÍDA DO SCRIPT

```
========================================
🧪 POPULANDO SISTEMA COM DADOS DE TESTE
========================================

✅ API está rodando!

📦 Criando 15 itens variados...

  ✅ Honda CB 500X
  ✅ Toyota Corolla
  ✅ Chevrolet Onix
  ...

✅ 15 itens criados!

📅 Criando 5 eventos retroativos para cada item...

  📦 Honda CB 500X:
    ✅ Troca de óleo e filtro
    ✅ Abastecimento completo
    ✅ Troca de pneus
    ✅ Revisão periódica
    ✅ Abastecimento
  ...

✅ 75 eventos criados!

🔔 Criando alertas baseados nos eventos...

  📦 Honda CB 500X:
    🔔 Troca de óleo vencendo
    🔔 Revisão periódica
  ...

✅ 30 alertas criados!

========================================
✅ POPULAÇÃO DE DADOS CONCLUÍDA!
========================================

📊 Estatísticas:
  📦 Items criados:   15
  📅 Eventos criados: 75
  🔔 Alertas criados: 30

🎉 Sistema populado com dados realistas para testes!
```

---

## 🎯 PRÓXIMOS PASSOS

Após executar a população:

1. ✅ Abrir Swagger UI e explorar endpoints
2. ✅ Ver dados no Mongo Express
3. ✅ Testar filtros e consultas
4. ✅ Marcar alertas como lidos
5. ✅ Resolver alertas
6. ✅ Registrar novos eventos
7. ✅ Criar novos alertas

---

## 📚 DOCUMENTAÇÃO

- **DADOS-TESTE.md** - Documentação completa dos dados
- **populate-test-data.ps1** - Script principal (comentado)
- **run-populate.ps1** - Executor com validações

---

**🎊 Scripts prontos para popular seu sistema com dados realistas de teste!** 🚀

---

**Criado em:** 22/01/2026  
**Versão:** 1.0  
**Status:** ✅ PRONTO PARA USO

