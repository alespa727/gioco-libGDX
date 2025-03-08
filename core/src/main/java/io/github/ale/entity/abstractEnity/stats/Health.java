package io.github.ale.entity.abstractEnity.stats;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Health {
    private float hp;
    private final Texture vita;
    public Health(float hp){
        this.hp = hp;
        vita = new Texture("healthbar.png");
    }

    public float getHp(){
        return hp;
    }

    public void setHp(float hp){
        this.hp = hp;
    }

    /**
     * stampa barra della vita
     * @param hud
     */
    public void draw(SpriteBatch hud){
        hud.draw(vita, 16, 512, 32*18, 32*6);
    }

}
