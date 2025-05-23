package progetto.graphics.shaders.specific;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import progetto.core.settings.model.ModelImpostazioni;
import progetto.graphics.shaders.base.Shader;
import progetto.core.CameraManager;

public class ColorFilter extends Shader {
    private static ColorFilter instance;

    public final Color color;

    public ColorFilter() {
        // Carica e crea il programma shader (per effetti grafici avanzati)
        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        String vertexShader = Gdx.files.internal("shaders/darken/vertex.glsl").readString();
        String fragmentShader = Gdx.files.internal("shaders/darken/fragment.glsl").readString();
        this.program = new ShaderProgram(vertexShader, fragmentShader);
        ShaderProgram.pedantic = false; // se vuoi evitare errori per uniform "extra"
        color = new Color(0.6f, 0.8f, 1.0f, 1.0f);
    }

    public static ColorFilter getInstance() {
        if (instance == null) {
            instance = new ColorFilter();
        }
        return instance;
    }

    public static ColorFilter getInstance(float r, float g, float b) {
        if (instance == null) {
            instance = new ColorFilter();
        }
        instance.color.set(r, g, b, 1);
        return instance;
    }

    public static void setColor(float r, float g, float b) {
        instance.color.set(r, g, b, 1);
    }

    public static void setColor(Color color) {
        instance.color.set(color);
    }

    public static Color getColor() {
        return instance.color;
    }
    @Override
    public void begin() {
        frameBuffer.begin();
    }

    public void begin(Color color) {
        frameBuffer.begin();
        this.color.set(color);
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

        float multiplier = ModelImpostazioni.getLUMINOSITA().getPosizione();
        program.bind();
        batch.setShader(program);                            // (1) assegna lo shader
        program.setUniformf("u_tint", color.r*multiplier, color.g*multiplier, color.b*multiplier);
        batch.begin();                                       // (3) inizia il batch
        batch.draw(region,
            CameraManager.getFrustumCorners()[0].x,
            CameraManager.getFrustumCorners()[0].y,
            CameraManager.getViewportWidth(),
            CameraManager.getViewportHeight());
        batch.end();                                         // (4) finisci il batch
        batch.setShader(null);                               // (5) resetta lo shader (opzionale)
    }

    @Override
    public void draw(SpriteBatch batch, float x, float y, float width, float height) {

        Texture texture = frameBuffer.getColorBufferTexture();
        TextureRegion region = new TextureRegion(texture);
        region.flip(false, true);

        float multiplier = Math.max(0.2f, ModelImpostazioni.getLUMINOSITA().getPosizione());

        program.bind();
        batch.setShader(program);                            // (1) assegna lo shader
        program.setUniformf("u_tint", color.r*multiplier, color.g*multiplier, color.b*multiplier);
        batch.begin();                                       // (3) inizia il batch
        batch.draw(region, x, y, width, height);
        batch.end();                                         // (4) finisci il batch
        batch.setShader(null);                               // (5) resetta lo shader (opzionale)
    }

}
