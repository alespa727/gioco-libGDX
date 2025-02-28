package io.github.ale;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Player {

    Rectangle hitbox;

    private float worldX;
    private float worldY;

    private Direzione direzione;

    TexturesPlayer player;

    ShapeRenderer shape;

    private Animation<TextureRegion> animation;

    private float elapsedTime;

    float speed = 2.5f;
    float delta;
    boolean isAlive;

    // Costruttore
    public Player() {
        setDefaultValues();
    }

    public void update() {
        input();
        hitbox.x = worldX+0.65f;
        hitbox.y = worldY+0.5f;
        checkIfKilled();
        checkIfRevived();
        checkIfDead();
        checkIfAlive();
    }

    public void input() {
        delta = Gdx.graphics.getDeltaTime();

        gestioneTasti();
    }


    public void setAnimation(){
        // Se la direzione è W (su), usiamo i frame relativi
        animation = player.setAnimazione(direzione);
    }


    public void draw(SpriteBatch batch) {
        elapsedTime += Gdx.graphics.getDeltaTime();

        setAnimation();

        
        worldX = MathUtils.clamp(worldX, 0-0.7f, Map.getWidth()-1.3f);
        worldY = MathUtils.clamp(worldY, 0-0.5f, Map.getHeight()-1.5f);
       
        // Disegnamo l'animazione
        
        batch.draw(animation.getKeyFrame(elapsedTime, true), worldX, worldY, 2, 2);
    }

    // Inizializza le variabili, carica i frame
    private void setDefaultValues() {
        isAlive = true;
        worldX=5f;
        worldY=5f;

        player = new TexturesPlayer("Finn.png");

        hitbox = new Rectangle(getWorldX(), getWorldY(), 0.65f, 0.5f);

        direzione = new Direzione();
        
        direzione.setDirezione("S");
        animation = player.setAnimazione(direzione);
    }

    public void gestioneTasti(){ //creare una classe a parte

        nothingPressed();

        everythingPressed();


    }



    public void nothingPressed(){
        if(!Gdx.input.isKeyPressed(Input.Keys.S) && !Gdx.input.isKeyPressed(Input.Keys.W) && !Gdx.input.isKeyPressed(Input.Keys.D) && !Gdx.input.isKeyPressed(Input.Keys.A)){
            switch (direzione.getDirezione()) {
                case "W" -> direzione.setDirezione("fermoW");
                case "S" -> direzione.setDirezione("fermoS");
                //} else if (direzione.getDirezione().equals("A")) {
                //    animation = new Animation<>(1f / 2f, playerFramesLeft);
                case "A" -> direzione.setDirezione("fermoA");
                case "D" -> direzione.setDirezione("fermoD");
                default -> {
                }
            }
        }
    }

    public void everythingPressed(){
        if (Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.D) && Gdx.input.isKeyPressed(Input.Keys.A)) {
            direzione.setDirezione("fermoS");
        }else{
            if (upPressed() && downPressed()) {
                updownPressed();
            }else if(leftPressed() && rightPressed()){
                leftrightPressed();
            }else{
            
                if (upPressed()) {
                    worldY += speed * delta;
                    direzione.setDirezione("W");
                }
                if (downPressed()) {
                    worldY -= speed * delta;
                    direzione.setDirezione("S");
                }
    
                if (rightPressed()) {
                    worldX += speed * delta;
                    direzione.setDirezione("D");
                }
                if (leftPressed()) {
                    worldX -= speed * delta;
                    direzione.setDirezione("A");
                }
            }
        }
        
    }

    public boolean rightPressed(){
        return Gdx.input.isKeyPressed(Input.Keys.D);
    }
    public boolean leftPressed(){
        return Gdx.input.isKeyPressed(Input.Keys.A);
    }
    public boolean upPressed(){
        return Gdx.input.isKeyPressed(Input.Keys.W);
    }
    public boolean downPressed(){
        return Gdx.input.isKeyPressed(Input.Keys.S);
    }

    public void leftrightPressed(){
        
        if (direzione.getDirezione().equals("D")) {
            direzione.setDirezione("fermoD");
        }else if (direzione.getDirezione().equals("A")) {
            direzione.setDirezione("fermoA");
        }

    }

    public void updownPressed(){
        
        if (direzione.getDirezione().equals("W")) {
            direzione.setDirezione("fermoW");
        }else if (direzione.getDirezione().equals("S")) {
            direzione.setDirezione("fermoS");
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
