import { $, browser } from "@wdio/globals";


class LoginPage {
  public get inputEmail() {
    return $("[data-testid=\"login-email\"]");
  }
  public get inputPassword() {
    return $("[data-testid=\"login-password\"]");
  }
  public get btnSubmit() {
    return $("[data-testid=\"login-btn\"]");
  }
  public get linkSignUp() {
    return $("[data-testid=\"login-signup-link\"]");
  }

  public get emailError() {
    return $("[data-testid=\"login-email-error\"]");
  }
  public get passwordError() {
    return $("[data-testid=\"login-password-error\"]");
  }
  public get loginError() {
    return $("[data-testid=\"login-error\"]");
  }

  public async open() {
    await browser.url("/account/sign-in");
    await this.inputEmail.waitForDisplayed({ timeout: 5000 });
  }

  public async login(email: string, password: string) {
    await this.inputEmail.setValue(email);
    await this.inputPassword.setValue(password);
    await this.btnSubmit.click();
  }
}

export default new LoginPage();
