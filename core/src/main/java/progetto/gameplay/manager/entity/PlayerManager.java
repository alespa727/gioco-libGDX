package progetto.gameplay.manager.entity;

import progetto.factories.EntityConfigFactory;
import progetto.gameplay.entity.types.EntityConfig;
import progetto.gameplay.manager.ManagerEntity;
import progetto.gameplay.player.Player;

public class PlayerManager {

    ManagerEntity managerEntity;
    EntityLifeCycleManager lifeCycleManager;

    private final Player player;

    public PlayerManager(ManagerEntity managerEntity, EntityLifeCycleManager lifeCycleManager) {
        this.managerEntity = managerEntity;
        this.lifeCycleManager = lifeCycleManager;
        EntityConfig p = EntityConfigFactory.createPlayerConfig();
        player = new Player(p, managerEntity);
    }

    /**
     * @return Restituisce il giocatore
     */
    public Player getPlayer() {
        return player;
    }

}
