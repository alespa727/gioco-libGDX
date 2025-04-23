package progetto.entity.entities.specific.living.combat.enemy;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.utils.Array;
import progetto.entity.Engine;
import progetto.entity.components.specific.ai.StatemachineComponent;
import progetto.entity.components.specific.base.Cooldown;
import progetto.entity.components.specific.combat.MortalComponent;
import progetto.entity.components.specific.combat.MultiCooldownComponent;
import progetto.entity.components.specific.general.skills.specific.enemy.EnemySwordAttack;
import progetto.entity.entities.base.EntityConfig;
import progetto.entity.entities.specific.living.combat.Warrior;
import progetto.statemachines.StatesEnemy;

public abstract class Enemy extends Warrior {

    // === ATTRIBUTI ===
    public final float viewDistance;
    public final float pursueMaxDistance;

    private Array<Warrior> inRange;

    // === COSTRUTTORI ===
    public Enemy(EnemyInstance instance, Engine manager) {
        super(instance, manager);
        viewDistance = instance.viewDistance;
        pursueMaxDistance = instance.pursueMaxDistance;
    }

    public Enemy(EntityConfig config, Engine manager, float attackcooldown) {
        super(config, manager);
        viewDistance = 11f;
        pursueMaxDistance = 12f;
    }

    @Override
    public void create() {
        super.create();
        add(
            new StatemachineComponent<>(this, StatesEnemy.PATROLLING),
            new MortalComponent(),
            new MultiCooldownComponent()
        );

        get(MultiCooldownComponent.class).add("attack", new Cooldown(1.5f, true));
        getAttackCooldown().reset();


        this.inRange = new Array<>();
        getSkillset().add(new EnemySwordAttack(this, "", "", 10));
    }

    public Cooldown getAttackCooldown() {
        return get(MultiCooldownComponent.class).getCooldown("attack");
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

    @SuppressWarnings("unchecked")
    public <E extends Enemy, S extends State<E>> DefaultStateMachine<E, S> getStateMachine() {
        return get(StatemachineComponent.class).getStateMachine();
    }

    // === ATTACCO ===
    public abstract void attack();
}
