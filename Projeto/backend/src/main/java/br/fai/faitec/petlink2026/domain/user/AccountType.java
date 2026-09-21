package br.fai.faitec.petlink2026.domain.user;

public enum AccountType {

    PERSON {
        @Override
        public UserModel createUser() {
            return new PersonModel();
        }
    },

    ENTERPRISE {
        @Override
        public UserModel createUser() {
            return new EnterpriseModel();
        }
    };

    public abstract UserModel createUser();
}
