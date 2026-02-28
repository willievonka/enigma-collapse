import { ChangeDetectionStrategy, Component, computed, DestroyRef, inject, signal, Signal, WritableSignal } from '@angular/core';
import { DataRequestService } from '../../services/data-request.service';
import { IRequest } from '../../interfaces/request.interface';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { tap } from 'rxjs';
import { CounterBarComponent } from '../../components/counter-bar/counter-bar.component';
import { IStatusCounter } from '../../interfaces/status-counter.interface';
import { STATUS_COUNTERS } from '../../constants/status-counters.constant';

@Component({
    selector: 'home-page',
    templateUrl: './home-page.component.html',
    styleUrl: './styles/home-page.master.scss',
    changeDetection: ChangeDetectionStrategy.OnPush,
    providers: [DataRequestService],
    imports: [CounterBarComponent]
})
export class HomePageComponent {
    protected readonly counters: Signal<IStatusCounter[]> = computed(() =>
        STATUS_COUNTERS.map(counter => ({
            title: counter.title,
            icon: counter.icon,
            count: this._requestList().filter(request => counter.match(request.status)).length
        }))
    );

    private readonly _requestList: WritableSignal<IRequest[]> = signal([]);
    private readonly _dataRequestService: DataRequestService = inject(DataRequestService);
    private readonly _destroyRef: DestroyRef = inject(DestroyRef);

    constructor() {
        this.init();
    }

    /** Инициализация */
    private init(): void {
        this._dataRequestService.getRequests()
            .pipe(
                tap((requests) => this._requestList.set(requests)),
                takeUntilDestroyed(this._destroyRef)
            )
            .subscribe();
    }
}
