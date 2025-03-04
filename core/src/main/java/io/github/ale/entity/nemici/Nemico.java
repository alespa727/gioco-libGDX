package io.github.ale.entity.nemici;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import io.github.ale.entity.Direzione;
import io.github.ale.entity.Health;
import io.github.ale.entity.TexturesEntity;
import io.github.ale.entity.player.Player;
import io.github.ale.maps.Map;

public class Nemico {
    private Health hp;

    private Rectangle hitbox;
    private Rectangle range;

    protected float x;
    protected float y;
    protected Direzione direzione;

    TexturesEntity enemy;

    private Animation<TextureRegion> animation;

    private float elapsedTime;

    protected boolean inCollisione;
    private boolean inRange;
    private boolean hasFinishedMoving;
    private boolean isMovingX, isMovingY, isDashingX, isDashingY;

    private final float baseAttackDamage = 10;
    private float attackMultiplier = 1f;
    private float attackDamage;

    protected final float baseSpeed = 2.5f;
    protected float delta = 1f;
    protected float speed;
    public boolean isAlive;

    private float cooldownTimer = 0; // Tempo rimanente prima del prossimo attacco
    private final float ATTACK_COOLDOWN = 2.0f; // Cooldown in secondi

    private int counter=0;

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

        hp = new Health(100);
        enemy = new TexturesEntity("Finn.png");
        hitbox = new Rectangle(this.x, this.y, 0.65f, 0.4f);
        range = new Rectangle(this.x, this.y, 2f, 2f);
        direzione = new Direzione();
        movement = new EnemyMovementManager();
        inRange = false;

        direzione.setDirezione("fermoS");
        animation = enemy.setAnimazione(direzione);
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
        if (cooldownTimer > 0) {
            cooldownTimer -= delta;
        }
        if (inRange || !hasFinishedMoving) {
            int[] comandi = new int[4];
            comandi[0] = 0;
            comandi[1] = 1;
            comandi[2] = 2;
            comandi[3] = 3;
            if (counter >= comandi.length) {
                counter=0;
            }
            switch(comandi[counter]){
                case 0 -> spostaX(3);
                case 1 -> spostaY(3);
                case 2 -> spostaX(8);
                case 3 -> spostaY(8);
            }
            
        } //COMANDI PER FAR MUOVERE IL NEMICO

        movement.update(this);
        hitbox.x = this.x + 0.65f;
        hitbox.y = this.y + 0.55f;
        range.x = this.x + 0.65f;
        range.y = this.y + 0.55f;
        inAttackRange(p);
    }

    /**
     * il nemico attacca
     * @param p
     */
    private void attack(Player p) {
        if (cooldownTimer <= 0) {
            
            System.out.println("Nemico attacca il giocatore!");
            p.getHealth().setHp(p.getHealth().getHp()-attackDamage);
            System.out.println(p.getHealth().getHp());
        
            cooldownTimer = ATTACK_COOLDOWN;
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
     * sposta il nemico nella casella specificata dell'asse x
     * @param x
     */
    public void spostaX(float x) {
        if (isMovingY || isDashingY || isDashingX)
            return;

        float deltaTime = Gdx.graphics.getDeltaTime();

        if (Math.abs(this.x - x) > 0.01f) {
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
            direzione.setDirezione("fermoS");
            hasFinishedMoving = true;
            isMovingX = false;
            counter++;
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
        if (Math.abs(this.y - y) > 0.01f) {
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
            direzione.setDirezione("fermoS");
            hasFinishedMoving = true;
            isMovingY = false;
            counter++;
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
            direzione.setDirezione("fermoS");
            hasFinishedMoving = true;
            isDashingX = false;
            counter++;
        }

    }

     /**
     * sposta il nemico con una "dash" nella casella specificata dell'asse y
     * @param x
     */
    public void dashY(float y) {
        if (isMovingX || isMovingY || isDashingY)
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
                direzione.setDirezione("D");
                hasFinishedMoving = false;
                isDashingY = true;
            } else {
                direzione.setDirezione("A");
                hasFinishedMoving = false;
                isDashingY = true;
            }
        } else {
            this.y = y;
            direzione.setDirezione("fermoS");
            hasFinishedMoving = true;
            isDashingY = false;
            counter++;
        }

    }

    /**
     * setta l'animazione attuale da utilizzare
     */
    private void setAnimation() {
        animation = enemy.setAnimazione(direzione);
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
}
