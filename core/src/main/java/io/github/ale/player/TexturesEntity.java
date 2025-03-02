package io.github.ale.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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
    private Animation<TextureRegion> animation;

    public TexturesEntity(String path){
        entity = new Texture(path);
        TextureRegion[][] tmpFrames = TextureRegion.split(entity, 32, 32);

        // Assegna i frame per le diverse direzioni, creare una classe che puo contenere le texture
        movingUp = new TextureRegion[4];
        movingDown = new TextureRegion[4];
        movingLeft = new TextureRegion[4];
        movingRight = new TextureRegion[4];

        up = new TextureRegion[2];
        down = new TextureRegion[2];
        left = new TextureRegion[2];
        right = new TextureRegion[2];

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

    public TextureRegion[] getMovingUp() {
        return movingUp;
    }
    
    /**
     * restituisce la animazione corretta a seconda della direzione dell'entità
     * @param direzione
     * @return
     */

    public Animation<TextureRegion> setAnimazione(Direzione direzione) {
        switch (direzione.getDirezione()) {
            case "W" -> animation = new Animation<>(1f / 8f, movingUp);
            case "S" -> animation = new Animation<>(1f / 8f, movingDown);
            case "A" -> animation = new Animation<>(1f / 8f, movingLeft);
            case "D" -> animation = new Animation<>(1f / 8f, movingRight);
            case "fermoW" -> animation = new Animation<>(1f / 2f, up);
            case "fermoS" -> animation = new Animation<>(1f / 2f, down);
            case "fermoA" -> animation = new Animation<>(1f / 2f, left);
            case "fermoD" -> animation = new Animation<>(1f / 2f, right);
            default -> {
            }
            
        }
        return animation;
    }
}
