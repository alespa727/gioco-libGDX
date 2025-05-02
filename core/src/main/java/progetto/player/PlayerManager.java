package progetto.player;

import progetto.entity.Engine;
import progetto.entity.entities.specific.EntityConfig;
import progetto.factories.EntityConfigFactory;

public class PlayerManager {

    final Engine engine;

    private final Player player;

    public PlayerManager(Engine engine) {
        this.engine = engine;
        EntityConfig p = EntityConfigFactory.createPlayerConfig();
        player = new Player(p, engine);
    }

    /**
     * @return Restituisce il giocatore
     */
    public Player getPlayer() {
        return player;
    }

}
