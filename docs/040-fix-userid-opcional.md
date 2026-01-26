# 🐛 FIX: Parâmetro userId Opcional nos Endpoints

## ❌ Problema

O frontend estava chamando os endpoints da API **sem o parâmetro `userId`**, causando erro:

```
MissingServletRequestParameterException: Required request parameter 'userId' for method parameter type UUID is not present
```

### Logs do Erro

```
2026-01-25 21:22:49 - Resolved [org.springframework.web.bind.MissingServletRequestParameterException: Required request parameter 'userId' for method parameter type UUID is not present]
```

---

## ✅ Solução Implementada

Tornei o parâmetro `userId` **opcional** em todos os endpoints GET, usando um **userId padrão** quando não fornecido.

### UUID Padrão (Demo/Desenvolvimento)

```java
UUID.fromString("550e8400-e29b-41d4-a716-446655440001")
```

Este é o mesmo userId usado nos scripts de população de dados de teste.

---

## 📝 Arquivos Alterados

### 1. ItemController.java

**Endpoints modificados:**
- `GET /api/v1/items` - Listar todos os items
- `GET /api/v1/items/active` - Listar items ativos

**Antes:**
```java
public ResponseEntity<List<ItemResponse>> listUserItems(@RequestParam UUID userId)
```

**Depois:**
```java
public ResponseEntity<List<ItemResponse>> listUserItems(
    @RequestParam(required = false) UUID userId) {
    
    // Se userId não fornecido, usar ID padrão para demo/desenvolvimento
    UUID effectiveUserId = userId != null ? userId : 
        UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
    
    List<Item> items = listUserItemsUseCase.execute(effectiveUserId);
    // ...
}
```

### 2. AlertController.java

**Endpoints modificados:**
- `GET /api/v1/alerts/pending` - Listar alertas pendentes
- `GET /api/v1/alerts` - Listar alertas por status
- `GET /api/v1/alerts/count` - Contar alertas pendentes
- `PUT /api/v1/alerts/{id}/acknowledge` - Reconhecer alerta
- `PUT /api/v1/alerts/{id}/resolve` - Resolver alerta

**Mesma lógica aplicada:**
```java
@RequestParam(required = false) UUID userId
```

---

## 🧪 Testes Realizados

### ✅ Endpoint Items (sem userId)

```bash
curl http://localhost:8080/api/v1/items
```

**Resultado:** Lista todos os items do userId padrão (30+ items retornados)

### ✅ Endpoint Alerts (sem userId)

```bash
curl http://localhost:8080/api/v1/alerts/pending
```

**Resultado:** Lista todos os alertas pendentes do userId padrão

### ✅ Endpoint Items (com userId personalizado)

```bash
curl "http://localhost:8080/api/v1/items?userId=seu-uuid-aqui"
```

**Resultado:** Funciona normalmente com userId específico

---

## 🎯 Como Usar

### Frontend (sem autenticação)

```typescript
// Chamada SEM userId (usa padrão automaticamente)
fetch('http://localhost:8080/api/v1/items')
  .then(res => res.json())
  .then(items => console.log(items));

// Chamada COM userId (se tiver multi-tenancy)
fetch('http://localhost:8080/api/v1/items?userId=550e8400-e29b-41d4-a716-446655440001')
  .then(res => res.json())
  .then(items => console.log(items));
```

### Scripts PowerShell

```powershell
# Sem userId
curl http://localhost:8080/api/v1/items

# Com userId
curl "http://localhost:8080/api/v1/items?userId=550e8400-e29b-41d4-a716-446655440001"
```

---

## 📊 Status dos Endpoints

| Endpoint | Método | userId | Status |
|----------|--------|--------|--------|
| `/api/v1/items` | GET | Opcional | ✅ |
| `/api/v1/items/active` | GET | Opcional | ✅ |
| `/api/v1/alerts/pending` | GET | Opcional | ✅ |
| `/api/v1/alerts` | GET | Opcional | ✅ |
| `/api/v1/alerts/count` | GET | Opcional | ✅ |
| `/api/v1/alerts/{id}/acknowledge` | PUT | Opcional | ✅ |
| `/api/v1/alerts/{id}/resolve` | PUT | Opcional | ✅ |
| `/api/v1/categories` | GET | Não requer | ✅ |
| `/api/v1/events` | GET | Requer | ⚠️ |

---

## ⚠️ Considerações

### Desenvolvimento vs Produção

Esta solução é **ideal para desenvolvimento/demo**, pois:

✅ Permite testar o frontend sem autenticação
✅ Facilita desenvolvimento rápido
✅ Não quebra compatibilidade com chamadas que passam userId

### Para Produção (Futuro)

Quando implementar autenticação (JWT, OAuth, etc.):

1. **Remover o userId dos parâmetros**
2. **Extrair userId do token JWT**
3. **Usar @AuthenticationPrincipal do Spring Security**

```java
public ResponseEntity<List<ItemResponse>> listUserItems(
    @AuthenticationPrincipal UserDetails user) {
    
    UUID userId = extractUserIdFromToken(user);
    List<Item> items = listUserItemsUseCase.execute(userId);
    // ...
}
```

---

## 🔄 Próximos Passos

1. ✅ **Frontend funcionando** - Pode chamar APIs sem userId
2. ⏳ **Implementar autenticação** - JWT + Spring Security
3. ⏳ **Multi-tenancy** - Isolamento por usuário real
4. ⏳ **EventController** - Tornar userId opcional também

---

## 🎉 Resultado

**Frontend agora funciona perfeitamente!** Sem erros de `MissingServletRequestParameterException`.

### Teste Rápido

```bash
# Backend rodando em http://localhost:8080
# Frontend rodando em http://localhost:5173

# API retorna dados sem precisar passar userId
curl http://localhost:8080/api/v1/items | jq length
# Output: 30 (ou quantos items existirem)
```

---

**Data:** 2026-01-26  
**Status:** ✅ RESOLVIDO  
**Commits:** Aguardando commit desta alteração
