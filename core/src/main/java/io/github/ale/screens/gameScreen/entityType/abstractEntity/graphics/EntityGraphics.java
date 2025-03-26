package io.github.ale.screens.gameScreen.entityType.abstractEntity.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.github.ale.screens.gameScreen.entityType.abstractEntity.Entity;
import io.github.ale.screens.gameScreen.entityType.abstractEntity.texture.TexturesEntity;

public class EntityGraphics {
    private TexturesEntity texture;
    private Animation<TextureRegion> animation;

    public TexturesEntity getTexture(){
        return texture;
    }

    public void setTexture(Texture img){
        texture = new TexturesEntity(img);
    }

    /**
     * setta l'animazione attuale da utilizzare
     */

    public void setAnimation(Entity entity) {
         if (animation == null || animation != texture.setAnimazione(entity.direzione())) {
             animation = texture.setAnimazione(entity.direzione());
         }
    }

     public void inizializzaAnimazione(Entity entity) {
         animation = getTexture().setAnimazione(entity.direzione());
     }

     public Animation<TextureRegion> getAnimazione() {
        return animation;
    }

}
