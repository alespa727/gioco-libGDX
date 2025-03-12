package io.github.ale.entity.enemy.abstractEnemy;



import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.Segment;

import io.github.ale.entity.abstractEntity.Entity;
import io.github.ale.entity.abstractEntity.EntityConfig;
import io.github.ale.entity.abstractEntity.movement.ComandiAzioni;
import io.github.ale.entity.abstractEntity.movement.EntityMovementManager;
import io.github.ale.entity.enemy.abstractEnemy.state.EnemyState;
import io.github.ale.entity.player.Player;
import io.github.ale.entity.player.lineofsight.LineOfSight;
import io.github.ale.enums.Azioni;
import io.github.ale.maps.Map;

public abstract class Nemico extends Entity{

    EnemyState stati;

    public Rectangle range;
    public Segment linea;

    public Vector2 obbiettivo;
    public Vector2 obbiettivoDrawCoord;

    public Circle playerCircle;
    public Circle areaInseguimento;
    
    public final boolean attacksPlayer=true;
    
    
    public final float ATTACK_COOLDOWN = 2f; // Cooldown in secondi
    public final float FOLLOWING_COOLDOWN = .5f;

    public EntityMovementManager movement;

    public Nemico(EntityConfig config) {
        super(config);
        stati = new EnemyState();
    }

    public EnemyState getEnemyStates(){ return stati;}
    
    
    /**
     * aggiorna lo stato del nemico
     * @param delta variabile del tempo
     * @param p
     */
    public void update(float delta, Player p) {
        inAreaInseguimento(p);
        inAreaAttacco(p);
        
        gestioneInseguimento(p, delta);
       
        setX(MathUtils.clamp(getX(), 0 - 0.65f, Map.getWidth() - getHitbox().width - getHitbox().width));
        setY(MathUtils.clamp(getY(), 0 - 0.55f, Map.getHeight() - getHitbox().height - getHitbox().height));

        //obbiettivo.set(LineOfSight.mutualLineOfSight(this));
        
        getHitbox().x = getX() + 0.65f;
        getHitbox().y = getY() + 0.55f;
        range.x = getX();
        range.y = getY();
        areaInseguimento.x = getX()+getSize().getWidth()/2;
        areaInseguimento.y = getY()+getSize().getHeight()/2;
        linea.a.x = getX()+getSize().getWidth()/2;
        linea.a.y = getY()+getSize().getHeight()/2;
        linea.b.x = p.getX()+p.getSize().getWidth()/2;
        linea.b.y = p.getY()+p.getSize().getHeight()/2;

        if (attacksPlayer) attacksPlayer(delta);
    }

    @Override
    public void drawHitbox(ShapeRenderer renderer){
        renderer.rect(getHitbox().x, getHitbox().y, getHitbox().width, getHitbox().height);
        if (stati.isOutOfPursuing()) {
            renderer.rectLine(linea.a.x, linea.a.y, obbiettivoDrawCoord.x, obbiettivoDrawCoord.y, 0.1f);
        }
        if (stati.isPursuing() && !stati.isOutOfPursuing()) {
            renderer.rectLine(linea.a.x, linea.a.y, linea.b.x, linea.b.y, 0.1f);
        }


    }

    public void drawEnemyRange(ShapeRenderer renderer){
        if (stati.isInRange()) {
            renderer.setColor(Color.BLUE);
        }
        renderer.rect(range.x, range.y, range.width, range.height);
        if (stati.isPursuing()) {
            renderer.setColor(Color.VIOLET);
        }
        renderer.circle(areaInseguimento.x, areaInseguimento.y, areaInseguimento.radius, 100);
        renderer.circle(playerCircle.x, playerCircle.y, playerCircle.radius, 100);
        renderer.circle(playerCircle.x, playerCircle.y, Player.getLineOfSight().getRaggio(), 100);
        renderer.setColor(Color.BLACK);
    }


    private void attacksPlayer(float delta){
        if (cooldownAttack > 0) {
            cooldownAttack -= delta;
        }
    }

    private void gestioneInseguimento(Player p, float delta){
        boolean inseguimento = (stati.isPursuing() && !stati.isInRange()) || (stati.isOutOfPursuing() && !stati.isInRange());
        if (inseguimento){
            movement.update(this);
            followsPlayer(p, delta);
        }else{
            movement.clearAzioni();
            if (!getDirezione().contains("fermo")) {
                setDirezione("fermo" .concat(getDirezione())) ;
            }
        }
    }

    private void followsPlayer(Player p, float delta){
        if (cooldownFollowing > 0) {
            cooldownFollowing -= delta;
            //System.out.println(cooldownFollowing);
        }
        
        
        if(cooldownFollowing <= 0){
            if (!stati.isInRange()) {
                
                ComandiAzioni[] comandi = new ComandiAzioni[1];
                comandi[0] = new ComandiAzioni(Azioni.sposta, obbiettivo.x, obbiettivo.y);
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
    private void inAreaAttacco(Player p) {
        Rectangle hitboxPlayer = p.getHitbox();
        if (range.overlaps(hitboxPlayer)) {
            stati.setInRange(true);
            attack(p);
        } else
            stati.setInRange(false);
    }
    
    public void inAreaInseguimento(Player p){
        playerCircle.x = p.getX()+p.getSize().getWidth()/2;
        playerCircle.y = p.getY()+p.getSize().getHeight()/2;
        playerCircle.radius = 0.5f;
        stati.setInAreaInseguimento(areaInseguimento.overlaps(playerCircle));

        LineOfSight.mutualLineOfSight(this, p, areaInseguimento.radius);

        if (stati.isPursuing() || !stati.isInAreaInseguimento()) {
            if(LineOfSight.mutualLineOfSight(this, p, areaInseguimento.radius)!=null){
                stati.setOutOfPursuing(true);
                obbiettivo.set(new Vector2(LineOfSight.mutualLineOfSight(this, p, areaInseguimento.radius)).sub(1f, 1f));
                obbiettivoDrawCoord.set(new Vector2(LineOfSight.mutualLineOfSight(this, p, areaInseguimento.radius)));
            }else{
                stati.setPursuing(false);
                movement.clearAzioni();
                stati.setOutOfPursuing(false);
            } 
        }
        
        if (stati.isInAreaInseguimento() && !Map.checkLineCollision(p.getCenterVector(), getCenterVector())) {
            stati.setPursuing(!Map.checkLineCollision(p.getCenterVector(), getCenterVector()));
            obbiettivoDrawCoord=new Vector2(p.getCenterVector());
            obbiettivo=new Vector2(p.getVector());
            
        }

        

    }
}
