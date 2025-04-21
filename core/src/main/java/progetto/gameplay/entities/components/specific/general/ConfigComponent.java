package progetto.gameplay.entities.components.specific.general;

import progetto.gameplay.entities.components.base.Component;
import progetto.gameplay.entities.specific.base.EntityConfig;

public class ConfigComponent extends Component {
    private final EntityConfig config;

    public ConfigComponent(EntityConfig config, int id) {
        this.config = config;
        this.config.id = id;
    }

    public EntityConfig getConfig() {
        return config;
    }
}
