package progetto.gameplay.entities.components.specific;

import progetto.gameplay.entities.components.base.Component;

public class PhaseComponent extends Component {
    public int phase=0;

    public PhaseComponent() {}

    public PhaseComponent(int phase) {
        this.phase = phase;
    }

    public void nextPhase() {
        phase++;
    }
}
