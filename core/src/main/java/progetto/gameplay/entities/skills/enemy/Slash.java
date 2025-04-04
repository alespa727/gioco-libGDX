package progetto.gameplay.entities.skills.enemy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import progetto.gameplay.entities.types.CombatEntity;
import progetto.gameplay.entities.skills.skillType.CombatSkill;
import progetto.gameplay.entities.types.Enemy;

public class Slash extends CombatSkill {

    public Slash(CombatEntity entity, String name, String description, float damage) {
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
            entity.manager.player().hit((CombatEntity) entity, damage);
        }
    }

}
