import { inject, Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { IRequest } from '../interfaces/request.interface';
import { HttpClient } from '@angular/common/http';

const API_URL: string = 'http://localhost:3123';

@Injectable({ providedIn: 'root' })
export class DataRequestService {
    private readonly _httpClient: HttpClient = inject(HttpClient);

    /** Получить заявки */
    public getRequests(): Observable<IRequest[]> {
        return this._httpClient.get<IRequest[]>(`${API_URL}/offers`)
            .pipe(
                map((requests) =>
                    [...requests].sort((a, b) => b.id - a.id))
            );
    }
}
