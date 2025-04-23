package progetto.entity.components.specific.general;

import progetto.entity.components.base.Component;
import progetto.entity.entities.base.EntityConfig;

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
