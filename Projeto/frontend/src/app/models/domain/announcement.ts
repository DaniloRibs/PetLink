export enum AnnouncementType {
    VACCINE = 'VACCINE',
    ADOPTION = 'ADOPTION',
}

export interface Announcement {
    id?: number;
    title: string;
    description: string;
    date?: string;
    location?: string;
    lot?: string;
    creatorName: string;
    creatorEmail: string;
    type: AnnouncementType;
}