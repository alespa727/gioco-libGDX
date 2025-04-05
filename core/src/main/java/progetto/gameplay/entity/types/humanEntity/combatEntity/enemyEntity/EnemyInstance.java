package progetto.gameplay.entity.types.humanEntity.combatEntity.enemyEntity;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import progetto.gameplay.entity.types.humanEntity.HumanInstance;
import progetto.gameplay.manager.entity.behaviours.EnemyStates;

public class EnemyInstance extends HumanInstance {

    // === ATTRIBUTI ===
    public final float viewDistance;
    public final float pursueMaxDistance;
    public final DefaultStateMachine<Enemy, EnemyStates> statemachine;

    // === COSTRUTTORE ===
    public EnemyInstance(Enemy e) {
        super(e);

        // Inizializzazione dei valori specifici dell'EnemyInstance
        this.viewDistance = e.viewDistance;
        this.pursueMaxDistance = e.pursueMaxDistance;
        this.statemachine = e.statemachine;
    }
}
