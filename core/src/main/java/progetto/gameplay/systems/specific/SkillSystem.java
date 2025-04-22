package progetto.gameplay.systems.specific;

import com.badlogic.gdx.utils.Array;
import progetto.gameplay.entities.components.specific.general.SkillSet;
import progetto.gameplay.entities.skills.base.Skill;
import progetto.gameplay.entities.specific.base.Entity;
import progetto.gameplay.systems.base.AutomaticSystem;
import progetto.gameplay.systems.base.System;

public class SkillSystem extends AutomaticSystem {

    public SkillSystem() {
        super(Array.with(new SkillSet()));
    }

    @Override
    public void processEntity(Entity entity, float delta) {
        if (!entity.shouldRender()) return;
        if (!entity.components.contains(SkillSet.class)) return;

        SkillSet s = entity.components.get(SkillSet.class);

        Array<Skill> skills = s.getSkills();

        for (int i = 0; i < skills.size; i++) {
            skills.get(i).update(delta);
        }
    }
}
