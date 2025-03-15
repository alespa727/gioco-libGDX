package io.github.ale.screens.gameScreen.entity.enemy.abstractEnemy;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import io.github.ale.screens.gameScreen.entity.abstractEntity.Entity;
import io.github.ale.screens.gameScreen.entity.abstractEntity.EntityConfig;
import io.github.ale.screens.gameScreen.entity.abstractEntity.movement.ComandiAzioni;
import io.github.ale.screens.gameScreen.entity.abstractEntity.movement.EntityMovementManager;
import io.github.ale.screens.gameScreen.entity.enemy.abstractEnemy.awareness.EnemyAwareness;
import io.github.ale.screens.gameScreen.entity.enemy.abstractEnemy.state.EnemyState;
import io.github.ale.screens.gameScreen.entity.player.Player;
import io.github.ale.screens.gameScreen.enums.Azioni;
import io.github.ale.screens.gameScreen.maps.Map;

public abstract class Nemico extends Entity{
    
    private final Player player;

    private final EntityMovementManager movement;
    private final EnemyState stati;
    private EnemyAwareness awareness;

    private float areaInseguimento;
    private float areaAttacco;
    
    public final float ATTACK_COOLDOWN = 2f; // Cooldown in secondi
    public final float FOLLOWING_COOLDOWN = 0.75f;
    
    public float cooldownFollowing=0;

    private boolean flag=false;

    public Nemico(EntityConfig config, Player player) {
        super(config);
        this.player = player;
        this.movement = new EntityMovementManager();
        this.stati = new EnemyState();
        this.awareness = new EnemyAwareness();
    }

    public EnemyState getEnemyStates(){ return stati;}
    public EntityMovementManager getMovementManager(){ return movement; }
    protected Player player(){ return player;}

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
    @Override
    public void updateEntity() {
        float delta = Gdx.graphics.getDeltaTime();
        inAreaInseguimento();
        inAreaAttacco();
        mantieniNeiLimiti();
        gestioneInseguimento(delta);
        adjustHitbox();

        awareness.setRange(coordinateCentro().x - areaAttacco/2, coordinateCentro().y - areaAttacco/2, areaAttacco, areaAttacco);
        awareness.setAreaInseguimento(getX()+getSize().getWidth()/2, getY()+getSize().getHeight()/2, areaInseguimento);
    
        awareness.setVisione(coordinateCentro().x, coordinateCentro().y,  player.coordinateCentro().x, player.coordinateCentro().y);

        if (atkCooldown() > 0) {
            setAtkCooldown(atkCooldown()-delta);
        }

        if(stati.idle()){
            wandering();
        }
    }

   
    @Override
    public void drawHitbox(ShapeRenderer renderer){
        renderer.rect(hitbox().x, hitbox().y, hitbox().width, hitbox().height);
        if (stati.searching()) {
            renderer.rectLine(awareness.getVisione().a.x, awareness.getVisione().a.y, awareness.getObbiettivoDrawCoord().x, awareness.getObbiettivoDrawCoord().y, 0.1f);
        }
        if (stati.isPursuing() && !stati.searching()) {
            renderer.rectLine(awareness.getVisione().a.x, awareness.getVisione().a.y, awareness.getVisione().b.x, awareness.getVisione().b.y, 0.1f);
        }
        renderer.rect(awareness.getObbiettivoDrawCoord().x - getAwareness().getRange().width/2, awareness.getObbiettivoDrawCoord().y - getAwareness().getRange().height/2, getAwareness().getRange().width, getAwareness().getRange().height);
    }

    /**
     * disegna il range del nemico (attacco e inseguimento)
     * @param renderer
     */
    @Override
    public void drawRange(ShapeRenderer renderer){
        if (stati.inRange()) {
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
    private void gestioneInseguimento(float delta){
        boolean inseguimento = (stati.isPursuing() && !stati.inRange()) || (stati.searching() && !stati.inRange());
        if (inseguimento){
            movement.update(this);
            followsPlayer(delta);
            if (flag) {
                cooldownFollowing=0;
                flag=false;
            }
        }else{
            movement.clearAzioni();
            if (!direzione().contains("fermo")) {
                setDirezione("fermo" .concat(direzione())) ;
            }
        }
        
        if (stati.inRange()) {
            if (Math.abs(player.getX()-getX()) > Math.abs(player.getY()-getY())) {
                if (player.getX() < getX()) {
                    setDirezione("fermoA");
                }
                if (player.getX() > getX()) {
                    setDirezione("fermoD");
                }
            }else{   
                if (player.getY() > getY()) {
                    setDirezione("fermoW");
                }
                if (player.getY() < getY()) {
                    setDirezione("fermoS");
                }
            }
            
        }

        if(stati.idle()){

        }
    }

    /**
     * inseguimento player cooldown
     * @param p
     * @param delta
     */
    private void followsPlayer(float delta){
        if (cooldownFollowing > 0) {
            cooldownFollowing -= delta;
            //System.out.println(cooldownFollowing);
        }
        
        if(cooldownFollowing <= 0){
            if (!stati.inRange()) {
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
    public abstract void attack();
    
    /**
     * controlla se il player è nel range attacco
     */
    private void inAreaAttacco() {
        Rectangle hitboxPlayer = player.hitbox();
        if (awareness.getRange().overlaps(hitboxPlayer)) {
            stati.setInRange(true);
            attack();
        } else
            stati.setInRange(false);
    }
    
    /**
     * setta l'obbiettivo del nemico
     * @param p
     */
    private void inAreaInseguimento(){
        stati.setInAreaInseguimento(awareness.getAreaInseguimento().overlaps(player.circle()));
        Vector2 lineOfSightPoint = player.los().mutualLineOfSight(this, player, awareness.getAreaInseguimento().radius);
        boolean mutualLos = lineOfSightPoint!=null;
        boolean lineCollision = 
    Map.checkLineCollision(new Vector2(hitbox().x + hitbox().width, hitbox().y), 
                           new Vector2(player.hitbox().x + player.hitbox().width, player.hitbox().y)) 
|| Map.checkLineCollision(new Vector2(hitbox().x, hitbox().y), 
                          new Vector2(player.hitbox().x, player.hitbox().y)) 
|| Map.checkLineCollision(new Vector2(hitbox().x + hitbox().width, hitbox().y + hitbox().height), 
                          new Vector2(player.hitbox().x + player.hitbox().width, player.hitbox().y + player.hitbox().height)) 
|| Map.checkLineCollision(new Vector2(hitbox().x, hitbox().y + hitbox().height), 
                          new Vector2(player.hitbox().x, player.hitbox().y + player.hitbox().height));

        if (stati.isPursuing() || !stati.inAreaInseguimento()) {
            if(mutualLos){
                
                stati.setSearching(true);
                awareness.getObbiettivo().set(new Vector2(lineOfSightPoint).sub(1f, 1f));
                awareness.getObbiettivoDrawCoord().set(new Vector2(lineOfSightPoint));
            }else{
                stati.setPursuing(false);
                movement.clearAzioni();
                stati.setSearching(false);
            } 
        }
        
        if (stati.inAreaInseguimento() && !lineCollision) {
            stati.setPursuing(!lineCollision);
            awareness.setObbiettivoDrawCoord(player.coordinateCentro().x, player.coordinateCentro().y);
            awareness.setObbiettivo(player.coordinate().x, player.coordinate().y);
        }

        
        if (!stati.isPursuing() && !stati.searching()) {
            stati.setIdle(true);
        } else {
            stati.setIdle(false);
            areaInseguimento = 5.5f;
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
