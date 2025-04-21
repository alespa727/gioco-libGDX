package progetto.gameplay.entities.specific.specific.living.combat.boss;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import progetto.gameplay.entities.components.specific.combat.MultiCooldownComponent;
import progetto.gameplay.entities.components.specific.ai.StatemachineComponent;
import progetto.gameplay.entities.components.specific.base.PhysicsComponent;
import progetto.statemachines.StatesLich;
import progetto.gameplay.entities.components.specific.base.Cooldown;
import progetto.gameplay.entities.components.specific.combat.MortalComponent;
import progetto.gameplay.entities.skills.specific.boss.LichFireDomain;
import progetto.gameplay.entities.skills.specific.boss.LichFireball;
import progetto.gameplay.entities.specific.base.EntityConfig;
import progetto.gameplay.entities.specific.base.EntityInstance;
import progetto.gameplay.entities.specific.specific.living.HumanoidInstances;
import progetto.manager.entities.Engine;
import progetto.manager.world.WorldManager;

public class Lich extends Boss {

    // === Campi ===
    public String[] types;

    // === Costruttori ===
    public Lich(HumanoidInstances instance, Engine engine) {
        super(instance, engine);
        init(engine);
    }

    public Lich(EntityConfig config, Engine engine) {
        super(config, engine);
        init(engine);
    }

    // === Inizializzazione comune ===
    private void init(Engine engine) {
        this.types = new String[]{"fireball", "firedomain", "changestates"};
        Cooldown[] cooldowns = {new Cooldown(2f), new Cooldown(0), new Cooldown(0.5f, true)};
        cooldowns[2].autoUpdate();
        components.add(new MultiCooldownComponent(types, cooldowns));
        components.add(new MortalComponent());
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
        return getComponent(StatemachineComponent.class).getStateMachine();
    }
}
