# ☁️ MIGRAÇÃO PARA RAILWAY.COM - MONGODB
**Data:** 2026-01-23  
**Objetivo:** Migrar MongoDB do Docker local para Railway.com (cloud)
---
## 📋 RESUMO
Railway.com é uma plataforma cloud moderna que oferece:
- ✅ MongoDB gratuito (500MB)
- ✅ Deploy automático
- ✅ Backup automático
- ✅ SSL/TLS incluído
- ✅ Monitoramento integrado
---
## 🚀 PASSO A PASSO COMPLETO
### **ETAPA 1: CRIAR MONGODB NO RAILWAY** ☁️
#### **1. Acessar Railway**
1. Acesse: https://railway.com/
2. Clique em **"Start a New Project"**
3. Faça login com GitHub, Google ou Email
#### **2. Criar Projeto MongoDB**
1. Clique em **"+ New"**
2. Selecione **"Database"**
3. Escolha **"MongoDB"**
4. Aguarde o provisionamento (1-2 minutos)
#### **3. Obter String de Conexão**
1. Clique no serviço MongoDB criado
2. Vá na aba **"Variables"**
3. Copie o valor da variável **`MONGO_URL`**
**Formato esperado:**
```
mongodb://mongo:SENHA_GERADA@containers-us-west-XXX.railway.app:PORTA
```
**⚠️ IMPORTANTE:** Guarde essa URL com segurança!
---
### **ETAPA 2: CONFIGURAR O PROJETO** ⚙️
Já criamos os arquivos de configuração para você:
#### **Arquivos Criados:**
```
modules/api/src/main/resources/
├── application.yml              # Config padrão (local)
├── application-dev.yml          # Desenvolvimento (local)
└── application-prod.yml         # Produção (Railway) ← NOVO!
modules/worker/src/main/resources/
├── application.yml              # Config padrão (local)
└── application-prod.yml         # Produção (Railway) ← NOVO!
scripts/
├── config-railway.ps1           # Guia de configuração ← NOVO!
└── start-api-railway.ps1        # Iniciar com Railway ← NOVO!
```
---
### **ETAPA 3: CONFIGURAR VARIÁVEL DE AMBIENTE** 🔐
#### **Opção 1: Variável de Ambiente (RECOMENDADO)** ✅
**Windows PowerShell:**
```powershell
$env:MONGODB_URI = "mongodb://mongo:SUA_SENHA@containers-us-west-XXX.railway.app:PORTA/item_control_db"
```
**Linux/Mac:**
```bash
export MONGODB_URI="mongodb://mongo:SUA_SENHA@containers-us-west-XXX.railway.app:PORTA/item_control_db"
```
**Verificar se configurou:**
```powershell
echo $env:MONGODB_URI
```
---
#### **Opção 2: Editar application-prod.yml** ⚙️
Se preferir, edite diretamente o arquivo:
**Arquivo:** `modules/api/src/main/resources/application-prod.yml`
```yaml
spring:
  data:
    mongodb:
      # Substitua pela sua URL do Railway
      uri: mongodb://mongo:SUA_SENHA@containers-us-west-XXX.railway.app:PORTA/item_control_db
      auto-index-creation: true
```
⚠️ **NÃO commite senha no Git!** Use variável de ambiente.
---
### **ETAPA 4: INICIAR APLICAÇÃO** 🚀
#### **1. Configurar URI do Railway**
Execute o guia interativo:
```powershell
.\scripts\config-railway.ps1
```
Ou configure manualmente:
```powershell
$env:MONGODB_URI = "sua-url-do-railway"
```
#### **2. Iniciar API com Railway**
```powershell
.\scripts\start-api-railway.ps1
```
Ou manualmente:
```powershell
cd modules\api
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```
#### **3. Iniciar Worker com Railway**
```powershell
$env:MONGODB_URI = "sua-url-do-railway"
cd modules\worker
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```
---
## 📦 DATABASE E COLLECTIONS
### **Nome da Database:**
```
item_control_db
```
### **Collections (criadas automaticamente):**
O Spring Data MongoDB cria as collections automaticamente quando você salva o primeiro documento de cada tipo:
| Collection | Descrição | Documento Model |
|-----------|-----------|-----------------|
| `items` | Itens gerenciados | ItemDocument |
| `events` | Eventos registrados | EventDocument |
| `alerts` | Alertas gerados | AlertDocument |
| `rules` | Regras configuradas | RuleDocument |
| `categories` | Categorias hierárquicas | CategoryDocument |
**⚠️ VOCÊ NÃO PRECISA CRIAR AS COLLECTIONS MANUALMENTE!**
O Spring Data faz isso automaticamente ao salvar o primeiro registro.
---
## 🔄 MIGRAR DADOS DO DOCKER PARA RAILWAY
Se você já tem dados no MongoDB local e quer migrar para o Railway:
### **1. Exportar dados do MongoDB local**
```powershell
# Exportar cada collection
mongodump --uri="mongodb://localhost:27017/item_control_db" --out=backup
# Ou exportar tudo de uma vez
docker exec mongodb mongodump --db=item_control_db --out=/backup
docker cp mongodb:/backup ./mongodb-backup
```
### **2. Importar para Railway**
```powershell
# Importar cada collection
mongorestore --uri="mongodb://mongo:SENHA@containers-us-west-XXX.railway.app:PORTA/item_control_db" backup/item_control_db/
# Ou via MongoDB Compass:
# 1. Conecte no Railway usando a URI
# 2. Import Data → Escolha os arquivos JSON/BSON
```
---
## 🧪 TESTAR CONEXÃO
### **1. Via API Health Check**
```bash
curl http://localhost:8080/actuator/health
```
**Response esperado:**
```json
{
  "status": "UP",
  "components": {
    "mongo": {
      "status": "UP",
      "details": {
        "version": "7.0.x"
      }
    }
  }
}
```
### **2. Via Logs**
Ao iniciar a API, você verá nos logs:
```
INFO  o.s.d.m.c.MongoMappingContext - Finished initializing MongoDB mappings
INFO  o.s.d.m.c.MongoTemplate - Connecting to database: item_control_db
```
### **3. Criar primeiro Item**
```bash
POST http://localhost:8080/api/v1/items
{
  "name": "Teste Railway",
  "userId": "550e8400-e29b-41d4-a716-446655440001"
}
```
Verifique no Railway Dashboard:
- Vá em **Data** → **Browse**
- A collection `items` deve aparecer automaticamente!
---
## 🔐 SEGURANÇA
### **Boas Práticas:**
1. ✅ **Nunca commite a URL do MongoDB no Git**
   - Use variáveis de ambiente
   - Adicione `application-prod.yml` ao `.gitignore`
2. ✅ **Rotacione senhas periodicamente**
   - Railway permite gerar nova senha
   - Atualize a variável `MONGODB_URI`
3. ✅ **Use SSL/TLS**
   - Railway já usa SSL por padrão
   - Conexões são criptografadas
4. ✅ **Limite IPs (opcional)**
   - Railway permite whitelist de IPs
   - Útil para ambientes corporativos
---
## 🆚 COMPARAÇÃO: LOCAL vs RAILWAY
| Característica | Docker Local | Railway.com |
|---------------|--------------|-------------|
| **Custo** | Gratuito | Gratuito (500MB) |
| **Disponibilidade** | Apenas na sua máquina | Acessível de qualquer lugar |
| **Backup** | Manual | Automático |
| **SSL/TLS** | Não | Sim (padrão) |
| **Monitoramento** | Manual | Dashboard integrado |
| **Escalabilidade** | Limitado | Automática |
| **Deploy** | Manual | Automático via Git |
---
## 📊 MONITORAMENTO NO RAILWAY
### **Dashboard Railway:**
1. **Métricas:**
   - CPU usage
   - Memory usage
   - Network I/O
   - Disk usage
2. **Logs:**
   - Logs em tempo real
   - Filtros por severidade
   - Download de logs históricos
3. **Backups:**
   - Backups automáticos diários
   - Restore com 1 clique
   - Versionamento
4. **Alertas:**
   - Notificações por email
   - Webhooks para integração
   - Limite de recursos
---
## 🔍 TROUBLESHOOTING
### **Erro: Connection Refused**
```
com.mongodb.MongoTimeoutException: Timed out after 30000 ms
```
**Soluções:**
1. Verificar se URL está correta
2. Verificar se Railway está UP (Dashboard)
3. Verificar firewall/antivírus
---
### **Erro: Authentication Failed**
```
Command failed with error 18 (AuthenticationFailed)
```
**Soluções:**
1. Verificar senha na URL
2. Regenerar credenciais no Railway
3. Atualizar variável `MONGODB_URI`
---
### **Collections não aparecem**
**Causa:** Normal! Collections são criadas quando você salva o primeiro documento.
**Solução:** Crie um item via API e a collection aparecerá automaticamente.
---
## ✅ CHECKLIST DE MIGRAÇÃO
- [ ] Criar MongoDB no Railway
- [ ] Copiar `MONGO_URL` do Railway
- [ ] Criar `application-prod.yml` (✅ já criado)
- [ ] Configurar variável `MONGODB_URI`
- [ ] Testar API com `.\scripts\start-api-railway.ps1`
- [ ] Verificar `actuator/health`
- [ ] Criar primeiro item via API
- [ ] Verificar collection no Railway Dashboard
- [ ] (Opcional) Migrar dados do local para Railway
- [ ] Iniciar Worker com Railway
---
## 🎯 PRÓXIMOS PASSOS
Após migrar para Railway:
1. **Deploy Contínuo:**
   - Conectar GitHub ao Railway
   - Deploy automático a cada commit
2. **Ambientes:**
   - Criar ambiente de staging
   - Criar ambiente de produção
3. **Escalabilidade:**
   - Configurar replicas
   - Auto-scaling
4. **Monitoramento:**
   - Configurar alertas
   - Integrar com ferramentas de APM
---
## 📚 RECURSOS
- 📖 Railway Docs: https://docs.railway.app/
- 🎥 Railway YouTube: https://www.youtube.com/@railwayapp
- 💬 Railway Discord: https://discord.gg/railway
- 📧 Suporte: support@railway.app
---
## ✅ CONCLUSÃO
A migração para Railway.com oferece:
- ✅ MongoDB cloud profissional
- ✅ Gratuito até 500MB
- ✅ SSL/TLS automático
- ✅ Backups automáticos
- ✅ Monitoramento integrado
- ✅ Deploy automático
**Tudo pronto para usar!** Execute:
```powershell
.\scripts\config-railway.ps1
```
---
**Desenvolvido por:** Harlem Silva  
**Data:** 23/01/2026  
**Versão:** 0.1.0-SNAPSHOT
