package io.github.ale.screens.gameplay.entities.skills.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.ale.screens.gameplay.entities.skills.Skill;
import io.github.ale.screens.gameplay.entities.types.mobs.LivingEntity;
import io.github.ale.screens.gameplay.entities.types.player.Player;
import io.github.ale.utils.Cooldown;

public class Dash extends Skill {
    private final float dodgeSpeed;

    public Dash(LivingEntity entity, String name, String description, float dodgeSpeed) {
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
                entity.manager.gameInfo.screen.setTimeScale(0.01f, 1f);
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
