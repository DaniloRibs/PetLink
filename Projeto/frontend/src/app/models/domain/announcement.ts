export enum AnnouncementType {
    VACCINE = 'VACCINE',
    ADOPTION = 'ADOPTION',
    LOST = 'LOST',
}

export interface Announcement {
    id?: number;
    title: string;
    description: string;
    eventDate?: string;
    publicationDate?: string;
    location?: string;
    creatorEmail: string;
    creatorName: string;
    announcementType: AnnouncementType;
}