import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { IRequest } from '../interfaces/request.interface';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class DataRequestService {
    private readonly _httpClient: HttpClient = inject(HttpClient);

    /** Получить данные */
    public getData(): Observable<IRequest[]> {
        return this._httpClient.get<IRequest[]>('https://data.ru/mocks');
    }
}
