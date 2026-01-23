@echo off
echo ==========================================
echo TESTE DE CRIACAO DE ALERTA
echo ==========================================
echo.

REM Buscar primeiro item
echo Buscando items...
curl -s -X GET "http://localhost:8080/api/v1/items?userId=550e8400-e29b-41d4-a716-446655440001" > items.json

REM Mostrar primeiro item
echo Items encontrados
type items.json
echo.

REM Tentar criar alerta (substitua ITEM_ID manualmente)
echo.
echo Testando criacao de alerta...
echo.

curl -v -X POST "http://localhost:8080/api/v1/alerts" ^
-H "Content-Type: application/json" ^
-d "{\"itemId\":\"806f3330-aacf-4add-bf3b-35f78f838b4f\",\"userId\":\"550e8400-e29b-41d4-a716-446655440001\",\"ruleId\":\"750e8400-e29b-41d4-a716-446655440005\",\"alertType\":\"SCHEDULED\",\"title\":\"Teste de alerta\",\"message\":\"Mensagem de teste\",\"priority\":4,\"dueAt\":\"2026-02-01T00:00:00Z\"}"

echo.
echo.
echo Teste concluido
pause
