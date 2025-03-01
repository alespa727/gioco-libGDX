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

    public static Rectangle hitbox;

    private float worldX;
    private float worldY;

    private Direzione direzione;

    private TexturesPlayer player;

    private Animation<TextureRegion> animation;

    private float elapsedTime;

    private final float baseSpeed=2.5f;
    private float delta = 1f;
    private float speed;
    public boolean isAlive;

    // Costruttore
    public Player() {
        this.speed = baseSpeed*delta;
        setDefaultValues();
    }

    /**
     * disegna l'animazione del player
     * @param batch
     */

    public void draw(SpriteBatch batch) { 
        elapsedTime += Gdx.graphics.getDeltaTime();

        setAnimation();

        worldX = MathUtils.clamp(worldX, 0-0.65f, Map.getWidth()-hitbox.width-hitbox.width);
        worldY = MathUtils.clamp(worldY, 0-0.55f, Map.getHeight()-hitbox.height-hitbox.height);
        
        batch.draw(animation.getKeyFrame(elapsedTime, true), worldX, worldY, 2, 2);
    }

    /**
     * disegna l'hitbox del player
     * @param renderer
     */

    public void drawHitbox(ShapeRenderer renderer) {
        renderer.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
    }

    /**
     * aggiorna le informazioni del player
     */

    public void update() {
        input();
        hitbox.x = worldX+0.65f;
        hitbox.y = worldY+0.55f;
        checkIfKilled();
        checkIfRevived();
        checkIfDead();
        checkIfAlive();
    }

    /**
     * aggiorna gli input
     */

    private void input() {
        delta = Gdx.graphics.getDeltaTime();
        gestioneTasti();
    }

    /**
     * setta le animazioni del player a seconda della direzione
     */

    private void setAnimation(){
        animation = player.setAnimazione(direzione);
    }

    /**
     * inizializzazione player
     */

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

    /**
     * gestione tasti
     */

    private void gestioneTasti(){ //creare una classe a parte
        sprint();
        nothingPressed();
        anythingPressed();
    }

    /**
     * input sprint
     */

    private void sprint(){
        boolean shift = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT);
        float delta;

        if (shift) {
            //System.out.println("SPRINT!");
            delta = 1.5f;
            speed = baseSpeed*delta;
        }

        if (!shift) {
            //System.out.println("NIENTE SPRINT");
            delta = 1f;
            speed = baseSpeed*delta;
        }
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

    private void anythingPressed(){
        boolean w = Gdx.input.isKeyPressed(Input.Keys.W), s = Gdx.input.isKeyPressed(Input.Keys.S), a = Gdx.input.isKeyPressed(Input.Keys.A), d = Gdx.input.isKeyPressed(Input.Keys.D);
        if (w && s && d && a) {
            direzione.setDirezione("fermoS");
        }else{
            if (w && s) {
                if (direzione.getDirezione().equals("A")) direzione.setDirezione("fermoA");
                else if (direzione.getDirezione().equals("D")) direzione.setDirezione("fermoD");

                if (a) direzione.setDirezione("A");
                if (d) direzione.setDirezione("D");

                if (!Map.checkCollisionX(direzione.getDirezione())) {
                    if (a) worldX -= speed * delta;
                    if (d) worldX += speed * delta;
                }

                if (direzione.getDirezione().equals("W")) direzione.setDirezione("fermoW");
                else if (direzione.getDirezione().equals("S")) direzione.setDirezione("fermoS");

            }else if(a && d){
                if (direzione.getDirezione().equals("W")) direzione.setDirezione("fermoW");
                else if (direzione.getDirezione().equals("S")) direzione.setDirezione("fermoS");

                if (w) direzione.setDirezione("W");
                if (s) direzione.setDirezione("S");

                if (!Map.checkCollisionY(direzione.getDirezione())) {
                    if (s) worldY -= speed * delta;
                    if (w) worldY += speed * delta;
                }

                if (direzione.getDirezione().equals("D")) direzione.setDirezione("fermoD");
                else if (direzione.getDirezione().equals("A")) direzione.setDirezione("fermoA");

            }else{

                if (w) direzione.setDirezione("W");
                if (s) direzione.setDirezione("S");

                if (!Map.checkCollisionY(direzione.getDirezione())) {
                    if (s) worldY -= speed * delta;
                    if (w) worldY += speed * delta;
                }
                
                if (d) direzione.setDirezione("D");
                if (a) direzione.setDirezione("A");
    
                if (!Map.checkCollisionX(direzione.getDirezione())) {
                    if (a) worldX -= speed * delta;
                    if (d) worldX += speed * delta;
                }
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
