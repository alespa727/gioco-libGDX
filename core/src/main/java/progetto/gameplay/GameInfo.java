package progetto.gameplay;

import progetto.Core;
import progetto.gameplay.manager.ManagerEntity;
import progetto.gameplay.map.MapManager;

public class GameInfo {
    public Core game;
    public Game screen;
    public ManagerEntity managerEntity;
    public MapManager mapManager;

    public GameInfo() {}

    public GameInfo(Core game, Game screen, ManagerEntity managerEntity, MapManager mapManager) {
        this.game = game;
        this.screen = screen;
        this.managerEntity = managerEntity;
        this.mapManager = mapManager;
    }
}
