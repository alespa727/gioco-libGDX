package progetto.gameplay.entity.types.notliving;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import progetto.Core;
import progetto.gameplay.entity.components.bullet.BulletComponent;
import progetto.gameplay.entity.components.entity.Cooldown;
import progetto.gameplay.entity.components.entity.NodeTrackerComponent;
import progetto.gameplay.entity.types.Entity;
import progetto.gameplay.entity.types.EntityConfig;
import progetto.gameplay.entity.types.EntityInstance;
import progetto.gameplay.manager.entity.EntityManager;
import progetto.gameplay.manager.WorldManager;

/**
 * Rappresenta un proiettile nel gioco.
 * Il proiettile ha una velocità, danno e raggio specifici.
 * Una volta lanciato, si muove in una direzione e può causare danni agli oggetti con cui entra in collisione.
 */
public class Bullet extends Entity {

    // === Attributi specifici ===
    /** Classe del target, ovvero l'entità che avrà collisione con il proiettile ({@link Entity}) */
    private final Class<? extends Entity> target;

    /** Texture del proiettile */
    private final Texture texture;

    ParticleEffect effect = new ParticleEffect();

    // === Costruttore ===
    /**
     * Costruttore del proiettile.
     * Inizializza i parametri specifici del proiettile come velocità, danno, raggio, e target.
     *
     * @param config configurazione dell'entità ({@link EntityConfig})
     * @param manager gestore delle entità nel gioco ({@link EntityManager})
     * @param radius raggio del proiettile
     * @param velocity velocità del proiettile
     * @param damage danno inflitto dal proiettile
     * @param target entità a cui è sparato il proiettile ({@link Entity})
     */
    public Bullet(EntityConfig config, EntityManager manager, float radius, float velocity, float damage, Entity target) {
        super(config, manager);
        this.target = target.getClass();
        this.texture = Core.assetManager.get("particle/particle.png", Texture.class);
        this.getDirection().set(config.direzione); // Imposta la direzione

        addComponent(new BulletComponent(damage, velocity, radius));
        getComponent(NodeTrackerComponent.class).setAwake(false);
        addComponent(new Cooldown(2));
        getComponent(Cooldown.class).reset();
        getComponent(Cooldown.class).setAwake(false);

        effect.load(Gdx.files.internal("particle/a.p"), Gdx.files.internal("particle"));
        effect.scaleEffect(getConfig().radius/2);
        effect.start();
    }

    /**
     * Costruttore del proiettile.
     * Inizializza i parametri specifici del proiettile come velocità, danno, raggio, e target.
     *
     * @param config configurazione dell'entità ({@link EntityConfig})
     * @param manager gestore delle entità nel gioco ({@link EntityManager})
     * @param radius raggio del proiettile
     * @param velocity velocità del proiettile
     * @param damage danno inflitto dal proiettile
     * @param target classe dell'entità a cui è sparato il proiettile ({@link Entity})
     */
    public Bullet(EntityConfig config, EntityManager manager, float radius, float velocity, float damage, Class<? extends Entity> target) {
        super(config, manager);
        this.target = target;
        this.texture = Core.assetManager.get("particle/particle.png", Texture.class);
        this.getDirection().set(config.direzione); // Imposta la direzione

        addComponent(new BulletComponent(damage, velocity, radius));
        getComponent(NodeTrackerComponent.class).setAwake(false);
        addComponent(new Cooldown(2));
        getComponent(Cooldown.class).reset();
        getComponent(Cooldown.class).setAwake(false);

        effect.load(Gdx.files.internal("particle/a.p"), Gdx.files.internal("particle"));
        effect.scaleEffect(getConfig().radius/2);
        effect.start();
    }

    /**
     * Avvia il cooldown per il proiettile.
     *
     * @param time durata del cooldown in secondi
     */
    public void startCooldown(float time) {
        getComponent(Cooldown.class).reset(time);
        getComponent(Cooldown.class).setAwake(true);
    }

    /**
     * Ottiene la classe con cui avrà collisioni
     *
     * @return {@link Entity} target
     */
    public Class<? extends Entity> getTargetClass() {
        return target;
    }

    // === Override metodi principali ===

    /**
     * Aggiorna la logica del proiettile (es. cooldown e distruzione).
     *
     * @param delta tempo trascorso dall'ultimo aggiornamento
     */
    @Override
    public void updateEntity(float delta) {
        Cooldown cooldown = getComponent(Cooldown.class);
        if (cooldown.isReady) {
            cooldown.isReady = false;
            despawn();
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
        if (getPhysics().getBody() == null) {
            despawn();
            return;
        }
        setColor(Color.BLACK);
        getPhysics().getBody().setLinearDamping(0f); // Impedisce rallentamenti
        getPhysics().getBody().setLinearVelocity(new Vector2(getDirection()).scl(getComponent(BulletComponent.class).velocity)); // Imposta la velocità
        getPhysics().getBody().getFixtureList().get(0).setSensor(true); // Imposta il corpo come sensore (non influisce sulla fisica)
        getPhysics().getBody().setUserData(this); // Associa il proiettile al corpo fisico
    }

    /**
     * Rimuove il proiettile dal mondo di gioco e distrugge il suo corpo fisico.
     *
     * @return una nuova {@link EntityInstance} del proiettile
     */
    @Override
    public EntityInstance despawn() {
        manager.remove(this); // Rimuove il proiettile dal gestore
        WorldManager.destroyBody(getPhysics().getBody());
        return null;
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
            despawn();
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
        effect.setPosition(getPosition().x, getPosition().y); // o qualsiasi posizione iniziale
        effect.update(manager.delta);
        effect.draw(batch);
        float radius = getComponent(BulletComponent.class).radius;
        Sprite sprite = new Sprite(texture); // Crea uno sprite per il proiettile
        sprite.setColor(color);
        sprite.setSize(radius * 2, radius * 2); // Imposta la dimensione in base al raggio
        sprite.setPosition(getPosition().x - sprite.getWidth() / 2, getPosition().y - sprite.getHeight() / 2); // Posiziona lo sprite
        sprite.draw(batch); // Disegna lo sprite
    }
}
