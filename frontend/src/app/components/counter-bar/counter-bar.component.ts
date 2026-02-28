import { ChangeDetectionStrategy, Component, input, InputSignal } from '@angular/core';
import { TuiCardLarge, TuiHeader } from '@taiga-ui/layout';
import { TuiSurface, TuiTitle } from '@taiga-ui/core';
import { TuiAvatar } from '@taiga-ui/kit';
import { CommonModule } from '@angular/common';
import { IStatusCounter } from '../../interfaces/status-counter.interface';

@Component({
    selector: 'counter-bar',
    standalone: true,
    templateUrl: './counter-bar.component.html',
    styleUrl: './styles/counter-bar.master.scss',
    changeDetection: ChangeDetectionStrategy.OnPush,
    imports: [
        CommonModule,
        TuiCardLarge,
        TuiTitle,
        TuiHeader,
        TuiAvatar,
        TuiSurface,
    ]
})
export class CounterBarComponent {
    public readonly counters: InputSignal<IStatusCounter[]> = input.required();
}
