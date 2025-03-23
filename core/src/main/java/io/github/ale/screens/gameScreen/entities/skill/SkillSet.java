package io.github.ale.screens.gameScreen.entities.skill;

import com.badlogic.gdx.utils.Array;

import io.github.ale.screens.gameScreen.entity.abstractEntity.caratteristiche.Skill;

public class SkillSet {
    private final Array<Skill> lista;

    public SkillSet(){
        lista = new Array<>();
    }

    public void add(Skill skill){
        lista.add(skill);
    }

    public void remove(Skill skill){
        lista.removeValue(skill, false);
    }

    public Skill getSkill(Class<? extends Skill> skillClass) {
        for (Skill skill : lista) {
            if (skill.getClass().equals(skillClass)) {
                return skill;
            }
        }
        return null;
    }

    public void execute(Class<? extends Skill> skillClass){
        getSkill(skillClass).execute();
    }
}
