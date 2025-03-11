package io.github.ale.entity.nemici;



import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Segment;

import io.github.ale.entity.abstractEntity.Entity;
import io.github.ale.entity.abstractEntity.caratteristiche.Dimensioni;
import io.github.ale.entity.abstractEntity.movement.ComandiAzioni;
import io.github.ale.entity.abstractEntity.movement.EntityMovementManager;
import io.github.ale.entity.player.Player;
import io.github.ale.entity.player.lineofsight.LineOfSight;
import io.github.ale.enums.Azioni;
import io.github.ale.maps.Map;

public final class Nemico extends Entity{

    private boolean inRange;
    private Rectangle range;
    private Segment linea;

    private Vector2 obbiettivo;

    private Circle playerCircle;
    private Circle areaInseguimento;

    public boolean inAreaInseguimento;
    public final boolean attacksPlayer=true;
    
    public boolean idle;
    public boolean pursuing;

    private final float ATTACK_COOLDOWN = 2f; // Cooldown in secondi
    private final float FOLLOWING_COOLDOWN = 0.2f;

    EntityMovementManager movement;

    public Nemico() {
        create();
    }

    /**
     * inizializza il nemico
     */
    @Override
    public final void create() {

        obbiettivo = new Vector2();
        
        inizializzaEntityGraphics();
        inizializzaCoordinate(5f, 12f);

        getEntityGraphics().setTexture("Finn.png");

        inizializzaHitbox(getX(), getY(), 0.65f, 0.4f);
        range = new Rectangle(getX(), getY(), 2f, 2f);
        
        inizializzaDirezione("fermoS");
        linea = new Segment(new Vector3(0, 0, 0), new Vector3(0, 0, 0));
        movement = new EntityMovementManager();
        inRange = false;
        playerCircle = new Circle(0, 0, 0.5f);
            
        inizializzaStati(true, false, false);
        inizializzaStatistiche(100, 1.5f, 10);
        setDirezione("fermoS");
        inizializzaDimensione(new Dimensioni(2f, 2f));
        inizializzaAnimazione();
        areaInseguimento = new Circle(getX()+getSize().getWidth()/2, getY()+getSize().getHeight()/2, 4f);
    }

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
        
        obbiettivo = p.getVector();
        
        
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
        if (pursuing) {
            renderer.rectLine(linea.a.x, linea.a.y, linea.b.x, linea.b.y, 0.1f);
        }
    }

    public void drawEnemyRange(ShapeRenderer renderer){
        if (inRange) {
            renderer.setColor(Color.BLUE);
        }
        renderer.rect(range.x, range.y, range.width, range.height);
        if (pursuing) {
            renderer.setColor(Color.VIOLET);
        }
        renderer.circle(areaInseguimento.x, areaInseguimento.y, areaInseguimento.radius, 100);
        renderer.circle(playerCircle.x, playerCircle.y, playerCircle.radius, 100);
        renderer.setColor(Color.BLACK);
    }


    private void attacksPlayer(float delta){
        if (cooldownAttack > 0) {
            cooldownAttack -= delta;
        }
    }

    private void gestioneInseguimento(Player p, float delta){
        if (pursuing){
            movement.update(this);
            followsPlayer(delta);
        }else{
            movement.clearAzioni();
            if (!getDirezione().contains("fermo")) {
                setDirezione("fermo" .concat(getDirezione())) ;
            }
        }
    }

    private void followsPlayer(float delta){
        if (cooldownFollowing > 0) {
            cooldownFollowing -= delta;
            //System.out.println(cooldownFollowing);
        }
        
        
        if(cooldownFollowing <= 0){
            if (!inRange) {
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
            inRange = true;
            attack(p);
        } else
            inRange = false;
    }
    
    public void inAreaInseguimento(Player p){
        playerCircle.x = p.getX()+p.getSize().getWidth()/2;
        playerCircle.y = p.getY()+p.getSize().getHeight()/2;
        playerCircle.radius = 0.5f;
        inAreaInseguimento = areaInseguimento.overlaps(playerCircle);

        LineOfSight.mutualLineOfSight(this, areaInseguimento.radius);
    } 
    
}
