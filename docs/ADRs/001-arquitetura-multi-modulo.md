# ADR 001: Arquitetura Multi-Módulo Maven

**Data:** 22/01/2026  
**Status:** Aceito  
**Autor:** Harlem Silvas

## Contexto

Precisamos de uma arquitetura que permita começar como monólito mas que facilite a separação futura em microserviços (API + Worker).

## Decisão

Adotar arquitetura multi-módulo Maven com 3 módulos:

1. **core**: Domínio e regras de negócio (sem Spring)
2. **api**: REST API com Spring Boot
3. **worker**: Processamento background com scheduler

## Consequências

### Positivas

- ✅ Separação clara de responsabilidades
- ✅ Core reutilizável e testável
- ✅ Fácil migração para microserviços
- ✅ Pode rodar como monólito ou separado

### Negativas

- ⚠️ Complexidade inicial maior
- ⚠️ Curva de aprendizado

## Alternativas Consideradas

1. **Monólito único**: Descartado - dificulta separação futura
2. **Microserviços desde o início**: Descartado - complexidade desnecessária no MVP
