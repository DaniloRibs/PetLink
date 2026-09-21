package br.fai.faitec.petlink2026.domain.vaccine;

import java.text.Normalizer;
import java.util.Locale;


public enum VaccineType {

    RABIES("Antirrábica", "raiva", "rabica", "antirrabica", "antirrabico", "anti rabica", "rabies"),

    CANINE_MULTIPLE("Múltipla canina (V8/V10)", "v8", "v10", "octupla", "decupla",
            "multipla canina", "polivalente canina"),

    FELINE_MULTIPLE("Múltipla felina (V3/V4/V5)", "v3", "v4", "v5", "triplice felina",
            "quadrupla felina", "quintupla felina", "multipla felina", "polivalente felina"),

    LEPTOSPIROSIS("Leptospirose", "leptospirose", "lepto"),

    KENNEL_COUGH("Gripe canina", "gripe canina", "tosse dos canis", "tosse canina",
            "traqueobronquite", "bordetella"),

    GIARDIA("Giárdia", "giardia", "giardiase"),

    LEISHMANIASIS("Leishmaniose", "leishmaniose", "leish"),

    FELV("Leucemia felina (FeLV)", "felv", "leucemia felina"),

    FOOT_AND_MOUTH("Febre aftosa", "febre aftosa", "aftosa"),

    BRUCELLOSIS("Brucelose", "brucelose"),

    TETANUS("Tétano", "tetano", "antitetanica", "antitetanico", "anti tetanica", "anti tetanico"),

    EQUINE_INFLUENZA("Influenza equina", "influenza equina", "gripe equina"),

    CLOSTRIDIAL("Clostridioses", "clostridiose", "clostridioses", "clostridial", "clostridiais"),

    NEWCASTLE("Newcastle", "newcastle"),

    SWINE_PARVOVIRUS("Parvovirose suína", "parvovirose suina", "parvovirose suino"),

    POLYOMAVIRUS("Poliomavírus", "poliomavirus");

    private final String displayName;
    private final String[] aliases;

    VaccineType(final String displayName, final String... aliases) {
        this.displayName = displayName;
        this.aliases = aliases;
    }

    public String getDisplayName() {
        return displayName;
    }


    public static VaccineType fromName(final String rawName) {
        final String padded = " " + normalize(rawName) + " ";

        for (final VaccineType type : values()) {
            for (final String alias : type.aliases) {
                if (padded.contains(" " + alias + " ")) {
                    return type;
                }
            }
        }

        return null;
    }


    public static String groupKey(final String rawName) {
        final VaccineType type = fromName(rawName);
        return type != null ? type.name() : "OTHER:" + normalize(rawName);
    }

    public static String displayNameOf(final String rawName) {
        final VaccineType type = fromName(rawName);

        if (type != null) {
            return type.displayName;
        }

        return rawName == null ? "" : rawName.trim();
    }

    static String normalize(final String rawName) {
        if (rawName == null) {
            return "";
        }

        final String withoutAccents = Normalizer.normalize(rawName, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        return withoutAccents.toLowerCase(Locale.ROOT)
                .replaceAll("\\bv[\\s\\-_.]?(\\d{1,2})\\b", "v$1")
                .replaceAll("[^a-z0-9]+", " ")
                .trim();
    }
}
