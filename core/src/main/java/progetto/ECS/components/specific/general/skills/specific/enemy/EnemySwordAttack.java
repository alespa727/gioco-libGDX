package progetto.ECS.components.specific.general.skills.specific.enemy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import progetto.ECS.components.specific.general.skills.specific.CombatSkill;
import progetto.ECS.components.specific.sensors.InRangeListComponent;
import progetto.ECS.entities.specific.living.combat.Warrior;
import progetto.ECS.entities.specific.living.combat.enemy.BaseEnemy;

public class EnemySwordAttack extends CombatSkill {

    public EnemySwordAttack(Warrior entity, String name, String description, float damage) {
        super(entity, name, description, damage);
    }


    @Override
    public void update(float delta) {

    }

    @Override
    public void draw(SpriteBatch batch) {

    }


    @Override
    public void execute() {
        if (owner.get(InRangeListComponent.class).inRange.size > 0) {
            owner.entityEngine.player().hit((Warrior) owner, damage, 5);
        }
    }

}
