package io.github.ale;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TexturesPlayer {
    private Texture player;
    private TextureRegion[] movingUp; // Frame per la direzione su
    private TextureRegion[] movingDown; // Frame per la direzione giù
    private TextureRegion[] movingLeft; // Frame per la direzione sinistra
    private TextureRegion[] movingRight; // Frame per la direzione destra
    private TextureRegion[] up; // Frame per la direzione su fermo
    private TextureRegion[] down; // Frame per la direzione giù fermo
    private TextureRegion[] left; // Frame per la direzione sinistra fermo
    private TextureRegion[] right; // Frame per la direzione destra fermo
    private Animation<TextureRegion> animation;
    public TexturesPlayer(String path){
        player = new Texture(path);
        TextureRegion[][] tmpFrames = TextureRegion.split(player, 32, 32);

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
