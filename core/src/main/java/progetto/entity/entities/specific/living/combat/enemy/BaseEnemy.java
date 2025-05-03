package progetto.entity.entities.specific.living.combat.enemy;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.utils.Array;
import progetto.entity.EntityEngine;
import progetto.entity.components.specific.ai.StatemachineComponent;
import progetto.entity.components.specific.base.Cooldown;
import progetto.entity.components.specific.combat.MortalComponent;
import progetto.entity.components.specific.combat.MultiCooldownComponent;
import progetto.entity.components.specific.general.Saveable;
import progetto.entity.components.specific.general.skills.specific.enemy.EnemySwordAttack;
import progetto.entity.components.specific.sensors.InRangeListComponent;
import progetto.entity.entities.specific.EntityConfig;
import progetto.entity.entities.specific.living.combat.Warrior;
import progetto.entity.statemachines.StatesEnemy;

public abstract class BaseEnemy extends Warrior {

    private Array<Warrior> inRange;

    // === COSTRUTTORI ===
    public BaseEnemy(EnemyInstance instance, EntityEngine manager) {
        super(instance, manager);
    }

    public BaseEnemy(EntityConfig config, EntityEngine manager) {
        super(config, manager);
    }

    @Override
    public void create() {
        super.create();
        add(
            new InRangeListComponent(),
            new StatemachineComponent<>(this, StatesEnemy.PATROLLING),
            new MortalComponent(),
            new MultiCooldownComponent(),
            new Saveable()
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
    public <E extends BaseEnemy, S extends State<E>> DefaultStateMachine<E, S> getStateMachine() {
        return get(StatemachineComponent.class).getStateMachine();
    }

    // === ATTACCO ===
    public abstract void attack();
}
