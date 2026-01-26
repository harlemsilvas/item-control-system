# 🚀 QUICK START - Ambiente Local

## ⚡ 3 Passos Rápidos

### 1. Inicie o Docker Desktop
Aguarde o ícone ficar verde ✅

### 2. Inicie o MongoDB
```powershell
.\scripts\start-mongodb-local.ps1
```

### 3. Inicie a Aplicação
```powershell
.\scripts\start-app-local.ps1
```

---

## ✅ Testar

```powershell
# Health Check
curl http://localhost:8080/actuator/health

# API funcionando
curl http://localhost:8080/api/v1/categories
```

---

## 📚 Documentação Completa

Ver [docs/039-setup-local-completo.md](docs/039-setup-local-completo.md)

---

## 🐛 Problemas?

**Docker não inicia?** → Abra o Docker Desktop primeiro

**Porta 8080 ocupada?**
```powershell
Get-NetTCPConnection -LocalPort 8080 | Select-Object -ExpandProperty OwningProcess | Stop-Process -Force
```

**MongoDB não conecta?**
```powershell
docker start mongodb
```
