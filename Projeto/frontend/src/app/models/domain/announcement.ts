export enum AnnouncementType {
    VACCINE = 'vaccine',
    ADOPTION = 'adoption',
}

export interface Announcement {
    id?: number;
    title: string;
    description: string;
    date?: string;
    location?: string;
    creatorName: string;
    creatorEmail: string;
    type: AnnouncementType;
}