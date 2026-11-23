import { browser, expect } from "@wdio/globals";
import LoginPage from "../../pageobjects/login.page";
import DashboardPage from "../../pageobjects/dashboard.page";
import { clearBrowserStorage, getStoredEmail } from "../../utils/browser";
import users from "../../data/users";

describe("Login - fluxo crítico", () => {
  beforeEach(async () => {
    await LoginPage.open();
  });

  afterEach(async () => {
    await clearBrowserStorage();
  });

  it("E2E-LOG-01: deve entrar com credenciais válidas e abrir o painel", async () => {
    await LoginPage.login(users.valid.email, users.valid.password);

    await expect(browser).toHaveUrl(expect.stringContaining("/painel"));
    await expect(DashboardPage.hero).toBeDisplayed();
    expect(await getStoredEmail()).toBe(users.valid.email);
  });

  it("E2E-LOG-02: deve exibir as mensagens de validação ao tentar entrar com os campos vazios", async () => {
    await LoginPage.btnSubmit.click();

    await expect(LoginPage.emailError).toBeDisplayed();
    await expect(LoginPage.emailError).toHaveText(expect.stringContaining("e-mail válido"));
    await expect(LoginPage.passwordError).toHaveText(expect.stringContaining("Digite sua senha"));
    await expect(browser).toHaveUrl(expect.stringContaining("/account/sign-in"));
  });

  it("E2E-LOG-03: deve abrir a tela de cadastro ao clicar em Criar conta", async () => {
    await LoginPage.linkSignUp.click();

    await expect(browser).toHaveUrl(expect.stringContaining("/account/sign-up"));
  });
});
