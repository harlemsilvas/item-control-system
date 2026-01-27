<#
.SYNOPSIS
  Executa um comando PowerShell e salva a saída (stdout/stderr) em .logs/ com timestamp.

.DESCRIPTION
  Uso recomendado para troubleshooting sem poluir o git.
  A pasta .logs/ é ignorada pelo .gitignore.

.EXAMPLE
  .\scripts\run-with-log.ps1 -Name "tests" -Command "mvn -q test"

.EXAMPLE
  .\scripts\run-with-log.ps1 -Name "populate-templates" -Command ".\scripts\populate-templates.ps1"
#>

param(
  [Parameter(Mandatory=$true)]
  [string]$Name,

  [Parameter(Mandatory=$true)]
  [string]$Command
)

$ErrorActionPreference = 'Stop'

$root = (Resolve-Path "$PSScriptRoot\..\").Path
$logDir = Join-Path $root '.logs'

if (!(Test-Path $logDir)) {
  New-Item -ItemType Directory -Path $logDir | Out-Null
}

$timestamp = Get-Date -Format 'yyyyMMdd-HHmmss'
$logPath = Join-Path $logDir ("{0}-{1}.log" -f $timestamp, $Name)

Write-Host "Logging to: $logPath" -ForegroundColor Yellow

# Executa o comando via cmd /c para capturar saída consistente
cmd /c "$Command" 2>&1 | Tee-Object -FilePath $logPath

Write-Host "Done." -ForegroundColor Green
