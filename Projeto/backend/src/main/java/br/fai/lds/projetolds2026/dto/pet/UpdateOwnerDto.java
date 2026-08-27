package br.fai.lds.projetolds2026.dto.pet;

import br.fai.lds.projetolds2026.domain.user.UserModel;

public class UpdateOwnerDto {
    private int id;
    private UserModel oldOwner;
    private UserModel newOwner;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserModel getOldOwner() {
        return oldOwner;
    }

    public void setOldOwner(UserModel oldOwner) {
        this.oldOwner = oldOwner;
    }

    public UserModel getNewOwner() {
        return newOwner;
    }

    public void setNewOwner(UserModel newOwner) {
        this.newOwner = newOwner;
    }
}
