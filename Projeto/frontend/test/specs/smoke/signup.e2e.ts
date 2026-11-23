import { browser, expect } from "@wdio/globals";
import SignUpPage from "../../pageobjects/signup.page";
import { clearBrowserStorage, uniqueEmail } from "../../utils/browser";
import { deleteUserByEmail } from "../../utils/api";
import users from "../../data/users";

describe("Cadastro - fluxo crítico", () => {
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

  it("E2E-CAD-01: deve cadastrar uma conta de pessoa apenas com os campos obrigatórios", async () => {
    const email = uniqueEmail("pessoa");
    createdEmails.push(email);

    await SignUpPage.register({
      fullname: users.person.fullname,
      email,
      password: users.person.password,
    });

    await expect(browser).toHaveUrl(expect.stringContaining("/account/sign-in"));
  });

  it("E2E-CAD-02: não deve abrir a confirmação de privacidade com o formulário vazio", async () => {
    await SignUpPage.submit();

    await expect(SignUpPage.emailError).toBeDisplayed();
    await expect(SignUpPage.lgpdModal).not.toBeExisting();
    await expect(browser).toHaveUrl(expect.stringContaining("/account/sign-up"));
  });
});
