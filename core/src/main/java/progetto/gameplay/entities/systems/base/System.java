package progetto.gameplay.entities.systems.base;

import com.badlogic.gdx.utils.Array;
import progetto.gameplay.entities.specific.base.Entity;


public abstract class System {
    private boolean state=true;

    public void activate() {
        this.state=true;
    }

    public void deactivate() {
        this.state=false;
    }

    public boolean isActive() {
        return this.state;
    }

    public abstract void update(float delta, Array<Entity> list);

}
