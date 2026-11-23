import { $, browser } from "@wdio/globals";

export interface SignUpData {
  fullname?: string;
  email?: string;
  phone?: string;
  document?: string;
  password?: string;
}

class SignUpPage {
  public get radioPerson() {
    return $("[data-testid=\"signup-type-person\"]");
  }
  public get radioEnterprise() {
    return $("[data-testid=\"signup-type-enterprise\"]");
  }
  public get inputFullname() {
    return $("[data-testid=\"signup-fullname\"]");
  }
  public get inputEmail() {
    return $("[data-testid=\"signup-email\"]");
  }
  public get inputPhone() {
    return $("[data-testid=\"signup-phone\"]");
  }
  public get inputDocument() {
    return $("[data-testid=\"signup-document\"]");
  }
  public get inputPassword() {
    return $("[data-testid=\"signup-password\"]");
  }
  public get btnSubmit() {
    return $("[data-testid=\"signup-btn\"]");
  }
  public get linkSignIn() {
    return $("[data-testid=\"signup-signin-link\"]");
  }

  public get emailError() {
    return $("[data-testid=\"signup-email-error\"]");
  }
  public get phoneError() {
    return $("[data-testid=\"signup-phone-error\"]");
  }
  public get documentError() {
    return $("[data-testid=\"signup-document-error\"]");
  }
  public get createError() {
    return $("[data-testid=\"signup-error\"]");
  }

  public get lgpdModal() {
    return $("[data-testid=\"lgpd-modal\"]");
  }
  public get btnLgpdAccept() {
    return $("[data-testid=\"lgpd-accept\"]");
  }
  public get btnLgpdDecline() {
    return $("[data-testid=\"lgpd-decline\"]");
  }

  public async open() {
    await browser.url("/account/sign-up");
    await this.inputFullname.waitForDisplayed({ timeout: 5000 });
  }

  public async fill(data: SignUpData) {
    if (data.fullname !== undefined) await this.inputFullname.setValue(data.fullname);
    if (data.email !== undefined) await this.inputEmail.setValue(data.email);
    if (data.phone !== undefined) await this.inputPhone.setValue(data.phone);
    if (data.document !== undefined) await this.inputDocument.setValue(data.document);
    if (data.password !== undefined) await this.inputPassword.setValue(data.password);
  }

  public get labelEnterprise() {
    return $("[data-testid=\"signup-type-enterprise\"] + span");
  }

  public async selectEnterprise() {
    await this.labelEnterprise.click();
  }

  public async submit() {
    await this.btnSubmit.click();
  }

  public async register(data: SignUpData) {
    await this.fill(data);
    await this.submit();
    await this.lgpdModal.waitForDisplayed({ timeout: 5000 });
    await this.btnLgpdAccept.click();
  }
}

export default new SignUpPage();
