#!/usr/bin/env python3
"""
Diagnostico de criacao de alertas - Python
Mais confiavel que PowerShell para debug HTTP
"""

import requests
import json
from datetime import datetime, timedelta
import uuid

BASE_URL = "http://localhost:8080/api/v1"
USER_ID = "550e8400-e29b-41d4-a716-446655440001"

print("=" * 50)
print("DIAGNOSTICO - Criacao de Alertas (Python)")
print("=" * 50)
print()

# Passo 1: Buscar items
print("PASSO 1: Buscando items...")
try:
    response = requests.get(f"{BASE_URL}/items", params={"userId": USER_ID})
    response.raise_for_status()
    items = response.json()
    print(f"  ✓ {len(items)} items encontrados")

    if len(items) == 0:
        print("  ✗ Nenhum item encontrado!")
        exit(1)

    test_item = items[0]
    print(f"  Item de teste: {test_item['name']}")
    print(f"  ID: {test_item['id']}")
    print()

except Exception as e:
    print(f"  ✗ Erro: {e}")
    exit(1)

# Passo 2: Testar criacao de alerta
print("PASSO 2: Testando criacao de alerta...")

# Formato de data que funcionou para eventos
due_date = (datetime.utcnow() + timedelta(days=7)).strftime("%Y-%m-%dT%H:%M:%SZ")

alert_data = {
    "itemId": test_item['id'],
    "userId": USER_ID,
    "ruleId": str(uuid.uuid4()),
    "alertType": "SCHEDULED",
    "title": "Teste Python - Alert",
    "message": "Teste de criacao via Python",
    "priority": 4,
    "dueAt": due_date
}

print("  JSON sendo enviado:")
print(json.dumps(alert_data, indent=2))
print()

try:
    response = requests.post(
        f"{BASE_URL}/alerts",
        json=alert_data,
        headers={"Content-Type": "application/json"}
    )

    print(f"  Status Code: {response.status_code}")

    if response.status_code == 201 or response.status_code == 200:
        print("  ✓ SUCESSO!")
        alert = response.json()
        print(f"  Alert ID: {alert['id']}")
        print(f"  Titulo: {alert['title']}")
        print(f"  Prioridade: {alert['priority']}")
    else:
        print(f"  ✗ FALHOU!")
        print(f"  Response: {response.text}")

except requests.exceptions.HTTPError as e:
    print(f"  ✗ HTTP Error: {e}")
    print(f"  Response: {e.response.text}")
except Exception as e:
    print(f"  ✗ Erro: {e}")

print()

# Passo 3: Testar diferentes formatos de data
print("PASSO 3: Testando formatos de data...")

date_formats = {
    "yyyy-MM-ddTHH:mm:ssZ": datetime.utcnow() + timedelta(days=10),
    "yyyy-MM-ddTHH:mm:ss.fffZ": datetime.utcnow() + timedelta(days=10),
}

for format_name, date_obj in date_formats.items():
    if "fff" in format_name:
        date_str = date_obj.strftime("%Y-%m-%dT%H:%M:%S.%fZ")
    else:
        date_str = date_obj.strftime("%Y-%m-%dT%H:%M:%SZ")

    print(f"  Formato: {format_name}")
    print(f"  Valor: {date_str}")

    alert_data = {
        "itemId": test_item['id'],
        "userId": USER_ID,
        "ruleId": str(uuid.uuid4()),
        "alertType": "SCHEDULED",
        "title": f"Teste formato {format_name}",
        "message": "Teste de formato de data",
        "priority": 3,
        "dueAt": date_str
    }

    try:
        response = requests.post(f"{BASE_URL}/alerts", json=alert_data)
        if response.status_code in [200, 201]:
            print(f"    ✓ OK - Formato aceito")
        else:
            print(f"    ✗ FALHOU - Status: {response.status_code}")
            print(f"    Response: {response.text[:200]}")
    except Exception as e:
        print(f"    ✗ Erro: {e}")
    print()

# Passo 4: Verificar alertas criados
print("PASSO 4: Verificando alertas criados...")
try:
    response = requests.get(f"{BASE_URL}/alerts/pending", params={"userId": USER_ID})
    response.raise_for_status()
    alerts = response.json()
    print(f"  Total de alertas pendentes: {len(alerts)}")

    if len(alerts) > 0:
        print("  Ultimo alerta:")
        last = alerts[-1]
        print(f"    ID: {last['id']}")
        print(f"    Titulo: {last['title']}")
        print(f"    Prioridade: {last['priority']}")

except Exception as e:
    print(f"  Erro ao buscar alertas: {e}")

print()
print("=" * 50)
print("FIM DO DIAGNOSTICO")
print("=" * 50)
