package progetto.gameplay.map.events;

import com.badlogic.gdx.math.Vector2;
import progetto.gameplay.entity.types.Entity;
import progetto.gameplay.entity.types.living.Humanoid;

public class HealthEvent extends MapEvent{
    float multiplier;

    /**
     * Crea l'evento fisicamente
     *
     * @param position
     * @param radius
     */
    public HealthEvent(Vector2 position, float radius, float multiplier) {
        super(position, radius);
        this.multiplier = multiplier;
    }

    @Override
    public void update() {
    }

    @Override
    public void trigger(Entity entity) {
        if (entity instanceof Humanoid){
            System.out.println("EVENTO");
            if (!((Humanoid) entity).hasAnyHealthBuff())
                ((Humanoid) entity).setHealthBuff(multiplier);
        }
    }
}
