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


# Testes 

# Como executar
1. Rode o projeto normalmente.
3. Rode os testes:

- npm run test:e2e      (Ambos)    
- npm run test:smoke        
- npm run test:regression   


# Relatório
```
npm run allure:report
```

## Estrutura
Os elementos são buscados por `data-testid` (ex.: `login-email`, `login-btn`).
