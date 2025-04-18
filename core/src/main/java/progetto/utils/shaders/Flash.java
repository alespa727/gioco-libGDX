package progetto.utils.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Flash{
    private static Flash instance;
    private final ShaderProgram program;

    private Flash() {
        String vertexShader = Gdx.files.internal("shaders/flash/vertex.vsh").readString();
        String fragmentShader = Gdx.files.internal("shaders/flash/fragment.fsh").readString();
        this.program = new ShaderProgram(vertexShader, fragmentShader);
    }

    public static Flash getInstance() {
        if (instance == null) {
            instance = new Flash();
        }
        return instance;
    }

    public void apply(SpriteBatch batch) {
        if (!program.isCompiled()) {
            throw new IllegalArgumentException("Errore nello shader: " + program.getLog());
        }
        batch.setShader(program);
    }

    public void apply(SpriteBatch batch, Color color){
        batch.setShader(program);
        program.setUniformf("u_color", color);
    }

    public void end(SpriteBatch batch) {
        if (!program.isCompiled()) {
            throw new IllegalArgumentException("Errore nello shader: " + program.getLog());
        }
        batch.setShader(null);
    }

    public ShaderProgram getShader() {
        if (!program.isCompiled()) {
            throw new IllegalArgumentException("Errore nello shader: " + program.getLog());
        }
        return program;
    }

}
