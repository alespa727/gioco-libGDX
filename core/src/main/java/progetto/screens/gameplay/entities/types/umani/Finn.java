package progetto.screens.gameplay.entities.types.umani;

import progetto.screens.gameplay.entities.types.enemy.Enemy;
import progetto.screens.gameplay.entities.types.enemy.enemyStates.EnemyStates;
import progetto.screens.gameplay.entities.types.entity.EntityConfig;
import progetto.screens.gameplay.entities.skills.enemy.Slash;
import progetto.screens.gameplay.manager.entity.EntityManager;

public final class Finn extends Enemy {

    public Finn(EntityConfig config, EntityManager manager, Float attackcooldown) {
        super(config, manager, attackcooldown);
        createRange(1.5f);
    }

    @Override
    public void create() {
        System.out.println("Finn n." + id() + " creato");
        statemachine.changeState(EnemyStates.PURSUE);
        getSkillset().add(new Slash(this, "pugno", "un pugno molto forte!", 20));
    }

    @Override
    public void attack() {
        attack.update(delta);
        if (attack.isReady) {
            getSkillset().execute(Slash.class);
            attack.reset();
        }
    }

}
