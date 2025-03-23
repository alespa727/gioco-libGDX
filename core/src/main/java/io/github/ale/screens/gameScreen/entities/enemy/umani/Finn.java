package io.github.ale.screens.gameScreen.entities.enemy.umani;

import io.github.ale.screens.gameScreen.entity.EntityManager;
import io.github.ale.screens.gameScreen.entity.abstractEntity.EntityConfig;
import io.github.ale.screens.gameScreen.entities.enemy.abstractEnemy.Nemico;
import io.github.ale.screens.gameScreen.entity.livingEntity.states.States;
import io.github.ale.screens.gameScreen.entities.player.Player;
import io.github.ale.screens.gameScreen.entities.skill.skillist.Punch;

public final class Finn extends Nemico {

    public Finn(EntityConfig config, EntityManager manager, Float attackcooldown) {
        super(config, manager, attackcooldown);

    }

    @Override
    public void updateEntityType() {
        setIsAttacking(manager.isAnyDifferentEntityInRect(this, range().x, range().y, range().width, range().height));
        statemachine.update();
    }

    @Override
    public void create() {
        System.out.println("Finn n."+id()+" creato");
        statemachine.changeState(States.PURSUE);
        skillset().add(new Punch(this, "pugno", "un pugno molto forte!", 20));
    }

    @Override
    public void attack() {
        skillset().execute(Punch.class);
    }

}
