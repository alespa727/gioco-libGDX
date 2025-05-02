package progetto.entity.components.specific.item;

import progetto.entity.components.base.Component;
import progetto.entity.components.specific.movement.DirectionComponent;
import progetto.entity.entities.Entity;

public class ItemComponent extends Component {
    private Entity entity=null;
    private float angleDeg=0;

    public ItemComponent(Entity entity) {
        if (entity==null) return;
        this.entity = entity;
        this.angleDeg = entity.get(DirectionComponent.class).direction.angleDeg();
    }

    public ItemComponent() {}

    public Entity getEntity() {
        return entity;
    }

    public float getAngleDeg() {
        return angleDeg;
    }

    public void setAngleDeg(float angleDeg) {
        this.angleDeg = angleDeg;
    }
}
