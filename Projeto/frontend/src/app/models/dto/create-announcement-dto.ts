import { AnnouncementType } from '../domain/announcement';

export interface CreateAnnouncementDto {
    title: string;
    description: string;
    eventDate?: string;
    location?: string;
    idCreator: number;
    announcementType: AnnouncementType;
}