package io.github.ale.screens.game.entities.skills;

import com.badlogic.gdx.utils.Array;

public class SkillSet {
    private final Array<Skill> skillList;

    private boolean active;

    public SkillSet() {
        skillList = new Array<>();
        active = true;
    }

    public void add(Skill skill) {
        skillList.add(skill);
    }

    public void remove(Skill skill) {
        skillList.removeValue(skill, false);
    }

    public Skill getSkill(Class<? extends Skill> skillClass) {
        if (!active) return null;

        for (Skill skill : skillList) {
            if (skill.getClass().equals(skillClass)) {
                return skill;
            }
        }
        return null;
    }

    private void disable() {
        if (active) active = false;
    }

    private void enable() {
        if (!active) active = true;
    }

    /**
     * Esegue la skill richiesta
     */
    public void execute(Class<? extends Skill> skillClass) {
        getSkill(skillClass).execute();
    }
}
