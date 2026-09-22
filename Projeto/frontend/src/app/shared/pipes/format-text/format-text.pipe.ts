import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'formatText',
    standalone: true
})
export class FormatTextPipe implements PipeTransform {

    transform(value: string | null | undefined): string {
        if (!value) {
            return '';
        }

        let formatted = value;


        formatted = formatted.replace(/^# (.*)$/gm, '<strong class="text-heading">$1</strong>');


        formatted = formatted.replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>');


        formatted = formatted.replace(/\*(.+?)\*/g, '<em>$1</em>');


        formatted = formatted.replace(/\n/g, '<br>');

        return formatted;
    }
} 