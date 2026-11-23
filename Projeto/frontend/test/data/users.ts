// Dados de teste (evita hardcode nos specs).
const users = {
  "valid": {
    "email": "d@d.com",
    "password": "12345"
  },
  "invalidPassword": {
    "email": "d@d.com",
    "password": "senha-errada"
  },
  "unknown": {
    "email": "nao.existe@petlink.com",
    "password": "12345"
  },
  "person": {
    "fullname": "Maria E2E Silva",
    "phone": "31999990000",
    "cpf": "52998224725",
    "password": "1234"
  },
  "enterprise": {
    "fullname": "Agropecuária E2E Ltda",
    "phone": "31988887777",
    "cnpj": "11222333000181",
    "password": "1234"
  },
  "invalid": {
    "cpf": "11111111111",
    "cnpj": "11111111111111",
    "emailNoDot": "maria@email",
    "shortPassword": "123"
  }
};

export default users;
