import { ChangeDetectionStrategy, Component, computed, DestroyRef, effect, inject, input, InputSignal, output, OutputEmitterRef, Signal, signal, WritableSignal } from '@angular/core';
import { IRequest } from '../../interfaces/request.interface';
import { TuiTable, TuiTableCaption, TuiTablePagination, TuiTablePaginationEvent, TuiTableTbody } from '@taiga-ui/addon-table';
import { DatePipe } from '@angular/common';
import { TuiChevron, TuiChip, TuiSelect, TuiDataListWrapperComponent, TuiTextarea, TuiButtonLoading, TuiDataListWrapper } from '@taiga-ui/kit';
import { TuiButton, TuiDialogContext, TuiDialogService, TuiDropdown, TuiDropdownDirective, TuiTextfield, TuiTextfieldDropdownDirective } from '@taiga-ui/core';
import { Tonality } from '../../enums/tonality.enum';
import { RequestStatus } from '../../enums/request-status.enum';
import { TONALITY_COLOR } from './constants/tonality-color.constant';
import { STATUS_COLOR } from './constants/status-color.constant';
import { Category } from '../../enums/category.enum';
import { CATEGORY_COLOR } from './constants/category-color.constant';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { type PolymorpheusContent } from '@taiga-ui/polymorpheus';
import { TuiActiveZone } from '@taiga-ui/cdk/directives/active-zone';
import { TuiObscured } from '@taiga-ui/cdk/directives/obscured';
import { TuiContext, TuiStringHandler } from '@taiga-ui/cdk/types';
import { DataRequestService } from '../../services/data-request.service';
import { take } from 'rxjs';

@Component({
    selector: 'request-table',
    standalone: true,
    templateUrl: './request-table.component.html',
    styleUrl: './styles/request-table.master.scss',
    changeDetection: ChangeDetectionStrategy.OnPush,
    imports: [
        ReactiveFormsModule,
        FormsModule,
        DatePipe,
        TuiTable,
        TuiTableTbody,
        TuiChip,
        TuiButton,
        TuiSelect,
        TuiTextfield,
        TuiChevron,
        TuiDataListWrapperComponent,
        TuiTextarea,
        TuiButtonLoading,
        TuiDropdown,
        TuiActiveZone,
        TuiObscured,
        TuiTablePagination,
        TuiDropdownDirective,
        TuiTextfieldDropdownDirective,
        TuiTableCaption,
        TuiDataListWrapper,
    ]
})
export class RequestTableComponent {
    public readonly data: InputSignal<IRequest[]> = input.required();
    public readonly requestAnswered: OutputEmitterRef<number> = output();

    protected readonly paginatedData: Signal<IRequest[]> = computed(() => {
        const page: number = this.page();
        const size: number = this.size();
        const rows: IRequest[] = this.filteredData();

        const start: number = page * size;
        const end: number = start + size;

        return rows.slice(start, end);
    });
    protected readonly filteredData: Signal<IRequest[]> = computed(() => {
        let rows: IRequest[] = this.data();

        if (this.categoryFilter()) {
            rows = rows.filter(r => r.category === this.categoryFilter());
        }
        if (this.statusFilter()) {
            rows = rows.filter(r => r.status === this.statusFilter());
        }

        const searchValue: string | undefined = this.search()?.toLowerCase().trim();
        if (searchValue) {
            rows = rows.filter(r =>
                r.id?.toString().toLowerCase().includes(searchValue) ||
                r.name?.toLowerCase().includes(searchValue) ||
                r.email?.toLowerCase().includes(searchValue) ||
                r.enterpriseTitle?.toLowerCase().includes(searchValue) ||
                r.deviceType?.toLowerCase().includes(searchValue)
            );
        }

        return rows;
    });

    protected readonly page: WritableSignal<number> = signal(0);
    protected readonly size: WritableSignal<number> = signal(10);

    protected readonly statusTitles: RequestStatus[] = Object.values(RequestStatus);
    protected readonly categoryTitles: Category[] = Object.values(Category);

    protected readonly tonalityColor: Record<Tonality, string> = TONALITY_COLOR;
    protected readonly statusColor: Record<RequestStatus, string> = STATUS_COLOR;
    protected readonly categoryColor: Record<Category, string> = CATEGORY_COLOR;

    protected readonly categoryFilter: WritableSignal<Category | null> = signal(null);
    protected readonly statusFilter: WritableSignal<RequestStatus | null> = signal(null);
    protected readonly search: WritableSignal<string | null> = signal(null);

    protected readonly question: FormControl<string | null> = new FormControl<string | null>('');
    protected readonly reply: FormControl<string | null> = new FormControl<string | null>('');

    protected readonly activeRow: WritableSignal<IRequest | null> = signal(null);
    protected readonly replyInProgress: WritableSignal<boolean> = signal(false);
    protected readonly isDownloadDropdownOpen: WritableSignal<boolean> = signal(false);

    private readonly _dataService: DataRequestService = inject(DataRequestService);
    private readonly _modalService: TuiDialogService = inject(TuiDialogService);
    private readonly _destroyRef: DestroyRef = inject(DestroyRef);

    constructor() {
        effect(() => {
            this.categoryFilter();
            this.statusFilter();
            this.search();

            this.page.set(0);
        });
    }

    protected readonly content: TuiStringHandler<TuiContext<number>> = ({ $implicit }: { $implicit: number }) => `${$implicit} items per page`;

    /** Обработчик изменения пагинации */
    protected handlePaginationChange({ page, size }: TuiTablePaginationEvent): void {
        this.page.set(page);
        this.size.set(size);
    }

    /** Открыть / закрыть дропдаун экспорта */
    protected toggleDownloadDropdown(): void {
        this.isDownloadDropdownOpen.set(!this.isDownloadDropdownOpen());
    }

    /** Обработчик Obscrured стейта */
    protected handleObscured(obscured: boolean): void {
        if (obscured) {
            this.isDownloadDropdownOpen.set(false);
        }
    }

    /** Обработчик handleActiveZone */
    protected handleActiveZone(active: boolean): void {
        this.isDownloadDropdownOpen.set(active && this.isDownloadDropdownOpen());
    }

    /** Открыть модалку */
    protected openModal(row: IRequest, content: PolymorpheusContent<TuiDialogContext>): void {
        this.activeRow.set(row);
        this.question.setValue(row.description);
        this.reply.setValue(row.response || null);

        this._modalService.open(content, { size: 'm' })
            .pipe(takeUntilDestroyed(this._destroyRef))
            .subscribe();
    }

    /** Ответить на заявку */
    protected replyOnRequest(request: IRequest): void {
        if (this.replyInProgress()) {
            return;
        }

        this.replyInProgress.set(true);
        this._dataService.replyOnRequest(request.id, this.reply.getRawValue() || 'Без ответа')
            .pipe(take(1))
            .subscribe(() => {
                request.status = RequestStatus.Resolved;
                this.requestAnswered.emit(request.id);
                this.replyInProgress.set(false);
            });
    }

    /** Скачать документ */
    protected downloadDocument(type: 'csv' | 'xlsx'): void {
        this._dataService.downloadDocument(type)
            .pipe(take(1))
            .subscribe({
                next: (blob) => {
                    const url: string = URL.createObjectURL(blob);

                    const a: HTMLAnchorElement = document.createElement('a');
                    a.href = url;
                    a.download = `tickets.${type}`;
                    a.click();

                    URL.revokeObjectURL(url);
                },
                error: (err) => {
                    console.error('Ошибка при скачивании файла', err);
                },
            });
    }

    /** К начертанию с заглавной */
    protected capitalizeFirstLetter(string: string): string {
        return string.charAt(0).toUpperCase() + string.slice(1);
    }
}
