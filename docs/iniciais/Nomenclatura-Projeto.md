.# Nomenclatura e Estratégia de Evolução do Projeto

## 📋 Resumo Executivo

Este documento define a nomenclatura profissional para o projeto de controle de itens, planejando desde a fase MVP até a comercialização futura.

---

## 🎯 Visão Geral

### Fase Atual: MVP/Uso Local

- **Proprietário:** harlemsilvas (GitHub)
- **Objetivo:** Desenvolvimento local, aprendizado e validação
- **Público:** Pessoal/Portfólio

### Fase Futura: Produto Comercial

- **Nome Comercial:** **ItemFlow**
- **Objetivo:** Produto SaaS comercial
- **Público:** Empresas e usuários finais

---

## 📦 Nomenclatura Definida (Fase MVP)

### Repositório GitHub

```
item-control-system
```

**URL:** `https://github.com/harlemsilvas/item-control-system`

### Estrutura Local

```
Projeto-Harlem/
└── item-control-system/
    ├── modules/
    │   ├── core/
    │   ├── api/
    │   └── worker/
    └── docs/
```

### Maven Coordinates

#### Parent POM

```xml
<groupId>br.com.harlemsilvas</groupId>
<artifactId>item-control-parent</artifactId>
<version>0.1.0-SNAPSHOT</version>
```

#### Módulos

| Módulo | artifactId            | Descrição                   |
| ------ | --------------------- | --------------------------- |
| Core   | `item-control-core`   | Domínio e regras de negócio |
| API    | `item-control-api`    | REST API e controllers      |
| Worker | `item-control-worker` | Processamento em background |

### Package Structure

```java
br.com.harlemsilvas.itemcontrol.core
br.com.harlemsilvas.itemcontrol.api
br.com.harlemsilvas.itemcontrol.worker
```

---

## 🚀 Nomenclatura Futura (ItemFlow)

### Quando Migrar?

- Quando houver interesse comercial
- Ao definir modelo de negócio
- Ao registrar marca/domínio

### Maven Coordinates (Futuro)

```xml
<groupId>br.com.itemflow</groupId>
<artifactId>itemflow-parent</artifactId>
```

### Package Structure (Futuro)

```java
br.com.itemflow.core
br.com.itemflow.api
br.com.itemflow.worker
```

### Domínio Web

```
itemflow.com.br
app.itemflow.com.br
api.itemflow.com.br
```

---

## 🔄 Plano de Migração (MVP → ItemFlow)

### Etapa 1: Preparação (Durante MVP)

- ✅ Manter código modular e desacoplado
- ✅ Documentar decisões arquiteturais (ADRs)
- ✅ Usar interfaces e abstrações

### Etapa 2: Refatoração de Packages

```bash
# Refactor packages
br.com.harlemsilvas.itemcontrol → br.com.itemflow
```

### Etapa 3: Atualização Maven

```xml
<!-- Atualizar groupId em todos os POMs -->
<groupId>br.com.itemflow</groupId>
```

### Etapa 4: Branding

- [ ] Registrar domínio itemflow.com.br
- [ ] Criar logo e identidade visual
- [ ] Atualizar README e documentação
- [ ] Configurar CI/CD para produção

---

## 💡 Justificativa das Escolhas

### Por que "item-control-system"?

- ✅ Nome descritivo e profissional
- ✅ Fácil de entender o propósito
- ✅ SEO-friendly (inglês)
- ✅ Evita nomes genéricos como "controle-itens"

### Por que "ItemFlow" no futuro?

- ✅ Nome comercial memorável
- ✅ Sugere fluidez e controle
- ✅ Brandable (fácil de criar marca)
- ✅ Disponível como domínio

### Por que "br.com.harlemsilvas"?

- ✅ Identifica autoria pessoal
- ✅ Padrão Java (domínio reverso)
- ✅ Fácil migração futura
- ✅ Evita conflitos de namespace

---

## 📊 Comparativo de Nomenclaturas

| Aspecto         | MVP (Atual)         | Comercial (Futuro) |
| --------------- | ------------------- | ------------------ |
| **Repositório** | item-control-system | itemflow           |
| **groupId**     | br.com.harlemsilvas | br.com.itemflow    |
| **Package**     | itemcontrol         | itemflow           |
| **Domínio**     | N/A                 | itemflow.com.br    |
| **Marca**       | Portfólio pessoal   | ItemFlow®          |

---

## 🎓 Convenções de Nomenclatura

### Branches Git

```
main           # Produção
develop        # Desenvolvimento
feature/xxx    # Novas funcionalidades
hotfix/xxx     # Correções urgentes
```

### Versionamento (SemVer)

```
0.1.0-SNAPSHOT    # MVP inicial
0.2.0-SNAPSHOT    # Funcionalidades básicas
1.0.0             # Primeira release estável
2.0.0             # ItemFlow (rebrand)
```

### Releases

```
v0.1.0-alpha      # Testes iniciais
v0.5.0-beta       # Testes com usuários
v1.0.0            # Primeira versão pública
```

---

## 📝 Checklist de Implementação

### Fase MVP (Atual)

- [x] Definir nomenclatura
- [ ] Criar estrutura de diretórios
- [ ] Configurar POMs Maven
- [ ] Implementar módulo core
- [ ] Implementar API REST
- [ ] Implementar worker
- [ ] Documentar arquitetura

### Fase Comercial (Futuro)

- [ ] Validar nome "ItemFlow"
- [ ] Registrar domínio
- [ ] Refatorar packages
- [ ] Criar identidade visual
- [ ] Definir modelo de negócio
- [ ] Configurar infraestrutura cloud
- [ ] Launch! 🚀

---

## 🔗 Referências

- [Maven Naming Convention](https://maven.apache.org/guides/mini/guide-naming-conventions.html)
- [Semantic Versioning](https://semver.org/)
- [Java Package Naming](https://docs.oracle.com/javase/tutorial/java/package/namingpkgs.html)

---

**Criado em:** 22 de janeiro de 2026  
**Autor:** Harlem Silvas  
**Status:** Em desenvolvimento (MVP)
