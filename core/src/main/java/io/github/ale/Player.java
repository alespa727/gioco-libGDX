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
        hitbox.y = worldY+0.55f;
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

        
        worldX = MathUtils.clamp(worldX, 0-hitbox.width, Map.getWidth()-hitbox.width-hitbox.width);
        worldY = MathUtils.clamp(worldY, 0-hitbox.height, Map.getHeight()-hitbox.height-hitbox.height);
       
        // Disegnamo l'animazione
        
        batch.draw(animation.getKeyFrame(elapsedTime, true), worldX, worldY, 2, 2);
    }

    // Inizializza le variabili, carica i frame
    private void setDefaultValues() {
        isAlive = true;
        worldX=5f;
        worldY=5f;

        player = new TexturesPlayer("Finn.png");

        hitbox = new Rectangle(getWorldX(), getWorldY(), 0.65f, 0.4f);

        direzione = new Direzione();
        
        direzione.setDirezione("S");
        animation = player.setAnimazione(direzione);
    }

    public void gestioneTasti(){ //creare una classe a parte

        nothingPressed();

        everythingPressed();


    }



    public void nothingPressed(){
        boolean w = Gdx.input.isKeyPressed(Input.Keys.W), s = Gdx.input.isKeyPressed(Input.Keys.S), a = Gdx.input.isKeyPressed(Input.Keys.A), d = Gdx.input.isKeyPressed(Input.Keys.D);
        if(!w && !s && !a && !d){
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
        boolean w = Gdx.input.isKeyPressed(Input.Keys.W), s = Gdx.input.isKeyPressed(Input.Keys.S), a = Gdx.input.isKeyPressed(Input.Keys.A), d = Gdx.input.isKeyPressed(Input.Keys.D);
        if (w && s && d && a) {
            direzione.setDirezione("fermoS");
        }else{
            if (w && s) {
                updownPressed();
            }else if(a && d){
                leftrightPressed();
            }else{
                if (w) {
                    worldY += speed * delta;
                    direzione.setDirezione("W");
                }
                if (s) {
                    worldY -= speed * delta;
                    direzione.setDirezione("S");
                }
                if (d) {
                    worldX += speed * delta;
                    direzione.setDirezione("D");
                }
                if (a) {
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
