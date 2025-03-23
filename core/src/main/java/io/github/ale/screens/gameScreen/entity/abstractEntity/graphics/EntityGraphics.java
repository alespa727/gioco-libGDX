package io.github.ale.screens.gameScreen.entitytypes.abstractEntity.graphics;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.github.ale.screens.gameScreen.entitytypes.abstractEntity.Entity;
import io.github.ale.screens.gameScreen.entitytypes.abstractEntity.texture.TexturesEntity;

public class EntityGraphics {
    private TexturesEntity texture;
    private Animation<TextureRegion> animation;

    public TexturesEntity getTexture(){
        return texture;
    }

    public void setTexture(String path){
        texture = new TexturesEntity(path);
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
