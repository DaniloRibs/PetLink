import { browser } from "@wdio/globals";

export async function clearBrowserStorage() {
  await browser.execute(() => {
    window.localStorage.clear();
    window.sessionStorage.clear();
  });
}
