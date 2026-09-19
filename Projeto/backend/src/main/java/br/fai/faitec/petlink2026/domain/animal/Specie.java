package br.fai.faitec.petlink2026.domain.animal;

public enum Specie {
    DOG("Cachorro"),
    CAT("Gato"),
    BIRD("Pássaro"),
    OTHER("Outro"),
    HORSE("Equino"),
    COW("Bovino"),
    PIG("Suíno"),
    SHEEP("Ovino");

    private final String name;

    Specie(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}


