package progetto.entity.components.specific.ai;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import progetto.entity.components.base.Component;
import progetto.entity.entities.Entity;


public class StatemachineComponent<E extends Entity, S extends State<E>> extends Component {
    private final DefaultStateMachine<E, S> stateMachine;

    public StatemachineComponent(E entity, S state) {
        this.stateMachine = new DefaultStateMachine<>(entity);
        this.stateMachine.setInitialState(state);
    }

    public StatemachineComponent() {
        stateMachine = null;
    }

    public DefaultStateMachine<E, S> getStateMachine() {
        return stateMachine;
    }
}

