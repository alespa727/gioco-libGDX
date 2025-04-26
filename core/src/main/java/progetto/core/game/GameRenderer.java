package progetto.core.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import progetto.core.Gui;
import progetto.graphics.shaders.specific.ColorFilter;
import progetto.input.DebugWindow;
import progetto.player.ManagerCamera;
import progetto.player.inventory.Inventory;
import progetto.world.map.Map;

public class GameRenderer {

    GameScreen game;

    private DebugWindow debugWindow;
    private Inventory inventory;

    private Gui gui;

    private final ShapeRenderer shapeRenderer;

    public GameRenderer(GameScreen screen, ShapeRenderer shapeRenderer) {
        this.game = screen;
        this.shapeRenderer = shapeRenderer;
        this.gui = new Gui(screen);
    }

    public void draw() {
        //Impostazione dello sfondo
        ColorFilter.setColor(0.5f, 0.5f, 0.55f);
        Color darkGray = new Color(0.3f, 0.3f, 0.3f, 1.0f); // Grigio scuro
        ScreenUtils.clear(darkGray); // Clear dello schermo
        // Renderizza la mappa
        this.drawMap();
        // Renderizza le entità
        game.getEntityManager().draw();

        game.getDebugHandler().drawHitboxes();

        // Se necessario, disegna la GUI
        if (game.getMap().disegnaUI()) {
            gui.draw();
        }
    }

    private void drawMap() {
        OrthogonalTiledMapRenderer mapRenderer = game.getMap().getMap().getMapRenderer();
        ManagerCamera.getInstance().update();
        mapRenderer.setView(ManagerCamera.getInstance());

        mapRenderer.render();

        if (DebugWindow.renderPathfinding()) {
            Map.getGraph().drawConnections(shapeRenderer);
            Map.getGraph().drawNodes(shapeRenderer);
        }
    }


}
