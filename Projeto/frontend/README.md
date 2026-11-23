# Requisitos Obrigatorios
1. instalar o node.js
2. npm install
3. npm install -g @angular/cli


# Para se conectar 
-db.json
ng serve --configuration=development

-postgres db
ng serve --configuration=production

npx json-server db.json

# Bom fazer
baixe algumas extensoes para auxiliar no code do angular e no js

# CPF E CNPJ FAKE para teste

12.665.787/0001-90

342.858.710-33


# Testes E2E (WebdriverIO + Allure)

## Pré-requisitos

## Como executar
- `npm run test:e2e`         (todos)
- `npm run test:smoke`       (fluxos críticos: login e cadastro)
- `npm run test:regression`  (cenários alternativos e validações)
- `HEADLESS=true npm run test:e2e` roda sem abrir o Chrome.
- Variáveis opcionais: `BASE_URL` (padrão `http://localhost:4200`) e `API_URL` (padrão `http://localhost:8080/api`).

## Relatório
```
npm run allure:report
```
