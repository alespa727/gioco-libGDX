package progetto.ECS.systems.specific;

import com.badlogic.gdx.utils.Array;
import progetto.ECS.components.base.ComponentFilter;
import progetto.ECS.components.specific.base.StateComponent;
import progetto.ECS.components.specific.general.skills.SkillSet;
import progetto.ECS.components.specific.general.skills.base.Skill;
import progetto.ECS.entities.Entity;
import progetto.ECS.systems.base.IteratingSystem;

public class SkillSystem extends IteratingSystem {

    public SkillSystem() {
        super(ComponentFilter.all(SkillSet.class));
    }

    @Override
    public void processEntity(Entity entity, float delta) {
        if (!entity.get(StateComponent.class).shouldBeUpdated()) return;
        if (!entity.components.contains(SkillSet.class)) return;

        SkillSet s = entity.components.get(SkillSet.class);

        Array<Skill> skills = s.getSkills();

        for (int i = 0; i < skills.size; i++) {
            skills.get(i).update(delta);
        }
    }
}
