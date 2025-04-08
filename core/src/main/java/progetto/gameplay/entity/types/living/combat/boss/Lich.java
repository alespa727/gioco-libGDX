package progetto.gameplay.entity.types.living.combat.boss;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import progetto.gameplay.entity.skills.boss.LichFireDomain;
import progetto.gameplay.entity.skills.boss.LichFireball;
import progetto.gameplay.entity.types.EntityConfig;
import progetto.gameplay.entity.types.EntityInstance;
import progetto.gameplay.entity.types.living.HumanoidInstances;
import progetto.gameplay.manager.ManagerEntity;
import progetto.gameplay.manager.ManagerWorld;

public class Lich extends Boss{

    private final DefaultStateMachine<Lich, progetto.gameplay.entity.behaviors.manager.entity.behaviours.StatesLich> stateMachine;

    public Lich(HumanoidInstances instance, ManagerEntity managerEntity) {
        super(instance, managerEntity);
        stateMachine = new DefaultStateMachine<>(this);
        stateMachine.changeState(progetto.gameplay.entity.behaviors.manager.entity.behaviours.StatesLich.PURSUE);
    }

    public Lich(EntityConfig config, ManagerEntity managerEntity) {
        super(config, managerEntity);
        stateMachine = new DefaultStateMachine<>(this);
        stateMachine.changeState(progetto.gameplay.entity.behaviors.manager.entity.behaviours.StatesLich.PURSUE);
        getSkillset().add(new LichFireball(this, "Fireball", "Fireball", 50, 2));
        getSkillset().add(new LichFireDomain(this, "", "", 20));
    }

    @Override
    public void updateEntityType(float delta) {

        damageCooldown(delta);

        stateMachine.update();
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
        System.out.println("Boss id " + id() + " despawnata");

        // Rimuove l'entità dal manager
        manager.removeEntity(this);

        // Distrugge il corpo dell'entità e la sua area di range nel mondo
        Gdx.app.postRunnable(() -> ManagerWorld.getInstance().destroyBody(body));
        Gdx.app.postRunnable(() -> ManagerWorld.getInstance().destroyBody(directionalRange));

        return null;
    }

    @Override
    public void attack() {
    }

    public void fireball() {
        getSkillset().execute(LichFireball.class);
    }

    public void fireDomain() {
        getSkillset().execute(LichFireDomain.class);
    }

    public DefaultStateMachine<Lich, progetto.gameplay.entity.behaviors.manager.entity.behaviours.StatesLich> getStateMachine(){
        return stateMachine;
    }
}
