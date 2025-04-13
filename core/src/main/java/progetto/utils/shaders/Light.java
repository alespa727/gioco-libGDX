package progetto.utils.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import progetto.gameplay.manager.ManagerCamera;

public class Light extends Shader{

    private static Light instance;
    private final Vector2 position;

    private Light() {
        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        String vertexShader = Gdx.files.internal("shaders/light/vertex.glsl").readString();
        String fragmentShader = Gdx.files.internal("shaders/light/fragment.glsl").readString();
        this.program = new ShaderProgram(vertexShader, fragmentShader);
        ShaderProgram.pedantic = false; // se vuoi evitare errori per uniform "extra"
        position = new Vector2(0.5f, 0.5f);
    }

    public static Light getInstance() {
        if (instance == null) {
            instance = new Light();
        }
        return instance;
    }

    @Override
    public void begin() {
        frameBuffer.begin();
    }

    public void begin(Vector3 position) {
        frameBuffer.begin();
        float normX = position.x / Gdx.graphics.getWidth();
        float normY = position.y / Gdx.graphics.getHeight();
        this.position.set(normX, normY);
    }

    @Override
    public void end() {
        frameBuffer.end();
    }

    @Override
    public void draw(SpriteBatch batch) {
        Texture texture = frameBuffer.getColorBufferTexture();
        TextureRegion region = new TextureRegion(texture);
        region.flip(false, true);

        program.bind();
        batch.setShader(program);
        program.setUniformf("u_resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());// (1) assegna lo shader
        program.setUniformf("u_lightPos", position.x, position.y);
        program.setUniformf("u_lightRadius", 0.5f);
        program.setUniformf("u_lightColor", Color.ORANGE.cpy().mul(0.5f));
        batch.begin();                                       // (3) inizia il batch
        batch.draw(region,
            ManagerCamera.getFrustumCorners()[0].x,
            ManagerCamera.getFrustumCorners()[0].y,
            ManagerCamera.getViewportWidth(),
            ManagerCamera.getViewportHeight());
        batch.end();                                         // (4) finisci il batch
        batch.setShader(null);
    }
}
