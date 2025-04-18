package progetto.gameplay.entities.components.specific.base;

import progetto.gameplay.entities.components.base.Component;

public class ZLevelComponent extends Component {
    int z;

    public ZLevelComponent(int z) {
        this.z = z;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }
}
