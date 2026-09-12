package br.fai.faitec.petlink2026.dto.annoucement;

import br.fai.faitec.petlink2026.domain.annoucement.AnnoucementModel;
import br.fai.faitec.petlink2026.domain.annoucement.AnnoucementType;
import br.fai.faitec.petlink2026.domain.pet.PetModel;
import br.fai.faitec.petlink2026.domain.pet.Specie;

import java.sql.Date;

public class CreateAnnoucementDto {

    private String title;
    private String description;
    private Date publicationDate;
    private Date eventDate;
    private String location;
    private int idCreator;
    private AnnoucementType annoucementType;


    public AnnoucementModel toAnnoucementModel() {
        final AnnoucementModel annoucementModel = new AnnoucementModel();
        annoucementModel.setTitle(title);
        annoucementModel.setDescription(description);
        annoucementModel.setPublicationDate(publicationDate);
        annoucementModel.setEventDate(eventDate);
        annoucementModel.setLocation(location);
        annoucementModel.setIdCreator(idCreator);
        annoucementModel.setAnnoucementType(annoucementType);

        return annoucementModel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getIdCreator() {
        return idCreator;
    }

    public void setIdCreator(int idCreator) {
        this.idCreator = idCreator;
    }

    public AnnoucementType getAnnoucementType() {
        return annoucementType;
    }

    public void setAnnoucementType(AnnoucementType annoucementType) {
        this.annoucementType = annoucementType;
    }


}
