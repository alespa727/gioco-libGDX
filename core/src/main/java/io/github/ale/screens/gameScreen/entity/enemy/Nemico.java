package io.github.ale.screens.gameScreen.entity.enemy;



import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import io.github.ale.screens.gameScreen.maps.Map;
import io.github.ale.screens.gameScreen.entity.abstractEntity.Entity;
import io.github.ale.screens.gameScreen.entity.abstractEntity.EntityConfig;
import io.github.ale.screens.gameScreen.entity.abstractEntity.movement.ComandiAzioni;
import io.github.ale.screens.gameScreen.entity.abstractEntity.movement.EntityMovementManager;
import io.github.ale.screens.gameScreen.entity.enemy.abstractEnemy.awareness.EnemyAwareness;
import io.github.ale.screens.gameScreen.entity.enemy.abstractEnemy.state.EnemyState;
import io.github.ale.screens.gameScreen.entity.player.Player;
import io.github.ale.screens.gameScreen.entity.player.lineofsight.LineOfSight;
import io.github.ale.screens.gameScreen.enums.Azioni;

public abstract class Nemico extends Entity{

    boolean flag=false;

    private final EntityMovementManager movement;

    private final EnemyState stati;
    private EnemyAwareness awareness;

    private float areaInseguimento;
    private float areaAttacco;
    
    public final float ATTACK_COOLDOWN = 2f; // Cooldown in secondi
    public final float FOLLOWING_COOLDOWN = 1f;

    public Nemico(EntityConfig config, Player p) {
        super(config);
        this.movement = new EntityMovementManager();
        this.stati = new EnemyState();
        this.awareness = new EnemyAwareness();
    }

    public EnemyState getEnemyStates(){ return stati;}
    public EntityMovementManager getMovementManager(){ return movement; }

    /**
     * area di inseguimento, area in cui puo fare attack()
     * @param inseguimento
     * @param attacco
     */
    public void setAree(float inseguimento, float attacco){ 
        this.areaInseguimento=inseguimento;
        this.areaAttacco=attacco;
    }
    
    /**
     * aggiorna lo stato del nemico
     * @param delta variabile del tempo
     * @param p
     */
    public void updateEntity(float delta, Player p) {

        inAreaInseguimento(p);
        inAreaAttacco(p);
        mantieniNeiLimiti();
        gestioneInseguimento(p, delta);
        adjustHitbox();

        awareness.setRange(getX(), getY(), areaAttacco, areaAttacco);
        awareness.setAreaInseguimento(getX()+getSize().getWidth()/2, getY()+getSize().getHeight()/2, areaInseguimento);
    
        awareness.setVisione(getCenterVector().x, getCenterVector().y,  p.getCenterVector().x, p.getCenterVector().y);

        if (cooldownAttack > 0) {
            cooldownAttack -= delta;
        }

        if(stati.isIdle()){
            wandering();
        }
    }

   
    @Override
    public void drawHitbox(ShapeRenderer renderer){
        renderer.rect(getHitbox().x, getHitbox().y, getHitbox().width, getHitbox().height);
        if (stati.isOutOfPursuing()) {
            renderer.rectLine(awareness.getVisione().a.x, awareness.getVisione().a.y, awareness.getObbiettivoDrawCoord().x, awareness.getObbiettivoDrawCoord().y, 0.1f);
        }
        if (stati.isPursuing() && !stati.isOutOfPursuing()) {
            renderer.rectLine(awareness.getVisione().a.x, awareness.getVisione().a.y, awareness.getVisione().b.x, awareness.getVisione().b.y, 0.1f);
        }
    }

    /**
     * disegna il range del nemico (attacco e inseguimento)
     * @param renderer
     */
    public void drawEnemyRange(ShapeRenderer renderer){
        if (stati.isInRange()) {
            renderer.setColor(Color.BLUE);
        }
        renderer.rect(awareness.getRange().x, awareness.getRange().y, awareness.getRange().width, awareness.getRange().height);
        if (stati.isPursuing()) {
            renderer.setColor(Color.VIOLET);
        }
        renderer.circle(awareness.getAreaInseguimento().x, awareness.getAreaInseguimento().y, awareness.getAreaInseguimento().radius, 100);
        renderer.setColor(Color.BLACK);
    }


    /**
     * 
     * @param p
     * @param delta
     */
    private void gestioneInseguimento(Player p, float delta){
        boolean inseguimento = (stati.isPursuing() && !stati.isInRange()) || (stati.isOutOfPursuing() && !stati.isInRange());
        if (inseguimento){
            movement.update(this);
            followsPlayer(p, delta);
            if (flag) {
                cooldownFollowing=0;
                flag=false;
            }
        }else{
            movement.clearAzioni();
            if (!getDirezione().contains("fermo")) {
                setDirezione("fermo" .concat(getDirezione())) ;
            }
        }
        
        if (stati.isInRange()) {
            if (Math.abs(p.getX()-getX()) > Math.abs(p.getY()-getY())) {
                if (p.getX() < getX()) {
                    setDirezione("fermoA");
                }
                if (p.getX() > getX()) {
                    setDirezione("fermoD");
                }
            }else{   
                if (p.getY() > getY()) {
                    setDirezione("fermoW");
                }
                if (p.getY() < getY()) {
                    setDirezione("fermoS");
                }
            }
            
        }

        if(stati.isIdle()){

        }
    }

    /**
     * inseguimento player cooldown
     * @param p
     * @param delta
     */
    private void followsPlayer(Player p, float delta){
        if (cooldownFollowing > 0) {
            cooldownFollowing -= delta;
            //System.out.println(cooldownFollowing);
        }
        
        if(cooldownFollowing <= 0){
            if (!stati.isInRange()) {
                ComandiAzioni[] comandi = new ComandiAzioni[1];
                comandi[0] = new ComandiAzioni(Azioni.sposta, awareness.getObbiettivo().x, awareness.getObbiettivo().y);
                movement.addAzione(comandi);
                cooldownFollowing = FOLLOWING_COOLDOWN;
                
            }else flag=true;
        }
    }

    /**
     * il nemico attacca
     * @param p
     */
    public abstract void attack(Player p);
    
    /**
     * controlla se il player è nel range attacco
     */
    private void inAreaAttacco(Player p) {
        Rectangle hitboxPlayer = p.getHitbox();
        if (awareness.getRange().overlaps(hitboxPlayer)) {
            stati.setInRange(true);
            attack(p);
        } else
            stati.setInRange(false);
    }
    
    /**
     * setta l'obbiettivo del nemico
     * @param p
     */
    private void inAreaInseguimento(Player p){
        stati.setInAreaInseguimento(awareness.getAreaInseguimento().overlaps(p.circle()));

        LineOfSight.mutualLineOfSight(this, p, awareness.getAreaInseguimento().radius);
       
        if (stati.isPursuing() || !stati.isInAreaInseguimento()) {
            
            if(LineOfSight.mutualLineOfSight(this, p, awareness.getAreaInseguimento().radius)!=null){
                stati.setOutOfPursuing(true);
                awareness.getObbiettivo().set(new Vector2(LineOfSight.mutualLineOfSight(this, p, awareness.getAreaInseguimento().radius)).sub(1f, 1f));
                awareness.getObbiettivoDrawCoord().set(new Vector2(LineOfSight.mutualLineOfSight(this, p, awareness.getAreaInseguimento().radius)));
            }else{
                stati.setPursuing(false);
                movement.clearAzioni();
                stati.setOutOfPursuing(false);
            } 
        }
        
        if (stati.isInAreaInseguimento() && !Map.checkLineCollision(p.getCenterVector(), getCenterVector())) {
            stati.setPursuing(!Map.checkLineCollision(p.getCenterVector(), getCenterVector()));
            awareness.setObbiettivoDrawCoord(p.getCenterVector().x, p.getCenterVector().y);
            awareness.setObbiettivo(p.getVector().x, p.getVector().y);
        }

        
        if (!stati.isPursuing()) {
            stati.setIdle(true);
        }else{
            areaInseguimento=5.5f;
            stati.setIdle(false);
        }
        if (!stati.isOutOfPursuing()) {
            stati.setIdle(true);
        }else{
            areaInseguimento=5.5f;
            stati.setIdle(false);
        }
    }

    public EnemyAwareness getAwareness() {
        return awareness;
    }

    public void setAwareness(EnemyAwareness awareness) {
        this.awareness = awareness;
    }

    public void wandering(){
        areaInseguimento=1f;
    }

    
}
