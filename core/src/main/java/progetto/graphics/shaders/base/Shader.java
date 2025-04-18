package progetto.graphics.shaders.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public abstract class Shader{
    private boolean active=false;

    protected ShaderProgram program;
    protected FrameBuffer frameBuffer;

    public abstract void begin();
    public abstract void end();

    public abstract void draw(SpriteBatch batch);


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
