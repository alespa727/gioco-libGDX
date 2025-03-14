package io.github.ale.screens.gameScreen.interfaces;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.ale.screens.gameScreen.entity.abstractEntity.caratteristiche.Dimensioni;
import io.github.ale.screens.gameScreen.entity.abstractEntity.stats.Stats;

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
        if(statistiche().gotDamaged){
            batch.setColor(1, 0, 0, 0.6f);
        }
        batch.draw(getAnimazione().getKeyFrame(elapsedTime, true), getX(), getY(), getSize().getWidth(),
               getSize().getHeight());
        batch.setColor(Color.WHITE);
    }

    public Stats statistiche();
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
