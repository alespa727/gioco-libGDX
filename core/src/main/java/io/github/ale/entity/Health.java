package io.github.ale.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Health {
    private float hp;
    private Texture vita;
    public Health(int hp){
        this.hp = hp;
        vita = new Texture("healthbar.png");
    }

    public float getHp(){
        return hp;
    }

    public void setHp(float hp){
        this.hp = hp;
    }

    public void draw(SpriteBatch hud){
        hud.draw(vita, 16, 512, 32*18, 32*6);
    }

}
