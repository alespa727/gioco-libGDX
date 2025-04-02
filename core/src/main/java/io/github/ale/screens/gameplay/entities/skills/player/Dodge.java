package io.github.ale.screens.gameplay.entities.skills.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.ale.screens.gameplay.entities.types.mobs.LivingEntity;
import io.github.ale.screens.gameplay.entities.skills.Skill;

public class Dodge extends Skill {

    private float dodgeSpeed;

    public Dodge(LivingEntity entity, String name, String description, float dodgeSpeed) {
        super(entity, name, description);
        this.dodgeSpeed = dodgeSpeed;
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(SpriteBatch batch) {

    }


    @Override
    public void execute() {
        entity.body.setLinearVelocity(entity.body.getLinearVelocity().x * dodgeSpeed, entity.body.getLinearVelocity().y * dodgeSpeed);
        setBeingUsed(true);
    }
}
