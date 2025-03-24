package io.github.ale.screens.gameScreen.entities.enemy.umani;

import io.github.ale.screens.gameScreen.entityType.EntityManager;
import io.github.ale.screens.gameScreen.entityType.abstractEnemy.enemyStates.EnemyStates;
import io.github.ale.screens.gameScreen.entityType.abstractEntity.EntityConfig;
import io.github.ale.screens.gameScreen.entityType.abstractEnemy.Enemy;
import io.github.ale.screens.gameScreen.entities.skill.skillist.Melee;

public final class Finn extends Enemy {

    public Finn(EntityConfig config, EntityManager manager, Float attackcooldown) {
        super(config, manager, attackcooldown);
        range().setSize(1.5f, 1.5f);
    }

    @Override
    public void updateEntityType() {
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
