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

    protected boolean inCollisione;
    private boolean inRange;

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

        this.x=8f;
        this.y=8f;

        hp = new Health(100);
        enemy = new TexturesEntity("Finn.png");
        hitbox = new Rectangle(this.x, this.y, 0.65f, 0.4f);
        range = new Rectangle(0, 0, 2f, 2f);
        direzione = new Direzione();
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
        if (cooldownTimer > 0) {
            cooldownTimer -= delta;
        }
        hitbox.x = this.x+0.65f;
        hitbox.y = this.y+0.55f;
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


    private void setAnimation(){
        animation = enemy.setAnimazione(direzione);
    }

    private void inAttackRange(Player p){
        if (hitbox.overlaps(p.hitbox)) {
            inRange=true;
            attack();
        }else inRange=false;
    }
}
