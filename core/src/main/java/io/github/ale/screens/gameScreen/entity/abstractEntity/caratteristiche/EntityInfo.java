package io.github.ale.screens.gameScreen.entity.abstractEntity.caratteristiche;

import com.badlogic.gdx.utils.Array;

public class EntityInfo {
    private final int id;
    private final String nome;
    private final String descrizione;
    private final Array<Skill> skills;

    public EntityInfo(String nome, String descrizione, int id){
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
        skills = new Array<>();
    }

    public void addSkill(Skill skill){
        skills.add(skill);
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
