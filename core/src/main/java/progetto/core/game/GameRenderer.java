package progetto.core.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.ScreenUtils;
import progetto.ECS.components.specific.base.PhysicsComponent;
import progetto.graphics.shaders.specific.ColorFilter;
import progetto.input.DebugWindow;
import progetto.core.CameraManager;
import progetto.world.map.Map;

public class GameRenderer {

    private static boolean isTalking = false;
    private Queue<String> strings;
    private String message;
    private final TextAnimator textAnimator;
    private final TextDrawer textDrawer;
    private final GameScreen game;

    private final Gui gui;

    private final ShapeRenderer shapeRenderer;

    public GameRenderer(GameScreen screen, ShapeRenderer shapeRenderer) {
        this.game = screen;
        this.shapeRenderer = shapeRenderer;
        this.gui = new Gui(screen);
        this.textAnimator = new TextAnimator();
        textAnimator.setText("");
        this.textDrawer = new TextDrawer();
        this.message = "";
        this.strings = new Queue<>();
    }

    public static boolean isTalking() {
        return isTalking;
    }
    public static void setTalking(boolean isTalking) {
        GameRenderer.isTalking = isTalking;
    }

    public void setText(String text) {
        int size = strings.size;
        for (int i = 0; i < size - 1; i++) {
            strings.removeValue(strings.get(i), false);
        }
        strings.addLast(text);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void draw(float delta) {

        if (textAnimator.isReady() && !strings.isEmpty()){
            textAnimator.setText(strings.removeFirst());
            isTalking = false;
        }

        if (!message.isEmpty()){
            textDrawer.setText(message);
        }

        if ((!strings.isEmpty() || !textDrawer.getText().isEmpty()) && message.isEmpty()) {
            isTalking = true;
        }else{
            isTalking = false;
        }

        if (message.isEmpty()){
            textAnimator.update(delta);
            textDrawer.setText(textAnimator.getCurrentText());
        }


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

        Vector3 worldPos = new Vector3(game.getPlayer().get(PhysicsComponent.class).getPosition(), 0);
        Vector3 pos = CameraManager.getInstance().project(worldPos.add(0, 1.4f, 0));
        textDrawer.drawCenteredText(pos.x, pos.y);

    }

    private void drawMap() {
        OrthogonalTiledMapRenderer mapRenderer = game.getMap().getMap().getRenderer();
        CameraManager.getInstance().update();
        mapRenderer.setView(CameraManager.getInstance());

        mapRenderer.render();

        if (DebugWindow.renderPathfinding()) {
            Map.getGraph().drawConnections(shapeRenderer);
            Map.getGraph().drawNodes(shapeRenderer);
        }
    }

}
