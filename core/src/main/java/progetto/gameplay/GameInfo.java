package progetto.gameplay;

import progetto.Core;
import progetto.gameplay.manager.entity.ManagerEntity;
import progetto.gameplay.map.MapManager;

public class GameInfo {
    public Core core;
    public Game screen;
    public ManagerEntity managerEntity;
    public MapManager mapManager;

    public GameInfo() {}

    public GameInfo(Core core, Game screen, ManagerEntity managerEntity, MapManager mapManager) {
        this.core = core;
        this.screen = screen;
        this.managerEntity = managerEntity;
        this.mapManager = mapManager;
    }
}
