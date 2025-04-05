package progetto.gameplay.entity.skills.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import progetto.gameplay.entity.skills.Skill;
import progetto.gameplay.entity.types.humanEntity.HumanEntity;
import progetto.gameplay.entity.types.humanEntity.combatEntity.player.Player;
import progetto.utils.Cooldown;

public class Dash extends Skill {
    private final float dodgeSpeed;

    public Dash(HumanEntity entity, String name, String description, float dodgeSpeed) {
        super(entity, name, description);
        this.dodgeSpeed = dodgeSpeed;
        cooldown = new Cooldown(2f);
    }

    @Override
    public void update() {
        if (isBeingUsed) {
            elapsedTime += entity.delta;
            cooldown.update(entity.delta);
            if (((Player) entity).getInRange().size > 0){
                entity.manager.gameInfo.screen.setTimeScale(0.1f, 1f);
                isBeingUsed = false;
            }
            if (cooldown.isReady){
                isBeingUsed=false;
                elapsedTime = 0;
            }
        }
    }

    @Override
    public void draw(SpriteBatch batch) {

    }


    @Override
    public void execute() {
        entity.body.setLinearVelocity(entity.body.getLinearVelocity().x * dodgeSpeed, entity.body.getLinearVelocity().y * dodgeSpeed);
        cooldown.reset(0.5f);
        if (((Player) entity).getInRange().size == 0) setBeingUsed(true);
    }
}
