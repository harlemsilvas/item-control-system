import { type ReactNode, useEffect } from "react";

type ModalProps = {
  title: string;
  children: ReactNode;
  isOpen: boolean;
  onClose: () => void;
};

export function Modal({ title, children, isOpen, onClose }: ModalProps) {
  useEffect(() => {
    if (!isOpen) return;

    const onKeyDown = (event: KeyboardEvent) => {
      if (event.key === "Escape") onClose();
    };

    window.addEventListener("keydown", onKeyDown);
    return () => window.removeEventListener("keydown", onKeyDown);
  }, [isOpen, onClose]);

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center">
      <div
        className="absolute inset-0 bg-black/50"
        onClick={onClose}
        aria-hidden="true"
      />

      <div className="relative w-full max-w-xl mx-4 bg-white rounded-lg shadow-lg border border-gray-200">
        <div className="flex items-center justify-between px-6 py-4 border-b border-gray-200">
          <h2 className="text-lg font-semibold text-gray-900">{title}</h2>
          <button className="btn-secondary" type="button" onClick={onClose}>
            Fechar
          </button>
        </div>

        <div className="p-6">{children}</div>
      </div>
    </div>
  );
}
