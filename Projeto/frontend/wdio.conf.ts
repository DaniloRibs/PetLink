/// <reference types="@wdio/globals/types" />

export const config: WebdriverIO.Config = {
  runner: "local",

  specs: ["./test/specs/**/*.ts"],
  suites: {
    smoke: ["./test/specs/smoke/**/*.ts"],
    regression: ["./test/specs/regression/**/*.ts"],
  },
  exclude: [],

  maxInstances: 1,

  capabilities: [
    {
      browserName: "chrome",
      "goog:chromeOptions": {
        args: [
          "--window-size=1366,768",
          ...(process.env.HEADLESS === "true" ? ["--headless=new"] : []),
        ],
      },
    },
  ],

  logLevel: "warn",

  baseUrl: process.env.BASE_URL || "http://localhost:4200",

  waitforTimeout: 10000,
  connectionRetryTimeout: 120000,
  connectionRetryCount: 3,

  framework: "mocha",

  reporters: [
    "spec",
    [
      "allure",
      {
        outputDir: "allure-results",
        disableWebdriverStepsReporting: true,
        disableWebdriverScreenshotsReporting: false,
      },
    ],
  ],

  mochaOpts: {
    ui: "bdd",
    timeout: 60000,
  },

  onPrepare: async function () {
    const backend = process.env.BACKEND_URL || "http://localhost:8080";
    const login = async (email: string, password: string) => {
      const response = await fetch(`${backend}/authenticate`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password }),
        signal: AbortSignal.timeout(8000),
      });
      return response.status;
    };
    try {
      const status = await login("d@d.com", "12345");
      if (status === 200) {
        console.log("[pré-teste] Backend OK: login do usuário de teste d@d.com respondeu 200.");
      } else {
        console.warn(`[pré-teste] ATENÇÃO: login de d@d.com respondeu ${status} (esperado 200). ` +
          "Confira se o banco foi populado com o script insert-data-postgres-basic.sql.");
      }
    } catch (error) {
      console.warn(`[pré-teste] ATENÇÃO: o backend em ${backend} não respondeu em 8s (${String(error)}). ` +
        "Os testes de login e cadastro vão falhar. Verifique se o Spring Boot está rodando e se não há erro no console dele.");
    }
  },

  afterTest: async function (test, context, { passed }) {
    if (!passed) {
      await browser.takeScreenshot();
    }
  },
};
