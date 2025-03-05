package io.github.ale.entity.nemici;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import io.github.ale.Azioni;
import io.github.ale.ComandiAzioni;
import io.github.ale.entity.Direzione;
import io.github.ale.entity.Health;
import io.github.ale.entity.TexturesEntity;
import io.github.ale.entity.player.Player;
import io.github.ale.maps.Map;

public class Nemico {

    public Rectangle hitbox;

    public float x;
    public float y;
    public Direzione direzione;

    public TexturesEntity texture;

    public Animation<TextureRegion> animation;

    public float elapsedTime;

    public Health hp;

    public boolean inCollisione;

    public float baseAttackDamage = 10;
    public float attackMultiplier = 1f;
    public float attackDamage;

    public float cooldownAttack = 0; // Tempo rimanente prima del prossimo attacco
    public float cooldownFollowing = 0; // Tempo rimanente prima del prossimo attacco

    public float baseSpeed = 1.5f;
    public float delta = 1f;
    public float speed;
    public boolean isAlive;


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
    private void create() {
        isAlive = true;
        hasFinishedMoving = true;
        this.x = 8f;
        this.y = 8f;

        baseSpeed = 1.5f;
        delta = 1f;
        baseAttackDamage = 10;
        attackMultiplier = 1f;

        hp = new Health(100);
        texture = new TexturesEntity("Finn.png");
        hitbox = new Rectangle(this.x, this.y, 0.65f, 0.4f);
        range = new Rectangle(this.x, this.y, 2f, 2f);
        direzione = new Direzione();
        movement = new EnemyMovementManager();
        inRange = false;

        direzione.setDirezione("fermoS");
        animation = texture.setAnimazione(direzione);
    }

    /**
     * disegna il nemico
     * @param batch
     */
    public void draw(SpriteBatch batch) {
        elapsedTime += Gdx.graphics.getDeltaTime();

        setAnimation();

        this.x = MathUtils.clamp(x, 0 - 0.65f, Map.getWidth() - hitbox.width - hitbox.width);
        this.y = MathUtils.clamp(y, 0 - 0.55f, Map.getHeight() - hitbox.height - hitbox.height);

        batch.draw(animation.getKeyFrame(elapsedTime, true), x, y, 2, 2);
    }

    /**
     * aggiorna lo stato del nemico
     * @param delta variabile del tempo
     * @param p
     */
    public void update(float delta, Player p) {

        if (followsPlayer) followsPlayer(p, delta);

        movement.update(this);
        
        hitbox.x = this.x + 0.65f;
        hitbox.y = this.y + 0.55f;
        range.x = this.x;
        range.y = this.y;
        if (attacksPlayer) attacksPlayer(p, delta);
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
                comandi[0] = new ComandiAzioni(Azioni.spostaY, p.getWorldY());
                comandi[1] = new ComandiAzioni(Azioni.spostaX, p.getWorldX()+1);
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

    
    /**
     * setta l'animazione attuale da utilizzare
     */
    private void setAnimation() {
        animation = texture.setAnimazione(direzione);
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

        if (Math.abs(this.x - x) > 0.1f) {
            if (this.x < x) {
                this.x += speed * deltaTime;
                direzione.setDirezione("D");
                hasFinishedMoving = false;
                isMovingX = true;
            } else {
                this.x -= speed * deltaTime;
                direzione.setDirezione("A");
                hasFinishedMoving = false;
                isMovingX = true;
            }
        } else {
            this.x = x;
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
        if (Math.abs(this.y - y) > 0.1f) {
            isMovingY = true;

            if (this.y < y) {
                this.y += speed * deltaTime;
                direzione.setDirezione("W");
            } else {
                this.y -= speed * deltaTime;
                direzione.setDirezione("S");
            }

            hasFinishedMoving = false;
        } else {
            this.y = y;
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
        this.x += (x - this.x) * dashSpeed;

        // Controlla se il movimento è terminato
        if (Math.abs(this.x - x) < 0.01f) {
            this.x = x;
            hasFinishedMoving = true;
        } else {
            hasFinishedMoving = false;
        }

        // Imposta la direzione in base alla posizione attuale e alla destinazione
        if (Math.abs(this.x - x) > 0.01f) {
            if (this.x < x) {
                direzione.setDirezione("D");
                hasFinishedMoving = false;
                isDashingX = true;
            } else {
                direzione.setDirezione("A");
                hasFinishedMoving = false;
                isDashingX = true;
            }
        } else {
            this.x = x;
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
        this.y += (y - this.y) * dashSpeed;

        
        if (Math.abs(this.y - y) < 0.01f) {
            this.y = y;
            hasFinishedMoving = true;
        } else {
            hasFinishedMoving = false;
        }

        
        if (Math.abs(this.y - y) > 0.01f) {
            if (this.y < y) {
                direzione.setDirezione("W");
                hasFinishedMoving = false;
                isDashingY = true;
            } else {
                direzione.setDirezione("S");
                hasFinishedMoving = false;
                isDashingY = true;
            }
        } else {
            this.y = y;
            direzione.setDirezione("fermo".concat(direzione.getDirezione()));
            hasFinishedMoving = true;
            isDashingY = false;
        }

    }

}
