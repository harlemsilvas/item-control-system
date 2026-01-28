export function makeId() {
  return typeof crypto !== "undefined" && "randomUUID" in crypto
    ? crypto.randomUUID()
    : `${Date.now()}_${Math.random().toString(16).slice(2)}`;
}

export function toastDurationMs(variant: "success" | "error" | "info") {
  return variant === "error" ? 6000 : 3500;
}
