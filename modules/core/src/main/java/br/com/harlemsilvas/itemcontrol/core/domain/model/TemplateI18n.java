package br.com.harlemsilvas.itemcontrol.core.domain.model;

import java.util.Locale;
import java.util.Map;

/**
 * Utilitário simples para resolver textos por locale.
 *
 * Estratégia de fallback:
 * 1) locale completo (ex: pt-BR)
 * 2) linguagem (ex: pt)
 * 3) en / en-US
 * 4) primeiro valor disponível
 */
public final class TemplateI18n {

    private TemplateI18n() {
    }

    public static String normalize(String locale) {
        if (locale == null || locale.isBlank()) {
            return null;
        }
        // aceitamos pt_BR e pt-BR
        return locale.replace('_', '-');
    }

    public static String resolve(Map<String, String> byLocale, String locale) {
        if (byLocale == null || byLocale.isEmpty()) {
            return null;
        }

        String normalized = normalize(locale);
        if (normalized != null) {
            String direct = byLocale.get(normalized);
            if (direct != null && !direct.isBlank()) {
                return direct;
            }

            Locale parsed = Locale.forLanguageTag(normalized);
            if (parsed != null) {
                String language = parsed.getLanguage();
                if (language != null && !language.isBlank()) {
                    String byLanguage = byLocale.get(language);
                    if (byLanguage != null && !byLanguage.isBlank()) {
                        return byLanguage;
                    }
                }
            }
        }

        String en = byLocale.get("en");
        if (en != null && !en.isBlank()) {
            return en;
        }

        String enUs = byLocale.get("en-US");
        if (enUs != null && !enUs.isBlank()) {
            return enUs;
        }

        return byLocale.values().stream().filter(v -> v != null && !v.isBlank()).findFirst().orElse(null);
    }
}
