package progetto.graphics.shaders.specific;

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
import progetto.entity.components.specific.base.PhysicsComponent;
import progetto.entity.entities.Entity;
import progetto.graphics.shaders.base.Shader;
import progetto.player.ManagerCamera;

public class EntityLight extends Shader {

    private Vector2 worldPosition = null;
    private final Vector2 position;
    private final Color color;
    private Entity e=null;
    private float intensity;

    public EntityLight(Entity e, float intensity, Color color) {
        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        String vertexShader = Gdx.files.internal("shaders/light/vertex.glsl").readString();
        String fragmentShader = Gdx.files.internal("shaders/light/fragment.glsl").readString();
        this.program = new ShaderProgram(vertexShader, fragmentShader);

        ShaderProgram.pedantic = false; // se vuoi evitare errori per uniform "extra"
        this.intensity = intensity;
        position = new Vector2(0.5f, 0.5f);
        this.e = e;
        this.color = color;
    }

    public EntityLight(Vector2 pos, float intensity, Color color) {
        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        String vertexShader = Gdx.files.internal("shaders/light/vertex.glsl").readString();
        String fragmentShader = Gdx.files.internal("shaders/light/fragment.glsl").readString();
        this.program = new ShaderProgram(vertexShader, fragmentShader);

        ShaderProgram.pedantic = false; // se vuoi evitare errori per uniform "extra"
        this.intensity = intensity;
        position = new Vector2(0.5f, 0.5f);
        worldPosition = pos;
        this.color = color;
    }

    @Override
    public void begin() {
        if (e != null) {
            if (!e.contains(PhysicsComponent.class)) {
                return;
            }
            frameBuffer.begin();
            Vector3 position = new Vector3(e.get(PhysicsComponent.class).getPosition(), 0);
            Vector3 projectedPosition = ManagerCamera.getInstance().project(position);
            float normX = projectedPosition.x / Gdx.graphics.getWidth();
            float normY = projectedPosition.y / Gdx.graphics.getHeight();
            this.position.set(normX, normY);
        }else if (worldPosition != null){
            frameBuffer.begin();
            Vector3 position = new Vector3(worldPosition, 0);
            Vector3 projectedPosition = ManagerCamera.getInstance().project(position);
            float normX = projectedPosition.x / Gdx.graphics.getWidth();
            float normY = projectedPosition.y / Gdx.graphics.getHeight();
            this.position.set(normX, normY);
        }

    }

    public void begin(Vector3 position, float intensity) {
        if (!e.contains(PhysicsComponent.class)) {
            return;
        }
        frameBuffer.begin();
        float normX = position.x / Gdx.graphics.getWidth();
        float normY = position.y / Gdx.graphics.getHeight();
        this.position.set(normX, normY);
        this.intensity = intensity;
    }

    @Override
    public void end() {
        if (!e.contains(PhysicsComponent.class)) {
            return;
        }
        frameBuffer.end();
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (!e.contains(PhysicsComponent.class)) {
            return;
        }
        Texture texture = frameBuffer.getColorBufferTexture();
        TextureRegion region = new TextureRegion(texture);
        region.flip(false, true);

        program.bind();
        batch.setShader(program);
        program.setUniformf("u_resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());// (1) assegna lo shader
        program.setUniformf("u_lightPos", position.x, position.y);
        program.setUniformf("u_lightRadius", 0.5f);
        program.setUniformf("u_lightIntensity", intensity);
        program.setUniformf("u_lightColor", color);
        batch.begin();                                       // (3) inizia il batch
        batch.draw(region,
            ManagerCamera.getFrustumCorners()[0].x,
            ManagerCamera.getFrustumCorners()[0].y,
            ManagerCamera.getViewportWidth(),
            ManagerCamera.getViewportHeight());
        batch.end();                                         // (4) finisci il batch
        batch.setShader(null);
    }

    @Override
    public void draw(SpriteBatch batch, float x, float y, float width, float height) {
        Texture texture = frameBuffer.getColorBufferTexture();
        TextureRegion region = new TextureRegion(texture);
        region.flip(false, true);

        program.bind();
        batch.setShader(program);
        program.setUniformf("u_resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());// (1) assegna lo shader
        program.setUniformf("u_lightPos", position.x, position.y);
        program.setUniformf("u_lightRadius", 0.5f);
        program.setUniformf("u_lightIntensity", intensity);
        program.setUniformf("u_lightColor", Color.WHITE.cpy());
        batch.begin();                                       // (3) inizia il batch
        batch.draw(region, x, y, width, height);
        batch.end();                                         // (4) finisci il batch
        batch.setShader(null);
    }
}
