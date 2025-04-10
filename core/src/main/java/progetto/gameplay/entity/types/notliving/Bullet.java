package progetto.gameplay.entity.types.notliving;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import progetto.Core;
import progetto.gameplay.entity.types.Entity;
import progetto.gameplay.entity.types.EntityConfig;
import progetto.gameplay.entity.types.EntityInstance;
import progetto.gameplay.manager.entity.ManagerEntity;
import progetto.gameplay.manager.ManagerWorld;
import progetto.utils.Cooldown;

/**
 * Rappresenta un proiettile nel gioco.
 * Il proiettile ha una velocità, danno e raggio specifici.
 * Una volta lanciato, si muove in una direzione e può causare danni agli oggetti con cui entra in collisione.
 */
public class Bullet extends Entity {

    // === Attributi specifici ===
    /** Proprietario del proiettile, ovvero l'entità che lo ha sparato ({@link Entity}) */
    private final Entity owner;

    /** Danno che il proiettile infligge alle altre entità */
    public final float damage;

    /** Velocità di movimento del proiettile */
    public final float velocity;

    /** Raggio del proiettile */
    public final float radius;

    /** Texture del proiettile */
    private final Texture texture;

    /** Gestore del cooldown del proiettile ({@link Cooldown}) */
    private final Cooldown cooldown;

    /** Stato del cooldown (indica se il cooldown è attivo o meno) */
    private boolean cooldownActive;

    /** Flag per determinare se il proiettile è stato lanciato */
    private boolean flag = false;

    // === Costruttore ===
    /**
     * Costruttore del proiettile.
     * Inizializza i parametri specifici del proiettile come velocità, danno, raggio, e proprietario.
     *
     * @param config configurazione dell'entità ({@link EntityConfig})
     * @param manager gestore delle entità nel gioco ({@link ManagerEntity})
     * @param radius raggio del proiettile
     * @param velocity velocità del proiettile
     * @param damage danno inflitto dal proiettile
     * @param owner entità che ha sparato il proiettile ({@link Entity})
     */
    public Bullet(EntityConfig config, ManagerEntity manager, float radius, float velocity, float damage, Entity owner) {
        super(config, manager);
        this.velocity = velocity;
        this.damage = damage;
        this.radius = radius;
        this.owner = owner;
        this.texture = Core.assetManager.get("entities/circle.png", Texture.class);
        this.cooldownActive = false;
        this.cooldown = new Cooldown(2); // Durata del cooldown
        this.cooldown.reset();
        this.direction.getDirection().set(config.direzione); // Imposta la direzione
    }

    /**
     * Avvia il cooldown per il proiettile.
     *
     * @param time durata del cooldown in secondi
     */
    public void startCooldown(float time) {
        cooldownActive = true;
        this.cooldown.reset(time);
    }

    /**
     * Ottiene il proprietario del proiettile.
     *
     * @return {@link Entity} il proprietario del proiettile
     */
    public Entity getOwner() {
        return owner;
    }

    // === Override metodi principali ===

    /**
     * Aggiorna la logica del proiettile (es. cooldown e distruzione).
     *
     * @param delta tempo trascorso dall'ultimo aggiornamento
     */
    @Override
    public void updateEntity(float delta) {
        if (cooldownActive) {
            cooldown.update(delta); // Aggiorna il cooldown
        }
        if (cooldown.isReady) {
            despawn(); // Distrugge il proiettile quando il cooldown è finito
            System.out.println("DESPAWN");
        }
    }

    /**
     * Aggiorna la logica specifica del tipo di entità (vuoto per i proiettili).
     *
     * @param delta tempo trascorso dall'ultimo aggiornamento
     */
    @Override
    public void updateEntityType(float delta) {
        // Eventuale logica di aggiornamento specifica per il tipo di proiettile
    }

    /**
     * Crea il corpo fisico del proiettile.
     * Imposta la velocità iniziale e il comportamento del corpo fisico.
     */
    @Override
    public void create() {
        physics.getBody().setLinearDamping(0f); // Impedisce rallentamenti
        physics.getBody().setLinearVelocity(new Vector2(getDirection()).scl(velocity)); // Imposta la velocità
        physics.getBody().getFixtureList().get(0).setSensor(true); // Imposta il corpo come sensore (non influisce sulla fisica)
        physics.getBody().setUserData(this); // Associa il proiettile al corpo fisico
    }

    /**
     * Rimuove il proiettile dal mondo di gioco e distrugge il suo corpo fisico.
     *
     * @return una nuova {@link EntityInstance} del proiettile
     */
    @Override
    public EntityInstance despawn() {
        System.out.println("DESPAWN");
        manager.remove(this); // Rimuove il proiettile dal gestore
        Body body = getPhysics().getBody(); // Ottiene il corpo fisico del proiettile
        Gdx.app.postRunnable(() -> ManagerWorld.getInstance().destroyBody(body)); // Distrugge il corpo nel mondo fisico
        return new EntityInstance(this); // Ritorna l'istanza dell'entità
    }

    /**
     * Determina se il proiettile deve essere renderizzato.
     *
     * @param rendered true se il proiettile deve essere renderizzato, false altrimenti
     */
    @Override
    public void setShouldRender(boolean rendered) {
        super.setShouldRender(rendered);
        if (!rendered) {
            despawn(); // Rimuove il proiettile se non deve essere renderizzato
        }
    }

    /**
     * Disegna il proiettile sullo schermo.
     *
     * @param batch {@link SpriteBatch} per disegnare il proiettile
     * @param elapsedTime tempo trascorso dall'ultimo frame
     */
    @Override
    public void draw(SpriteBatch batch, float elapsedTime) {
        Sprite sprite = new Sprite(texture); // Crea uno sprite per il proiettile
        sprite.setSize(radius * 2, radius * 2); // Imposta la dimensione in base al raggio
        sprite.setPosition(getPosition().x - sprite.getWidth() / 2, getPosition().y - sprite.getHeight() / 2); // Posiziona lo sprite
        sprite.draw(batch); // Disegna lo sprite
    }
}
