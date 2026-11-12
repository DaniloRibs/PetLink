/// <reference types="@wdio/globals/types" />

// Configuração do WebdriverIO (WDIO) para os testes E2E do frontend do PetLink.
export const config: WebdriverIO.Config = {
  runner: "local",

  // Onde ficam os testes. As suítes permitem rodar só uma parte: npm run test:smoke
  specs: ["./test/specs/**/*.ts"],
  suites: {
    smoke: ["./test/specs/smoke/**/*.ts"],
    regression: ["./test/specs/regression/**/*.ts"],
  },
  exclude: [],

  // Testes em paralelo (um navegador por arquivo de teste)
  maxInstances: 5,

  capabilities: [
    {
      browserName: "chrome",
      "goog:chromeOptions": {
        args: [
          "--window-size=1366,768",
          // Para rodar sem abrir a janela do Chrome: defina a variável HEADLESS=true
          ...(process.env.HEADLESS === "true" ? ["--headless=new"] : []),
        ],
      },
    },
  ],

  logLevel: "warn",

  // O frontend precisa estar rodando (ng serve) neste endereço
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

  // Em caso de falha, o screenshot é anexado ao relatório do Allure
  afterTest: async function (test, context, { passed }) {
    if (!passed) {
      await browser.takeScreenshot();
    }
  },
};
