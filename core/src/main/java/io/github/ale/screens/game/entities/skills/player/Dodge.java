package io.github.ale.screens.game.entities.skills.player;

import io.github.ale.screens.game.entities.skills.Skill;
import io.github.ale.screens.game.entities.entityTypes.mobs.LivingEntity;

public class Dodge extends Skill {

    private float dodgeSpeed;

    public Dodge(LivingEntity entity, String name, String description, float dodgeSpeed) {
        super(entity, name, description);
    }

    @Override
    public void execute() {
        entity.body.setLinearVelocity(entity.body.getLinearVelocity().x*25f, entity.body.getLinearVelocity().y*25f);
        setBeingUsed(true);
    }
}
