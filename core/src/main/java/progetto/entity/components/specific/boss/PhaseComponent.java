package progetto.entity.components.specific.boss;

import progetto.entity.components.base.Component;

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
