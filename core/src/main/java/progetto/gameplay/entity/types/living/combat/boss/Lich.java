package progetto.gameplay.entity.types.living.combat.boss;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;

import progetto.gameplay.entity.behaviors.states.StatesLich;
import progetto.gameplay.entity.components.entity.Cooldown;
import progetto.gameplay.entity.components.humanoid.CheckDeathComponent;
import progetto.gameplay.entity.skills.boss.LichFireDomain;
import progetto.gameplay.entity.skills.boss.LichFireball;
import progetto.gameplay.entity.types.EntityConfig;
import progetto.gameplay.entity.types.EntityInstance;
import progetto.gameplay.entity.types.living.HumanoidInstances;
import progetto.gameplay.manager.ManagerWorld;
import progetto.gameplay.manager.ManagerEntity;
import progetto.utils.TerminalCommand;

public class Lich extends Boss{

    public Cooldown prepareToFireball = new Cooldown(2f);
    public Cooldown prepareFireDomain = new Cooldown(0);
    public Cooldown prepareToChangeStates = new Cooldown(0.5f);

    private final DefaultStateMachine<Lich, StatesLich> stateMachine;

    public Lich(HumanoidInstances instance, ManagerEntity managerEntity) {
        super(instance, managerEntity);
        addComponent(new CheckDeathComponent(this));
        stateMachine = new DefaultStateMachine<>(this);
        stateMachine.changeState(StatesLich.IDLE);
        getSkillset().add(new LichFireball(this, "Fireball", "Fireball", 50, 5));
        getSkillset().add(new LichFireDomain(this, "", "", 20));
    }

    public Lich(EntityConfig config, ManagerEntity managerEntity) {
        super(config, managerEntity);
        addComponent(new CheckDeathComponent(this));
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
        // Log per il despawn
        TerminalCommand.printError("Boss id " + getConfig().id + " despawnata");

        // Rimuove l'entità dal manager
        manager.remove(this);

        // Distrugge il corpo dell'entità e la sua area di range nel mondo
        ManagerWorld.destroyBody(getPhysics().getBody());
        ManagerWorld.destroyBody(getDirectionRangeComponent().getDirectionalRange());

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
