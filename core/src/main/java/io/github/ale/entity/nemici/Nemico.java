package io.github.ale.entity.nemici;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import io.github.ale.entity.Direzione;
import io.github.ale.entity.Health;
import io.github.ale.entity.TexturesEntity;
import io.github.ale.entity.player.Player;
import io.github.ale.maps.Map;

public class Nemico {
    private Health hp;

    private Rectangle hitbox;
    private Rectangle range;

    protected float x;
    protected float y;
    protected Direzione direzione;

    TexturesEntity enemy;

    private Animation<TextureRegion> animation;

    private float elapsedTime;
    private double elapsedTimeMovement;

    protected boolean inCollisione;
    private boolean inRange;
    private boolean hasFinishedMoving;
    private boolean isMovingX, isMovingY, isDashingX, isDashingY;

    protected final float baseSpeed=2.5f;
    protected float delta = 1f;
    protected float speed;
    public boolean isAlive;

    private float cooldownTimer = 0; // Tempo rimanente prima del prossimo attacco
    private final float ATTACK_COOLDOWN = 2.0f; // Cooldown in secondi

    EnemyMovementManager movement;

    public Nemico(){
        this.speed = baseSpeed*delta;
        create();
    }

    
    private void create() {
        isAlive = true;
        hasFinishedMoving=true;
        this.x=8f;
        this.y=8f;

        hp = new Health(100);
        enemy = new TexturesEntity("Finn.png");
        hitbox = new Rectangle(this.x, this.y, 0.65f, 0.4f);
        range = new Rectangle(this.x, this.y, 2f, 2f);
        direzione = new Direzione();
        movement = new EnemyMovementManager();
        inRange = false;

        direzione.setDirezione("fermoS");
        animation = enemy.setAnimazione(direzione);
    }

    public void draw(SpriteBatch batch) { 
        elapsedTime += Gdx.graphics.getDeltaTime();

        setAnimation();

        this.x = MathUtils.clamp(x, 0-0.65f, Map.getWidth()-hitbox.width-hitbox.width);
        this.y = MathUtils.clamp(y, 0-0.55f, Map.getHeight()-hitbox.height-hitbox.height);
        
        batch.draw(animation.getKeyFrame(elapsedTime, true), x, y, 2, 2);
    }

    public void update(float delta, Player p){
        elapsedTimeMovement += Gdx.graphics.getDeltaTime();
        if (cooldownTimer > 0) {
            cooldownTimer -= delta;
        }
        if (inRange || !hasFinishedMoving) {
            setAnimation();
            spostaY(3f);
            
            
        }
        
        movement.update(this);
        hitbox.x = this.x+0.65f;
        hitbox.y = this.y+0.55f;
        range.x = this.x+0.65f;
        range.y = this.y+0.55f;
        inAttackRange(p);
    }

    private void attack(){
        if (cooldownTimer <= 0) {
            // Il nemico può attaccare
            System.out.println("Nemico attacca il giocatore!");
            // Reset del cooldown
            cooldownTimer = ATTACK_COOLDOWN;
        }
    }

    // Getters
    public float getWorldX() {
        return x;
    }

    public float getWorldY() {
        return y;
    }

    // Getters
    public void setWorldX(float x) {
        this.x = x;
    }

    public void setWorldY(float y) {
        this.y = y;
    }

    public void spostaX(float x) {
        float deltaTime = Gdx.graphics.getDeltaTime();  
    
        if (Math.abs(this.x - x) > 0.01f) { 
            if (this.x < x) {
                this.x += speed * deltaTime;
                direzione.setDirezione("D"); 
                hasFinishedMoving = false;
                isMovingX=true;
            } else {
                this.x -= speed * deltaTime;
                direzione.setDirezione("A"); 
                hasFinishedMoving = false;
                isMovingX=true;
            }
        } else {
            this.x = x;
            direzione.setDirezione("fermoS"); 
            hasFinishedMoving = true;
            isMovingX=false;
            System.out.println(isMovingX);
        }
    }

    
    public void spostaY(float y) {
        float deltaTime = Gdx.graphics.getDeltaTime();  
        if (!isMovingX) {
            if (Math.abs(this.y - y) > 0.01f) { 
                if (this.y < y) {
                    this.y += speed * deltaTime;
                    direzione.setDirezione("W"); 
                    hasFinishedMoving = false;
                } else {
                    this.y -= speed * deltaTime;
                    direzione.setDirezione("S"); 
                    hasFinishedMoving = false;
                }
            } else {
                this.y = y;
                direzione.setDirezione("fermoS"); 
                hasFinishedMoving = true;
            }
        }  
    }

    public void dashX(float x){  
        
            float dashSpeed = 0.045f; 
            this.x += (x - this.x) * dashSpeed;
        
            // Controlla se il movimento è terminato
            if (Math.abs(this.x - x) < 0.01f) { 
                this.x = x; 
                hasFinishedMoving = true;
            } else {
                hasFinishedMoving = false;
            }
        
            // Imposta la direzione in base alla posizione attuale e alla destinazione
            if (Math.abs(this.x - x) > 0.01f) { 
                if (this.x < x) {
                    direzione.setDirezione("D"); 
                    hasFinishedMoving = false;
                    isDashingX=true;
                } else {
                    direzione.setDirezione("A"); 
                    hasFinishedMoving = false;
                    isDashingX=true;
                }
            } else {
                this.x = x;
                direzione.setDirezione("fermoS"); 
                hasFinishedMoving = true;
                isDashingX=false;
            }
        
        
    }
    
    


    private void setAnimation(){
        animation = enemy.setAnimazione(direzione);
    }

    private void inAttackRange(Player p){
        if (range.overlaps(p.hitbox)) {
            inRange=true;
            attack();
        }else inRange=false;
    }
}
