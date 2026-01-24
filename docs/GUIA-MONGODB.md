# 🗄️ GUIA: Como Ver Coleções no MongoDB

**Item Control System**

---

## 🌐 MÉTODO 1: Mongo Express (Interface Web) - RECOMENDADO ✨

### ✅ **Mais Fácil e Visual**

#### Passo 1: Abrir no Navegador
```
http://localhost:8081
```

#### Passo 2: Navegar até o Database
1. Na tela inicial, você verá os databases disponíveis
2. Clique em **`item_control_db_dev`**

#### Passo 3: Ver as Coleções
Você verá as collections:
- 📦 **`items`** - Todos os items criados (Honda CB 500X, etc.)
- 📅 **`events`** - Todos os eventos registrados (manutenção, abastecimento, etc.)

#### Passo 4: Ver os Documentos
1. Clique em uma collection (exemplo: **`items`**)
2. Clique no botão **"View documents"**
3. Você verá todos os registros em formato JSON

#### Passo 5: Explorar um Documento
- Clique no **ícone de lupa** 🔍 para ver detalhes
- Veja todos os campos: `id`, `name`, `metadata`, `createdAt`, etc.

---

## 💻 MÉTODO 2: MongoDB CLI (Linha de Comando)

### Opção A: Comandos Rápidos

#### Listar todas as coleções:
```powershell
docker exec item-control-mongodb mongosh item_control_db_dev --quiet --eval "db.getCollectionNames()"
```

#### Ver todos os items:
```powershell
docker exec item-control-mongodb mongosh item_control_db_dev --quiet --eval "db.items.find().pretty()"
```

#### Ver todos os eventos:
```powershell
docker exec item-control-mongodb mongosh item_control_db_dev --quiet --eval "db.events.find().pretty()"
```

#### Contar documentos:
```powershell
# Contar items
docker exec item-control-mongodb mongosh item_control_db_dev --quiet --eval "db.items.countDocuments()"

# Contar eventos
docker exec item-control-mongodb mongosh item_control_db_dev --quiet --eval "db.events.countDocuments()"
```

#### Ver últimos 5 items criados:
```powershell
docker exec item-control-mongodb mongosh item_control_db_dev --quiet --eval "db.items.find().sort({createdAt: -1}).limit(5).pretty()"
```

### Opção B: Shell Interativo

#### Entrar no shell do MongoDB:
```bash
docker exec -it item-control-mongodb mongosh
```

#### Depois, dentro do shell:
```javascript
// Selecionar o database
use item_control_db_dev

// Listar coleções
db.getCollectionNames()

// Ver items
db.items.find().pretty()

// Ver eventos
db.events.find().pretty()

// Contar documentos
db.items.countDocuments()
db.events.countDocuments()

// Buscar um item específico pelo nome
db.items.find({name: "Honda CB 500X"}).pretty()

// Ver últimos eventos
db.events.find().sort({eventDate: -1}).limit(10).pretty()

// Sair
exit
```

---

## 🎯 MÉTODO 3: Script PowerShell Automatizado

### Usar o script criado:

```powershell
.\view-mongodb.ps1
```

**Menu interativo com opções:**
1. Listar todas as coleções
2. Ver documentos de ITEMS
3. Ver documentos de EVENTS
4. Contar documentos em cada coleção
5. Ver últimos 5 items criados
6. Ver últimos 5 eventos registrados
7. Abrir shell interativo do MongoDB

---

## 📊 CONSULTAS ÚTEIS

### Buscar item por ID:
```javascript
db.items.find({_id: "550e8400-e29b-41d4-a716-446655440001"})
```

### Buscar items por usuário:
```javascript
db.items.find({userId: "550e8400-e29b-41d4-a716-446655440001"})
```

### Buscar items por template:
```javascript
db.items.find({templateCode: "VEHICLE"})
```

### Buscar eventos por tipo:
```javascript
db.events.find({eventType: "MAINTENANCE"})
```

### Buscar eventos de um item específico:
```javascript
db.events.find({itemId: "ID-DO-ITEM"})
```

### Ver apenas campos específicos:
```javascript
// Ver apenas name e status dos items
db.items.find({}, {name: 1, status: 1, _id: 0})
```

### Buscar com filtros:
```javascript
// Items ativos
db.items.find({status: "ACTIVE"})

// Events de manutenção nos últimos 30 dias
var thirtyDaysAgo = new Date(Date.now() - 30*24*60*60*1000);
db.events.find({
  eventType: "MAINTENANCE",
  eventDate: {$gte: thirtyDaysAgo}
})
```

---

## 🎨 Exemplo de Saída

### Collection: items
```json
{
  "_id": "550e8400-e29b-41d4-a716-446655440001",
  "userId": "550e8400-e29b-41d4-a716-446655440001",
  "name": "Honda CB 500X",
  "nickname": "Motoca",
  "categoryId": "650e8400-e29b-41d4-a716-446655440002",
  "templateCode": "VEHICLE",
  "status": "ACTIVE",
  "tags": ["moto", "honda", "transporte"],
  "metadata": {
    "brand": "Honda",
    "model": "CB 500X",
    "year": 2020,
    "plate": "ABC-1234",
    "color": "Vermelha"
  },
  "createdAt": "2026-01-22T22:13:40.337Z",
  "updatedAt": "2026-01-22T22:13:40.337Z"
}
```

### Collection: events
```json
{
  "_id": "660e8400-e29b-41d4-a716-446655440003",
  "itemId": "550e8400-e29b-41d4-a716-446655440001",
  "userId": "550e8400-e29b-41d4-a716-446655440001",
  "eventType": "MAINTENANCE",
  "eventDate": "2026-01-22T22:30:00.000Z",
  "description": "Troca de óleo e filtro",
  "metrics": {
    "odometer": 15000,
    "cost": 350.00,
    "serviceName": "Troca de óleo completa",
    "nextMaintenanceKm": 20000
  },
  "createdAt": "2026-01-22T22:30:15.123Z"
}
```

---

## 🔍 Verificação Rápida

### Ver estrutura do database:
```powershell
docker exec item-control-mongodb mongosh item_control_db_dev --quiet --eval "
  print('Database: item_control_db_dev');
  print('Collections:');
  db.getCollectionNames().forEach(function(col) {
    print('  - ' + col + ': ' + db[col].countDocuments() + ' documentos');
  });
"
```

---

## 🌐 URLs Úteis

| Interface | URL | Descrição |
|-----------|-----|-----------|
| **Mongo Express** | http://localhost:8081 | Interface web do MongoDB |
| **Swagger API** | http://localhost:8080/swagger-ui.html | Testar endpoints REST |
| **API Health** | http://localhost:8080/actuator/health | Status da API |

---

## ✅ Checklist de Verificação

- [ ] Mongo Express acessível (http://localhost:8081)
- [ ] Database `item_control_db_dev` visível
- [ ] Collection `items` existe
- [ ] Collection `events` existe
- [ ] Documentos visíveis nas collections
- [ ] Campos estão corretos (metadata, tags, etc.)

---

## 🎯 Próximos Passos

Depois de visualizar as coleções:

1. ✅ Verificar se os dados estão corretos
2. ✅ Testar queries de busca
3. ✅ Validar relacionamentos (itemId nos events)
4. ✅ Verificar timestamps (createdAt, updatedAt)
5. ✅ Explorar o Mongo Express

---

**💡 Dica:** O **Mongo Express** é a forma mais fácil e visual de explorar seus dados! Use-o para desenvolvimento e testes.

---

**Criado em:** 22/01/2026  
**Versão:** 1.0

