import { useMemo, useState } from "react";
import { apiBaseUrlStorageKey, getApiBaseUrl } from "../api/client";
import { Modal } from "./ui/Modal";

type ApiSettingsModalProps = {
  isOpen: boolean;
  onClose: () => void;
};

export function ApiSettingsModal({ isOpen, onClose }: ApiSettingsModalProps) {
  const current = useMemo(() => getApiBaseUrl(), []);
  const [value, setValue] = useState<string>(current);
  const [error, setError] = useState<string | null>(null);

  const onSave = () => {
    const trimmed = value.trim();
    if (!trimmed) {
      setError(
        "Informe uma URL (ex.: /api/v1 ou http://localhost:8080/api/v1)",
      );
      return;
    }

    try {
      window.localStorage.setItem(apiBaseUrlStorageKey, trimmed);
      setError(null);
      window.location.reload();
    } catch {
      setError(
        "Não foi possível salvar no navegador (localStorage indisponível)",
      );
    }
  };

  const onReset = () => {
    try {
      window.localStorage.removeItem(apiBaseUrlStorageKey);
      window.location.reload();
    } catch {
      setError("Não foi possível limpar a configuração");
    }
  };

  return (
    <Modal title="Configuração da API" isOpen={isOpen} onClose={onClose}>
      <div className="space-y-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Endpoint/Base URL
          </label>
          <input
            className="input"
            value={value}
            onChange={(e) => setValue(e.target.value)}
            placeholder="/api/v1"
          />
          <p className="text-xs text-gray-500 mt-2">
            Dica: no dev, use <span className="font-medium">/api/v1</span>{" "}
            (proxy do Vite). Em produção, use a URL completa do backend.
          </p>
        </div>

        {error && <p className="text-sm text-red-600">{error}</p>}

        <div className="flex items-center justify-end gap-2">
          <button type="button" className="btn-secondary" onClick={onReset}>
            Restaurar padrão
          </button>
          <button type="button" className="btn-primary" onClick={onSave}>
            Salvar
          </button>
        </div>
      </div>
    </Modal>
  );
}
