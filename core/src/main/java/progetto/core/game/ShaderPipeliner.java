package progetto.core.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import progetto.entity.components.specific.base.PhysicsComponent;
import progetto.graphics.shaders.base.Shader;
import progetto.player.ManagerCamera;
import progetto.player.Player;

public class ShaderPipeliner {
    private final TextDrawer textDrawer;
    private final Array<Shader> shaders;
    private final GameScreen game;

    public ShaderPipeliner(GameScreen game) {
        shaders = new Array<>();
        this.game = game;
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
            game.getRenderer().draw();
            return;
        }

        shaders.get(0).begin();
        game.getRenderer().draw();
        shaders.get(0).end();

        for (int i = 1; i < shaders.size; i++) {
            Shader input = shaders.get(i - 1);
            Shader output = shaders.get(i);
            output.begin();
            input.draw(batch);
            output.end();
        }

        Shader finalOutput = shaders.get(shaders.size - 1);
        finalOutput.draw(batch);

        Player player = game.getEntityManager().player();
        if (!player.contains(PhysicsComponent.class)) {
            return;
        }
        Vector3 position = ManagerCamera.getInstance().project(new Vector3(player.get(PhysicsComponent.class).getPosition(), 0));

        textDrawer.drawCenteredText(position.x, position.y+100);
    }
}
