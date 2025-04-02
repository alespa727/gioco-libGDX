package io.github.ale.screens.game.entities.skills;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.ale.Game;
import io.github.ale.cooldown.Cooldown;
import io.github.ale.screens.game.entities.types.mobs.LivingEntity;
import org.w3c.dom.Text;

public abstract class Skill {

    protected Texture[] texture;
    protected TextureRegion[] textureRegion;
    protected Animation<TextureRegion> animation;

    public final String name;
    public final String description;
    protected LivingEntity entity;

    protected boolean isBeingUsed = false;
    protected Cooldown cooldown;

    protected float elapsedTime;

    public Skill(LivingEntity entity, String name, String description) {
        this.entity = entity;
        this.name = name;
        this.description = description;
    }

    public boolean isBeingUsed() {
        return isBeingUsed;
    }

    public void loadTexture(String path, int frames, int framesPerSecond) {
        texture = new Texture[frames];
        textureRegion = new TextureRegion[frames];
        for (int i = 0; i < texture.length; i++) {
            texture[i] = new Texture(path+(i+1)+".png");
            System.out.println(path+(i+1)+".png");
        }

        for (int i = 0; i < frames; i++) {
            textureRegion[i] = new TextureRegion(texture[i]);
        }
        animation = new Animation<>(1f/framesPerSecond, this.textureRegion);
        cooldown = new Cooldown((float) frames/framesPerSecond);
        Animation.PlayMode loop = Animation.PlayMode.LOOP;
        animation.setPlayMode(loop);
    }

    public Animation<TextureRegion> getAnimation() {
        return animation;
    }

    public void setBeingUsed(boolean isBeingUsed) {
        this.isBeingUsed = isBeingUsed;
    }

    public abstract void update();
    public abstract void draw(SpriteBatch batch);
    public abstract void execute();
}
