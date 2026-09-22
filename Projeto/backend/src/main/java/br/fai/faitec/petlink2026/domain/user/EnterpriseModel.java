package br.fai.faitec.petlink2026.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class EnterpriseModel extends UserModel {

    private String cnpj;

    @JsonIgnore
    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.ENTERPRISE;
    }

    @Override
    public String getDocument() {
        return cnpj;
    }

    @Override
    public void setDocument(String document) {
        this.cnpj = document;
    }
}
