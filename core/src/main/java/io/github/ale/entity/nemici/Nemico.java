package io.github.ale.entity.nemici;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import io.github.ale.Azioni;
import io.github.ale.ComandiAzioni;
import io.github.ale.entity.Direzione;
import io.github.ale.entity.Entity;
import io.github.ale.entity.Health;
import io.github.ale.entity.player.Player;
import io.github.ale.maps.Map;

public final class Nemico extends Entity{

    private boolean inRange;
    private boolean hasFinishedMoving;
    private boolean isMovingX, isMovingY, isDashingX, isDashingY;

    private Rectangle range;

    public final boolean followsPlayer=true;
    public final boolean attacksPlayer=true;

    private final float ATTACK_COOLDOWN = 2f; // Cooldown in secondi
    private final float FOLLOWING_COOLDOWN = 4f;

    EnemyMovementManager movement;

    public Nemico() {
        this.speed = baseSpeed * delta;
        this.attackDamage = baseAttackDamage * attackMultiplier;
        create();
    }

    /**
     * inizializza il nemico
     */
    @Override
    protected void create() {
        isAlive = true;
        hasFinishedMoving = true;
        setX(8f);
        setY(8f);

        baseSpeed = 1.5f;
        delta = 1f;
        baseAttackDamage = 10;
        attackMultiplier = 1f;

        hp = new Health(100);
        setTexture("Finn.png");
        hitbox = new Rectangle(getX(), getY(), 0.65f, 0.4f);
        range = new Rectangle(getX(), getY(), 2f, 2f);
        direzione = new Direzione();
        movement = new EnemyMovementManager();
        inRange = false;

        direzione.setDirezione("fermoS");
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

    }
    

    // Getters
    @Override
    public void setWorldX(float x) {
        setX(x);
    }

    @Override
    public void setWorldY(float y) {
        setY(y);
    }


    public void drawEnemyRange(ShapeRenderer renderer){
        renderer.rect(range.x, range.y, range.width, range.height);
    }

    public boolean getHasFinishedMoving(){
        return hasFinishedMoving;
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
                comandi[0] = new ComandiAzioni(Azioni.spostaY, p.getY());
                comandi[1] = new ComandiAzioni(Azioni.spostaX, p.getX()+1);
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
            p.getHealth().setHp(p.getHealth().getHp()-attackDamage);
            System.out.println(p.getHealth().getHp());
        
            cooldownAttack = ATTACK_COOLDOWN;
        }
    }
    
    /**
     * setta l'animazione attuale da utilizzare
     */
    @Override
    public void setAnimation() {
        animation = getTexture().setAnimazione(direzione);
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
    
    /**
     * cambia il moltiplicatore d'attacco
     * @param attackMultiplier
     */
    public void setAttackMultiplier(float attackMultiplier) {
        this.attackMultiplier = attackMultiplier;
        this.attackDamage = this.baseAttackDamage * this.attackMultiplier;
    }

    /**
     * sposta il nemico nella casella specificata dell'asse x
     * @param x
     */
    public void spostaX(float x) {
        if (isMovingY || isDashingY || isDashingX)
            return;

        float deltaTime = Gdx.graphics.getDeltaTime();

        if (Math.abs(getX() - x) > 0.1f) {
            if (getX() < x) {
                setX(getX() + speed * deltaTime);
                direzione.setDirezione("D");
                hasFinishedMoving = false;
                isMovingX = true;
            } else {
                setX(getX() - speed * deltaTime);
                direzione.setDirezione("A");
                hasFinishedMoving = false;
                isMovingX = true;
            }
        } else {
            setX(x);
            direzione.setDirezione("fermo".concat(direzione.getDirezione()));
            hasFinishedMoving = true;
            isMovingX = false;
        }

    }

    /**
     * sposta il nemico nella casella specificata dell'asse y
     * @param x
     */
    public void spostaY(float y) {
        if (isMovingX || isDashingY || isDashingX)
            return;

        float deltaTime = Gdx.graphics.getDeltaTime();
        if (Math.abs(getY() - y) > 0.1f) {
            isMovingY = true;

            if (getY() < y) {
                setY(getY() + speed * deltaTime);
                direzione.setDirezione("W");
            } else {
                setY(getY() - speed * deltaTime);
                direzione.setDirezione("S");
            }

            hasFinishedMoving = false;
        } else {
            setY(y);
            direzione.setDirezione("fermo".concat(direzione.getDirezione()));
            hasFinishedMoving = true;
            isMovingY = false;
        }

    }

    /**
     * sposta il nemico con una "dash" nella casella specificata dell'asse x
     * @param x
     */
    public void dashX(float x) {
        if (isMovingX || isMovingY || isDashingY)
            return;

        float dashSpeed = 0.045f;
        setX(getX() + (x - getX()) * dashSpeed);

        // Controlla se il movimento è terminato
        if (Math.abs(getX() - x) < 0.01f) {
            setX(x);
            hasFinishedMoving = true;
        } else {
            hasFinishedMoving = false;
        }

        // Imposta la direzione in base alla posizione attuale e alla destinazione
        if (Math.abs(getX() - x) > 0.01f) {
            if (getX() < x) {
                direzione.setDirezione("D");
                hasFinishedMoving = false;
                isDashingX = true;
            } else {
                direzione.setDirezione("A");
                hasFinishedMoving = false;
                isDashingX = true;
            }
        } else {
            setX(x);
            direzione.setDirezione("fermo".concat(direzione.getDirezione()));
            hasFinishedMoving = true;
            isDashingX = false;
        }

    }

     /**
     * sposta il nemico con una "dash" nella casella specificata dell'asse y
     * @param x
     */
    public void dashY(float y) {
        if (isMovingX || isMovingY || isDashingX)
            return;

        float dashSpeed = 0.045f;
        setY(getY() + (y - getY()) * dashSpeed); 

        
        if (Math.abs(getY() - y) < 0.01f) {
            setY(y);
            hasFinishedMoving = true;
        } else {
            hasFinishedMoving = false;
        }

        
        if (Math.abs(getY() - y) > 0.01f) {
            if (getY() < y) {
                direzione.setDirezione("W");
                hasFinishedMoving = false;
                isDashingY = true;
            } else {
                direzione.setDirezione("S");
                hasFinishedMoving = false;
                isDashingY = true;
            }
        } else {
            setY(y);
            direzione.setDirezione("fermo".concat(direzione.getDirezione()));
            hasFinishedMoving = true;
            isDashingY = false;
        }

    }

}
