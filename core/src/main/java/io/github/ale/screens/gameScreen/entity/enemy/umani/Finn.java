package io.github.ale.screens.gameScreen.entity.enemy.umani;

import io.github.ale.screens.gameScreen.entity.EntityManager;
import io.github.ale.screens.gameScreen.entity.abstractEntity.EntityConfig;
import io.github.ale.screens.gameScreen.entity.enemy.abstractEnemy.Nemico;
import io.github.ale.screens.gameScreen.entity.player.Player;
import io.github.ale.screens.gameScreen.entity.skill.SkillSet;
import io.github.ale.screens.gameScreen.entity.skill.skillist.Punch;

public final class Finn extends Nemico {

    SkillSet skillset;

    public Finn(EntityConfig config, EntityManager manager, Float attackcooldown, Player p) {
        super(config, manager, attackcooldown, p);
        skillset = new SkillSet(p);
        skillset.add(new Punch(this, "pugno", "un pugno molto forte!"));
    }

    @Override
    public void updateEntityType() {
        setIsAttacking(manager.isAnyDifferentEntityInRect(this, range.x, range.y, range.width, range.height));
        pursue(player().coordinateCentro().x, player().coordinateCentro().y);
        checkIfDead();
    }
    
    @Override
    public void create() {
        System.out.println("Finn n."+id()+" creato");
    }

    @Override
    public void attack() {
        skillset.execute(Punch.class);
    }

}
