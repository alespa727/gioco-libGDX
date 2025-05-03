package progetto.core.game;

import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import progetto.input.DebugWindow;
import progetto.core.CameraManager;
import progetto.world.WorldManager;

public class Debug {
    private Terminal terminal;
    private Box2DDebugRenderer hitboxViewer;

    public Debug(GameScreen screen) {
        terminal = new Terminal(screen);
        hitboxViewer = new Box2DDebugRenderer();

        hitboxViewer.setDrawVelocities(true);
    }

    public void drawHitboxes() {
        if (DebugWindow.renderHitboxes())
            hitboxViewer.render(WorldManager.getInstance(), CameraManager.getInstance().combined);
    }

    public void startTerminal(){
        terminal.start();
    }

    public void stopTerminal(){
        terminal.stopRunning();
    }
}
