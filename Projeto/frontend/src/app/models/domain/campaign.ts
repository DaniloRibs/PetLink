export interface VaccineCampaign {
    id?: string;
    title: string;
    description: string;
    date?: string;

    // Quem publicou a campanha (sempre uma conta do tipo empresa).
    companyEmail: string;
    companyName: string;
}
