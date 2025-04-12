package progetto.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import org.w3c.dom.Text;
import progetto.gameplay.manager.ManagerCamera;

public class Vignette {
    private static Vignette instance;
    private ShaderProgram program;
    private FrameBuffer frameBuffer;

    public static Vignette getInstance() {
        if (instance == null) {
            instance = new Vignette();
        }
        return instance;
    }

    private Vignette() {
        // Carica e crea il programma shader (per effetti grafici avanzati)
        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        String vertexShader = Gdx.files.internal("shaders/vertex.vsh").readString();
        String fragmentShader = Gdx.files.internal("shaders/vignette.fsh").readString();
        this.program = new ShaderProgram(vertexShader, fragmentShader);
        ShaderProgram.pedantic = false; // se vuoi evitare errori per uniform "extra"
    }

    public void begin(){
        frameBuffer.begin();
    }

    public void end(){
        frameBuffer.end();
    }

    public void draw(SpriteBatch batch) {
        program.setUniformf("resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
