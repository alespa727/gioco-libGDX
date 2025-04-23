package progetto.player;

import progetto.factories.EntityConfigFactory;
import progetto.entity.entities.base.EntityConfig;
import progetto.entity.Engine;

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
