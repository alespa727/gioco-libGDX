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

    protected float x;
    protected float y;
    protected Direzione direzione;

    TexturesEntity enemy;

    private Animation<TextureRegion> animation;

    private float elapsedTime;

    protected boolean inCollisione;

    protected final float baseSpeed=2.5f;
    protected float delta = 1f;
    protected float speed;
    public boolean isAlive;

    MovementManager movement;

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
        direzione = new Direzione();
        
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

    public void update(Player p){
        hitbox.x = this.x+0.65f;
        hitbox.y = this.y+0.55f;
        collisioneConPlayer(p);
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

    private void collisioneConPlayer(Player p){

        boolean inCollisionTemp=false;
        Rectangle hitboxTemp = new Rectangle(Player.hitbox);
        if (p.direzione.getDirezione().equals("A")) {
            hitboxTemp.x-=1f/32f;
        }
        if (p.direzione.getDirezione().equals("D")) {
            hitboxTemp.x+=1f/32f;
        }
        if (p.direzione.getDirezione().equals("W")) {
            hitboxTemp.y+=1f/32f;
        }
        if (p.direzione.getDirezione().equals("S")) {
            hitboxTemp.y-=1f/32f;
        }
        
        if (hitbox.overlaps(hitboxTemp)) {
            p.setInCollisione(true);
            System.out.println(p.getInCollisione());
        }else p.setInCollisione(false);
        
    }
}
