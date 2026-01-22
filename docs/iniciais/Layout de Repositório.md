# Layout de Repositório + Maven Multi-módulo (core / api / worker) — Monólito pronto pra split

Este layout permite começar **como monólito** (API + Worker juntos, mesmo deploy se quiser) e, quando crescer, separar em **2 processos** (API e Worker) reaproveitando o mesmo `core`.

---

## 1) Estrutura de pastas (recomendada)

```txt
controle-itens/
├── pom.xml                         # POM pai (multi-módulo)
├── README.md
├── .gitignore
├── docker-compose.yml              # Mongo local (dev)
├── docs/
│   ├── ADRs/
│   └── arquitetura.md
└── modules/
    ├── core/
    │   ├── pom.xml
    │   └── src/
    │       ├── main/java/br/com/seuprojeto/core/
    │       │   ├── domain/
    │       │   │   ├── model/                 # entidades / value objects (sem Spring)
    │       │   │   ├── rules/                 # motor de regras (Strategy + Factory)
    │       │   │   └── exceptions/
    │       │   ├── application/
    │       │   │   ├── ports/
    │       │   │   │   ├── repositories/      # interfaces (ports) de persistência
    │       │   │   │   ├── notifications/     # interface de envio (email/push/whatsapp)
    │       │   │   │   └── clock/             # Clock port (testável)
    │       │   │   ├── usecases/              # serviços / casos de uso
    │       │   │   └── dto/
    │       │   └── shared/
    │       │       ├── ids/
    │       │       └── utils/
    │       └── test/java/br/com/seuprojeto/core/
    │           ├── domain/
    │           └── application/
    ├── api/
    │   ├── pom.xml
    │   └── src/
    │       ├── main/java/br/com/seuprojeto/api/
    │       │   ├── ApiApplication.java
    │       │   ├── config/
    │       │   ├── security/
    │       │   ├── web/
    │       │   │   ├── controllers/
    │       │   │   ├── request/
    │       │   │   └── response/
    │       │   └── infra/
    │       │       ├── mongo/                 # implementações dos repositórios (adapters)
    │       │       └── notifications/         # adapter (stub inicial)
    │       ├── main/resources/
    │       │   ├── application.yml
    │       │   └── application-dev.yml
    │       └── test/java/br/com/seuprojeto/api/
    └── worker/
        ├── pom.xml
        └── src/
            ├── main/java/br/com/seuprojeto/worker/
            │   ├── WorkerApplication.java
            │   ├── scheduler/
            │   │   ├── RuleEngineScheduler.java
            │   │   └── SchedulerConfig.java
            │   └── infra/
            │       └── mongo/                 # se o worker precisar de adapters próprios
            ├── main/resources/
            │   ├── application.yml
            │   └── application-dev.yml
            └── test/java/br/com/seuprojeto/worker/
```

> Observação: você pode rodar api e worker como processos separados ou empacotar/rodar um só (monólito), escolhendo quais perfis e módulos subir.

# 2) Documentação do módulo `core` (o “coração” do projeto)

# Objetivo do `core`

O módulo`core`contém toda a lógica de negócio independente de framework:

- Modelos de domínio (Item, Rule, Alert, Event…)
- Motor de regras (Strategy/Factory)
- Casos de uso (application layer)
- Interfaces (ports) para persistência e notificação

## Regras do `core`

- NÃO depende de Spring Boot
- NÃO conhece Mongo diretamente
- Tudo externo entra por interfaces:
  - `ItemRepository`, `EventRepository`, `RuleRepository`, `AlertRepository`
  - `NotificationPort`
  - `ClockPort`

Isso garante que o `core` seja:

- altamente testável
- facilmente reaproveitado no api e worker
- pronto para migrar de Mongo → outro banco se um dia for necessário

---

# 3. POM Pai (raiz) — controle-itens/pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">

  <modelVersion>4.0.0</modelVersion>

  <groupId>br.com.seuprojeto</groupId>
  <artifactId>controle-itens-parent</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <packaging>pom</packaging>
  <name>controle-itens-parent</name>

  <properties>
    <java.version>21</java.version>

    <!-- Controle de versões -->
    <spring.boot.version>3.3.7</spring.boot.version>
    <springdoc.version>2.6.0</springdoc.version>

    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
  </properties>

  <modules>
    <module>modules/core</module>
    <module>modules/api</module>
    <module>modules/worker</module>
  </modules>

  <!-- Importa BOM do Spring Boot para os módulos que precisarem -->
  <dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-dependencies</artifactId>
        <version>${spring.boot.version}</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>

      <!-- Springdoc (Swagger/OpenAPI) -->
      <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        <version>${springdoc.version}</version>
      </dependency>
    </dependencies>
  </dependencyManagement>

  <build>
    <pluginManagement>
      <plugins>
        <!-- Compiler Java 21 -->
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-compiler-plugin</artifactId>
          <version>3.13.0</version>
          <configuration>
            <release>${java.version}</release>
          </configuration>
        </plugin>
      </plugins>
    </pluginManagement>
  </build>

</project>

```

---

# 4) POM do módulo`core`— modules/core/pom.xml

> `core` não é Spring Boot. É um JAR simples com regras e casos de uso.

```xmls
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">

  <modelVersion>4.0.0</modelVersion>

  <parent>
    <groupId>br.com.seuprojeto</groupId>
    <artifactId>controle-itens-parent</artifactId>
    <version>0.0.1-SNAPSHOT</version>
  </parent>

  <artifactId>controle-itens-core</artifactId>
  <packaging>jar</packaging>
  <name>controle-itens-core</name>

  <dependencies>
    <!-- JSON (se quiser manipular params/config no core) -->
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-databind</artifactId>
    </dependency>

    <!-- Validação (opcional; útil para validar DTOs do core) -->
    <dependency>
      <groupId>jakarta.validation</groupId>
      <artifactId>jakarta.validation-api</artifactId>
    </dependency>

    <!-- Lombok (opcional) -->
    <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <optional>true</optional>
    </dependency>

    <!-- Testes -->
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter</artifactId>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.mockito</groupId>
      <artifactId>mockito-core</artifactId>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <version>3.5.2</version>
        <configuration>
          <useModulePath>false</useModulePath>
        </configuration>
      </plugin>
    </plugins>
  </build>

</project>
```

---

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">

  <modelVersion>4.0.0</modelVersion>

  <parent>
    <groupId>br.com.seuprojeto</groupId>
    <artifactId>controle-itens-parent</artifactId>
    <version>0.0.1-SNAPSHOT</version>
  </parent>

  <artifactId>controle-itens-api</artifactId>
  <packaging>jar</packaging>
  <name>controle-itens-api</name>

  <dependencies>
    <!-- Core -->
    <dependency>
      <groupId>br.com.seuprojeto</groupId>
      <artifactId>controle-itens-core</artifactId>
      <version>${project.version}</version>
    </dependency>

    <!-- Spring Boot Web -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Mongo -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-mongodb</artifactId>
    </dependency>

    <!-- Validation -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>

    <!-- Security + OAuth2 Client -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-oauth2-client</artifactId>
    </dependency>

    <!-- OpenAPI / Swagger UI -->
    <dependency>
      <groupId>org.springdoc</groupId>
      <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    </dependency>

    <!-- Actuator -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>

    <!-- Lombok (opcional) -->
    <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <optional>true</optional>
    </dependency>

    <!-- Testes -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.springframework.security</groupId>
      <artifactId>spring-security-test</artifactId>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <!-- Spring Boot plugin -->
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
      </plugin>
    </plugins>
  </build>

</project>
```

---

# 6) POM do módulo worker — modules/worker/pom.xml

> O `worker` é outro Spring Boot, que roda scheduler e o motor de regras.
> Ele também depende do `core` e pode ter adapters próprios (ex.: Mongo repos).

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">

  <modelVersion>4.0.0</modelVersion>

  <parent>
    <groupId>br.com.seuprojeto</groupId>
    <artifactId>controle-itens-parent</artifactId>
    <version>0.0.1-SNAPSHOT</version>
  </parent>

  <artifactId>controle-itens-worker</artifactId>
  <packaging>jar</packaging>
  <name>controle-itens-worker</name>

  <dependencies>
    <!-- Core -->
    <dependency>
      <groupId>br.com.seuprojeto</groupId>
      <artifactId>controle-itens-core</artifactId>
      <version>${project.version}</version>
    </dependency>

    <!-- Spring (para scheduler e infra) -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter</artifactId>
    </dependency>

    <!-- Mongo -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-mongodb</artifactId>
    </dependency>

    <!-- Actuator -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>

    <!-- Lombok (opcional) -->
    <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <optional>true</optional>
    </dependency>

    <!-- Testes -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <!-- Spring Boot plugin -->
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
      </plugin>
    </plugins>
  </build>

</project>

```

---

# 7) Profiles para ligar/desligar scheduler por ambiente

## Estratégia

- O worker terá uma propriedade:
  - `scheduler.enabled=true|false`
- O scheduler só roda se `scheduler.enabled=true`
- Você controla isso por profiles do Spring (`dev`, `prod`, etc.)

## 7.1 Worker — classe de configuração do scheduler (exemplo)

```java
package br.com.seuprojeto.worker.scheduler;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "scheduler.enabled", havingValue = "true", matchIfMissing = false)
public class SchedulerConfig {
}
```

> Resultado:
>
> - Se scheduler.enabled=false, o agendamento não ativa.
> - Se scheduler.enabled=true, ativa.

## 7.2 Worker — application.yml (default)

`modules/worker/src/main/resources/application.yml`

```yaml
spring:
  application:
    name: controle-itens-worker

scheduler:
  enabled: false
```

## 7.3 Worker — application-dev.yml (liga scheduler em dev)

`modules/worker/src/main/resources/application-dev.yml`

```yaml
scheduler:
  enabled: true
```

## 7.4 Worker — application-prod.yml (você decide)

`modules/worker/src/main/resources/application-prod.yml`

```yaml
scheduler:
  enabled: true
```

## 7.5 Como rodar

- Rodar API:

```bash
mvn -pl modules/api spring-boot:run
```

- Rodar Worker em dev (scheduler ligado):

```bash
mvn -pl modules/worker spring-boot:run -Dspring-boot.run.profiles=dev
```

- Rodar Worker em prod:

```bash
mvn -pl modules/worker spring-boot:run -Dspring-boot.run.profiles=prod
```

# 8) Monólito “pronto pra split” na prática

## Como você pode operar como monólito no início

- Rode api e worker na mesma máquina/host (dois processos).
- Ou até subir ambos no mesmo docker-compose.

Isso já te dá:

- separação de responsabilidades
- caminho fácil para escalar (replicar worker/API separadamente)
- migração suave para mensageria

---

# 9) Próximo passo recomendado (para o Sprint 1)

**1.** Criar `docker-compose.yml` com Mongo + (opcional) Mongo Express

**2.** Criar entidades mínimas no core:

- `Item`, `Rule`, `Alert` , `Event`

  **3.** Criar repositórios (ports) no `core` e adapters Mongo no `api` e `worker`

  **4.** Criar RuleEngineService no`core`(application layer)

  **5.** Expor no `api`:

- CRUD Item/Rule/Event

  **6.** Rodar no worker:

- scheduler chamando `RuleEngineService.evaluateAllEnabledRules()`

---

```makefile
::contentReference[oaicite:0]{index=0}
```
