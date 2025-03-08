package io.github.ale.entity.abstractEnity.texture;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ObjectMap;

public class TexturesEntity {
    private final Texture entity;
    private final TextureRegion[] movingUp; // Frame per la direzione su
    private final TextureRegion[] movingDown; // Frame per la direzione giù
    private final TextureRegion[] movingLeft; // Frame per la direzione sinistra
    private final TextureRegion[] movingRight; // Frame per la direzione destra
    private final TextureRegion[] up; // Frame per la direzione su fermo
    private final TextureRegion[] down; // Frame per la direzione giù fermo
    private final TextureRegion[] left; // Frame per la direzione sinistra fermo
    private final TextureRegion[] right; // Frame per la direzione destra fermo
    
    private final ObjectMap<String, Animation <TextureRegion>> animations;

    public TexturesEntity(String path){
        entity = new Texture(path);
        animations = new ObjectMap<>();

        // Assegna i frame per le diverse direzioni, creare una classe che puo contenere le texture
        movingUp = new TextureRegion[4];
        movingDown = new TextureRegion[4];
        movingLeft = new TextureRegion[4];
        movingRight = new TextureRegion[4];

        up = new TextureRegion[2];
        down = new TextureRegion[2];
        left = new TextureRegion[2];
        right = new TextureRegion[2];

        salvaAnimazioni(entity);

        animations.put("W", new Animation<>(1 / 5f, movingUp));
        animations.put("S", new Animation<>(1 / 5f, movingDown));
        animations.put("A", new Animation<>(1 / 5f, movingLeft));
        animations.put("D", new Animation<>(1 / 5f, movingRight));
        animations.put("fermoW", new Animation<>(1 / 2f, up));
        animations.put("fermoS", new Animation<>(1 / 2f, down));
        animations.put("fermoA", new Animation<>(1 / 2f, left));
        animations.put("fermoD", new Animation<>(1 / 2f, right));
        
    }

    /**
     * salva le varie animazioni per ogni direzione
     * @param entity
     */
    private void salvaAnimazioni(Texture entity){
        
        TextureRegion[][] tmpFrames = TextureRegion.split(entity, 32, 32);
        for (int i = 0; i < 4; i++) {
            // Carica i frame per la direzione su
            movingUp[i] = tmpFrames[5][i];
            // Carica i frame per la direzione giù
            movingDown[i] = tmpFrames[3][i];
            // Carica i frame per la direzione sinistra
            movingLeft[i] = new TextureRegion(tmpFrames[4][i]);
            movingLeft[i].flip(true, false);
            // Carica i frame per la direzione destra
            movingRight[i] = tmpFrames[4][i];
        }
        for (int i = 0; i < 2; i++) {
            
            up[i]  = tmpFrames[2][i];
            down[i]  = tmpFrames[0][i];
            right[i]  = tmpFrames[1][i];
            left[i]  = new TextureRegion(tmpFrames[1][i]);
            left[i].flip(true, false);
            
            
        }
    }

    /**
     * restituisce la animazione corretta a seconda della direzione dell'entità
     * @param direzione
     * @return
     */

    public Animation<TextureRegion> setAnimazione(String direzione) {
        return animations.get(direzione);
    }
}
