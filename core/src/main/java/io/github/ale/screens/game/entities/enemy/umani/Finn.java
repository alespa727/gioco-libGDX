package io.github.ale.screens.game.entities.enemy.umani;

import io.github.ale.screens.game.entityType.EntityManager;
import io.github.ale.screens.game.entityType.enemy.enemyStates.EnemyStates;
import io.github.ale.screens.game.entityType.entity.EntityConfig;
import io.github.ale.screens.game.entityType.enemy.Enemy;
import io.github.ale.screens.game.entities.skill.skillist.Melee;

public final class Finn extends Enemy {

    public Finn(EntityConfig config, EntityManager manager, Float attackcooldown) {
        super(config, manager, attackcooldown);
        viewDistance = 11f;
        pursueMaxDistance = 12f;
    }

    @Override
    public void updateEntityType(float delta) {
        statemachine.update();
    }

    @Override
    public void create() {
        System.out.println("Finn n."+id()+" creato");
        statemachine.changeState(EnemyStates.PURSUE);
        skillset().add(new Melee(this, "pugno", "un pugno molto forte!", 20));
    }

    @Override
    public void attack() {
        skillset().execute(Melee.class);
    }

}
