package progetto.gameplay.entity.types.living.combat.boss;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import progetto.gameplay.entity.behaviors.states.StatesLich;
import progetto.gameplay.entity.skills.boss.LichFireDomain;
import progetto.gameplay.entity.skills.boss.LichFireball;
import progetto.gameplay.entity.types.EntityConfig;
import progetto.gameplay.entity.types.EntityInstance;
import progetto.gameplay.entity.types.living.HumanoidInstances;
import progetto.gameplay.manager.entity.ManagerEntity;
import progetto.gameplay.manager.ManagerWorld;
import progetto.utils.Cooldown;

public class Lich extends Boss{

    public Cooldown prepareToFireball = new Cooldown(2f);
    public Cooldown prepareFireDomain = new Cooldown(0);
    public Cooldown prepareToChangeStates = new Cooldown(0.5f);

    private final DefaultStateMachine<Lich, StatesLich> stateMachine;

    public Lich(HumanoidInstances instance, ManagerEntity managerEntity) {
        super(instance, managerEntity);
        stateMachine = new DefaultStateMachine<>(this);
        stateMachine.changeState(StatesLich.LONG_RANGE_ATTACKS);
        getSkillset().add(new LichFireball(this, "Fireball", "Fireball", 50, 2));
        getSkillset().add(new LichFireDomain(this, "", "", 20));
    }

    public Lich(EntityConfig config, ManagerEntity managerEntity) {
        super(config, managerEntity);
        stateMachine = new DefaultStateMachine<>(this);
        stateMachine.changeState(StatesLich.LONG_RANGE_ATTACKS);
        getSkillset().add(new LichFireball(this, "Fireball", "Fireball", 50, 2));
        getSkillset().add(new LichFireDomain(this, "", "", 20));
    }

    @Override
    public void updateEntityType(float delta) {

        damageCooldown(delta);
        prepareToChangeStates.update(delta);
        stateMachine.update();
        checkIfDead();
    }



    @Override
    public void create() {

    }

    @Override
    public void cooldown(float delta) {

    }

    @Override
    public EntityInstance despawn() {
        // Log per il despawn
        System.out.println("Boss id " + getConfig().id + " despawnata");

        // Rimuove l'entità dal manager
        manager.remove(this);

        // Distrugge il corpo dell'entità e la sua area di range nel mondo
        Gdx.app.postRunnable(() -> ManagerWorld.getInstance().destroyBody(getPhysics().getBody()));
        Gdx.app.postRunnable(() -> ManagerWorld.getInstance().destroyBody(directionalRange));

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
