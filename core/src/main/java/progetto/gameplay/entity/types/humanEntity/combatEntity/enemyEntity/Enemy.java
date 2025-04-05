package progetto.gameplay.entity.types.humanEntity.combatEntity.enemyEntity;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.utils.Array;

import progetto.gameplay.entity.types.humanEntity.combatEntity.CombatEntity;
import progetto.utils.Cooldown;
import progetto.gameplay.manager.entity.behaviours.EnemyStates;
import progetto.gameplay.entity.skills.enemy.EnemySwordAttack;
import progetto.gameplay.entity.types.abstractEntity.EntityConfig;
import progetto.gameplay.manager.entity.EntityManager;

public abstract class Enemy extends CombatEntity {

    // === ATTRIBUTI ===
    public final float viewDistance;
    public final float pursueMaxDistance;

    private final Array<CombatEntity> inRange;
    protected final Cooldown attack;

    public final DefaultStateMachine<Enemy, EnemyStates> statemachine;

    // === COSTRUTTORI ===
    public Enemy(EnemyInstance instance, EntityManager manager) {
        super(instance, manager);

        attack = new Cooldown(1.5f);
        attack.reset(0f);

        statemachine = new DefaultStateMachine<>(this);
        statemachine.setInitialState(EnemyStates.PATROLLING);

        viewDistance = instance.viewDistance;
        pursueMaxDistance = instance.pursueMaxDistance;

        this.inRange = new Array<>();
        getSkillset().add(new EnemySwordAttack(this, "", "", 10));
    }

    public Enemy(EntityConfig config, EntityManager manager, float attackcooldown) {
        super(config, manager);

        attack = new Cooldown(attackcooldown);
        attack.reset(0f);

        statemachine = new DefaultStateMachine<>(this);
        statemachine.setInitialState(EnemyStates.PATROLLING);

        viewDistance = 11f;
        pursueMaxDistance = 12f;

        this.inRange = new Array<>();
        getSkillset().add(new EnemySwordAttack(this, "", "", 10));
    }

    // === METODI DI ACCESSO ===
    public Array<CombatEntity> getInRange() {
        return inRange;
    }

    // === GESTIONE ENTITÀ IN RANGE ===
    public void addEntity(CombatEntity entity) {
        inRange.add(entity);
    }

    public void removeEntity(CombatEntity entity) {
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
        damageCooldown(delta);
    }

    // === ATTACCO ===
    public abstract void attack();
}
