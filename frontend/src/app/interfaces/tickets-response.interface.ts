export interface ITicketsResponse {
    content: Array<{
        id: number,
        fullName: string,
        companyName: string,
        phone: string,
        email: string,
        subject: string,
        rawEmailText: string,
        serialNumbers: string[],
        deviceType: string,
        category: string,
        sentiment: string,
        status: string,
        response: string,
        createdAt: string
    }>
    page: number,
    size: number,
    totalElements: number,
    totalPages: number
}
