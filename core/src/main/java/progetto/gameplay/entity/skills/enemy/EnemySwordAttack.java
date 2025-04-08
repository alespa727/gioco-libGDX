package progetto.gameplay.entity.skills.enemy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import progetto.gameplay.entity.types.living.combat.Warriors;
import progetto.gameplay.entity.skills.CombatSkill;
import progetto.gameplay.entity.types.living.combat.enemy.Enemy;

public class EnemySwordAttack extends CombatSkill {

    public EnemySwordAttack(Warriors entity, String name, String description, float damage) {
        super(entity, name, description,  damage);
    }


    @Override
    public void update() {

    }

    @Override
    public void draw(SpriteBatch batch) {

    }


    @Override
    public void execute() {
        if (((Enemy) entity).getInRange().size>0){
            entity.manager.player().hit((Warriors) entity, damage, 2);
        }
    }

}
