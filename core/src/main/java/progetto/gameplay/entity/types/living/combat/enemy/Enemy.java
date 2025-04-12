package progetto.gameplay.entity.types.living.combat.enemy;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.utils.Array;

import progetto.gameplay.entity.behaviors.states.StatesEnemy;
import progetto.gameplay.entity.components.humanoid.DeathController;
import progetto.gameplay.entity.types.living.combat.Warrior;
import progetto.gameplay.entity.components.entity.Cooldown;
import progetto.gameplay.entity.skills.enemy.EnemySwordAttack;
import progetto.gameplay.entity.types.EntityConfig;
import progetto.gameplay.manager.ManagerEntity;

public abstract class Enemy extends Warrior {

    // === ATTRIBUTI ===
    public final float viewDistance;
    public final float pursueMaxDistance;

    private final Array<Warrior> inRange;
    protected final Cooldown attack;

    public final DefaultStateMachine<Enemy, StatesEnemy> statemachine;

    // === COSTRUTTORI ===
    public Enemy(EnemyInstance instance, ManagerEntity manager) {
        super(instance, manager);
        addComponent(new DeathController(this));

        attack = new Cooldown(1.5f);
        attack.reset(0f);

        statemachine = new DefaultStateMachine<>(this);
        statemachine.setInitialState(StatesEnemy.PATROLLING);

        viewDistance = instance.viewDistance;
        pursueMaxDistance = instance.pursueMaxDistance;

        this.inRange = new Array<>();
        getSkillset().add(new EnemySwordAttack(this, "", "", 10));
    }

    public Enemy(EntityConfig config, ManagerEntity manager, float attackcooldown) {
        super(config, manager);

        attack = new Cooldown(attackcooldown);
        attack.reset(0f);

        statemachine = new DefaultStateMachine<>(this);
        statemachine.setInitialState(StatesEnemy.PATROLLING);

        viewDistance = 11f;
        pursueMaxDistance = 12f;

        this.inRange = new Array<>();
        getSkillset().add(new EnemySwordAttack(this, "", "", 10));
    }

    // === METODI DI ACCESSO ===
    public Array<Warrior> getInRange() {
        return inRange;
    }

    // === GESTIONE ENTITÀ IN RANGE ===
    public void addEntity(Warrior entity) {
        inRange.add(entity);
    }

    public void removeEntity(Warrior entity) {
        inRange.removeValue(entity, false);
    }

    // === AGGIORNAMENTO E GESTIONE STATO ===
    @Override
    public void updateEntityType(float delta) {
        statemachine.update();
    }

    // === COOLDOWN ===
    @Override
    public void cooldown(float delta) {
    }

    // === ATTACCO ===
    public abstract void attack();
}
