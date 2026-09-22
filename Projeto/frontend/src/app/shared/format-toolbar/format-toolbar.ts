import { Component, Input } from '@angular/core';
import { AbstractControl } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';

type InlineFormat = 'bold' | 'italic';

@Component({
    selector: 'app-format-toolbar',
    standalone: true,
    imports: [MatIconModule],
    templateUrl: './format-toolbar.html',
    styleUrl: './format-toolbar.css',
})
export class FormatToolbarComponent {

    @Input({ required: true }) control!: AbstractControl;
    @Input({ required: true }) textarea!: HTMLTextAreaElement;

    boldActive = false;
    italicActive = false;

    private readonly markers: Record<InlineFormat, string> = {
        bold: '**',
        italic: '*',
    };

    toggleInline(type: InlineFormat): void {
        if (!this.textarea || !this.control) {
            return;
        }

        const marker = this.markers[type];
        const isActive = type === 'bold' ? this.boldActive : this.italicActive;
        const cursor = this.textarea.selectionStart ?? 0;
        const value: string = this.control.value || '';

        const newValue = value.substring(0, cursor) + marker + value.substring(cursor);
        this.control.setValue(newValue);

        if (type === 'bold') {
            this.boldActive = !isActive;
        } else {
            this.italicActive = !isActive;
        }

        const newCursorPos = cursor + marker.length;
        setTimeout(() => {
            this.textarea.focus();
            this.textarea.setSelectionRange(newCursorPos, newCursorPos);
        });
    }

    toggleHeading(): void {
        if (!this.textarea || !this.control) {
            return;
        }

        const cursor = this.textarea.selectionStart ?? 0;
        const value: string = this.control.value || '';

        const lineStart = value.lastIndexOf('\n', cursor - 1) + 1;
        let lineEnd = value.indexOf('\n', cursor);
        if (lineEnd === -1) {
            lineEnd = value.length;
        }

        const line = value.substring(lineStart, lineEnd);
        let newLine: string;
        let cursorShift: number;

        if (line.startsWith('# ')) {
            newLine = line.substring(2);
            cursorShift = -2;
        } else {
            newLine = '# ' + line;
            cursorShift = 2;
        }

        const newValue = value.substring(0, lineStart) + newLine + value.substring(lineEnd);
        this.control.setValue(newValue);

        const newCursorPos = Math.max(lineStart, cursor + cursorShift);
        setTimeout(() => {
            this.textarea.focus();
            this.textarea.setSelectionRange(newCursorPos, newCursorPos);
        });
    }

    applyPlainText(): void {
        if (!this.textarea || !this.control) {
            return;
        }

        let cursor = this.textarea.selectionStart ?? 0;
        let value: string = this.control.value || '';

        if (this.boldActive) {
            value = value.substring(0, cursor) + '**' + value.substring(cursor);
            cursor += 2;
            this.boldActive = false;
        }
        if (this.italicActive) {
            value = value.substring(0, cursor) + '*' + value.substring(cursor);
            cursor += 1;
            this.italicActive = false;
        }

        this.control.setValue(value);

        setTimeout(() => {
            this.textarea.focus();
            this.textarea.setSelectionRange(cursor, cursor);
        });
    }
}