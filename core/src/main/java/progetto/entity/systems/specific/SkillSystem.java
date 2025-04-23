package progetto.entity.systems.specific;

import com.badlogic.gdx.utils.Array;
import progetto.entity.components.specific.general.skills.SkillSet;
import progetto.entity.components.specific.general.skills.base.Skill;
import progetto.entity.entities.base.Entity;
import progetto.entity.systems.base.AutomaticSystem;
import progetto.entity.components.base.ComponentFilter;

public class SkillSystem extends AutomaticSystem {

    public SkillSystem() {
        super(ComponentFilter.all(SkillSet.class));
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
