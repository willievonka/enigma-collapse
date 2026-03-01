import { inject, Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { IRequest } from '../interfaces/request.interface';
import { HttpClient } from '@angular/common/http';
import { ITicketsResponse } from '../interfaces/tickets-response.interface';
import { Category } from '../enums/category.enum';
import { RequestStatus } from '../enums/request-status.enum';
import { Tonality } from '../enums/tonality.enum';

const API_URL: string = 'https://enigma.yakovlev05.ru/api/v1';

type CategoryToken =
    | 'MAINTENANCE'
    | 'MODERNIZATION'
    | 'DIAGNOSTICS'
    | 'NODE_REPLACEMENT'
    | 'APPROVAL'
    | 'REPORTING'
    | 'AUDIT'
    | 'CRITICAL_ERROR'
    | 'WARNING'
    | 'RISK'
    | 'SETUP'
    | 'CALIBRATION'
    | 'CONSERVATION'
    | 'TESTING'
    | 'UNKNOWN';

type RequestStatusToken =
    | 'CREATED'
    | 'IN_PROGRESS'
    | 'PROCESSED'
    | 'RESOLVED';

type TonalityToken =
    | 'POSITIVE'
    | 'NEGATIVE'
    | 'NEUTRAL';


@Injectable({ providedIn: 'root' })
export class DataRequestService {
    private readonly _httpClient: HttpClient = inject(HttpClient);

    private readonly _categoryMap: Record<CategoryToken, Category> = {
        MAINTENANCE: Category.Maintenance,
        MODERNIZATION: Category.Modernization,
        DIAGNOSTICS: Category.Diagnostics,
        NODE_REPLACEMENT: Category.NodeReplacement,
        APPROVAL: Category.Approval,
        REPORTING: Category.Reporting,
        AUDIT: Category.Audit,
        CRITICAL_ERROR: Category.CriticalError,
        WARNING: Category.Warning,
        RISK: Category.Risk,
        SETUP: Category.Setup,
        CALIBRATION: Category.Calibration,
        CONSERVATION: Category.Conservation,
        TESTING: Category.Testing,
        UNKNOWN: Category.Unknown,
    };
    private readonly _statusMap: Record<RequestStatusToken, RequestStatus> = {
        CREATED: RequestStatus.Created,
        IN_PROGRESS: RequestStatus.InProgress,
        PROCESSED: RequestStatus.Proccessed,
        RESOLVED: RequestStatus.Resolved,
    };
    private readonly _tonalityMap: Record<TonalityToken, Tonality> = {
        POSITIVE: Tonality.Positive,
        NEUTRAL: Tonality.Neutral,
        NEGATIVE: Tonality.Negative
    };


    /** Получить заявки */
    public getRequests(): Observable<IRequest[]> {
        return this._httpClient.get<ITicketsResponse>(`${API_URL}/tickets`, {
            params: { page: 0, size: 10000 },
        })
            .pipe(
                map((tickets) => tickets.content.map((ticket) => ({
                    id: ticket.id,
                    date: new Date(ticket.createdAt),
                    name: ticket.fullName,
                    enterpriseTitle: ticket.companyName,
                    email: ticket.email,
                    phone: ticket.phone,
                    subject: ticket.subject,
                    factoryNumberList: ticket.serialNumbers,
                    deviceType: ticket.deviceType,
                    category: this.mapTokenToCategory(ticket.category),
                    tonality: this.mapTokenToTonality(ticket.sentiment),
                    status: this.mapTokenToStatus(ticket.status),
                    description: ticket.rawEmailText,
                    response: ticket.response,
                }) satisfies IRequest))
            );
    }

    /**
     * Скачать документ
     * @param type
     */
    public downloadDocument(type: 'csv' | 'xlsx'): Observable<Blob> {
        return this._httpClient.get(`${API_URL}/tickets/export/${type}`, {
            responseType: 'blob',
        });
    }

    /**
     * Ответить на заявку
     * @param id
     * @param message
     */
    public replyOnRequest(id: number, message: string): Observable<void> {
        return this._httpClient.post<void>(`${API_URL}/tickets/${id}/reply`, { message });
    }

    /** Маппинг категорий */
    private mapTokenToCategory(token: string | null | undefined): Category {
        if (!token) {
            return Category.Unknown;
        }

        return this._categoryMap[token as CategoryToken] ?? Category.Unknown;
    }

    /** Маппинг статусов */
    private mapTokenToStatus(token: string | null | undefined): RequestStatus {
        if (!token) {
            return RequestStatus.Created;
        }

        return this._statusMap[token as RequestStatusToken] ?? RequestStatus.Created;
    }

    /** Маппинг тональности */
    private mapTokenToTonality(token: string | null | undefined): Tonality {
        if (!token) {
            return Tonality.Neutral;
        }

        return this._tonalityMap[token as TonalityToken] ?? Tonality.Neutral;
    }
}
