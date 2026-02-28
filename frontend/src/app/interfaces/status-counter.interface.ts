import { StatusCounterTitle } from '../enums/status-counter-title.enum';

export interface IStatusCounter {
  title: StatusCounterTitle;
  count: number;
  icon: string;
}
