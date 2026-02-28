import { RequestStatus } from '../enums/request-status.enum';
import { Tonality } from '../enums/tonality.enum';

export interface IRequest {
    readonly date: Date;
    readonly name: string;
    readonly enterpriseTitle: string;
    readonly phone: string;
    readonly email: string;
    readonly factoryNumberList: string[];
    readonly deviceType: string;
    readonly tonality: Tonality;
    readonly status: RequestStatus;
    readonly description: string;
}
