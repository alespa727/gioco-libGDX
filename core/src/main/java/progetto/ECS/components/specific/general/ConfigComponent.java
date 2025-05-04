package progetto.ECS.components.specific.general;

import progetto.ECS.components.base.Component;
import progetto.ECS.entities.specific.EntityConfig;

public class ConfigComponent extends Component {
    private final EntityConfig config;

    public ConfigComponent(EntityConfig config, int id) {
        this.config = config;
        this.config.id = id;
    }

    public EntityConfig getConfig() {
        return new EntityConfig(config);
    }
}
