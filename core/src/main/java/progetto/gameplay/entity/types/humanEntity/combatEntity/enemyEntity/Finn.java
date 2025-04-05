package progetto.gameplay.entity.types.humanEntity.combatEntity.enemyEntity;

import com.badlogic.gdx.Gdx;
import progetto.gameplay.entity.types.abstractEntity.EntityInstance;
import progetto.gameplay.manager.entity.behaviours.EnemyStates;
import progetto.gameplay.entity.types.abstractEntity.EntityConfig;
import progetto.gameplay.entity.skills.enemy.EnemySwordAttack;
import progetto.gameplay.manager.entity.EntityManager;
import progetto.gameplay.manager.map.WorldManager;

public final class Finn extends Enemy {

    // === COSTRUTTORI ===

    // Costruttore con EnemyInstance
    public Finn(EnemyInstance instance, EntityManager manager) {
        super(instance, manager);
        createRange(1.5f);
    }

    // Costruttore con EntityConfig
    public Finn(EntityConfig config, EntityManager manager, Float attackcooldown) {
        super(config, manager, attackcooldown);
        createRange(1.5f);
    }

    // === METODI ===

    @Override
    public void create() {
        // Log di creazione
        System.out.println("Finn n." + id() + " creato");

        // Imposta lo stato iniziale
        statemachine.changeState(EnemyStates.PATROLLING);

        // Aggiungi le skill
        getSkillset().add(new EnemySwordAttack(this, "pugno", "un pugno molto forte!", 20));
    }

    @Override
    public void attack() {
        // Gestione dell'attacco con cooldown
        attack.update(delta);
        if (attack.isReady) {
            getSkillset().execute(EnemySwordAttack.class);
            attack.reset();
        }
    }

    @Override
    public EntityInstance despawn() {
        // Log per il despawn
        System.out.println("Entità id " + id() + " despawnata");

        // Rimuove l'entità dal manager
        manager.removeEntity(this);

        // Distrugge il corpo dell'entità e la sua area di range nel mondo
        Gdx.app.postRunnable(() -> WorldManager.getInstance().destroyBody(body));
        Gdx.app.postRunnable(() -> WorldManager.getInstance().destroyBody(range));

        return new EnemyInstance(this);
    }
}
