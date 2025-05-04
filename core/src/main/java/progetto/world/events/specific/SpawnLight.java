package progetto.world.events.specific;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import progetto.core.game.GameScreen;
import progetto.ECS.entities.Entity;
import progetto.graphics.shaders.specific.EntityLight;
import progetto.world.events.base.MapEvent;

public class SpawnLight extends MapEvent {

    private GameScreen game;
    private final EntityLight light;

    public SpawnLight(GameScreen game, Vector2 position, float radius, float intensity, Color color) {
        super(position, radius);
        this.light = new EntityLight(position, intensity, color, radius);
        this.game = game;
        this.game.getGameDrawer().addShader(light);
    }

    /**
     * Aggiorna l'evento
     *
     * @param delta
     */
    @Override
    public void update(float delta) {

    }

    /**
     * Attiva l'evento
     *
     * @param entity entità che lo ha triggerato
     *               <p>
     *               Gestire l'evento a seconda dell'entità
     *               </p>
     */
    @Override
    public void trigger(Entity entity) {

    }

    @Override
    public void destroy() {
        super.destroy();
        game.getGameDrawer().removeShaders(light);
    }
}
