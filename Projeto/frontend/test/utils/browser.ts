import { browser } from "@wdio/globals";

export async function clearBrowserStorage() {
  await browser.execute(() => {
    window.localStorage.clear();
    window.sessionStorage.clear();
  });
}

export function uniqueEmail(prefix = "e2e"): string {
  const stamp = `${Date.now()}${Math.floor(Math.random() * 1000)}`;
  return `${prefix}.${stamp}@petlink.com`;
}

export async function getStoredEmail(): Promise<string | null> {
  return browser.execute(() => window.localStorage.getItem("email"));
}
