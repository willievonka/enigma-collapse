import { RequestStatus } from '../../../enums/request-status.enum';

export const STATUS_COLOR: Record<RequestStatus, string> = {
    [RequestStatus.Created]: '#0097b5',
    [RequestStatus.InProgress]: '#ea8100',
    [RequestStatus.Proccessed]: '#b51eab',
    [RequestStatus.Resolved]: '#007515'
};
