package progetto.entity.specific.specific.living.combat.enemy;

import progetto.statemachines.entity.StatesEnemy;
import progetto.skills.specific.boss.LichFireball;
import progetto.skills.specific.enemy.EnemySwordAttack;
import progetto.entity.specific.base.EntityConfig;
import progetto.entity.specific.base.EntityInstance;
import progetto.rendering.entity.EntityManager;
import progetto.manager.world.WorldManager;

public final class Finn extends Enemy {

    // === COSTRUTTORI ===

    // Costruttore con EnemyInstance
    public Finn(EnemyInstance instance, EntityManager manager) {
        super(instance, manager);
    }

    // Costruttore con EntityConfig
    public Finn(EntityConfig config, EntityManager manager, Float attackcooldown) {
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
