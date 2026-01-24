# 🔧 SOLUÇÃO: Erro 400 ao Criar Item

**Erro:** `O servidor remoto retornou um erro: (400) Solicitação Incorreta`

---

## ❌ Problema

O JSON enviado estava incorreto:

**Formato ERRADO:**
```json
{
  "name": "Item Teste",
  "userId": "user-railway-test",  // ❌ String (deveria ser UUID)
  "categoryId": null                // ❌ null explícito causa problema
}
```

**Campos faltando:**
- ❌ `templateCode` é **obrigatório** (@NotBlank)
- ❌ `userId` deve ser **UUID**, não string

---

## ✅ Solução

**Formato CORRETO:**
```json
{
  "userId": "123e4567-e89b-12d3-a456-426614174000",  // ✅ UUID válido
  "name": "Primeiro Item Railway",
  "nickname": "item-railway-001",
  "templateCode": "GENERAL",                          // ✅ Obrigatório
  "tags": ["railway", "teste"],
  "metadata": {
    "ambiente": "railway",
    "descricao": "Teste"
  }
}
```

---

## 🚀 Como Criar Item Corretamente

### Opção 1: Script Automatizado (RECOMENDADO)

```powershell
# 1. Iniciar API (Terminal 1)
.\scripts\quick-start-prod.ps1

# 2. Criar item (Terminal 2, após API iniciar)
.\scripts\criar-primeiro-item.ps1
```

---

### Opção 2: Manual com PowerShell

```powershell
# 1. Gerar UUID
$userId = [System.Guid]::NewGuid().ToString()

# 2. Criar dados do item
$itemData = @{
    userId = $userId
    name = "Primeiro Item Railway"
    nickname = "item-railway-001"
    templateCode = "GENERAL"
    tags = @("railway", "teste")
    metadata = @{
        ambiente = "railway"
        descricao = "Teste"
    }
} | ConvertTo-Json

# 3. Enviar requisição
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/items" `
    -Method POST `
    -Body $itemData `
    -ContentType "application/json; charset=utf-8"
```

---

## 📋 Campos do CreateItemRequest

| Campo | Tipo | Obrigatório | Observações |
|-------|------|-------------|-------------|
| `userId` | UUID | ✅ Sim | Deve ser UUID válido |
| `name` | String | ✅ Sim | Não pode ser vazio |
| `templateCode` | String | ✅ Sim | Ex: "GENERAL", "CUSTOM" |
| `nickname` | String | ❌ Não | Identificador amigável |
| `categoryId` | UUID | ❌ Não | Se não informar, omitir do JSON |
| `tags` | Array | ❌ Não | Lista de tags |
| `metadata` | Object | ❌ Não | Dados customizados |

---

## ✅ Validações

O DTO aplica estas validações:

```java
@NotNull(message = "UserId cannot be null")
private UUID userId;

@NotBlank(message = "Name cannot be blank")
private String name;

@NotBlank(message = "TemplateCode cannot be blank")
private String templateCode;
```

**Erros comuns:**
- ❌ userId como string → `userId: "user123"` 
- ✅ userId como UUID → `userId: "123e4567-e89b-12d3-a456-426614174000"`

- ❌ templateCode ausente → Erro 400
- ✅ templateCode presente → `templateCode: "GENERAL"`

---

## 🧪 Testar Agora

### Terminal 1 - Iniciar API
```powershell
.\scripts\quick-start-prod.ps1
```

### Terminal 2 - Criar Item (após 15 segundos)
```powershell
.\scripts\criar-primeiro-item.ps1
```

### MongoDB Compass - Verificar
1. Conecte: `mongodb://mongo:vrzaNIBSuwNrVIMLLvKfmuiJwBFvglAG@hopper.proxy.rlwy.net:40930`
2. Atualize (F5)
3. Database: `item_control_db`
4. Collection: `items` ✅ Criada!

---

## 📚 Scripts Atualizados

✅ `scripts/criar-primeiro-item.ps1` - Criar item com validação  
✅ `scripts/teste-railway-primeiro-item.ps1` - Teste completo corrigido  
✅ `docs/013-railway-problema-resolvido.md` - Documentação atualizada  

---

## ✅ Checklist

Antes de criar item:

- [ ] API está rodando (`quick-start-prod.ps1`)
- [ ] MongoDB Railway conectado
- [ ] userId é UUID válido
- [ ] name não está vazio
- [ ] templateCode está presente

---

**Status:** ✅ CORRIGIDO  
**Próximo teste:** Execute `.\scripts\criar-primeiro-item.ps1`

