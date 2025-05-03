package progetto.core.game.player;

import progetto.entity.EntityEngine;
import progetto.entity.entities.specific.EntityConfig;
import progetto.factories.EntityConfigFactory;

public class PlayerManager {

    final EntityEngine entityEngine;

    private final Player player;

    public PlayerManager(EntityEngine entityEngine) {
        this.entityEngine = entityEngine;
        EntityConfig p = EntityConfigFactory.createPlayerConfig();
        player = new Player(p, entityEngine);
    }

    /**
     * @return Restituisce il giocatore
     */
    public Player getPlayer() {
        return player;
    }

}
