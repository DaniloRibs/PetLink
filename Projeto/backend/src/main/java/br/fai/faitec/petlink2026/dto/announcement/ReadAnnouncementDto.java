package br.fai.faitec.petlink2026.dto.announcement;

import br.fai.faitec.petlink2026.domain.announcement.AnnouncementType;

import java.sql.Date;

public class ReadAnnouncementDto {

    private int id;
    private String title;
    private String description;
    private Date eventDate;
    private String location;
    private String creatorEmail;
    private String creatorName;
    private AnnouncementType announcementType;

    public ReadAnnouncementDto(int id,
                               String title,
                               String description,
                               Date eventDate,
                               String location,
                               String creatorEmail,
                               String creatorName,
                               AnnouncementType announcementType) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.eventDate = eventDate;
        this.location = location;
        this.creatorEmail = creatorEmail;
        this.creatorName = creatorName;
        this.announcementType = announcementType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public AnnouncementType getAnnouncementType() {
        return announcementType;
    }

    public void setAnnouncementType(AnnouncementType announcementType) {
        this.announcementType = announcementType;
    }
}
