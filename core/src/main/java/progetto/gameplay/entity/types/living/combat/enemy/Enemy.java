package progetto.gameplay.entity.types.living.combat.enemy;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.utils.Array;
import progetto.gameplay.entity.behaviors.states.StatesEnemy;
import progetto.gameplay.entity.components.entity.Cooldown;
import progetto.gameplay.entity.components.humanoid.CheckDeathComponent;
import progetto.gameplay.entity.components.warrior.AttackCooldown;
import progetto.gameplay.entity.skills.enemy.EnemySwordAttack;
import progetto.gameplay.entity.types.EntityConfig;
import progetto.gameplay.entity.types.living.combat.Warrior;
import progetto.gameplay.manager.ManagerEntity;

public abstract class Enemy extends Warrior {

    // === ATTRIBUTI ===
    public final float viewDistance;
    public final float pursueMaxDistance;

    private Array<Warrior> inRange;

    public DefaultStateMachine<Enemy, StatesEnemy> statemachine;

    // === COSTRUTTORI ===
    public Enemy(EnemyInstance instance, ManagerEntity manager) {
        super(instance, manager);
        viewDistance = instance.viewDistance;
        pursueMaxDistance = instance.pursueMaxDistance;
    }

    public Enemy(EntityConfig config, ManagerEntity manager, float attackcooldown) {
        super(config, manager);
        viewDistance = 11f;
        pursueMaxDistance = 12f;
    }

    @Override
    public void create() {
        super.create();
        statemachine = new DefaultStateMachine<>(this);
        statemachine.setInitialState(StatesEnemy.PATROLLING);
        addComponent(new CheckDeathComponent(this));
        addComponent(new AttackCooldown(1.5f));
        getAttackCooldown().reset();


        this.inRange = new Array<>();
        getSkillset().add(new EnemySwordAttack(this, "", "", 10));
    }

    public Cooldown getAttackCooldown(){
        return getComponent(AttackCooldown.class);
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
