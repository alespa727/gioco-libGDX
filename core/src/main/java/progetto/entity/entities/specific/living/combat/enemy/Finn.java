package progetto.entity.entities.specific.living.combat.enemy;

import progetto.entity.components.specific.base.PhysicsComponent;
import progetto.entity.components.specific.general.skills.specific.boss.LichFireball;
import progetto.entity.components.specific.general.skills.specific.enemy.EnemySwordAttack;
import progetto.entity.entities.base.EntityConfig;
import progetto.entity.entities.base.EntityInstance;
import progetto.entity.Engine;
import progetto.world.WorldManager;

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
    public EntityInstance unregister() {
        // Rimuove l'entità dal manager
        engine.remove(this);

        // Distrugge il corpo dell'entità e la sua area di range nel mondo
        WorldManager.destroyBody(components.get(PhysicsComponent.class).getBody());

        return new EnemyInstance(this);
    }
}
