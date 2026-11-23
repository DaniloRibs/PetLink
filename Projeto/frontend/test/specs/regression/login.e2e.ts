import { browser, expect } from "@wdio/globals";
import LoginPage from "../../pageobjects/login.page";
import DashboardPage from "../../pageobjects/dashboard.page";
import { clearBrowserStorage, getStoredEmail } from "../../utils/browser";
import users from "../../data/users";

describe("Login - cenários alternativos", () => {
  beforeEach(async () => {
    await LoginPage.open();
  });

  afterEach(async () => {
    await clearBrowserStorage();
  });

  it("E2E-LOG-04: deve recusar o login com senha incorreta", async () => {
    await LoginPage.login(users.invalidPassword.email, users.invalidPassword.password);

    await expect(LoginPage.loginError).toBeDisplayed();
    await expect(LoginPage.loginError).toHaveText(expect.stringContaining("incorretos"));
    await expect(browser).toHaveUrl(expect.stringContaining("/account/sign-in"));
    expect(await getStoredEmail()).toBeNull();
  });

  it("E2E-LOG-05: deve recusar o login de um e-mail não cadastrado", async () => {
    await LoginPage.login(users.unknown.email, users.unknown.password);

    await expect(LoginPage.loginError).toBeDisplayed();
    await expect(browser).toHaveUrl(expect.stringContaining("/account/sign-in"));
    expect(await getStoredEmail()).toBeNull();
  });

  it("E2E-LOG-06: deve validar o formato do e-mail antes de enviar", async () => {
    await LoginPage.login("email-sem-arroba", users.valid.password);

    await expect(LoginPage.emailError).toBeDisplayed();
    await expect(LoginPage.loginError).not.toBeExisting();
    await expect(browser).toHaveUrl(expect.stringContaining("/account/sign-in"));
  });

  it("E2E-LOG-07: deve exigir a senha quando só o e-mail é informado", async () => {
    await LoginPage.inputEmail.setValue(users.valid.email);
    await LoginPage.btnSubmit.click();

    await expect(LoginPage.passwordError).toBeDisplayed();
    await expect(LoginPage.passwordError).toHaveText(expect.stringContaining("Digite sua senha"));
    await expect(LoginPage.emailError).not.toBeExisting();
    await expect(browser).toHaveUrl(expect.stringContaining("/account/sign-in"));
  });

  it("E2E-LOG-08: deve limpar o erro de credenciais ao entrar com dados corretos em seguida", async () => {
    await LoginPage.login(users.invalidPassword.email, users.invalidPassword.password);
    await expect(LoginPage.loginError).toBeDisplayed();

    await LoginPage.inputPassword.setValue(users.valid.password);
    await LoginPage.btnSubmit.click();

    await expect(browser).toHaveUrl(expect.stringContaining("/painel"));
  });

  it("E2E-LOG-09: deve redirecionar para o painel quem já está autenticado", async () => {
    await LoginPage.login(users.valid.email, users.valid.password);
    await expect(browser).toHaveUrl(expect.stringContaining("/painel"));

    await browser.url("/account/sign-in");

    await expect(browser).toHaveUrl(expect.stringContaining("/painel"));
  });
});

describe("Autenticação - proteção de rota e logout", () => {
  afterEach(async () => {
    await clearBrowserStorage();
  });

  it("E2E-LOG-10: deve redirecionar para o login ao acessar o painel sem estar autenticado", async () => {
    await browser.url("/painel");

    await expect(browser).toHaveUrl(expect.stringContaining("/account/sign-in"));
  });

  it("E2E-LOG-11: deve encerrar a sessão ao clicar em Sair e bloquear o painel depois", async () => {
    await LoginPage.open();
    await LoginPage.login(users.valid.email, users.valid.password);
    await expect(browser).toHaveUrl(expect.stringContaining("/painel"));

    await DashboardPage.logout();

    await expect(browser).toHaveUrl(expect.stringContaining("/account/sign-in"));
    expect(await getStoredEmail()).toBeNull();

    await browser.url("/painel");
    await expect(browser).toHaveUrl(expect.stringContaining("/account/sign-in"));
  });
});
