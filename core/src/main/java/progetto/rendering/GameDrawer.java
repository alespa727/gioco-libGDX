package progetto.rendering;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import progetto.screens.GameScreen;
import progetto.rendering.shaders.ScreenShader;

public class GameDrawer {
    private final Array<ScreenShader> shaders;
    private final GameScreen game;

    public GameDrawer(GameScreen game) {
        shaders = new Array<>();
        this.game = game;

    }

    public void addShader(ScreenShader screenShader) {
        shaders.add(screenShader);
    }

    public void removeShaders(ScreenShader screenShader) {
        shaders.removeValue(screenShader, false);
    }

    public void draw(SpriteBatch batch) {

        shaders.get(0).begin();
        game.draw();
        shaders.get(0).end();

        for (int i = 1; i < shaders.size; i++) {
            ScreenShader input = shaders.get(i-1);
            ScreenShader output = shaders.get(i);
            output.begin();
            input.draw(batch);
            output.end();
        }

        ScreenShader finalOutput = shaders.get(shaders.size-1);
        finalOutput.draw(batch);

    }
}
