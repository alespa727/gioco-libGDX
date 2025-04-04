package progetto.screens.gameplay.entities.types.enemy;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.utils.Array;
import progetto.utils.Cooldown;
import progetto.screens.gameplay.entities.types.combat.CombatEntity;
import progetto.screens.gameplay.entities.types.enemy.enemyStates.EnemyStates;
import progetto.screens.gameplay.entities.types.entity.EntityConfig;
import progetto.screens.gameplay.entities.skills.enemy.Slash;
import progetto.screens.gameplay.manager.entity.EntityManager;

public abstract class Enemy extends CombatEntity {

    public final float viewDistance;
    public final float pursueMaxDistance;

    private final Array<CombatEntity> inRange;

    protected Cooldown attack;

    public final DefaultStateMachine<Enemy, EnemyStates> statemachine;

    public Enemy(EntityConfig config, EntityManager manager, float attackcooldown) {
        super(config, manager);

        attack = new Cooldown(attackcooldown);
        attack.reset(0f);
        statemachine = new DefaultStateMachine<>(this);
        viewDistance = 11f;
        pursueMaxDistance = 12f;
        statemachine.setInitialState(EnemyStates.PATROLLING);

        this.inRange = new Array<>();

        getSkillset().add(new Slash(this, "", "", 10));
    }

    /**
     * Gestione entità in range
     */
    public void addEntity(CombatEntity entity) {
        inRange.add(entity);
    }

    public void removeEntity(CombatEntity entity) {
        inRange.removeValue(entity, false);
    }

    public Array<CombatEntity> getInRange() {
        return inRange;
    }


    @Override
    public void updateEntityType(float delta) {
        statemachine.update();
    }

    @Override
    public void hit(CombatEntity entity, float damage) {
        super.hit(entity, damage);
    }


    @Override
    public void cooldown(float delta) {
        damageCooldown(delta);
    }

    public abstract void attack();
}
