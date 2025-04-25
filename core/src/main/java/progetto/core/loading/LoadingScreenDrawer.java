package progetto.core.loading;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import progetto.core.game.TextDrawer;
import progetto.graphics.shaders.base.Shader;


public class LoadingScreenDrawer{
    private final TextDrawer textDrawer;
    private final Array<Shader> shaders;
    private final LoadingScreen screen;

    public LoadingScreenDrawer(LoadingScreen screen) {
        shaders = new Array<>();
        this.screen = screen;
        this.textDrawer = new TextDrawer();
    }

    public void addShader(Shader shader) {
        shaders.add(shader);
    }

    public void removeShaders(Shader shader) {
        shaders.removeValue(shader, false);
    }

    public void draw(SpriteBatch batch) {

        if (shaders.size == 0) {
            screen.draw();
            return;
        }

        shaders.get(0).begin();
        screen.draw();
        shaders.get(0).end();

        for (int i = 1; i < shaders.size; i++) {
            Shader input = shaders.get(i - 1);
            Shader output = shaders.get(i);
            output.begin();
            input.draw(batch, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            output.end();
        }

        Shader finalOutput = shaders.get(shaders.size - 1);
        finalOutput.draw(batch, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    }
}
