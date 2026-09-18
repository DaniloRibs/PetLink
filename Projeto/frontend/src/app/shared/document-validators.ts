import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export function isValidCpf(rawCpf: string): boolean {
  const cpf = (rawCpf || '').replace(/\D/g, '');

  if (cpf.length !== 11 || /^(\d)\1{10}$/.test(cpf)) {
    return false;
  }

  let sum = 0;
  for (let i = 0; i < 9; i++) {
    sum += Number(cpf.charAt(i)) * (10 - i);
  }
  let digit = 11 - (sum % 11);
  if (digit >= 10) {
    digit = 0;
  }
  if (digit !== Number(cpf.charAt(9))) {
    return false;
  }

  sum = 0;
  for (let i = 0; i < 10; i++) {
    sum += Number(cpf.charAt(i)) * (11 - i);
  }
  digit = 11 - (sum % 11);
  if (digit >= 10) {
    digit = 0;
  }

  return digit === Number(cpf.charAt(10));
}

export function isValidCnpj(rawCnpj: string): boolean {
  const cnpj = (rawCnpj || '').replace(/\D/g, '');

  if (cnpj.length !== 14 || /^(\d)\1{13}$/.test(cnpj)) {
    return false;
  }

  const weights1 = [5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2];
  const weights2 = [6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2];

  let sum = 0;
  for (let i = 0; i < 12; i++) {
    sum += Number(cnpj.charAt(i)) * weights1[i];
  }
  let digit = 11 - (sum % 11);
  if (digit >= 10) {
    digit = 0;
  }
  if (digit !== Number(cnpj.charAt(12))) {
    return false;
  }

  sum = 0;
  for (let i = 0; i < 13; i++) {
    sum += Number(cnpj.charAt(i)) * weights2[i];
  }
  digit = 11 - (sum % 11);
  if (digit >= 10) {
    digit = 0;
  }

  return digit === Number(cnpj.charAt(13));
}

export function isValidPhone(rawPhone: string): boolean {
  const digits = (rawPhone || '').replace(/\D/g, '');
  return digits.length === 10 || digits.length === 11;
}

/** Telefone é opcional: só valida o formato se algo foi digitado. */
export function optionalPhoneValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = (control.value ?? '').toString().trim();
    if (!value) {
      return null;
    }
    return isValidPhone(value) ? null : { invalidPhone: true };
  };
}

/** CPF é opcional para PERSON: só valida o formato se algo foi digitado. */
export function optionalCpfValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = (control.value ?? '').toString().trim();
    if (!value) {
      return null;
    }
    return isValidCpf(value) ? null : { invalidCpf: true };
  };
}

/** CNPJ é obrigatório e deve ser válido para ENTERPRISE. */
export function requiredCnpjValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = (control.value ?? '').toString().trim();
    if (!value) {
      return { required: true };
    }
    return isValidCnpj(value) ? null : { invalidCnpj: true };
  };
}
