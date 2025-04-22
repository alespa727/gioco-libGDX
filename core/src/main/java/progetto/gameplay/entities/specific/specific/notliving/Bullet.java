package progetto.gameplay.entities.specific.specific.notliving;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import progetto.core.Core;
import progetto.gameplay.entities.components.specific.base.PhysicsComponent;
import progetto.gameplay.entities.components.specific.general.BulletComponent;
import progetto.gameplay.entities.components.specific.graphics.ColorComponent;
import progetto.gameplay.entities.components.specific.movement.NodeComponent;
import progetto.gameplay.entities.specific.base.Entity;
import progetto.gameplay.entities.specific.base.EntityConfig;
import progetto.gameplay.entities.specific.base.EntityInstance;
import progetto.manager.entities.Engine;
import progetto.manager.world.WorldManager;

/**
 * Rappresenta un proiettile nel gioco.
 * Il proiettile ha una velocità, danno e raggio specifici.
 * Una volta lanciato, si muove in una direzione e può causare danni agli oggetti con cui entra in collisione.
 */
public class Bullet extends GameObject {

    // === Attributi specifici ===
    /**
     * Classe del target, ovvero l'entità che avrà collisione con il proiettile ({@link Entity})
     */
    private final Class<? extends Entity> target;

    public ParticleEffect effect = new ParticleEffect();

    // === Costruttore ===

    /**
     * Costruttore del proiettile.
     * Inizializza i parametri specifici del proiettile come velocità, danno, raggio, e target.
     *
     * @param config   configurazione dell'entità ({@link EntityConfig})
     * @param manager  gestore delle entità nel gioco ({@link Engine})
     * @param radius   raggio del proiettile
     * @param velocity velocità del proiettile
     * @param damage   danno inflitto dal proiettile
     * @param target   entità a cui è sparato il proiettile ({@link Entity})
     */
    public Bullet(EntityConfig config, Engine manager, float radius, float velocity, float damage, Entity target) {
        super(config, manager, 0.1f);
        this.target = target.getClass();
        this.texture = Core.assetManager.get("particle/particle.png", Texture.class);
        this.getDirection().set(config.direzione); // Imposta la direzione

        components.add(new BulletComponent(damage, velocity, radius));
        components.get(NodeComponent.class).setAwake(false);

        effect.load(Gdx.files.internal("particle/a.p"), Gdx.files.internal("particle"));
        effect.scaleEffect(getConfig().radius / 2);
        effect.start();
    }

    /**
     * Costruttore del proiettile.
     * Inizializza i parametri specifici del proiettile come velocità, danno, raggio, e target.
     *
     * @param config   configurazione dell'entità ({@link EntityConfig})
     * @param manager  gestore delle entità nel gioco ({@link Engine})
     * @param radius   raggio del proiettile
     * @param velocity velocità del proiettile
     * @param damage   danno inflitto dal proiettile
     * @param target   classe dell'entità a cui è sparato il proiettile ({@link Entity})
     */
    public Bullet(EntityConfig config, Engine manager, float radius, float velocity, float damage, Class<? extends Entity> target) {
        super(config, manager, 0.1f);
        this.target = target;
        this.texture = Core.assetManager.get("particle/particle.png", Texture.class);
        this.getDirection().set(config.direzione); // Imposta la direzione

        components.add(new BulletComponent(damage, velocity, radius));
        components.get(NodeComponent.class).setAwake(false);

        effect.load(Gdx.files.internal("particle/a.p"), Gdx.files.internal("particle"));
        effect.scaleEffect(getConfig().radius / 2);
        effect.start();
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
     * Crea il corpo fisico del proiettile.
     * Imposta la velocità iniziale e il comportamento del corpo fisico.
     */
    @Override
    public void create() {
        if (components.get(PhysicsComponent.class).getBody() == null) {
            this.unregister();
            return;
        }
        components.get(ColorComponent.class).color.set(Color.BLACK.cpy());
        components.get(PhysicsComponent.class).getBody().setLinearDamping(0f); // Impedisce rallentamenti
        components.get(PhysicsComponent.class).getBody().setLinearVelocity(new Vector2(getDirection()).scl(components.get(BulletComponent.class).velocity)); // Imposta la velocità
        components.get(PhysicsComponent.class).getBody().getFixtureList().get(0).setSensor(true); // Imposta il corpo come sensore (non influisce sulla fisica)
        components.get(PhysicsComponent.class).getBody().setUserData(this); // Associa il proiettile al corpo fisico
    }

    /**
     * Rimuove il proiettile dal mondo di gioco e distrugge il suo corpo fisico.
     *
     * @return una nuova {@link EntityInstance} del proiettile
     */
    @Override
    public EntityInstance unregister() {
        engine.remove(this); // Rimuove il proiettile dal gestore
        WorldManager.destroyBody(components.get(PhysicsComponent.class).getBody());
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
            this.unregister();
        }
    }

}
