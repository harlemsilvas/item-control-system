# 🔧 TROUBLESHOOTING - POWERSHELL + MAVEN
**Data:** 2026-01-23  
**Problema:** Erro ao executar Maven com parâmetros Spring Boot no PowerShell
---
## ❌ PROBLEMA
### **Erro:**
```
[ERROR] Unknown lifecycle phase ".run.profiles=prod". You must specify a valid lifecycle phase...
```
### **Comando que causava o erro:**
```powershell
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```
### **Causa:**
O **PowerShell interpreta o ponto (.)** como um **separador de propriedades**, então o parâmetro `-Dspring-boot.run.profiles=prod` é quebrado em:
- `-Dspring-boot`
- `.run.profiles=prod` ← interpretado como fase Maven (erro!)
---
## ✅ SOLUÇÃO
### **Adicionar aspas duplas no parâmetro:**
```powershell
mvn spring-boot:run "-Dspring-boot.run.profiles=prod"
```
As **aspas duplas** protegem o parâmetro completo e o Maven recebe corretamente:
```
-Dspring-boot.run.profiles=prod
```
---
## 📝 ARQUIVOS CORRIGIDOS
- ✅ `scripts/start-api-railway.ps1`
**Antes:**
```powershell
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```
**Depois:**
```powershell
mvn spring-boot:run "-Dspring-boot.run.profiles=prod"
```
---
## 💡 REGRA GERAL
**No PowerShell, sempre use aspas em parâmetros Maven que contenham:**
### ❌ SEM aspas (pode causar erro):
```powershell
mvn spring-boot:run -Dspring.profiles.active=prod
mvn package -Dmaven.test.skip=true
mvn clean -Dspring-boot.run.profiles=dev
```
### ✅ COM aspas (funcionam corretamente):
```powershell
mvn spring-boot:run "-Dspring.profiles.active=prod"
mvn package "-Dmaven.test.skip=true"
mvn clean "-Dspring-boot.run.profiles=dev"
```
---
## 🎯 EXEMPLOS PRÁTICOS
### **1. Iniciar aplicação com perfil específico:**
```powershell
mvn spring-boot:run "-Dspring-boot.run.profiles=prod"
```
### **2. Múltiplos parâmetros:**
```powershell
mvn spring-boot:run "-Dspring-boot.run.profiles=prod" "-Dserver.port=8081"
```
### **3. Variáveis de ambiente:**
```powershell
$env:MONGODB_URI = "mongodb://..."
mvn spring-boot:run "-Dspring-boot.run.profiles=prod"
```
---
## 🔍 OUTROS SHELLS
### **Bash/Linux:**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod  # OK sem aspas
```
### **CMD (Windows):**
```cmd
mvn spring-boot:run -Dspring-boot.run.profiles=prod  # OK sem aspas
```
### **PowerShell (Windows):**
```powershell
mvn spring-boot:run "-Dspring-boot.run.profiles=prod"  # PRECISA de aspas
```
---
## ✅ VERIFICAÇÃO
Para testar se o parâmetro está sendo passado corretamente, adicione `-X` (debug):
```powershell
mvn spring-boot:run "-Dspring-boot.run.profiles=prod" -X
```
Procure na saída:
```
[DEBUG] Properties: {...spring.profiles.active=prod...}
```
---
## 📚 REFERÊNCIAS
- Maven Properties: https://maven.apache.org/guides/introduction/introduction-to-profiles.html
- PowerShell Parsing: https://learn.microsoft.com/en-us/powershell/
- Spring Boot Maven Plugin: https://docs.spring.io/spring-boot/maven-plugin/
---
**Desenvolvido por:** Harlem Silva  
**Data:** 23/01/2026
