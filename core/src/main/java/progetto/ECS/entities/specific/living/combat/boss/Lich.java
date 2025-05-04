package progetto.ECS.entities.specific.living.combat.boss;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import progetto.ECS.EntityEngine;
import progetto.ECS.components.specific.ai.StatemachineComponent;
import progetto.ECS.components.specific.base.Cooldown;
import progetto.ECS.components.specific.base.PhysicsComponent;
import progetto.ECS.components.specific.combat.MortalComponent;
import progetto.ECS.components.specific.combat.MultiCooldownComponent;
import progetto.ECS.components.specific.general.skills.specific.boss.LichFireDomain;
import progetto.ECS.components.specific.general.skills.specific.boss.LichFireball;
import progetto.ECS.components.specific.movement.MovementComponent;
import progetto.ECS.entities.specific.EntityConfig;
import progetto.ECS.entities.specific.EntityInstance;
import progetto.ECS.entities.specific.living.HumanoidInstances;
import progetto.ECS.statemachines.StatesLich;
import progetto.world.WorldManager;

public class Lich extends Boss {

    // === Campi ===
    public String[] types;

    // === Costruttori ===
    public Lich(HumanoidInstances instance, EntityEngine entityEngine) {
        super(instance, entityEngine);
        init();
    }

    public Lich(EntityConfig config, EntityEngine entityEngine) {
        super(config, entityEngine);
        init();
    }

    // === Inizializzazione comune ===
    private void init() {
        this.types = new String[]{"fireball", "firedomain", "changestates"};
        Cooldown[] cooldowns = {new Cooldown(2f), new Cooldown(0), new Cooldown(0.5f, true)};
        cooldowns[2].autoUpdate();
        components.add(new MultiCooldownComponent(types, cooldowns));
        components.add(new MortalComponent());
        components.add(new MovementComponent());
        components.add(new StatemachineComponent<>(this, StatesLich.IDLE));
        getSkillset().add(new LichFireball(this, "Fireball", "Fireball", 50, 5f));
        getSkillset().add(new LichFireDomain(this, "", "", 20));
    }

    // === Override: Metodi di lifecycle ===
    @Override
    public void create() {
        super.create();

    }

    @Override
    public EntityInstance unregister() {
        entityEngine.remove(this);
        WorldManager.destroyBody(components.get(PhysicsComponent.class).getBody());
        return new BossInstance(this);
    }

    // === Azioni custom ===
    public void fireball() {
        getSkillset().execute(LichFireball.class);
    }

    public void fireDomain() {
        getSkillset().execute(LichFireDomain.class);
    }

    // === Getter per lo statemachine ===
    @SuppressWarnings("unchecked")
    public <E extends Lich, S extends State<E>> DefaultStateMachine<E, S> getStateMachine() {
        return get(StatemachineComponent.class).getStateMachine();
    }
}
