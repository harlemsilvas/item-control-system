import type { ReactNode } from "react";

export function InlineCode({ children }: { children: ReactNode }) {
  return (
    <code className="px-1.5 py-0.5 rounded bg-gray-100 border border-gray-200 font-mono text-xs">
      {children}
    </code>
  );
}
