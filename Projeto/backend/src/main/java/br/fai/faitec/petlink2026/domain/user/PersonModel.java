package br.fai.faitec.petlink2026.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PersonModel extends UserModel {

    private String cpf;

    @JsonIgnore
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.PERSON;
    }

    @Override
    public String getDocument() {
        return cpf;
    }

    @Override
    public void setDocument(String document) {
        this.cpf = document;
    }
}
