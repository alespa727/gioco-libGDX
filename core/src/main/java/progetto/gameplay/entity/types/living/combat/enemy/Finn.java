package progetto.gameplay.entity.types.living.combat.enemy;

import com.badlogic.gdx.Gdx;
import progetto.gameplay.entity.behaviors.states.StatesEnemy;
import progetto.gameplay.entity.types.EntityInstance;
import progetto.gameplay.entity.types.EntityConfig;
import progetto.gameplay.entity.skills.enemy.EnemySwordAttack;
import progetto.gameplay.manager.ManagerEntity;
import progetto.gameplay.manager.ManagerWorld;

public final class Finn extends Enemy {

    // === COSTRUTTORI ===

    // Costruttore con EnemyInstance
    public Finn(EnemyInstance instance, ManagerEntity manager) {
        super(instance, manager);
        createRange(1.5f);
    }

    // Costruttore con EntityConfig
    public Finn(EntityConfig config, ManagerEntity manager, Float attackcooldown) {
        super(config, manager, attackcooldown);
        createRange(1.5f);
    }

    // === METODI ===

    @Override
    public void create() {
        // Log di creazione
        System.out.println("Finn n." + id() + " creato");

        // Imposta lo stato iniziale
        statemachine.changeState(StatesEnemy.PATROLLING);

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
        Gdx.app.postRunnable(() -> ManagerWorld.getInstance().destroyBody(body));
        Gdx.app.postRunnable(() -> ManagerWorld.getInstance().destroyBody(directionalRange));

        return new EnemyInstance(this);
    }
}
