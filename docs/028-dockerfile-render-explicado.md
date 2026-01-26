# 🐳 DOCKERFILE OTIMIZADO PARA RENDER

**Arquivo:** `Dockerfile`  
**Propósito:** Deploy Java/Spring Boot no Render.com

---

## ⚠️ POR QUE DOCKER NO RENDER?

**Render NÃO tem runtime Java nativo!**

Linguagens nativas suportadas:
- Node.js / Bun
- Python 3
- Ruby
- Go
- Rust
- Elixir

**Para Java (Spring Boot, Quarkus, etc.):**
- ✅ Usar **Docker** (forma oficial e recomendada)
- ✅ Ou subir imagem pronta (Docker Hub/GHCR)

---

## 📋 ESTRUTURA DO DOCKERFILE

### Multi-Stage Build (2 estágios)

```dockerfile
# STAGE 1: Build (Maven + Java 21)
FROM maven:3.9-eclipse-temurin-21-alpine AS build

# STAGE 2: Runtime (apenas JRE 21)
FROM eclipse-temurin:21-jre-alpine
```

**Por que multi-stage?**
- ✅ Imagem final menor (apenas JRE, sem Maven)
- ✅ Build reproduzível
- ✅ Segurança (menos ferramentas na imagem final)

---

## 🔧 OTIMIZAÇÕES PARA RENDER

### 1. Variável PORT Dinâmica

```dockerfile
ENV PORT=10000
EXPOSE ${PORT}

# ...

ENTRYPOINT ["sh", "-c", "java \
  -Dserver.port=${PORT} \
  -jar app.jar"]
```

**Por quê?**
- Render expõe variável `PORT` (padrão: 10000)
- Spring Boot precisa escutar nessa porta
- `-Dserver.port=${PORT}` garante que a app use a porta correta

### 2. Spring Profile via ENV

```dockerfile
-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-prod}
```

**Por quê?**
- Render define `SPRING_PROFILES_ACTIVE=prod` nas env vars
- Fallback para `prod` se não definido (`:- `)

### 3. Health Check Dinâmico

```dockerfile
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:${PORT}/actuator/health || exit 1
```

**Por quê?**
- Usa `${PORT}` dinâmica
- Render monitora saúde da aplicação
- Reinicia container se falhar

### 4. Non-Root User (Segurança)

```dockerfile
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
```

**Por quê?**
- ✅ Boa prática de segurança
- ✅ Container não roda como root
- ✅ Limita danos se houver vulnerabilidade

### 5. Cache de Dependências

```dockerfile
# Build stage - ordem otimizada
COPY pom.xml .
COPY modules/core/pom.xml modules/core/
COPY modules/api/pom.xml modules/api/
COPY modules/worker/pom.xml modules/worker/

# Download dependencies (camada cacheável)
RUN mvn dependency:go-offline -B

# Copy source (muda com frequência)
COPY modules/core/src modules/core/src
COPY modules/api/src modules/api/src
```

**Por quê?**
- ✅ Dependências baixadas uma vez
- ✅ Rebuild rápido se só código mudar
- ✅ Economiza tempo (5-8 min vs 10-15 min)

---

## 🚀 COMO O RENDER USA O DOCKERFILE

### Processo de Deploy:

1. **Render clona o repo** (branch `deploy/render`)
2. **Detecta `Dockerfile`** na raiz
3. **Build da imagem:**
   ```bash
   docker build -t item-control-api .
   ```
4. **Injeta variáveis de ambiente:**
   - `MONGODB_URI`
   - `SPRING_PROFILES_ACTIVE=prod`
   - `PORT=10000`
5. **Inicia container:**
   ```bash
   docker run -p 10000:10000 \
     -e MONGODB_URI="mongodb+srv://..." \
     -e SPRING_PROFILES_ACTIVE="prod" \
     -e PORT="10000" \
     item-control-api
   ```
6. **Monitora health check** (`/actuator/health`)
7. **Expõe URL pública** (ex: `https://item-control-api.onrender.com`)

---

## 📊 COMPARAÇÃO DE TEMPOS

| Método | Primeira Build | Rebuild | Imagem Final |
|--------|---------------|---------|--------------|
| **Docker Multi-Stage** | 10-15 min | 5-8 min | ~250MB |
| Maven direto (se existisse) | 8-10 min | 4-6 min | N/A |
| Imagem pronta (GHCR) | 2-3 min | 2-3 min | ~250MB |

---

## 🔍 TROUBLESHOOTING

### Build timeout (>30 min)

**Problema:** Maven baixando dependências demora muito

**Solução:**
```dockerfile
# Adicionar retry nas dependências
RUN mvn dependency:go-offline -B --fail-never
RUN mvn clean package -DskipTests -pl modules/api -am -B
```

### Port mismatch

**Problema:** App não responde no health check

**Solução:**
- Verificar se `PORT` está configurada nas env vars do Render
- Confirmar que Spring Boot usa `server.port=${PORT:8080}`

### MongoDB connection failed

**Problema:** App não conecta no MongoDB Atlas

**Solução:**
- Verificar `MONGODB_URI` nas env vars
- Atlas Network Access: `0.0.0.0/0` liberado
- Connection string tem `?retryWrites=true&w=majority`

---

## ✅ CHECKLIST DOCKERFILE

- [x] Multi-stage build (build + runtime)
- [x] Usa `PORT` env var do Render
- [x] Spring profile via `SPRING_PROFILES_ACTIVE`
- [x] Health check dinâmico
- [x] Non-root user (segurança)
- [x] Cache de dependências Maven
- [x] Imagem alpine (menor)
- [x] Java 21 (mesmo da dev)

---

## 🆚 ALTERNATIVAS

### Opção 1: Imagem Pronta (Mais Rápido)

**Vantagem:** Deploy em 2-3 min

**Como fazer:**
1. Build local: `docker build -t item-control-api .`
2. Push para GHCR/Docker Hub
3. Render → "Existing Image"

**Desvantagem:** Precisa CI/CD para automatizar

### Opção 2: Buildpack (Se existisse Java nativo)

**Render não suporta Java nativo**, então não é possível.

---

## 📚 REFERÊNCIAS

- **Render Docs - Docker:** https://render.com/docs/docker
- **Spring Boot Docker:** https://spring.io/guides/topicals/spring-boot-docker
- **Best Practices:** https://docs.docker.com/develop/dev-best-practices/

---

**Dockerfile pronto para produção no Render! 🐳🚀**
