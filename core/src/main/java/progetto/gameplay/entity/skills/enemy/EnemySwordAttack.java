package progetto.gameplay.entity.skills.enemy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import progetto.gameplay.entity.types.living.combat.Warrior;
import progetto.gameplay.entity.skills.CombatSkill;
import progetto.gameplay.entity.types.living.combat.enemy.Enemy;

public class EnemySwordAttack extends CombatSkill {

    public EnemySwordAttack(Warrior entity, String name, String description, float damage) {
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
        if (((Enemy) owner).getInRange().size>0){
            owner.manager.player().hit((Warrior) owner, damage, 2);
        }
    }

}
