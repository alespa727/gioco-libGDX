package progetto.gameplay.entity.types.living.combat.enemy;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import progetto.gameplay.entity.behaviors.states.StatesEnemy;
import progetto.gameplay.entity.types.living.HumanoidInstances;

public class EnemyInstance extends HumanoidInstances {

    // === ATTRIBUTI ===
    public final float viewDistance;
    public final float pursueMaxDistance;
    public final DefaultStateMachine<Enemy, StatesEnemy> statemachine;

    // === COSTRUTTORE ===
    public EnemyInstance(Enemy e) {
        super(e);

        // Inizializzazione dei valori specifici dell'EnemyInstance
        this.viewDistance = e.viewDistance;
        this.pursueMaxDistance = e.pursueMaxDistance;
        this.statemachine = e.statemachine;
    }
}
