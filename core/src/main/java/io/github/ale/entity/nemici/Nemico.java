package io.github.ale.entity.nemici;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import io.github.ale.Azioni;
import io.github.ale.ComandiAzioni;
import io.github.ale.entity.Direzione;
import io.github.ale.entity.abstractEnity.Entity;
import io.github.ale.entity.abstractEnity.movement.EntityMovementManager;
import io.github.ale.entity.player.Player;
import io.github.ale.maps.Map;

public final class Nemico extends Entity{

    private boolean inRange;
    private Rectangle range;

    public final boolean followsPlayer=true;
    public final boolean attacksPlayer=true;

    private final float ATTACK_COOLDOWN = 2f; // Cooldown in secondi
    private final float FOLLOWING_COOLDOWN = 4f;

    EntityMovementManager movement;

    public Nemico() {
        create();
    }

    /**
     * inizializza il nemico
     */
    @Override
    protected void create() {
        isAlive = true;

        setX(8f);
        setY(8f);

        setStatistiche(100, 1.5f, 10);

        setTexture("Finn.png");
        hitbox = new Rectangle(getX(), getY(), 0.65f, 0.4f);
        range = new Rectangle(getX(), getY(), 2f, 2f);
        direzione = new Direzione();
        movement = new EntityMovementManager();
        inRange = false;

        setDirezione("fermoS");
        animation = getTexture().setAnimazione(direzione);
    }

    /**
     * disegna il nemico
     * @param batch
     */
    @Override
    public void draw(SpriteBatch batch) {
        elapsedTime += Gdx.graphics.getDeltaTime();

        setAnimation();

        setX(MathUtils.clamp(getX(), 0 - 0.65f, Map.getWidth() - hitbox.width - hitbox.width));
        setY(MathUtils.clamp(getY(), 0 - 0.55f, Map.getHeight() - hitbox.height - hitbox.height));

        batch.draw(animation.getKeyFrame(elapsedTime, true), getX(), getY(), 2, 2);
    }

    /**
     * aggiorna lo stato del nemico
     * @param delta variabile del tempo
     * @param p
     */
    public void update(float delta, Player p) {

        if (followsPlayer) followsPlayer(p, delta);

        movement.update(this);
        
        hitbox.x = getX() + 0.65f;
        hitbox.y = getY() + 0.55f;
        range.x = getX();
        range.y = getY();
        if (attacksPlayer) attacksPlayer(p, delta);
    }

    @Override
    public void drawHitbox(ShapeRenderer renderer){
        renderer.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
    }
    


    public void drawEnemyRange(ShapeRenderer renderer){
        renderer.rect(range.x, range.y, range.width, range.height);
    }


    private void attacksPlayer(Player p, float delta){
        if (cooldownAttack > 0) {
            cooldownAttack -= delta;
        }
        inAttackRange(p);
    }

    private void followsPlayer(Player p, float delta){
        if (cooldownFollowing > 0) {
            cooldownFollowing -= delta;
            //System.out.println(cooldownFollowing);
            if (inRange) {
                cooldownFollowing = 2f;
            }
        }
        
        
        if(cooldownFollowing <= 0){
            if (!inRange) {
                ComandiAzioni[] comandi = new ComandiAzioni[2];
                comandi[1] = new ComandiAzioni(Azioni.spostaY, p.getY());
                comandi[0] = new ComandiAzioni(Azioni.spostaX, p.getX()+1);
                movement.updateAddAzione(comandi);
                cooldownFollowing = FOLLOWING_COOLDOWN;
           
            }
            
        }
    }

    /**
     * il nemico attacca
     * @param p
     */
    private void attack(Player p) {
        
        if (cooldownAttack <= 0) {
            
            System.out.println("Nemico attacca il giocatore!");

            p.statistiche.inflictDamage(statistiche.getAttackDamage());
            System.out.println(p.statistiche.getHealth());
        
            cooldownAttack = ATTACK_COOLDOWN;
        }
    }
    
  
    /**
     * controlla se il player è nel range attacco
     */
    private void inAttackRange(Player p) {
        Rectangle hitboxPlayer = p.getHitbox();
        if (range.overlaps(hitboxPlayer)) {
            inRange = true;
            attack(p);
        } else
            inRange = false;
    }
    
    
}
