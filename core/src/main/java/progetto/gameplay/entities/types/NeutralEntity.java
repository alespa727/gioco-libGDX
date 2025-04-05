package progetto.gameplay.entities.types;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import progetto.gameplay.entities.types.entity.EntityConfig;
import progetto.gameplay.manager.entity.behaviours.NeutralEntityStates;
import progetto.gameplay.manager.entity.EntityManager;

public abstract class NeutralEntity extends HumanEntity {

    private final DefaultStateMachine<NeutralEntity, NeutralEntityStates> stateMachine;

    public NeutralEntity(EntityConfig config, EntityManager manager) {
        super(config, manager);
        stateMachine = new DefaultStateMachine<>(this);
    }

    public DefaultStateMachine<NeutralEntity, NeutralEntityStates> statemachine() {
        return stateMachine;
    }

    @Override
    public void create() {

    }
}
