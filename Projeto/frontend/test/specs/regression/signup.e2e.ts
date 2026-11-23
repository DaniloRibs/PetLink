import { browser, expect } from "@wdio/globals";
import SignUpPage from "../../pageobjects/signup.page";
import LoginPage from "../../pageobjects/login.page";
import DashboardPage from "../../pageobjects/dashboard.page";
import { clearBrowserStorage, uniqueEmail } from "../../utils/browser";
import { deleteUserByEmail } from "../../utils/api";
import users from "../../data/users";

describe("Cadastro - cenários principais", () => {
  const createdEmails: string[] = [];

  beforeEach(async () => {
    await SignUpPage.open();
  });

  afterEach(async () => {
    await clearBrowserStorage();
  });

  after(async () => {
    for (const email of createdEmails) {
      await deleteUserByEmail(email);
    }
  });

  it("E2E-CAD-03: deve cadastrar uma pessoa com telefone e CPF válidos", async () => {
    const email = uniqueEmail("pessoa.completa");
    createdEmails.push(email);

    await SignUpPage.register({
      fullname: users.person.fullname,
      email,
      phone: users.person.phone,
      document: users.person.cpf,
      password: users.person.password,
    });

    await expect(browser).toHaveUrl(expect.stringContaining("/account/sign-in"));
  });

  it("E2E-CAD-04: deve cadastrar uma empresa com CNPJ válido", async () => {
    const email = uniqueEmail("empresa");
    createdEmails.push(email);

    await SignUpPage.selectEnterprise();
    await SignUpPage.register({
      fullname: users.enterprise.fullname,
      email,
      phone: users.enterprise.phone,
      document: users.enterprise.cnpj,
      password: users.enterprise.password,
    });

    await expect(browser).toHaveUrl(expect.stringContaining("/account/sign-in"));
  });

  it("E2E-CAD-05: deve permitir entrar no sistema com a conta recém-criada", async () => {
    const email = uniqueEmail("fluxo");
    createdEmails.push(email);

    await SignUpPage.register({
      fullname: users.person.fullname,
      email,
      password: users.person.password,
    });
    await expect(browser).toHaveUrl(expect.stringContaining("/account/sign-in"));

    await LoginPage.login(email, users.person.password);

    await expect(browser).toHaveUrl(expect.stringContaining("/painel"));
    await expect(DashboardPage.hero).toBeDisplayed();
  });

  it("E2E-CAD-06: não deve criar a conta quando o usuário discorda dos termos da LGPD", async () => {
    const email = uniqueEmail("lgpd");
    createdEmails.push(email);

    await SignUpPage.fill({
      fullname: users.person.fullname,
      email,
      password: users.person.password,
    });
    await SignUpPage.submit();
    await SignUpPage.lgpdModal.waitForDisplayed({ timeout: 5000 });
    await SignUpPage.btnLgpdDecline.click();

    await expect(SignUpPage.lgpdModal).not.toBeExisting();
    await expect(browser).toHaveUrl(expect.stringContaining("/account/sign-up"));

    await LoginPage.open();
    await LoginPage.login(email, users.person.password);
    await expect(LoginPage.loginError).toBeDisplayed();
  });

  it("E2E-CAD-07: deve trocar os rótulos do formulário ao escolher conta de empresa", async () => {
    await expect(SignUpPage.inputFullname).toHaveAttribute("placeholder", "Nome Completo");
    await expect(SignUpPage.inputDocument).toHaveAttribute("placeholder", "CPF (opcional)");

    await SignUpPage.selectEnterprise();

    await expect(SignUpPage.inputFullname).toHaveAttribute("placeholder", "Razão Social");
    await expect(SignUpPage.inputDocument).toHaveAttribute("placeholder", "CNPJ");
  });

  it("E2E-CAD-08: deve abrir a tela de login ao clicar em Já tenho uma conta", async () => {
    await SignUpPage.linkSignIn.click();

    await expect(browser).toHaveUrl(expect.stringContaining("/account/sign-in"));
  });
});

describe("Cadastro - validações e dados inválidos", () => {
  const createdEmails: string[] = [];

  beforeEach(async () => {
    await SignUpPage.open();
  });

  afterEach(async () => {
    await clearBrowserStorage();
  });

  after(async () => {
    for (const email of createdEmails) {
      await deleteUserByEmail(email);
    }
  });

  it("E2E-CAD-09: deve recusar e-mail sem ponto no domínio", async () => {
    await SignUpPage.fill({
      fullname: users.person.fullname,
      email: users.invalid.emailNoDot,
      password: users.person.password,
    });
    await SignUpPage.submit();

    await expect(SignUpPage.emailError).toBeDisplayed();
    await expect(SignUpPage.lgpdModal).not.toBeExisting();
  });

  it("E2E-CAD-10: deve recusar senha com menos de 4 caracteres", async () => {
    const email = uniqueEmail("senha.curta");
    createdEmails.push(email);

    await SignUpPage.fill({
      fullname: users.person.fullname,
      email,
      password: users.invalid.shortPassword,
    });
    await SignUpPage.submit();

    await expect(SignUpPage.lgpdModal).not.toBeExisting();
    await expect(browser).toHaveUrl(expect.stringContaining("/account/sign-up"));
  });

  it("E2E-CAD-11: deve recusar CPF inválido (todos os dígitos iguais)", async () => {
    const email = uniqueEmail("cpf.invalido");
    createdEmails.push(email);

    await SignUpPage.fill({
      fullname: users.person.fullname,
      email,
      document: users.invalid.cpf,
      password: users.person.password,
    });
    await SignUpPage.submit();

    await expect(SignUpPage.documentError).toBeDisplayed();
    await expect(SignUpPage.documentError).toHaveText(expect.stringContaining("CPF inválido"));
    await expect(SignUpPage.lgpdModal).not.toBeExisting();
  });

  it("E2E-CAD-12: deve recusar telefone com quantidade de dígitos inválida", async () => {
    const email = uniqueEmail("telefone.invalido");
    createdEmails.push(email);

    await SignUpPage.fill({
      fullname: users.person.fullname,
      email,
      phone: "3199",
      password: users.person.password,
    });
    await SignUpPage.submit();

    await expect(SignUpPage.phoneError).toBeDisplayed();
    await expect(SignUpPage.lgpdModal).not.toBeExisting();
  });

  it("E2E-CAD-13: deve exigir o CNPJ para conta de empresa", async () => {
    const email = uniqueEmail("sem.cnpj");
    createdEmails.push(email);

    await SignUpPage.selectEnterprise();
    await SignUpPage.fill({
      fullname: users.enterprise.fullname,
      email,
      password: users.enterprise.password,
    });
    await SignUpPage.submit();

    await expect(SignUpPage.documentError).toBeDisplayed();
    await expect(SignUpPage.documentError).toHaveText(expect.stringContaining("CNPJ obrigatório"));
    await expect(SignUpPage.lgpdModal).not.toBeExisting();
  });

  it("E2E-CAD-14: deve recusar CNPJ inválido em conta de empresa", async () => {
    const email = uniqueEmail("cnpj.invalido");
    createdEmails.push(email);

    await SignUpPage.selectEnterprise();
    await SignUpPage.fill({
      fullname: users.enterprise.fullname,
      email,
      document: users.invalid.cnpj,
      password: users.enterprise.password,
    });
    await SignUpPage.submit();

    await expect(SignUpPage.documentError).toBeDisplayed();
    await expect(SignUpPage.lgpdModal).not.toBeExisting();
  });

  it("E2E-CAD-15: deve informar erro ao cadastrar um e-mail que já existe", async () => {
    const email = uniqueEmail("duplicado");
    createdEmails.push(email);
    const data = {
      fullname: users.person.fullname,
      email,
      password: users.person.password,
    };

    await SignUpPage.register(data);
    await expect(browser).toHaveUrl(expect.stringContaining("/account/sign-in"));

    await SignUpPage.open();
    await SignUpPage.register(data);

    await SignUpPage.createError.waitForDisplayed({ timeout: 10000 });
    await expect(SignUpPage.createError).toHaveText(expect.stringContaining("Confira os dados"));
    await expect(browser).toHaveUrl(expect.stringContaining("/account/sign-up"));
  });
});
