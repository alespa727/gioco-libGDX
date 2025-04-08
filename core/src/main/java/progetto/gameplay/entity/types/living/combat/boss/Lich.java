package progetto.gameplay.entity.types.living.combat.boss;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import progetto.gameplay.entity.skills.boss.LichFireball;
import progetto.gameplay.entity.types.EntityConfig;
import progetto.gameplay.entity.types.EntityInstance;
import progetto.gameplay.entity.types.living.HumanoidInstances;
import progetto.gameplay.entity.behaviors.EntityManager;
import progetto.gameplay.entity.behaviors.manager.entity.behaviours.LichStates;
import progetto.gameplay.entity.behaviors.manager.map.WorldManager;
import progetto.utils.Cooldown;

public class Lich extends Boss{

    private final DefaultStateMachine<Lich, LichStates> stateMachine;

    public Lich(HumanoidInstances instance, EntityManager entityManager) {
        super(instance, entityManager);
        stateMachine = new DefaultStateMachine<>(this);
        stateMachine.changeState(LichStates.PURSUE1);
    }

    public Lich(EntityConfig config, EntityManager entityManager) {
        super(config, entityManager);
        stateMachine = new DefaultStateMachine<>(this);
        stateMachine.changeState(LichStates.PURSUE1);
        getSkillset().add(new LichFireball(this, "Fireball", "Fireball", 50, 2));
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
        Gdx.app.postRunnable(() -> WorldManager.getInstance().destroyBody(body));
        Gdx.app.postRunnable(() -> WorldManager.getInstance().destroyBody(directionalRange));

        return null;
    }

    @Override
    public void attack() {
    }

    public void fireball() {
        getSkillset().execute(LichFireball.class);
    }

    public DefaultStateMachine<Lich, LichStates> getStateMachine(){
        return stateMachine;
    }
}
