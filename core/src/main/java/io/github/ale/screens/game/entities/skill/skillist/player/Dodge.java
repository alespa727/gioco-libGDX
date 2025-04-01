package io.github.ale.screens.game.entities.skill.skillist.player;

import io.github.ale.screens.game.entities.player.Player;
import io.github.ale.screens.game.entities.skill.skillist.Skill;
import io.github.ale.screens.game.entityType.mobs.LivingEntity;

public class Dodge extends Skill {

    public Dodge(LivingEntity entity, String name, String description) {
        super(entity, name, description);
    }

    @Override
    public void execute() {
        System.out.println("Dodge");
        entity.body.setLinearVelocity(entity.body.getLinearVelocity().x*25f, entity.body.getLinearVelocity().y*25f);
    }
}
