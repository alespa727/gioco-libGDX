package io.github.ale;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player {

    private float worldX;
    private float worldY;

    private Direzione direzione;

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

    private float elapsedTime;

    float speed = 4f;
    float delta;
    boolean isAlive;

    // Costruttore
    public Player() {
        setDefaultValues();
    }

    public void update() {
        input();
        checkIfKilled();
        checkIfRevived();
        checkIfDead();
        checkIfAlive();
    }

    public void input() {
        delta = Gdx.graphics.getDeltaTime();

        gestioneTasti();
    }


    public void draw(SpriteBatch batch) {
        elapsedTime += Gdx.graphics.getDeltaTime();

        // Se la direzione è W (su), usiamo i frame relativi
        if (direzione.getDirezione().equals("W")) {
            animation = new Animation<>(1f / 4f, movingUp);
        } else if (direzione.getDirezione().equals("S")) {
            animation = new Animation<>(1f / 4f, movingDown);
        //} else if (direzione.getDirezione().equals("A")) {
        //    animation = new Animation<>(1f / 2f, playerFramesLeft);
        } else if (direzione.getDirezione().equals("D")) {
            animation = new Animation<>(1f / 4f, movingRight);
        }else if (direzione.getDirezione().equals("fermoW")) {
            animation = new Animation<>(1f / 4f, up);
        }else if (direzione.getDirezione().equals("fermoS")) {
            animation = new Animation<>(1f / 4f, down);
        }else if (direzione.getDirezione().equals("fermoA")) {
            animation = new Animation<>(1f / 4f, left);
        }else if (direzione.getDirezione().equals("fermoD")) {
            animation = new Animation<>(1f / 4f, right);
        }

        // Disegnamo l'animazione
        batch.draw(animation.getKeyFrame(elapsedTime, true), worldX, worldY, 2, 2);
    }

    // Inizializza le variabili, carica i frame
    private void setDefaultValues() {
        isAlive = true;
        player = new Texture("Finn.png");

        direzione = new Direzione();

        direzione.setDirezione("S");
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
            //playerFramesLeft[i] = tmpFrames[2][i];
            // Carica i frame per la direzione destra
            movingRight[i] = tmpFrames[4][i];
            if(i<2){
                up[i]  = tmpFrames[2][i];
                down[i]  = tmpFrames[0][i];
                //left[i]  = tmpFrames[1][i];
                right[i]  = tmpFrames[1][i];
            }
            
        }

        animation = new Animation<>(1f / 4f, movingDown); 
    }

    public void gestioneTasti(){ //creare una classe a parte
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            worldX += speed * delta;
            direzione.setDirezione("D");
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            worldX -= speed * delta;
            direzione.setDirezione("A");
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            worldY += speed * delta;
            direzione.setDirezione("W");
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            worldY -= speed * delta;
            direzione.setDirezione("S");
        }
        if (!Gdx.input.isKeyPressed(Input.Keys.S) && !Gdx.input.isKeyPressed(Input.Keys.W) && !Gdx.input.isKeyPressed(Input.Keys.D) && !Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (direzione.getDirezione().equals("W")) {
                direzione.setDirezione("fermoW");
            } else if (direzione.getDirezione().equals("S")) {
                direzione.setDirezione("fermoS");
            //} else if (direzione.getDirezione().equals("A")) {
            //    animation = new Animation<>(1f / 2f, playerFramesLeft);
            } else if (direzione.getDirezione().equals("D")) {
                direzione.setDirezione("fermoD");
            }else{
                direzione.setDirezione("fermoS");
            }
        }
    }

    public void checkIfKilled() {
        // Logica per controllare se il giocatore è stato ucciso
    }

    public void checkIfRevived() {
        // Logica per controllare se il giocatore è stato ripristinato
    }

    public void checkIfDead() {
        // Logica per controllare se il giocatore è morto
    }

    public void checkIfAlive() {
        // Logica per controllare se il giocatore è vivo
    }

    // Getters
    public float getWorldX() {
        return worldX;
    }

    public float getWorldY() {
        return worldY;
    }
}
