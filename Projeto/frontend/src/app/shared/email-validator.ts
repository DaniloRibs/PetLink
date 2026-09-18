import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export function isValidEmailFormat(rawEmail: string): boolean {
  const email = (rawEmail || '').trim();
  if (!email.includes('@') || !email.includes('.')) {
    return false;
  }

  const [local, domain] = email.split('@');
  if (!local || !domain || !domain.includes('.')) {
    return false;
  }

  return !domain.startsWith('.') && !domain.endsWith('.');
}

/** Backend só aceita e-mails com "@"; aqui também exigimos um "." no domínio. */
export function strictEmailValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = (control.value ?? '').toString().trim();
    if (!value) {
      return null;
    }
    return isValidEmailFormat(value) ? null : { invalidEmail: true };
  };
}
