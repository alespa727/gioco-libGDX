package io.github.ale.screens.game.entities.skill;

import com.badlogic.gdx.utils.Array;
import io.github.ale.screens.game.entities.skill.skillist.Skill;

public class SkillSet {
    private final Array<Skill> skillList;

    public SkillSet(){
        skillList = new Array<>();
    }

    public void add(Skill skill){
        skillList.add(skill);
    }

    public void remove(Skill skill){
        skillList.removeValue(skill, false);
    }

    public Skill getSkill(Class<? extends Skill> skillClass) {
        for (Skill skill : skillList) {
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
