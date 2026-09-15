package br.fai.faitec.petlink2026.dto.annoucement;

import br.fai.faitec.petlink2026.domain.annoucement.AnnoucementType;

import java.sql.Date;

public class ReadAnnouncementDto {


    private String title;
    private String description;
    private Date publicationDate;
    private Date eventDate;
    private String location;
    private String creatorEmail;
    private String creatorName;
    private AnnoucementType announcementType;

    public ReadAnnouncementDto(String title,
                               String description,
                               Date publicationDate,
                               Date eventDate,
                               String location,
                               String creatorEmail,
                               String creatorName,
                               AnnoucementType announcementType) {
        this.title = title;
        this.description = description;
        this.publicationDate = publicationDate;
        this.eventDate = eventDate;
        this.location = location;
        this.creatorEmail = creatorEmail;
        this.creatorName = creatorName;
        this.announcementType = announcementType;
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    public void setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
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

    public AnnoucementType getAnnouncementType() {
        return announcementType;
    }

    public void setAnnouncementType(AnnoucementType announcementType) {
        this.announcementType = announcementType;
    }
}
