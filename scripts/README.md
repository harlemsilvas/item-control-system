# 📜 Scripts do Projeto - Guia de Uso

Este diretório contém scripts PowerShell para facilitar o desenvolvimento e operação do **Item Control System**.

---

## 🚀 Scripts de Inicialização

### ⚡ **RECOMENDADO: Inicialização Rápida**

#### `quick-start.ps1` 
**Inicialização rápida em modo DESENVOLVIMENTO**

```powershell
.\quick-start.ps1
```

**O que faz:**
1. ✅ Verifica se a porta 8080 está em uso
2. ✅ Encerra automaticamente processos travados
3. ✅ Valida se o JAR foi compilado
4. ✅ Exibe informações de conexão
5. ✅ Inicia a API em modo `dev` (MongoDB local)

**Quando usar:** Desenvolvimento diário com MongoDB local via Docker

---

#### `quick-start-prod.ps1`
**Inicialização rápida em modo PRODUÇÃO**

```powershell
.\quick-start-prod.ps1
```

**O que faz:**
1. ✅ Verifica se a porta 8080 está em uso
2. ✅ Encerra automaticamente processos travados
3. ✅ Valida se o JAR foi compilado
4. ✅ Exibe informações de conexão
5. ✅ Inicia a API em modo `prod` (MongoDB Railway)

**Quando usar:** Testes com banco de dados de produção (Railway)

---

### 📋 Scripts Tradicionais

#### `start-api.ps1`
**Iniciar API em modo desenvolvimento**

```powershell
.\start-api.ps1
```

**Características:**
- Verifica e limpa porta 8080 automaticamente
- Perfil: `dev`
- MongoDB: `localhost:27017` (Docker)
- Porta: `8080`

---

#### `start-api-prod.ps1`
**Iniciar API em modo produção**

```powershell
.\start-api-prod.ps1
```

**Características:**
- Verifica e limpa porta 8080 automaticamente
- Perfil: `prod`
- MongoDB: Railway (hopper.proxy.rlwy.net:40930)
- Porta: `8080`

---

## 🛑 Scripts de Gerenciamento

### `Encerrar.ps1`
**Encerrar processos na porta 8080**

```powershell
.\Encerrar.ps1
```

**O que faz:**
1. 🔍 Lista todos os processos usando a porta 8080
2. 📊 Exibe informações detalhadas:
   - Nome do processo
   - PID
   - Memória utilizada
   - Tempo de execução
3. ⚠️ Solicita confirmação antes de encerrar
4. ✅ Valida se a porta foi liberada

**Quando usar:** 
- Quando precisar ver detalhes dos processos antes de encerrar
- Para debugging de processos travados
- Quando quiser confirmação manual

---

## 🧪 Scripts de Teste

### `test-api.ps1`
**Testar endpoints da API**

```powershell
.\test-api.ps1
```

Testa endpoints básicos como health check e funcionalidades da API.

---

### `test-categories.ps1`
**Testar CRUD de categorias**

```powershell
.\test-categories.ps1
```

Executa testes completos do módulo de categorias.

---

### `test-complete.ps1`
**Suite completa de testes**

```powershell
.\test-complete.ps1
```

Executa todos os testes da API incluindo Items, Events, Alerts e Rules.

---

### `test-railway-complete.ps1`
**Testes com MongoDB Railway**

```powershell
.\test-railway-complete.ps1
```

Executa testes usando o banco de produção (Railway).

---

## 📊 Scripts de Dados

### `populate-test-data.ps1`
**Popular banco com dados de teste**

```powershell
.\populate-test-data.ps1
```

Cria dados de teste incluindo:
- 15 itens
- 5 eventos por item (com datas retroativas)
- Alertas gerados
- Categorias e regras

---

### `quick-populate.ps1`
**População rápida de dados**

```powershell
.\quick-populate.ps1
```

Versão simplificada para popular rapidamente o banco.

---

## 🔧 Scripts Utilitários

### `view-mongodb.ps1`
**Visualizar dados no MongoDB**

```powershell
.\view-mongodb.ps1
```

Exibe coleções e dados do MongoDB via CLI.

---

### `config-railway.ps1`
**Configurar conexão Railway**

```powershell
.\config-railway.ps1
```

Auxilia na configuração da conexão com MongoDB Railway.

---

## 📝 Fluxo de Trabalho Recomendado

### Desenvolvimento Diário

1. **Iniciar MongoDB (Docker)**
   ```powershell
   docker-compose up -d
   ```

2. **Iniciar API**
   ```powershell
   .\scripts\quick-start.ps1
   ```

3. **Popular dados de teste** (se necessário)
   ```powershell
   .\scripts\populate-test-data.ps1
   ```

4. **Testar**
   ```powershell
   .\scripts\test-complete.ps1
   ```

---

### Teste com Produção (Railway)

1. **Iniciar API em modo produção**
   ```powershell
   .\scripts\quick-start-prod.ps1
   ```

2. **Testar com Railway**
   ```powershell
   .\scripts\test-railway-complete.ps1
   ```

---

### Resolver Problemas de Porta

1. **Verificar processos**
   ```powershell
   .\scripts\Encerrar.ps1
   ```

2. **Confirmar encerramento** quando solicitado

---

## ⚙️ Configurações

### Variáveis de Ambiente

Os scripts usam as seguintes configurações:

| Variável | Valor Dev | Valor Prod |
|----------|-----------|------------|
| Profile | dev | prod |
| MongoDB | localhost:27017 | hopper.proxy.rlwy.net:40930 |
| Database | item_control_db | item_control_db |
| Porta API | 8080 | 8080 |

---

## 🆘 Troubleshooting

### Erro: "Porta 8080 em uso"

**Solução automática:**
```powershell
.\scripts\quick-start.ps1
# OU
.\scripts\Encerrar.ps1
```

---

### Erro: "JAR não encontrado"

**Solução:**
```powershell
cd C:\Users\harle\Desktop\java-estudos\Projeto-Harlem\item-control-system
mvn clean install -DskipTests
```

---

### Erro: "MongoDB connection failed"

**Para dev:**
```powershell
docker-compose up -d
```

**Para prod:**
- Verificar credenciais Railway
- Verificar conectividade de rede
- Consultar: `docs/009-migracao-railway-mongodb.md`

---

## 📚 Documentação Relacionada

- `docs/011-migracao-railway-completa.md` - Status completo do projeto
- `docs/GUIA-VISUALIZAR-RAILWAY-MONGODB.md` - ⚡ **NOVO:** Como visualizar dados no Railway
- `docs/GUIA-TESTES-MANUAIS.md` - Guia de testes manuais
- `docs/GUIA-MONGODB.md` - Guia do MongoDB

---

**Última atualização:** 2026-01-23  
**Versão:** 1.1.0

