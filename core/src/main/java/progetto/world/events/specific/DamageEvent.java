package progetto.world.events.specific;

import com.badlogic.gdx.math.Vector2;
import progetto.entity.entities.base.Entity;
import progetto.world.events.base.MapEvent;

public class DamageEvent extends MapEvent {

    /**
     * Crea un evento nella mappa che può essere attivato quando il player entra nel suo raggio.
     * <p>
     * L'evento ha una posizione e un raggio. Viene subito creata la zona in cui può essere attivato.
     * </p>
     *
     * @param position posizione centrale dell’evento
     * @param radius   raggio entro cui l’evento può attivarsi
     */
    public DamageEvent(Vector2 position, float radius) {
        super(position, radius);
        setActive(true);
    }

    /**
     * Aggiorna l'evento
     */
    @Override
    public void update() {

    }

    @Override
    public void trigger(Entity entity) {

    }
}
