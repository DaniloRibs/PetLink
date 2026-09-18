package br.fai.faitec.petlink2026.dto.announcement;

import br.fai.faitec.petlink2026.domain.announcement.AnnouncementModel;
import br.fai.faitec.petlink2026.domain.announcement.AnnouncementType;

import java.sql.Date;

public class CreateAnnouncementDto {

    private String title;
    private String description;
    private Date eventDate;
    private String location;
    private int idCreator;
    private AnnouncementType announcementType;
    private String contact;


    public AnnouncementModel toAnnouncementModel() {
        final AnnouncementModel announcementModel = new AnnouncementModel();
        announcementModel.setTitle(title);
        announcementModel.setDescription(description);
        announcementModel.setEventDate(eventDate);
        announcementModel.setLocation(location);
        announcementModel.setIdCreator(idCreator);
        announcementModel.setAnnouncementType(announcementType);
        announcementModel.setContact(contact);

        return announcementModel;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }


}
