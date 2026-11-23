import { $, browser } from "@wdio/globals";

class DashboardPage {
  public get hero() {
    return $("[data-testid=\"dashboard-hero\"]");
  }
  public get userMenuTrigger() {
    return $("[data-testid=\"user-menu-trigger\"]");
  }
  public get btnLogout() {
    return $("[data-testid=\"logout-btn\"]");
  }

  public async open() {
    await browser.url("/painel");
  }

  public async logout() {
    await this.userMenuTrigger.waitForClickable({ timeout: 5000 });
    await this.userMenuTrigger.click();
    await this.btnLogout.waitForClickable({ timeout: 5000 });
    await this.btnLogout.click();
  }
}

export default new DashboardPage();
