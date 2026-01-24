# ====================================================================
# Script para encerrar processos travados na porta 8080
# ====================================================================

Write-Host "🔍 VERIFICANDO PORTA 8080" -ForegroundColor Cyan
Write-Host "=========================" -ForegroundColor Cyan
Write-Host ""

# Buscar processos usando a porta 8080
$port8080 = Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue

if ($port8080) {
    Write-Host "⚠️  Processos encontrados na porta 8080:" -ForegroundColor Yellow
    Write-Host ""

    foreach ($connection in $port8080) {
        $processId = $connection.OwningProcess
        $process = Get-Process -Id $processId -ErrorAction SilentlyContinue

        if ($process) {
            Write-Host "   📌 Processo: $($process.ProcessName)" -ForegroundColor White
            Write-Host "   🆔 PID: $processId" -ForegroundColor Gray
            Write-Host "   💾 Memória: $([math]::Round($process.WorkingSet64/1MB, 2)) MB" -ForegroundColor Gray
            Write-Host "   ⏱️  Iniciado: $($process.StartTime)" -ForegroundColor Gray
            Write-Host ""
        }
    }

    # Perguntar confirmação
    $resposta = Read-Host "Deseja encerrar TODOS os processos? (S/N)"

    if ($resposta -eq "S" -or $resposta -eq "s") {
        Write-Host ""
        Write-Host "🛑 Encerrando processos..." -ForegroundColor Red

        foreach ($connection in $port8080) {
            $processId = $connection.OwningProcess
            $process = Get-Process -Id $processId -ErrorAction SilentlyContinue

            if ($process) {
                Write-Host "   ❌ Encerrando: $($process.ProcessName) (PID: $processId)" -ForegroundColor Red
                Stop-Process -Id $processId -Force -ErrorAction SilentlyContinue
            }
        }

        Start-Sleep -Seconds 2

        # Verificar novamente
        $portaLivre = Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue

        if (-not $portaLivre) {
            Write-Host ""
            Write-Host "✅ Porta 8080 liberada com sucesso!" -ForegroundColor Green
        } else {
            Write-Host ""
            Write-Host "⚠️  Alguns processos ainda estão ativos. Tente novamente." -ForegroundColor Yellow
        }
    } else {
        Write-Host ""
        Write-Host "❌ Operação cancelada." -ForegroundColor Yellow
    }

} else {
    Write-Host "✅ Porta 8080 está disponível!" -ForegroundColor Green
    Write-Host "   Nenhum processo usando a porta." -ForegroundColor Gray
}

Write-Host ""
Write-Host "Pressione qualquer tecla para sair..."
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")

