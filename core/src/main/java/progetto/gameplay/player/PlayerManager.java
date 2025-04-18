package progetto.gameplay.player;

import progetto.factories.EntityConfigFactory;
import progetto.gameplay.entities.specific.base.EntityConfig;
import progetto.manager.entities.EntityLifeCycleManager;
import progetto.manager.entities.EntityManager;

public class PlayerManager {

    final EntityManager entityManager;
    final EntityLifeCycleManager lifeCycleManager;

    private final Player player;

    public PlayerManager(EntityManager entityManager, EntityLifeCycleManager lifeCycleManager) {
        this.entityManager = entityManager;
        this.lifeCycleManager = lifeCycleManager;
        EntityConfig p = EntityConfigFactory.createPlayerConfig();
        player = new Player(p, entityManager);
    }

    /**
     * @return Restituisce il giocatore
     */
    public Player getPlayer() {
        return player;
    }

}
