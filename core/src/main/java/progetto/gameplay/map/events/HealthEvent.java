package progetto.gameplay.map.events;

import com.badlogic.gdx.math.Vector2;
import progetto.gameplay.entity.types.Entity;
import progetto.gameplay.entity.types.living.Humanoid;
import progetto.gameplay.entity.types.living.combat.enemy.Enemy;

/**
 * Evento di cura
 */
public class HealthEvent extends MapEvent{
    float multiplier;

    /**
     * Crea un evento nella mappa che cura un'{@link Entity} quando ci entrà
     * <p>
     * L'evento ha una posizione e un raggio. Viene subito creata la zona in cui può essere attivato.
     * </p>
     * @param position posizione centrale dell’evento
     * @param radius raggio entro cui l’evento può attivarsi
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
