# 🔧 GUIA: ALTERNAR ENTRE LOCAL E PRODUÇÃO

## Arquivos Criados

`.env` (ativo) - Desenvolvimento LOCAL
`.env.production` - Produção (Render)
`.env.example` - Exemplo/Referência

---

## Como Usar

### OPÇÃO 1: Desenvolvimento Local (Atual) ✅

**Arquivo:** `.env`
**API URL:** /api/v1
**Uso:**

```bash
npm run dev
```

**Para quando:**

- Testar localmente
- Desenvolver features
- Debugar problemas
- Backend rodando em Docker

---

### OPÇÃO 2: Produção (Render)

**Arquivo:** `.env.production`
**API URL:** https://item-control-api.onrender.com/api/v1
**Uso:**

```bash
# Build de produção
npm run build

# Ou preview local com URL produção
npm run preview
```

**Para quando:**

- Testar integração com Render
- Deploy
- Demonstrações

---

## Alternar Entre Ambientes

### Método 1: Trocar Arquivo .env (Simples)

**Para usar LOCAL:**

```powershell
# .env já está configurado para local
# Basta rodar:
npm run dev
```

**Para usar PRODUÇÃO:**

```powershell
# Editar .env manualmente e trocar URL
# OU copiar .env.production para .env
Copy-Item .env.production .env -Force
npm run dev
```

**Voltar para LOCAL:**

```powershell
# Recriar .env com URL local
@'
VITE_API_URL=http://localhost:8080/api/v1
'@ | Out-File .env -Encoding UTF8
```

---

### Método 2: Scripts no package.json (Recomendado)

**Adicione em package.json:**

```json
{
  \"scripts\": {
    \"dev\": \"vite\",
    \"dev:prod\": \"vite --mode production\",
    \"build\": \"vite build\",
    \"preview\": \"vite preview\"
  }
}
```

**Uso:**

```bash
# Desenvolvimento local
npm run dev

# Desenvolvimento apontando para produção
npm run dev:prod

# Build de produção
npm run build
```

---

## Verificar Configuração Atual

```powershell
# Ver qual URL está configurada
Get-Content .env
```

---

## ✅ CONFIGURAÇÃO ATUAL

**Arquivo ativo:** `.env`
**API URL:** /api/v1
**Modo:** Desenvolvimento LOCAL ✅
**Para testar:**

1. Iniciar backend local (porta 8080)
2. npm run dev
3. Abrir http://localhost:5173
4. Frontend chamará /api/v1 (proxy do Vite) e o Vite encaminha para localhost:8080

> Nota: se você configurar uma URL completa (ex.: http://localhost:8080/api/v1) no navegador, pode precisar habilitar CORS no backend. No dev, prefira `/api/v1`.

---

## 🚀 Próximos Passos

**AGORA (Local):**

```powershell
# 1. Iniciar backend
cd ..\item-control-system\modules\api
mvn spring-boot:run

# 2. Popular dados
cd ..\..\scripts
.\populate-test-data-local.ps1

# 3. Iniciar frontend
cd ..\..\item-control-frontend
npm run dev
```

**DEPOIS (Produção):**

```powershell
# Trocar para produção
Copy-Item .env.production .env -Force

# Ou editar .env e trocar URL manualmente
```

**ARQUIVOS CRIADOS:**

- ✅ .env (LOCAL - ativo)
- ✅ .env.production (PRODUÇÃO)
- ✅ .env.example (REFERÊNCIA)
  **FRONTEND CONFIGURADO PARA TESTE LOCAL! 🎉**
