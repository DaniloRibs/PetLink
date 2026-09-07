export enum AnnouncementType {
    VACCINE = 'vaccine',
    ADOPTION = 'adoption',
}

export interface Announcement {
    id?: string;
    title: string;
    description: string;
    date?: string;
    creatorName: string;
    creatorEmail: string;
    type: AnnouncementType;
}