import { TuiButton, TuiRoot, TuiTitle } from '@taiga-ui/core';
import { ChangeDetectionStrategy, Component, signal, WritableSignal } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
    selector: 'app-root',
    imports: [RouterOutlet, TuiRoot, TuiTitle, TuiButton],
    templateUrl: './app.html',
    styleUrl: './app.scss',
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class App {
    protected readonly title: WritableSignal<string> = signal('Enigma Collapse');
}
