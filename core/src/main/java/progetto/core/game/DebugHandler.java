package progetto.core.game;

import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import progetto.input.DebugWindow;
import progetto.input.TerminalCommand;
import progetto.player.ManagerCamera;
import progetto.world.WorldManager;

public class DebugHandler {
    private TerminalCommand terminal;
    private Box2DDebugRenderer hitboxViewer;

    public DebugHandler(GameScreen screen) {
        terminal = new TerminalCommand(screen);
        hitboxViewer = new Box2DDebugRenderer();

        hitboxViewer.setDrawVelocities(true);
    }

    public void drawHitboxes() {
        if (DebugWindow.renderHitboxes())
            hitboxViewer.render(WorldManager.getInstance(), ManagerCamera.getInstance().combined);
    }

    public void startTerminal(){
        terminal.start();
    }

    public void stopTerminal(){
        terminal.interrupt();
    }
}
