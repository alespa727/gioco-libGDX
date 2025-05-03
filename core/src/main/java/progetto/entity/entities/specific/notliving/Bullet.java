package progetto.entity.entities.specific.notliving;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import progetto.core.ResourceManager;
import progetto.entity.EntityEngine;
import progetto.entity.components.specific.base.PhysicsComponent;
import progetto.entity.components.specific.general.BulletComponent;
import progetto.entity.components.specific.general.ConfigComponent;
import progetto.entity.components.specific.graphics.ColorComponent;
import progetto.entity.components.specific.movement.DirectionComponent;
import progetto.entity.components.specific.movement.NodeComponent;
import progetto.entity.entities.Entity;
import progetto.entity.entities.specific.EntityConfig;
import progetto.entity.entities.specific.EntityInstance;
import progetto.world.WorldManager;

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
     * @param manager  gestore delle entità nel gioco ({@link EntityEngine})
     * @param radius   raggio del proiettile
     * @param velocity velocità del proiettile
     * @param damage   danno inflitto dal proiettile
     * @param target   entità a cui è sparato il proiettile ({@link Entity})
     */
    public Bullet(EntityConfig config, EntityEngine manager, float radius, float velocity, float damage, Entity target) {
        super(config, manager, 0.1f);
        this.target = target.getClass();
        this.texture = ResourceManager.get().get("particle/particle.png", Texture.class);
        this.get(DirectionComponent.class).direction.set(config.direzione); // Imposta la direzione

        components.add(new BulletComponent(damage, velocity, radius));
        components.get(NodeComponent.class).setAwake(false);
    }

    /**
     * Costruttore del proiettile.
     * Inizializza i parametri specifici del proiettile come velocità, danno, raggio, e target.
     *
     * @param config   configurazione dell'entità ({@link EntityConfig})
     * @param manager  gestore delle entità nel gioco ({@link EntityEngine})
     * @param radius   raggio del proiettile
     * @param velocity velocità del proiettile
     * @param damage   danno inflitto dal proiettile
     * @param target   classe dell'entità a cui è sparato il proiettile ({@link Entity})
     */
    public Bullet(EntityConfig config, EntityEngine manager, float radius, float velocity, float damage, Class<? extends Entity> target) {
        super(config, manager, 0.1f);
        this.target = target;
        this.texture = ResourceManager.get().get("particle/particle.png", Texture.class);
        this.get(DirectionComponent.class).direction.set(config.direzione); // Imposta la direzione

        components.add(new BulletComponent(damage, velocity, radius));
        components.get(NodeComponent.class).setAwake(false);

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
        components.get(PhysicsComponent.class).getBody().setLinearDamping(0f);
        components.get(PhysicsComponent.class).getBody().setLinearVelocity(new Vector2(get(DirectionComponent.class).direction).scl(components.get(BulletComponent.class).velocity)); // Imposta la velocità
        components.get(PhysicsComponent.class).getBody().getFixtureList().get(0).setSensor(true);
        components.get(PhysicsComponent.class).getBody().setUserData(this);
        remove(NodeComponent.class);

        effect.load(Gdx.files.internal("particle/a.p"), Gdx.files.internal("particle"));
        effect.scaleEffect(get(ConfigComponent.class).getConfig().radius / 2);
        effect.start();
    }

    /**
     * Rimuove il proiettile dal mondo di gioco e distrugge il suo corpo fisico.
     *
     * @return una nuova {@link EntityInstance} del proiettile
     */
    @Override
    public EntityInstance unregister() {
        entityEngine.remove(this); // Rimuove il proiettile dal gestore
        WorldManager.destroyBody(components.get(PhysicsComponent.class).getBody());
        return null;
    }
}
