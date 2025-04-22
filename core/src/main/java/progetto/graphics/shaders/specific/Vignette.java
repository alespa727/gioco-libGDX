package progetto.graphics.shaders.specific;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import progetto.gameplay.player.ManagerCamera;
import progetto.graphics.shaders.base.Shader;

public class Vignette extends Shader {
    private static Vignette instance;

    private Vignette() {
        // Carica e crea il programma shader (per effetti grafici avanzati)
        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        String vertexShader = Gdx.files.internal("shaders/vignette/vertex.vsh").readString();
        String fragmentShader = Gdx.files.internal("shaders/vignette/fragment.fsh").readString();
        this.program = new ShaderProgram(vertexShader, fragmentShader);
        if (!program.isCompiled()) {
            System.out.println(program.getLog());
        }
        ShaderProgram.pedantic = false; // se vuoi evitare errori per uniform "extra"
    }

    public static Vignette getInstance() {
        if (instance == null) {
            instance = new Vignette();
        }
        return instance;
    }

    @Override
    public void begin() {
        frameBuffer.begin();
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
        batch.begin();
        batch.setShader(program);
        batch.draw(region, ManagerCamera.getFrustumCorners()[0].x, ManagerCamera.getFrustumCorners()[0].y, ManagerCamera.getViewportWidth(), ManagerCamera.getViewportHeight());
        batch.setShader(null);
        batch.end();
    }

}
