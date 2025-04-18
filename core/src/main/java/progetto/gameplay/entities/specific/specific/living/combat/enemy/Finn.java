package progetto.gameplay.entities.specific.specific.living.combat.enemy;

import progetto.statemachines.StatesEnemy;
import progetto.gameplay.entities.skills.specific.boss.LichFireball;
import progetto.gameplay.entities.skills.specific.enemy.EnemySwordAttack;
import progetto.gameplay.entities.specific.base.EntityConfig;
import progetto.gameplay.entities.specific.base.EntityInstance;
import progetto.manager.entities.Engine;
import progetto.manager.world.WorldManager;

public final class Finn extends Enemy {

    // === COSTRUTTORI ===

    // Costruttore con EnemyInstance
    public Finn(EnemyInstance instance, Engine manager) {
        super(instance, manager);
    }

    // Costruttore con EntityConfig
    public Finn(EntityConfig config, Engine manager, Float attackcooldown) {
        super(config, manager, attackcooldown);
    }

    // === METODI ===

    @Override
    public void create() {
        super.create();
        // Imposta lo stato iniziale
        statemachine.changeState(StatesEnemy.PATROLLING);

        // Aggiungi le skill
        getSkillset().add(new EnemySwordAttack(this, "pugno", "un pugno molto forte!", 20));
        getSkillset().add(new LichFireball(this, "", "", 10, 6));
    }

    @Override
    public void attack() {
        if (getAttackCooldown().isReady) {
            getSkillset().execute(EnemySwordAttack.class);
            getAttackCooldown().reset();
        }
    }

    @Override
    public EntityInstance despawn() {
        // Rimuove l'entità dal manager
        manager.remove(this);

        // Distrugge il corpo dell'entità e la sua area di range nel mondo
        WorldManager.destroyBody(getPhysics().getBody());
        WorldManager.destroyBody(getDirectionalRange());

        return new EnemyInstance(this);
    }
}
