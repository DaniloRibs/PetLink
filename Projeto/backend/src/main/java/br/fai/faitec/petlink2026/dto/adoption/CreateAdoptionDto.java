package br.fai.faitec.petlink2026.dto.adoption;

import br.fai.faitec.petlink2026.domain.adoption.AdoptionModel;

public class CreateAdoptionDto {

    private int petId;
    private int ownerId;
    private String description;
    private String contact;


    public AdoptionModel toAdoptionModel() {
        AdoptionModel adoptionModel = new AdoptionModel();
        adoptionModel.setPetId(petId);
        adoptionModel.setOwnerId(ownerId);
        adoptionModel.setDescription(description);
        adoptionModel.setContact(contact);
        return adoptionModel;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }


}