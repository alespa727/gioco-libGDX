package progetto.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import progetto.graphics.shaders.base.Shader;


public class ScreenRenderer {
    private final Array<Shader> shaders;
    private final CustomScreen screen;

    public ScreenRenderer(CustomScreen screen) {
        shaders = new Array<>();
        this.screen = screen;
    }

    public void addShader(Shader shader) {
        shaders.add(shader);
    }

    public void removeShaders(Shader shader) {
        shaders.removeValue(shader, false);
    }

    public void draw(SpriteBatch batch, float delta) {

        if (shaders.size <= 0) {
            screen.draw(delta);
            return;
        }

        shaders.get(0).begin();
        screen.draw(delta);
        shaders.get(0).end();

        for (int i = 1; i < shaders.size; i++) {
            Shader input = shaders.get(i - 1);
            Shader output = shaders.get(i);
            output.begin();
            input.draw(batch, 0, 0, 1600, 900);
            output.end();
        }

        Shader finalOutput = shaders.get(shaders.size - 1);
        finalOutput.draw(batch, 0, 0, 1600, 900);
    }

    public void draw(SpriteBatch batch, float delta, int x, int y, int width, int height) {

        if (shaders.size <= 0) {
            screen.draw(delta);
            return;
        }

        shaders.get(0).begin();
        screen.draw(delta);
        shaders.get(0).end();

        for (int i = 1; i < shaders.size; i++) {
            Shader input = shaders.get(i - 1);
            Shader output = shaders.get(i);
            output.begin();
            input.draw(batch, x, y, width, height);
            output.end();
        }

        Shader finalOutput = shaders.get(shaders.size - 1);
        finalOutput.draw(batch, x, y, width, height);
    }
}
