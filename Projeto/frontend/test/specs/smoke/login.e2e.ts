import { browser, expect } from "@wdio/globals";
import LoginPage from "../../pageobjects/login.page";
import { clearBrowserStorage } from "../../utils/browser";


describe("Login", () => {
  beforeEach(async () => {
    await LoginPage.open();
  });

  afterEach(async () => {
    await clearBrowserStorage();
  });

  it("Deve exibir as mensagens de validação ao tentar entrar com os campos vazios", async () => {
    await LoginPage.btnSubmit.click();

    await expect(LoginPage.emailError).toBeDisplayed();
    await expect(LoginPage.emailError).toHaveText(expect.stringContaining("e-mail válido"));
    await expect(LoginPage.passwordError).toHaveText(expect.stringContaining("Digite sua senha"));
    await expect(browser).toHaveUrl(expect.stringContaining("/account/sign-in"));
  });

  it("Deve abrir a tela de cadastro ao clicar em Criar conta", async () => {
    await LoginPage.linkSignUp.click();

    await expect(browser).toHaveUrl(expect.stringContaining("/account/sign-up"));
  });
});
