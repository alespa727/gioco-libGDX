package progetto.gameplay.entity.skills.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import progetto.gameplay.entity.skills.Skill;
import progetto.gameplay.entity.types.living.Humanoid;
import progetto.gameplay.entity.types.living.combat.player.Player;
import progetto.utils.Cooldown;

public class PlayerDash extends Skill {
    private final float dodgeSpeed;

    public PlayerDash(Humanoid entity, String name, String description, float dodgeSpeed) {
        super(entity, name, description);
        this.dodgeSpeed = dodgeSpeed;
        cooldown = new Cooldown(2f);
    }

    @Override
    public void update() {
        if (isBeingUsed) {
            elapsedTime += owner.delta;
            cooldown.update(owner.delta);
            if (((Player) owner).getInRange().size > 0){
                owner.manager.gameInfo.screen.setTimeScale(0.1f, 1f);
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
        owner.body.setLinearVelocity(owner.body.getLinearVelocity().x * dodgeSpeed, owner.body.getLinearVelocity().y * dodgeSpeed);
        cooldown.reset(0.5f);
        if (((Player) owner).getInRange().size == 0) setBeingUsed(true);
    }
}
