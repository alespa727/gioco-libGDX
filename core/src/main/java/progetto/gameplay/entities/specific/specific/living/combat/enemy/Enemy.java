package progetto.gameplay.entities.specific.specific.living.combat.enemy;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.utils.Array;
import progetto.gameplay.entities.components.specific.ai.StatemachineComponent;
import progetto.gameplay.entities.components.specific.base.Cooldown;
import progetto.gameplay.entities.components.specific.combat.MortalComponent;
import progetto.gameplay.entities.components.specific.combat.MultiCooldownComponent;
import progetto.gameplay.entities.skills.specific.enemy.EnemySwordAttack;
import progetto.gameplay.entities.specific.base.EntityConfig;
import progetto.gameplay.entities.specific.specific.living.combat.Warrior;
import progetto.manager.entities.Engine;
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
        addComponents(
            new StatemachineComponent<>(this, StatesEnemy.PATROLLING),
            new MortalComponent(),
            new MultiCooldownComponent()
        );

        getComponent(MultiCooldownComponent.class).add("attack", new Cooldown(1.5f));
        getAttackCooldown().reset();


        this.inRange = new Array<>();
        getSkillset().add(new EnemySwordAttack(this, "", "", 10));
    }

    public Cooldown getAttackCooldown() {
        return getComponent(MultiCooldownComponent.class).getCooldown("attack");
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
        return getComponent(StatemachineComponent.class).getStateMachine();
    }

    // === ATTACCO ===
    public abstract void attack();
}
