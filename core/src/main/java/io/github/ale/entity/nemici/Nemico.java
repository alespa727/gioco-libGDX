package io.github.ale.entity.nemici;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import io.github.ale.entity.abstractEntity.Entity;
import io.github.ale.entity.abstractEntity.caratteristiche.Dimensioni;
import io.github.ale.entity.abstractEntity.movement.ComandiAzioni;
import io.github.ale.entity.abstractEntity.movement.EntityMovementManager;
import io.github.ale.entity.player.Player;
import io.github.ale.enums.Azioni;
import io.github.ale.maps.Map;

public final class Nemico extends Entity{

    private boolean inRange;
    private Rectangle range;

    public final boolean followsPlayer=true;
    public final boolean attacksPlayer=true;

    private final float ATTACK_COOLDOWN = 2f; // Cooldown in secondi
    private final float FOLLOWING_COOLDOWN = 2f;

    EntityMovementManager movement;

    public Nemico() {
        create();
    }

    /**
     * inizializza il nemico
     */
    @Override
    public final void create() {
        inizializzaEntityGraphics();
        inizializzaCoordinate(8f, 8f);

        getEntityGraphics().setTexture("Finn.png");

        inizializzaHitbox(getX(), getY(), 0.65f, 0.4f);
        range = new Rectangle(getX(), getY(), 2f, 2f);
        inizializzaDirezione("fermoS");
        movement = new EntityMovementManager();
        inRange = false;

        inizializzaStati(true, false, false);
        inizializzaStatistiche(100, 1.5f, 10);
        setDirezione("fermoS");
        inizializzaDimensione(new Dimensioni(2f, 2f));
        inizializzaAnimazione();
    }

    /**
     * aggiorna lo stato del nemico
     * @param delta variabile del tempo
     * @param p
     */
    public void update(float delta, Player p) {

        if (followsPlayer) followsPlayer(p, delta);
        
        movement.update(this);
        
        setX(MathUtils.clamp(getX(), 0 - 0.65f, Map.getWidth() - getHitbox().width - getHitbox().width));
        setY(MathUtils.clamp(getY(), 0 - 0.55f, Map.getHeight() - getHitbox().height - getHitbox().height));

        
        getHitbox().x = getX() + 0.65f;
        getHitbox().y = getY() + 0.55f;
        range.x = getX();
        range.y = getY();
        if (attacksPlayer) attacksPlayer(p, delta);
    }

    @Override
    public void drawHitbox(ShapeRenderer renderer){
        renderer.rect(getHitbox().x, getHitbox().y, getHitbox().width, getHitbox().height);
    }

    public void drawEnemyRange(ShapeRenderer renderer){
        if (inRange) {
            renderer.setColor(Color.BLUE);
        }
        renderer.rect(range.x, range.y, range.width, range.height);
        renderer.setColor(Color.BLACK);
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
                movement.addAzione(comandi);
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

            p.getStatistiche().inflictDamage(getStatistiche().getAttackDamage());
            System.out.println(p.getStatistiche().getHealth());
        
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
