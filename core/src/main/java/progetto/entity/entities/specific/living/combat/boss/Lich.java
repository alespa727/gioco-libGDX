package progetto.entity.entities.specific.living.combat.boss;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import progetto.entity.Engine;
import progetto.entity.components.specific.ai.StatemachineComponent;
import progetto.entity.components.specific.base.Cooldown;
import progetto.entity.components.specific.base.PhysicsComponent;
import progetto.entity.components.specific.combat.MortalComponent;
import progetto.entity.components.specific.combat.MultiCooldownComponent;
import progetto.entity.components.specific.general.skills.specific.boss.LichFireDomain;
import progetto.entity.components.specific.general.skills.specific.boss.LichFireball;
import progetto.entity.components.specific.movement.MovementComponent;
import progetto.entity.entities.specific.EntityConfig;
import progetto.entity.entities.specific.EntityInstance;
import progetto.entity.entities.specific.living.HumanoidInstances;
import progetto.statemachines.StatesLich;
import progetto.world.WorldManager;

public class Lich extends Boss {

    // === Campi ===
    public String[] types;

    // === Costruttori ===
    public Lich(HumanoidInstances instance, Engine engine) {
        super(instance, engine);
        init();
    }

    public Lich(EntityConfig config, Engine engine) {
        super(config, engine);
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
        engine.remove(this);
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
