package br.fai.faitec.petlink2026.domain.announcement;

import java.sql.Date;

public class AnnouncementModel {
    private int id;
    private String title;
    private String description;
    private Date eventDate;
    private String location;
    private int idCreator;
    private AnnouncementType announcementType;

    
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

    public AnnouncementType getAnnouncementType() {
        return announcementType;
    }

    public void setAnnouncementType(AnnouncementType announcementType) {
        this.announcementType = announcementType;
    }
}