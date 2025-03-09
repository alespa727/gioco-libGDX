package io.github.ale.entity.abstractEnity.caratteristiche;

import com.badlogic.gdx.utils.Array;

public class EntityInfo {
    private final String nome;
    private final String descrizione;
    private final Array<Skill> skills;

    public EntityInfo(String nome, String descrizione){
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


}
