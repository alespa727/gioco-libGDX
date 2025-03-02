package io.github.ale.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import io.github.ale.maps.Map;

public class Player {

    public static Rectangle hitbox;

    protected float worldX;
    protected float worldY;

    protected Direzione direzione;

    private TexturesPlayer player;

    private Animation<TextureRegion> animation;

    private float elapsedTime;

    private Health hp;

    protected static boolean inCollisione;

    protected final float baseSpeed=2.5f;
    protected float delta = 1f;
    protected float speed;
    public boolean isAlive;

    private KeyHandlerPlayer keyH;

    // Costruttore
    public Player() {
        this.speed = baseSpeed*delta;
        create();
    }

    /**
     * inizializzazione player
     */

     private void create() {
        isAlive = true;

        worldX=5f;
        worldY=5f;

        keyH = new KeyHandlerPlayer();
        hp = new Health(100);
        player = new TexturesPlayer("Finn.png");
        hitbox = new Rectangle(getWorldX(), getWorldY(), 0.65f, 0.4f);
        direzione = new Direzione();
        
        direzione.setDirezione("S");
        animation = player.setAnimazione(direzione);
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
        if (inCollisione) {
            renderer.setColor(Color.RED);
        }
        renderer.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        renderer.setColor(Color.BLACK);
    }

    /**
     * aggiorna le informazioni del player
     */

    public void update() {
        keyH.input(this);
        
        hitbox.x = worldX+0.65f;
        hitbox.y = worldY+0.55f;

        checkIfKilled();
        checkIfRevived();
        checkIfDead();
        checkIfAlive();
    }

    /**
     * setta le animazioni del player a seconda della direzione
     */

    private void setAnimation(){
        animation = player.setAnimazione(direzione);
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

    // Getters
    public void setWorldX(float worldX) {
        this.worldX = worldX;
    }

    public void setWorldY(float worldY) {
        this.worldY = worldY;
    }

    public static boolean getInCollisione(){
        System.out.println(inCollisione);
        return inCollisione;
    }
}
