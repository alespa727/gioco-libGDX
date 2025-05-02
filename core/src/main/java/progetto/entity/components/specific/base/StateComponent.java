package progetto.entity.components.specific.base;

import progetto.entity.components.base.Component;

public class StateComponent extends Component {

    private boolean isAlive = true;
    private boolean isLoaded = false;
    private boolean shouldRender = false;

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

    public boolean shouldBeUpdated() {
        return shouldRender;
    }

    public void setShouldBeUpdated(boolean shouldRender) {
        this.shouldRender = shouldRender;
    }
}
