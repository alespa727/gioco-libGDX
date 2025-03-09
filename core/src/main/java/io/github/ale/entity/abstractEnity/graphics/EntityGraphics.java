package io.github.ale.entity.abstractEnity.graphics;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.github.ale.entity.abstractEnity.Entity;
import io.github.ale.entity.abstractEnity.texture.TexturesEntity;

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
         if (animation == null || animation != texture.setAnimazione(entity.getDirezione())) {
             animation = texture.setAnimazione(entity.getDirezione());
         }
    }
 
     public void inizializzaAnimazione(Entity entity) {
         animation = getTexture().setAnimazione(entity.getDirezione());
     }

     public Animation<TextureRegion> getAnimazione() {
        return animation;
    }

}
