package progetto.skills.specific.enemy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import progetto.skills.specific.CombatSkill;
import progetto.entity.specific.specific.living.combat.Warrior;
import progetto.entity.specific.specific.living.combat.enemy.Enemy;

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
            owner.manager.player().hit((Warrior) owner, damage, 5);
        }
    }

}
