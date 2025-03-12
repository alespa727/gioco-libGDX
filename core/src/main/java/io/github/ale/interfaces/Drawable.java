package io.github.ale.interfaces;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.ale.entity.abstractEntity.caratteristiche.Dimensioni;

public interface Drawable {

    /**
     * disegna il nemico
     * 
     * @param batch
     * @param elapsedTime
     */
    default void draw(SpriteBatch batch, float elapsedTime) {
        elapsedTime += Gdx.graphics.getDeltaTime();

        setAnimation();

        batch.draw(getAnimazione().getKeyFrame(elapsedTime, true), getX(), getY(), getSize().getWidth(),
                getSize().getHeight());
    }

    /**
     * disegna la hitbox
     * @param renderer
     */
    public abstract void drawHitbox(ShapeRenderer renderer);

    public float getX();

    public void setAnimation();

    public Animation<TextureRegion> getAnimazione();

    public Dimensioni getSize();

    public float getY();
}
