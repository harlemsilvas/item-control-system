<#
Cria stubs (arquivos "Documento movido") no caminho antigo, apontando para o novo.

Uso:
  cd <repo>
  powershell -ExecutionPolicy Bypass -File scripts/create-doc-stubs.ps1

O script lê os renames no index (staged) usando `git diff --cached --name-status`
 e cria stubs para arquivos .md que foram movidos dentro de docs/.

Regras:
- Só atua em arquivos markdown (.md)
- Só atua em caminhos antigos dentro de docs/
- Não sobrescreve stubs existentes

Criado em: 28/01/2026
Última modificação: 28/01/2026
Data de liquidação: —
#>

$ErrorActionPreference = "Stop"

function New-StubFile {
  param(
    [Parameter(Mandatory = $true)][string]$OldPath,
    [Parameter(Mandatory = $true)][string]$NewPath
  )

  if (Test-Path $OldPath) {
    # Se ainda existe, não sobrescreve.
    return
  }

  $parent = Split-Path -Parent $OldPath
  if ($parent -and -not (Test-Path $parent)) {
    New-Item -ItemType Directory -Path $parent | Out-Null
  }

  $rel = $NewPath.Replace("docs/", "")
  $link = $NewPath.Replace("docs/", "")

  $content = @(
    "# Documento movido",
    "",
    "Este documento foi movido para:",
    "- **[`$NewPath`]($link)**",
    "",
    "---",
    "",
    "**Criado em:** 28/01/2026  ",
    "**Última modificação:** 28/01/2026  ",
    "**Data de liquidação:** —",
    ""
  ) -join "`r`n"

  Set-Content -Path $OldPath -Value $content -Encoding UTF8
}

$diff = git diff --cached --name-status
if (-not $diff) {
  Write-Host "Nenhum rename staged encontrado (git diff --cached vazio)." -ForegroundColor Yellow
  exit 0
}

$lines = $diff -split "`n" | ForEach-Object { $_.Trim() } | Where-Object { $_ }

foreach ($line in $lines) {
  # Exemplo: R100<TAB>docs/A.md<TAB>docs/x/A.md
  if ($line -notmatch "^R\d+\s+") { continue }

  $parts = $line -split "\s+"
  if ($parts.Count -lt 3) { continue }

  $old = $parts[1]
  $new = $parts[2]

  if (-not $old.ToLower().EndsWith(".md")) { continue }
  if (-not $new.ToLower().EndsWith(".md")) { continue }
  if (-not $old.StartsWith("docs/")) { continue }
  if (-not $new.StartsWith("docs/")) { continue }

  New-StubFile -OldPath $old -NewPath $new
}

Write-Host "Stubs gerados (quando aplicável)." -ForegroundColor Green
