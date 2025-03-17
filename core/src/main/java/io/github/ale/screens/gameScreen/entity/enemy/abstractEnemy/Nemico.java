package io.github.ale.screens.gameScreen.entity.enemy.abstractEnemy;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import io.github.ale.screens.gameScreen.entity.abstractEntity.Entity;
import io.github.ale.screens.gameScreen.entity.abstractEntity.EntityConfig;
import io.github.ale.screens.gameScreen.entity.abstractEntity.movement.EntityMovementManager;
import io.github.ale.screens.gameScreen.entity.enemy.abstractEnemy.awareness.EnemyAwareness;
import io.github.ale.screens.gameScreen.entity.enemy.abstractEnemy.state.EnemyState;
import io.github.ale.screens.gameScreen.entity.player.Player;

public abstract class Nemico extends Entity{
    private final Player player;

    private final EntityMovementManager movement;
    private final EnemyState stati;
    private EnemyAwareness awareness;

    private float areaInseguimento;
    private float areaAttacco;
    
    public final float ATTACK_COOLDOWN = 2f; // Cooldown in secondi
    public final float FOLLOWING_COOLDOWN = 0.4f;
    
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
        //inAreaInseguimento();
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
            renderer.rectLine(hitbox().x, hitbox().y, player().hitbox().x, player().hitbox().y, 0.0001f);
            renderer.rectLine(hitbox().x+hitbox().width, hitbox().y, player().hitbox().x+player().hitbox().width, player().hitbox().y, 0.0001f);
            renderer.rectLine(hitbox().x, hitbox().y+hitbox().height, player().hitbox().x, player().hitbox().y+player().hitbox().height, 0.0001f);
            renderer.rectLine(hitbox().x+hitbox().width, hitbox().y+hitbox().height, player().hitbox().x+player().hitbox().width, player().hitbox().y+player().hitbox().height, 0.0001f);
        }
        if (stati.isPursuing() && !stati.searching()) {
            renderer.rectLine(awareness.getVisione().a.x, awareness.getVisione().a.y, awareness.getVisione().b.x, awareness.getVisione().b.y, 0.1f);
        }
        //renderer.rect(awareness.getObbiettivoDrawCoord().x - getAwareness().getRange().width/2, awareness.getObbiettivoDrawCoord().y - getAwareness().getRange().height/2, getAwareness().getRange().width, getAwareness().getRange().height);
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
        boolean inseguimento = true;
        if (inseguimento){
          
            
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
