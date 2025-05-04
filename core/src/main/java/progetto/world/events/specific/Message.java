package progetto.world.events.specific;

import com.badlogic.gdx.math.Vector2;
import progetto.ECS.components.specific.base.Cooldown;
import progetto.ECS.entities.Entity;
import progetto.core.game.GameRenderer;
import progetto.core.game.GameScreen;
import progetto.world.events.base.RectangleMapEvent;

public class Message extends RectangleMapEvent {

    private boolean wasActive=false;
    private boolean flag=false;
    private Cooldown cooldown;
    private GameRenderer renderer;
    private String text;
    float length;

    public Message(Vector2 position, float width, float height, String text, float delay, float length, GameScreen game) {
        super(position, width, height);
        renderer = game.getRenderer();
        this.text = text;
        this.length = length;
        cooldown = new Cooldown(delay);
        cooldown.reset();
        setActive(false);
    }

    /**
     * Aggiorna l'evento
     *
     * @param delta
     */
    @Override
    public void update(float delta) {
        if (isActive() && !wasActive) {
            cooldown.update(delta);
            if (cooldown.isReady && !flag){
                renderer.setMessage(text);
                flag=true;
                cooldown = new Cooldown(length);
                cooldown.reset();
            }
            if (cooldown.isReady && flag){
                renderer.setMessage("");
                wasActive=true;
            }
        }
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
        setActive(true);
    }
}
