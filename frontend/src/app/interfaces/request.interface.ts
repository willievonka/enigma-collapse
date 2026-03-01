import { Category } from '../enums/category.enum';
import { RequestStatus } from '../enums/request-status.enum';
import { Tonality } from '../enums/tonality.enum';

export interface IRequest {
    readonly id: number;
    readonly date: Date;
    readonly name: string;
    readonly enterpriseTitle: string;
    readonly phone: string;
    readonly email: string;
    readonly subject: string;
    readonly factoryNumberList: string[] | null;
    readonly deviceType: string;
    readonly category: Category;
    readonly tonality: Tonality;
    status: RequestStatus;
    readonly description: string;
    readonly response: string;
}
