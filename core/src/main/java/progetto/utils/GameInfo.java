package progetto.utils;

import progetto.Core;
import progetto.gameplay.GameScreen;
import progetto.gameplay.manager.ManagerEntity;
import progetto.gameplay.map.MapManager;

public class GameInfo {
    public Core core;
    public GameScreen screen;
    public ManagerEntity managerEntity;
    public MapManager mapManager;

    public GameInfo() {}

    public GameInfo(Core core, GameScreen screen, ManagerEntity managerEntity, MapManager mapManager) {
        this.core = core;
        this.screen = screen;
        this.managerEntity = managerEntity;
        this.mapManager = mapManager;
    }
}
