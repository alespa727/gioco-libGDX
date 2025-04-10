package progetto.gameplay.entity.components;

public class StateComponent {

    private boolean isAlive;
    private boolean isLoaded;
    private boolean shouldRender;

    public StateComponent() {
        this.isAlive = true;
        this.isLoaded = false;
        this.shouldRender = false;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }

    public boolean shouldRender() {
        return shouldRender;
    }

    public void setShouldRender(boolean shouldRender) {
        this.shouldRender = shouldRender;
    }
}
