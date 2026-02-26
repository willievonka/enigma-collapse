import { Status } from '../enums/status.enum';
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
    readonly status: Status;
    readonly description: string;
}
