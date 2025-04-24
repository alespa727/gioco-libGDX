package progetto.graphics.shaders.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public abstract class Shader {
    protected ShaderProgram program;
    protected FrameBuffer frameBuffer;
    private boolean active = false;

    public abstract void begin();

    public abstract void end();

    public abstract void draw(SpriteBatch batch);

    public abstract void draw(SpriteBatch batch, float x, float y, float width, float height);


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
