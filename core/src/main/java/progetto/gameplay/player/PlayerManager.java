package progetto.gameplay.player;

import progetto.factories.EntityConfigFactory;
import progetto.gameplay.entities.specific.base.EntityConfig;
import progetto.manager.entities.EntityLifeCycleManager;
import progetto.manager.entities.Engine;

public class PlayerManager {

    final Engine engine;
    final EntityLifeCycleManager lifeCycleManager;

    private final Player player;

    public PlayerManager(Engine engine, EntityLifeCycleManager lifeCycleManager) {
        this.engine = engine;
        this.lifeCycleManager = lifeCycleManager;
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
