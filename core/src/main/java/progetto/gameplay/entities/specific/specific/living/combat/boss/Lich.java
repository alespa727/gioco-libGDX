package progetto.gameplay.entities.specific.specific.living.combat.boss;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import progetto.statemachines.StatesLich;
import progetto.gameplay.entities.components.specific.base.Cooldown;
import progetto.gameplay.entities.components.specific.humanoid.DeathComponent;
import progetto.gameplay.entities.skills.specific.boss.LichFireDomain;
import progetto.gameplay.entities.skills.specific.boss.LichFireball;
import progetto.gameplay.entities.specific.base.EntityConfig;
import progetto.gameplay.entities.specific.base.EntityInstance;
import progetto.gameplay.entities.specific.specific.living.HumanoidInstances;
import progetto.manager.entities.Engine;
import progetto.manager.world.WorldManager;

public class Lich extends Boss{

    public final Cooldown prepareToFireball = new Cooldown(2f);
    public final Cooldown prepareFireDomain = new Cooldown(0);
    public final Cooldown prepareToChangeStates = new Cooldown(0.5f);

    private final DefaultStateMachine<Lich, StatesLich> stateMachine;

    public Lich(HumanoidInstances instance, Engine engine) {
        super(instance, engine);
        componentManager.add(new DeathComponent());
        stateMachine = new DefaultStateMachine<>(this);
        stateMachine.changeState(StatesLich.IDLE);
        getSkillset().add(new LichFireball(this, "Fireball", "Fireball", 50, 5));
        getSkillset().add(new LichFireDomain(this, "", "", 20));
    }

    public Lich(EntityConfig config, Engine engine) {
        super(config, engine);
        componentManager.add(new DeathComponent());
        stateMachine = new DefaultStateMachine<>(this);
        stateMachine.changeState(StatesLich.IDLE);
        getSkillset().add(new LichFireball(this, "Fireball", "Fireball", 50, 5f));
        getSkillset().add(new LichFireDomain(this, "", "", 20));
    }

    @Override
    public void updateEntityType(float delta) {
        prepareToChangeStates.update(delta);
        stateMachine.update();
    }

    @Override
    public void create() {
        super.create();
    }

    @Override
    public void cooldown(float delta) {

    }

    @Override
    public EntityInstance despawn() {
        // Rimuove l'entità dal manager
        manager.remove(this);

        // Distrugge il corpo dell'entità e la sua area di range nel mondo
        WorldManager.destroyBody(getPhysics().getBody());
        WorldManager.destroyBody(getDirectionRangeComponent().getDirectionalRange());

        return new BossInstance(this);
    }

    public void fireball() {
        getSkillset().execute(LichFireball.class);
    }

    public void fireDomain() {
        getSkillset().execute(LichFireDomain.class);
    }

    public DefaultStateMachine<Lich,StatesLich> getStateMachine(){
        return stateMachine;
    }
}
