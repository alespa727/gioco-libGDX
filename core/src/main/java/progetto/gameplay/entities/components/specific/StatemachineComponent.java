package progetto.gameplay.entities.components.specific;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import progetto.gameplay.entities.components.base.Component;
import progetto.gameplay.entities.specific.base.Entity;


public class StatemachineComponent<E extends Entity, S extends State<E>> extends Component {
    private final DefaultStateMachine<E, S> stateMachine;

    public StatemachineComponent(E entity, S state) {
        this.stateMachine = new DefaultStateMachine<>(entity);
        this.stateMachine.setInitialState(state);
    }

    public DefaultStateMachine<E, S> getStateMachine() {
        return stateMachine;
    }
}

