package progetto.core.game;

import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import progetto.input.DebugWindow;
import progetto.core.CameraManager;
import progetto.world.WorldManager;

public class Debug {
    private GameScreen screen;
    private Terminal terminal;
    private Box2DDebugRenderer hitboxViewer;

    public Debug(GameScreen screen) {
        this.screen = screen;
        terminal = new Terminal(screen);
        hitboxViewer = new Box2DDebugRenderer();

        hitboxViewer.setDrawVelocities(true);
    }

    public void reset() {
        terminal = new Terminal(screen);
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
