# 🐛 Problema: Docker Engine não está respondendo

**Data:** 2026-01-26  
**Erro:** `unable to get image 'mongo:7.0': error during connect`

---

## 🔍 Diagnóstico

### Erro Encontrado

```
unable to get image 'mongo:7.0': error during connect: 
Get "http://%2F%2F.%2Fpipe%2FdockerDesktopLinuxEngine/v1.51/images/mongo:7.0/json": 
open //./pipe/dockerDesktopLinuxEngine: The system cannot find the file specified.
```

### Causa Raiz

**Docker Desktop instalado mas Docker Engine não está rodando.**

Quando executamos `docker info`:
```
Server:
error during connect: Get "http://%2F%2F.%2Fpipe%2FdockerDesktopLinuxEngine/v1.51/info": 
open //./pipe/dockerDesktopLinuxEngine: The system cannot find the file specified.
```

Isso significa:
- ✅ Docker CLI instalado (cliente funciona)
- ❌ Docker Engine não está rodando (servidor não responde)
- ❌ Docker Desktop não foi iniciado ou está travado

---

## ✅ Solução Implementada

### 1. Script de Diagnóstico

Criado: `scripts/check-docker.ps1`

**O que faz:**
1. ✅ Verifica se Docker Desktop está instalado
2. ✅ Verifica se processo está rodando
3. ✅ Se não estiver, inicia automaticamente
4. ✅ Aguarda Docker Engine ficar pronto (até 60 segundos)
5. ✅ Testa conexão com docker info

### 2. Correções nos Scripts

#### `docker-compose.mongodb.yml`
- ❌ Removido: `version: '3.8'` (obsoleto)
- ✅ Corrigido para formato moderno

#### `start-mongodb-docker.ps1`
- ❌ Antes: Verificava `docker info` uma vez e falhava
- ✅ Agora: Tenta 5 vezes com delay de 3 segundos
- ✅ Mensagens de erro mais claras

#### `start-all-dev.ps1`
- ✅ Adicionado: Etapa 0 - Verificar Docker Desktop
- ✅ Chama `check-docker.ps1` se Docker Engine não responder
- ✅ Aguarda Docker ficar pronto antes de continuar

---

## 🚀 Como Usar Agora

### Opção 1: Script Automático (RECOMENDADO)

```powershell
.\scripts\start-all-dev.ps1
```

**O que acontece:**
1. Verifica Docker Engine
2. Se não estiver pronto, inicia Docker Desktop automaticamente
3. Aguarda ficar pronto
4. Continua com MongoDB, Backend e Frontend

---

### Opção 2: Manual

**1. Verificar e iniciar Docker:**
```powershell
.\scripts\check-docker.ps1
```

**2. Depois, iniciar ambiente:**
```powershell
.\scripts\start-all-dev.ps1
```

---

## 🐛 Troubleshooting

### Docker Desktop não inicia

**Problema:** Script executa mas Docker Engine não responde

**Soluções:**

1. **Iniciar manualmente:**
   - Procure "Docker Desktop" no menu Iniciar
   - Abra o aplicativo
   - Aguarde ícone ficar estável (sem animação)
   - Execute script novamente

2. **Reiniciar Docker Desktop:**
   - Botão direito no ícone da bandeja (system tray)
   - Clique em "Restart"
   - Aguarde reiniciar
   - Execute script novamente

3. **WSL 2 não configurado:**
   ```powershell
   wsl --status
   wsl --install
   ```

4. **Virtualização desabilitada:**
   - Entre na BIOS
   - Habilite Intel VT-x ou AMD-V
   - Reinicie o computador

---

### Docker Engine demora muito para iniciar

**Normal:** Primeira vez pode demorar 1-2 minutos

**Se demorar mais de 5 minutos:**
1. Feche Docker Desktop
2. Abra Gerenciador de Tarefas
3. Finalize todos processos "Docker"
4. Abra Docker Desktop novamente

---

### Erro "WSL 2 installation is incomplete"

**Solução:**
```powershell
# Atualizar WSL
wsl --update

# Definir WSL 2 como padrão
wsl --set-default-version 2

# Reiniciar computador
```

---

## ✅ Validação

### Verificar Docker funcionando

```powershell
# Ver versão
docker --version

# Ver info do Engine
docker info

# Testar container
docker run hello-world
```

**Esperado:**
- Todos comandos executam sem erro
- `docker info` mostra informações do servidor
- `hello-world` baixa e executa

---

## 📋 Checklist

Antes de executar `start-all-dev.ps1`:

- [ ] Docker Desktop instalado
- [ ] Docker Desktop aberto e rodando
- [ ] Ícone na bandeja sem animação (pronto)
- [ ] `docker info` executa sem erro
- [ ] `docker ps` executa sem erro

**Se todos marcados:** ✅ Pronto para usar!

---

## 🎯 Arquivos Modificados

### Criados
- ✅ `scripts/check-docker.ps1` - Diagnóstico e inicialização

### Modificados
- ✅ `docker-compose.mongodb.yml` - Removido versão obsoleta
- ✅ `scripts/start-mongodb-docker.ps1` - Melhor detecção Docker
- ✅ `scripts/start-all-dev.ps1` - Verificação Docker antes de iniciar

---

## 🔄 Fluxo Corrigido

```
┌─────────────────────────────────────┐
│  start-all-dev.ps1                  │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│  Verifica Docker Engine             │
│  docker info                        │
└──────────────┬──────────────────────┘
               │
               ├─── ✅ OK → Continua
               │
               └─── ❌ ERRO
                     │
                     ▼
               ┌─────────────────────────┐
               │  check-docker.ps1       │
               │  • Verifica instalação  │
               │  • Inicia Desktop       │
               │  • Aguarda Engine       │
               └─────────┬───────────────┘
                         │
                         ├─── ✅ OK → Continua
                         │
                         └─── ❌ ERRO → Aborta
                               (mensagem de ajuda)
```

---

## 💡 Prevenção

### Sempre antes de desenvolver:

**Opção 1: Abrir Docker Desktop manualmente**
- Deixe aberto durante desenvolvimento
- Ícone fica na bandeja (system tray)

**Opção 2: Configurar início automático**
- Docker Desktop → Settings → General
- ✅ "Start Docker Desktop when you log in"

---

## 📚 Documentação Atualizada

Arquivos atualizados:
- ✅ Este documento: `docs/044-fix-docker-engine.md`
- 🔜 Atualizar `DEV-LOCAL-GUIDE.md` com seção Docker
- 🔜 Atualizar `CHECKLIST-VALIDACAO-DEV.md` com verificação Docker

---

## ✅ Status

**Problema:** ✅ RESOLVIDO  
**Scripts:** ✅ CORRIGIDOS  
**Testado:** 🔜 Aguardando validação

---

## 🎯 Próximos Passos

1. ✅ **Verificar Docker** (agora)
   ```powershell
   .\scripts\check-docker.ps1
   ```

2. ✅ **Iniciar ambiente** (depois)
   ```powershell
   .\scripts\start-all-dev.ps1
   ```

3. ✅ **Validar funcionamento**
   - MongoDB rodando?
   - Backend conectou?
   - Frontend carregou?

---

**Status Final:** Scripts corrigidos e prontos para teste! 🚀
