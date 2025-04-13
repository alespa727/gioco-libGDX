package progetto.gameplay.entity.types.notliving;

import com.badlogic.gdx.graphics.Texture;
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
import progetto.gameplay.manager.ManagerWorld;
import progetto.gameplay.manager.ManagerEntity;

/**
 * Rappresenta un proiettile nel gioco.
 * Il proiettile ha una velocità, danno e raggio specifici.
 * Una volta lanciato, si muove in una direzione e può causare danni agli oggetti con cui entra in collisione.
 */
public class Bullet extends Entity {

    // === Attributi specifici ===
    /** Proprietario del proiettile, ovvero l'entità che lo ha sparato ({@link Entity}) */
    private final Entity owner;

    /** Texture del proiettile */
    private final Texture texture;

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
        this.owner = owner;
        this.texture = Core.assetManager.get("entities/circle.png", Texture.class);
        this.getDirection().set(config.direzione); // Imposta la direzione

        addComponent(new BulletComponent(damage, velocity, radius));
        getComponent(NodeTrackerComponent.class).setAwake(false);
        addComponent(new Cooldown(2));
        getComponent(Cooldown.class).reset();
        getComponent(Cooldown.class).setAwake(false);
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
        ManagerWorld.destroyBody(getPhysics().getBody());
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
        float radius = getComponent(BulletComponent.class).radius;
        Sprite sprite = new Sprite(texture); // Crea uno sprite per il proiettile
        sprite.setSize(radius * 2, radius * 2); // Imposta la dimensione in base al raggio
        sprite.setPosition(getPosition().x - sprite.getWidth() / 2, getPosition().y - sprite.getHeight() / 2); // Posiziona lo sprite
        sprite.draw(batch); // Disegna lo sprite
    }
}
