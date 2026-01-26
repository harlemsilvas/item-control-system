# 045 - Fix: Encoding/Unicode em scripts PowerShell (DEV)

**Data:** 2026-01-26  
**Status:** Resolvido (DEV local)  

## Contexto
Durante a execução do script `scripts/start-all-dev.ps1`, o PowerShell retornou erro de parser, impedindo a inicialização do ambiente de desenvolvimento.

## Sintomas
Ao rodar:

- `./scripts/start-all-dev.ps1`

O PowerShell falhava com erros como:

- `ParserError: TerminatorExpectedAtEndOfString` (string sem terminador)
- `ParserError: MissingEndCurlyBrace` (chave de fechamento ausente)

Esses erros apareciam mesmo com o script aparentemente correto.

## Causa raiz
O arquivo `start-all-dev.ps1` continha caracteres Unicode/emoji (ex.: ✅, 🚀, 🛑 etc.).

Em alguns cenários no Windows/PowerShell (dependendo de:
- encoding do arquivo no Git/Editor
- codepage ativa no terminal
- forma como o comando foi invocado via `powershell -Command`
), esses caracteres podem corromper o parse do script e causar falhas **falsas** de sintaxe (como se aspas/chaves estivessem quebradas).

## Solução aplicada
1. **Remoção/substituição de emojis** no cabeçalho e no resumo final do `scripts/start-all-dev.ps1`, trocando por mensagens ASCII simples.
2. Mantida toda a lógica do script (execução do MongoDB, backend e frontend), alterando apenas strings exibidas no console.

## Resultado
- O `start-all-dev.ps1` voltou a ser interpretado corretamente pelo PowerShell.
- O ambiente DEV consegue subir abrindo as janelas do backend e do frontend.

## Observações
- Nos demais scripts, emojis podem continuar sendo usados, mas o ideal é:
  - padronizar arquivos `.ps1` em **UTF-8 sem BOM**
  - evitar Unicode “exótico” em scripts críticos (especialmente os que são chamados por outros scripts ou via `powershell -Command`).

## Próximos passos (se quisermos robustez total)
- Padronizar encoding dos scripts com um check automatizado.
- Adicionar um script `scripts/fix-encoding.ps1` (opcional) para converter `.ps1` para UTF-8 sem BOM.
