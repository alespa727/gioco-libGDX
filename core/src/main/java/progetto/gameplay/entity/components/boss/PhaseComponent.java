package progetto.gameplay.entity.components.boss;

import progetto.gameplay.entity.components.Component;

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
