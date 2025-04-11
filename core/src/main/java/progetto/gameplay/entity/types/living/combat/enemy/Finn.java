package progetto.gameplay.entity.types.living.combat.enemy;

import com.badlogic.gdx.Gdx;

import progetto.gameplay.entity.behaviors.states.StatesEnemy;
import progetto.gameplay.entity.skills.boss.LichFireball;
import progetto.gameplay.entity.skills.enemy.EnemySwordAttack;
import progetto.gameplay.entity.types.EntityConfig;
import progetto.gameplay.entity.types.EntityInstance;
import progetto.gameplay.manager.ManagerWorld;
import progetto.gameplay.manager.entity.ManagerEntity;

public final class Finn extends Enemy {

    // === COSTRUTTORI ===

    // Costruttore con EnemyInstance
    public Finn(EnemyInstance instance, ManagerEntity manager) {
        super(instance, manager);
    }

    // Costruttore con EntityConfig
    public Finn(EntityConfig config, ManagerEntity manager, Float attackcooldown) {
        super(config, manager, attackcooldown);
    }

    // === METODI ===

    @Override
    public void create() {
        // Log di creazione
        System.out.println("Finn n." + getConfig().id + " creato");

        // Imposta lo stato iniziale
        statemachine.changeState(StatesEnemy.PATROLLING);

        // Aggiungi le skill
        getSkillset().add(new EnemySwordAttack(this, "pugno", "un pugno molto forte!", 20));
        getSkillset().add(new LichFireball(this, "", "", 10, 6));
    }

    @Override
    public void attack() {
        // Gestione dell'attacco con cooldown
        attack.update(manager.delta);
        if (attack.isReady) {
            getSkillset().execute(LichFireball.class    );
            attack.reset();
        }
    }

    @Override
    public EntityInstance despawn() {
        // Log per il despawn
        System.out.println("Entità id " + getConfig().id + " despawnata");

        // Rimuove l'entità dal manager
        manager.remove(this);

        // Distrugge il corpo dell'entità e la sua area di range nel mondo
        ManagerWorld.destroyBody(getPhysics().getBody());
        ManagerWorld.destroyBody(getDirectionalRange());

        return new EnemyInstance(this);
    }
}
