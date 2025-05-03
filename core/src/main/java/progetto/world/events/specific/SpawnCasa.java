package progetto.world.events.specific;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import progetto.core.ResourceManager;
import progetto.entity.EntityEngine;
import progetto.entity.entities.Entity;
import progetto.entity.entities.specific.EntityConfig;
import progetto.entity.entities.specific.living.combat.enemy.BaseEnemy;
import progetto.entity.entities.specific.notliving.Casa;
import progetto.world.events.base.MapEvent;

public class SpawnCasa extends MapEvent {
    private final EntityEngine entityEngine;

    /**
     * Crea un evento di spawn di un {@link BaseEnemy}
     * <p>
     * L'evento ha una posizione e un raggio. Viene subito creata la zona in cui può essere attivato.
     * </p>
     *
     * @param position  posizione centrale dell’evento
     * @param radius    raggio entro cui l’evento può attivarsi
     * @param entityEngine    gestore entità
     */
    public SpawnCasa(Vector2 position, float radius, EntityEngine entityEngine) {
        super(position, radius);
        this.entityEngine = entityEngine;
        this.trigger(null);
        this.destroy();
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void trigger(Entity entity) {
        EntityConfig config = new EntityConfig();
        config.nome = "Casa";
        config.descrizione = "Spada tagliente";
        config.x = position.x;
        config.y = position.y;
        config.img = ResourceManager.get().get("entities/Casa.png", Texture.class);
        config.radius = 10 / 32f;
        config.direzione = new Vector2(0, -0.5f);
        config.isAlive = true;
        config.inCollisione = false;
        config.isMoving = false;
        config.hp = 100;
        config.speed = 1.25f;
        config.attackdmg = 20;
        config.imageHeight = 2f;
        config.imageWidth = 2f;

        entityEngine.summon(new Casa(config, entityEngine, 6));
    }
}
