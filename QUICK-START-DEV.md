# 🚀 INÍCIO RÁPIDO - Desenvolvimento Local

## ⚡ Começar AGORA (1 comando)

```powershell
.\scripts\start-all-dev.ps1
```

**Isso inicia automaticamente:**
- 🐳 MongoDB (Docker)
- 🚀 Backend API (Local - porta 8080)
- 🎨 Frontend (Local - porta 5173)

---

## 📍 Acessar Aplicação

Após executar o comando acima, acesse:

- **Frontend:** http://localhost:5173
- **Backend API:** http://localhost:8080
- **Swagger Docs:** http://localhost:8080/swagger-ui.html

---

## 🛑 Parar Tudo

```powershell
.\scripts\stop-all-dev.ps1
```

---

## 📚 Mais Informações

**Guia Completo:** `DEV-LOCAL-GUIDE.md`  
**Documentação Técnica:** `docs/043-setup-dev-local-completo.md`

---

## 🏗️ Arquitetura

```
MongoDB (Docker) ← Backend (Local) ← Frontend (Local)
  porta 27017       porta 8080         porta 5173
```

---

## 🎯 Componentes Individuais

### Apenas MongoDB
```powershell
.\scripts\start-mongodb-docker.ps1
```

### Apenas Backend
```powershell
.\scripts\start-backend-dev.ps1
# ou pular build:
.\scripts\start-backend-dev.ps1 -SkipBuild
```

### Apenas Frontend
```powershell
.\scripts\start-frontend-dev.ps1
```

---

## 🐛 Problemas?

### Porta em uso?
```powershell
# Ver o que está usando a porta
Get-NetTCPConnection -LocalPort 8080

# Parar tudo e reiniciar
.\scripts\stop-all-dev.ps1
.\scripts\start-all-dev.ps1
```

### Docker não inicia?
- Abra Docker Desktop
- Aguarde iniciar completamente
- Execute novamente

### Mais ajuda?
Ver: `DEV-LOCAL-GUIDE.md` → seção Troubleshooting

---

✅ **Pronto para desenvolver!** 🎉
