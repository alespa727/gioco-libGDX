package progetto.gameplay.systems.specific;

import com.badlogic.gdx.utils.Array;
import progetto.gameplay.entities.skills.base.Skill;
import progetto.gameplay.entities.components.specific.SkillSet;
import progetto.gameplay.entities.specific.base.Entity;
import progetto.gameplay.systems.base.System;

public class SkillSystem extends System {
    @Override
    public void update(float delta, Array<Entity> list) {
        for (Entity e : list) {
            if (!e.shouldRender()) continue;
            if (!e.componentManager.contains(SkillSet.class)) return;

            SkillSet s = e.componentManager.get(SkillSet.class);

            Array<Skill> skills = s.getSkills();

            for (int i = 0; i < skills.size; i++) {
                skills.get(i).update();
            }
        }
    }
}
