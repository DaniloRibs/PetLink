package br.fai.faitec.petlink2026.domain.animal;

public class FarmAnimalModel extends AnimalModel {

    private String identify;
    private boolean forSell;
    private double weight;

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public boolean isForSell() {
        return forSell;
    }

    public void setForSell(boolean forSell) {
        this.forSell = forSell;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
