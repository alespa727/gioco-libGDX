package progetto.gameplay.manager.entity;

import progetto.factories.EntityConfigFactory;
import progetto.gameplay.entity.types.EntityConfig;
import progetto.gameplay.player.Player;

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
