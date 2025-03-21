package io.github.ale.screens.gameScreen.entity.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import io.github.ale.screens.gameScreen.entity.EntityManager;
import io.github.ale.screens.gameScreen.entity.abstractEntity.Entity;
import io.github.ale.screens.gameScreen.entity.abstractEntity.EntityConfig;
import io.github.ale.screens.gameScreen.entity.abstractEntity.caratteristiche.Skill;
import io.github.ale.screens.gameScreen.entity.player.movement.PlayerMovementManager;
import io.github.ale.screens.gameScreen.entity.skill.SkillSet;
import io.github.ale.screens.gameScreen.entity.skill.skillist.Punch;
import io.github.ale.screens.gameScreen.maps.Map;

public class Player extends Entity {

    private PlayerMovementManager movement;
    private final SkillSet skillset;
    
    private float x=0;
    private float y=0;
    private float dx;
    private float dy;

    private final Array<Vector2> direzioni = new Array<>();
    private int count=0;
    private final float salvaDirezione = 0.5f;
    private float countdown=1f;

    private final float maxDamageTime = 0.273f;
    private float countdownKnockback = 0.273f;
    private float countdownDamage = 0.273f;

    private float angolo;


    // Costruttore
    public Player(EntityConfig config, EntityManager manager) {
        super(config);
        this.manager = manager;
        this.range = new Rectangle(0, 0, 2f, 2f);
        this.skillset = new SkillSet(this);
        this.skillset.add(new Punch(this, "pugno", "un pugno molto forte!"));
        this.create();
    }

    /**
     * inizializzazione player
     */

    @Override
    public final void create() {
        this.movement = new PlayerMovementManager();
    }

    /**
     * disegna l'hitbox del player
     * 
     * @param renderer
     */
    @Override
    public void drawHitbox(ShapeRenderer renderer) {
        if (stati().inCollisione()) {
            renderer.setColor(Color.RED);
        }
        renderer.rect(hitbox().x, hitbox().y, hitbox().width, hitbox().height);
        renderer.setColor(Color.BLACK);
        renderer.rect(range.x, range.y, range.width, range.height);
        renderer.setColor(Color.BLACK);
    }

    public Vector2 predizione(Entity e){
        if ((direzioni.size>=3) && (direzioni.get(0).epsilonEquals(direzioni.get(1).x, direzioni.get(1).y))) 
            return new Vector2(coordinate()).add(direzioni.get(1).x * statistiche().getSpeed() * count, direzioni.get(1).y * statistiche().getSpeed() * count);
        this.count=0;
        return new Vector2(coordinate());
    }

    public Vector2 predizioneCentro(Entity e){
        return predizione(e).add(getSize().getWidth()/2, getSize().getHeight()/2);
    }

    /**
     * setta le animazioni del player a seconda della direzione
     */

    public boolean checkIfDead() {
        // Logica per controllare se il giocatore è morto
        if (statistiche().getHealth() <= 0) {
            this.stati().setIsAlive(false);
            System.out.println("Il giocatore è morto");
            System.out.println("Rianimazione..");
            statistiche().regenHealthTo(100);
        }
        return this.stati().isAlive();
    }

    @Override
    public void hit(float angolo, float damage){
        statistiche().inflictDamage(damage, true);
        dx = (float) Math.cos(Math.toRadians(angolo)) * 6f;
        dy = (float) Math.sin(Math.toRadians(angolo)) * 6f;
        countdownKnockback=0.5f;
        this.angolo=angolo;
        knockback();
    }

    @Override
    protected void knockback() {
        delta=Gdx.graphics.getDeltaTime();
        if (countdownKnockback>=0f) {
            countdownKnockback-=delta;
            dx*=0.9;
            dy*=0.9;
            if(!Map.checkCollisionX(this, 0.1f, angolo)){
                x = dx * delta;
                setX(getX() + x); 
            }
                
            if (!Map.checkCollisionY(this, 0.1f, angolo)) {
                y = dy * delta;
                setY(getY() + y);
            }
 
        }else{
            x = 0;
            y = 0;
        }
    }
    

    @Override
    public void updateEntity() {
        skillset.execute(Punch.class);
        
        if(direzione().x > 0)range.x = coordinateCentro().x+ (float) Math.ceil(direzione().x)-getSize().getWidth()/2;
        else range.x = coordinateCentro().x+ (float) Math.floor(direzione().x)-getSize().getWidth()/2;
        if(direzione().y > 0) range.y = coordinateCentro().y+ (float) Math.ceil(direzione().y)-getSize().getWidth()/2;
        else range.y = coordinateCentro().y+ (float) Math.floor(direzione().y)-getSize().getWidth()/2;

        delta = Gdx.graphics.getDeltaTime();
        limiti();

        hitbox().x = getX() + 0.65f;
        hitbox().y = getY() + 0.55f;
    }

    @Override
    public void updateEntityType() {
       
        movement.update(this);
        cooldown();
        knockback();
        checkIfDead();
    }

    public void cooldown(){
         
        if (statistiche().gotDamaged) {
            countdownDamage -= delta;
            countdownKnockback -= delta;
            if (countdownDamage <= 0) {
                countdownDamage = maxDamageTime;
                countdownKnockback = maxDamageTime;
                statistiche().gotDamaged = false;
            }
        }
        
        if (countdown>0.01f) {
            countdown-=delta;
        }else{
            countdown=salvaDirezione;
            if (direzioni.size<3) {
                direzioni.add(new Vector2(direzione()));
                //System.out.println("aggiunta" + direzione());
            }else{
                //System.out.println(countdown);
                direzioni.set(2, new Vector2(direzioni.get(1)));
                direzioni.set(1, new Vector2(direzioni.get(0)));
                direzioni.set(0, new Vector2(direzione()));
                if (direzioni.get(0).epsilonEquals(direzioni.get(1).x, direzioni.get(1).y)){
                    if(count<2) count++;
                } else count=0;
                //System.out.println("1." + direzioni.get(0));
                //System.out.println("2." + direzioni.get(1));
                //System.out.println("3." + direzioni.get(2));
            }
            
        }
    }

    @Override
    public void drawRange(ShapeRenderer renderer) {
       
    }

    public Skill getSkill(Class<? extends Skill> skillclass){
        return skillset.getSkill(skillclass);
    }
}
