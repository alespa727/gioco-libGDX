package progetto.gameplay.entities.components.base;

public abstract class Component{
    // Classe vuota, necessaria solamente per ereditarietà
    private boolean awake = true;

    public boolean isAwake() {
        return awake;
    }

    public void setAwake(boolean awake) {
        this.awake = awake;
    }
}
