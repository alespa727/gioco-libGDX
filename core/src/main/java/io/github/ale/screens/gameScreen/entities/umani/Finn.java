package io.github.ale.screens.gameScreen.entities.umani;

import io.github.ale.screens.gameScreen.entitytypes.EntityManager;
import io.github.ale.screens.gameScreen.entitytypes.abstractEntity.EntityConfig;
import io.github.ale.screens.gameScreen.entitytypes.abstractEnemy.Nemico;
import io.github.ale.screens.gameScreen.entitytypes.livingEntity.states.States;
import io.github.ale.screens.gameScreen.entities.player.Player;
import io.github.ale.screens.gameScreen.entities.skills.skillist.Punch;

public final class Finn extends Nemico {

    public Finn(EntityConfig config, EntityManager manager, Float attackcooldown, Player p) {
        super(config, manager, attackcooldown, p);

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
        skillset().add(new Punch(this, "pugno", "un pugno molto forte!"));
    }

    @Override
    public void attack() {
        skillset().execute(Punch.class);
    }

}
