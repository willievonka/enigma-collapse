import { RequestStatus } from '../enums/request-status.enum';
import { StatusCounterTitle } from '../enums/status-counter-title.enum';
import { IStatusCounter } from '../interfaces/status-counter.interface';

type StatusCounterConfig = Omit<IStatusCounter, 'count'> & {
  match: (status: RequestStatus) => boolean;
};

export const STATUS_COUNTERS: StatusCounterConfig[] = [
    {
        title: StatusCounterTitle.Total,
        icon: '@tui.list',
        match: () => true,
    },
    {
        title: StatusCounterTitle.New,
        icon: '@tui.star',
        match: (s: RequestStatus) => s === RequestStatus.New,
    },
    {
        title: StatusCounterTitle.InProgress,
        icon: '@tui.loader',
        match: (s: RequestStatus) => s === RequestStatus.InProgress,
    },
    {
        title: StatusCounterTitle.Resolved,
        icon: '@tui.check',
        match: (s: RequestStatus) => s === RequestStatus.Resolved,
    },
] as const;
