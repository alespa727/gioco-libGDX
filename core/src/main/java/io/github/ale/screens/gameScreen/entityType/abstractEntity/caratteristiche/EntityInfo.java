package io.github.ale.screens.gameScreen.entityType.abstractEntity.caratteristiche;

public class EntityInfo {
    private final int id;
    private final String nome;
    private final String descrizione;

    public EntityInfo(String nome, String descrizione, int id){
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public int id() {
        return id;
    }

}
