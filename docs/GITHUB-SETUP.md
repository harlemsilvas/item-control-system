# 🚀 Guia de Setup do Repositório GitHub

## 📋 Passos para Criar o Repositório no GitHub

### 1. Criar Repositório no GitHub

1. Acesse: https://github.com/new
2. **Repository name:** `item-control-system`
3. **Description:** `Sistema inteligente de controle de itens com motor de regras e alertas automáticos`
4. **Visibility:** Public (ou Private, conforme preferência)
5. ❌ **NÃO marque** "Initialize this repository with a README" (já temos um)
6. ❌ **NÃO adicione** .gitignore (já existe)
7. ❌ **NÃO adicione** license por enquanto
8. Clique em **"Create repository"**

---

### 2. Conectar Repositório Local ao GitHub

Após criar o repositório no GitHub, execute os comandos abaixo:

```powershell
cd "C:\Users\harle\Desktop\java-estudos\Projeto-Harlem\item-control-system"

# Adicionar remote origin
git remote add origin https://github.com/harlemsilvas/item-control-system.git

# Renomear branch para main (se necessário)
git branch -M main

# Push inicial
git push -u origin main
```

---

### 3. Verificar se o Push Foi Bem-Sucedido

```powershell
# Verificar remote configurado
git remote -v

# Ver status
git status

# Ver log
git log --oneline -5
```

---

### 4. Configurar Branch Protection (Opcional)

No GitHub, vá em:
1. **Settings** → **Branches**
2. **Add branch protection rule**
3. Branch name pattern: `main`
4. Marque:
   - ✅ Require a pull request before merging
   - ✅ Require status checks to pass before merging
   - ✅ Require conversation resolution before merging

---

## 🔄 Workflow Futuro

### Criar Feature Branch

```powershell
# Criar e mudar para nova branch
git checkout -b feature/domain-entities

# Fazer alterações...
# ...

# Adicionar e commitar
git add .
git commit -m "feat: implement core domain entities"

# Push da branch
git push origin feature/domain-entities
```

### Merge via Pull Request

1. No GitHub, vá em **Pull Requests**
2. Clique em **New Pull Request**
3. Base: `main` ← Compare: `feature/domain-entities`
4. Preencha descrição
5. Clique em **Create Pull Request**
6. Após review, clique em **Merge Pull Request**

### Atualizar Branch Local

```powershell
# Voltar para main
git checkout main

# Atualizar com remote
git pull origin main

# Deletar branch local (se já foi merged)
git branch -d feature/domain-entities
```

---

## 📝 Convenção de Commits

Seguimos [Conventional Commits](https://www.conventionalcommits.org/):

```
feat: implementar nova funcionalidade
fix: corrigir bug
docs: atualizar documentação
chore: tarefas de manutenção
test: adicionar/corrigir testes
refactor: refatorar código
style: formatação, ponto e vírgula, etc
perf: melhorias de performance
ci: alterações em CI/CD
```

### Exemplos:

```powershell
git commit -m "feat: add Item entity with validation"
git commit -m "fix: correct Rule evaluation logic"
git commit -m "docs: update README with API examples"
git commit -m "test: add unit tests for RuleEngine"
git commit -m "refactor: improve EventRepository interface"
```

---

## 🏷️ Tags de Versão

### Criar Tag

```powershell
# Tag simples
git tag v0.1.0

# Tag anotada (recomendado)
git tag -a v0.1.0 -m "Release v0.1.0 - MVP Foundation"

# Push da tag
git push origin v0.1.0

# Push de todas as tags
git push origin --tags
```

### Listar Tags

```powershell
git tag
git tag -l "v0.*"
```

---

## 🔧 Comandos Úteis

### Ver Diferenças

```powershell
# Diferenças não staged
git diff

# Diferenças staged
git diff --staged

# Diferenças entre branches
git diff main..feature/domain-entities
```

### Desfazer Mudanças

```powershell
# Descartar mudanças em arquivo específico (unstaged)
git checkout -- arquivo.java

# Remover arquivo do stage
git reset HEAD arquivo.java

# Voltar commit (mantendo alterações)
git reset --soft HEAD~1

# Voltar commit (descartando alterações) - CUIDADO!
git reset --hard HEAD~1
```

### Ver Histórico

```powershell
# Log simples
git log --oneline

# Log com gráfico
git log --oneline --graph --all

# Log de um arquivo específico
git log -- caminho/do/arquivo.java

# Log com estatísticas
git log --stat
```

---

## 🚨 Troubleshooting

### Erro: "fatal: remote origin already exists"

```powershell
# Remover remote existente
git remote remove origin

# Adicionar novamente
git remote add origin https://github.com/harlemsilvas/item-control-system.git
```

### Erro: "failed to push some refs"

```powershell
# Pull primeiro (se houver commits no remote)
git pull origin main --rebase

# Depois push
git push origin main
```

### Conflito de Merge

```powershell
# Ver arquivos em conflito
git status

# Após resolver conflitos manualmente
git add .
git commit -m "fix: resolve merge conflicts"
git push
```

---

## 📚 Recursos Adicionais

- [Git Documentation](https://git-scm.com/doc)
- [GitHub Guides](https://guides.github.com/)
- [Conventional Commits](https://www.conventionalcommits.org/)
- [Semantic Versioning](https://semver.org/)

---

**Próximo Passo:** Após configurar o GitHub, começar implementação das entidades de domínio!
