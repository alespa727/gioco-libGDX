package progetto.gameplay.entities.skills.specific.enemy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import progetto.gameplay.entities.skills.specific.CombatSkill;
import progetto.gameplay.entities.specific.specific.living.combat.Warrior;
import progetto.gameplay.entities.specific.specific.living.combat.enemy.Enemy;

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
        if (((Enemy) owner).getInRange().size > 0) {
            owner.engine.player().hit((Warrior) owner, damage, 5);
        }
    }

}
