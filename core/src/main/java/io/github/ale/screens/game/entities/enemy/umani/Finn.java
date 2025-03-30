package io.github.ale.screens.game.entities.enemy.umani;

import io.github.ale.screens.game.entityType.EntityManager;
import io.github.ale.screens.game.entityType.enemy.enemyStates.EnemyStates;
import io.github.ale.screens.game.entityType.entity.EntityConfig;
import io.github.ale.screens.game.entityType.enemy.Enemy;
import io.github.ale.screens.game.entities.skill.skillist.enemy.Slash;

public final class Finn extends Enemy {

    public Finn(EntityConfig config, EntityManager manager, Float attackcooldown) {
        super(config, manager, attackcooldown);
        viewDistance = 11f;
        pursueMaxDistance = 12f;
    }

    @Override
    public void create() {
        System.out.println("Finn n." +id()+ " creato");
        statemachine.changeState(EnemyStates.PURSUE);
        skillset().add(new Slash(this, "pugno", "un pugno molto forte!", 20));
    }

    @Override
    public void attack(){
        attack.update(delta);
        if (attack.isReady){
            skillset().execute(Slash.class);
            attack.reset();
        }
    }

}
