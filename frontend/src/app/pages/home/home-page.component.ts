import { ChangeDetectionStrategy, Component, computed, DestroyRef, inject, signal, Signal, WritableSignal } from '@angular/core';
import { DataRequestService } from '../../services/data-request.service';
import { IRequest } from '../../interfaces/request.interface';
import { Status } from '../../enums/status.enum';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { tap } from 'rxjs';

type StatusCounters = Record<Status, number> & { total: number };

@Component({
    selector: 'home-page',
    templateUrl: './home-page.component.html',
    styleUrl: './styles/home-page.master.scss',
    changeDetection: ChangeDetectionStrategy.OnPush,
    providers: [DataRequestService]
})
export class HomePageComponent {
    protected readonly data: WritableSignal<IRequest[]> = signal([]);

    protected readonly requestCount: Signal<number> = computed(() => this._counters().total);
    protected readonly newCount: Signal<number> = computed(() => this._counters()[Status.New]);
    protected readonly inProgressCount: Signal<number> = computed(() => this._counters()[Status.InProgress]);
    protected readonly resolvedCount: Signal<number> = computed(() => this._counters()[Status.Resolved]);

    private readonly _dataRequestService: DataRequestService = inject(DataRequestService);
    private readonly _destroyRef: DestroyRef = inject(DestroyRef);

    private readonly _counters: Signal<StatusCounters> = computed(() => {
        const initial: StatusCounters = {
            total: 0,
            [Status.New]: 0,
            [Status.InProgress]: 0,
            [Status.Resolved]: 0,
        };

        return this.data().reduce((acc: StatusCounters, item: IRequest) => {
            acc.total++;
            acc[item.status]++;

            return acc;
        }, initial);
    });

    constructor() {
        this.init();
    }

    /** Инициализация */
    private init(): void {
        this._dataRequestService.getData()
            .pipe(
                tap((data) => this.data.set(data)),
                takeUntilDestroyed(this._destroyRef)
            )
            .subscribe();
    }
}
