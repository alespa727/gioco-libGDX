package progetto.gameplay.entities.components.specific.boss;

import progetto.gameplay.entities.components.base.Component;

public class PhaseComponent extends Component {
    private int phase=0;

    public PhaseComponent() {}

    public PhaseComponent(int phase) {
        this.phase = phase;
    }

    public int getPhase() {
        return phase;
    }

    public void nextPhase() {
        phase++;
    }
}
