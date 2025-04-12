package progetto.utils.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import progetto.gameplay.manager.ManagerCamera;
import progetto.utils.Vignette;

public class Darken {
    private static Darken instance;
    private final ShaderProgram program;
    private final FrameBuffer frameBuffer;

    public static Darken getInstance() {
        if (instance == null) {
            instance = new Darken();
        }
        return instance;
    }

    private Darken() {
        // Carica e crea il programma shader (per effetti grafici avanzati)
        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        String vertexShader = Gdx.files.internal("shaders/darken/vertex.vsh").readString();
        String fragmentShader = Gdx.files.internal("shaders/darken/fragment.fsh").readString();
        this.program = new ShaderProgram(vertexShader, fragmentShader);
        ShaderProgram.pedantic = true; // se vuoi evitare errori per uniform "extra"
    }

    public void begin(){
        frameBuffer.begin();
    }

    public void end(){
        frameBuffer.end();
    }

    public void draw(SpriteBatch batch) {

        Texture texture = frameBuffer.getColorBufferTexture();
        TextureRegion region = new TextureRegion(texture);
        region.flip(false, true);

        batch.setShader(program);                            // (1) assegna lo shader

        batch.begin();                                       // (3) inizia il batch
        batch.draw(region,
            ManagerCamera.getFrustumCorners()[0].x,
            ManagerCamera.getFrustumCorners()[0].y,
            ManagerCamera.getViewportWidth(),
            ManagerCamera.getViewportHeight());
        batch.end();                                         // (4) finisci il batch
        batch.setShader(null);                               // (5) resetta lo shader (opzionale)
    }


}
