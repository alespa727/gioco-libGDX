package progetto.core.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import progetto.graphics.shaders.base.Shader;

public class GameDrawer {
    private final Array<Shader> shaders;
    private final GameScreen game;

    public GameDrawer(GameScreen game) {
        shaders = new Array<>();
        this.game = game;

    }

    public void addShader(Shader shader) {
        shaders.add(shader);
    }

    public void removeShaders(Shader shader) {
        shaders.removeValue(shader, false);
    }

    public void draw(SpriteBatch batch) {

        shaders.get(0).begin();
        game.draw();
        shaders.get(0).end();

        for (int i = 1; i < shaders.size; i++) {
            Shader input = shaders.get(i-1);
            Shader output = shaders.get(i);
            output.begin();
            input.draw(batch);
            output.end();
        }

        Shader finalOutput = shaders.get(shaders.size-1);
        finalOutput.draw(batch);

    }
}
