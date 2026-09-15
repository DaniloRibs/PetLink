package br.fai.faitec.petlink2026.domain.annoucement;

import java.sql.Date;

public class AnnoucementModel {
    private int id;
    private String title;
    private String description;
    private Date publicationDate;
    private Date eventDate;
    private String location;
    private int idCreator;
    private AnnoucementType announcementType;

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        return announcementType;
    }

    public void setAnnoucementType(AnnoucementType annoucementType) {
        this.announcementType = annoucementType;
    }
}