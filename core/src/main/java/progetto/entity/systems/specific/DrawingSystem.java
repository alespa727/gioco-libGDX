package progetto.entity.systems.specific;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import progetto.entity.components.specific.ai.StateComponent;
import progetto.entity.components.specific.base.PhysicsComponent;
import progetto.entity.components.specific.general.ConfigComponent;
import progetto.entity.components.specific.general.RadiusComponent;
import progetto.entity.components.specific.graphics.*;
import progetto.entity.entities.base.Entity;
import progetto.entity.entities.specific.living.Humanoid;
import progetto.entity.entities.specific.living.combat.Warrior;
import progetto.entity.entities.specific.notliving.Bullet;
import progetto.entity.entities.specific.notliving.GameObject;
import progetto.entity.systems.base.IterableSystem;
import progetto.graphics.shaders.specific.Flash;
import progetto.input.DebugWindow;

public class DrawingSystem extends IterableSystem {
    private SpriteBatch batch;
    private float tempoTrascorso = 0;

    public DrawingSystem(SpriteBatch batch) {
        super();
        this.batch = batch;
    }

    public void draw(SpriteBatch batch, float tempoTrascorso) {
        this.batch = batch;
        this.tempoTrascorso = tempoTrascorso;
    }

    @Override
    public void processEntity(Entity entity, float delta) {
        if (!DebugWindow.renderEntities()) {
            batch.begin();
            if (!entity.get(StateComponent.class).shouldBeUpdated()){
                batch.end();
                return;
            }

            if (entity.components.contains(DrawableComponent.class)) {
                drawDefault(entity, batch, tempoTrascorso);
            }
            batch.end();
            return;
        }

        tempoTrascorso = getElapsedTime();

        if (!entity.get(StateComponent.class).shouldBeUpdated()) return;

        batch.begin();

        if (entity instanceof GameObject object) {
            drawBullet(object);
            if (batch.isDrawing()) {
                batch.end();
            }
            return;
        }

        if (entity instanceof Humanoid h) {
            if (entity.components.contains(DespawnAnimationComponent.class)) {
                drawDespawnAnimation(h, batch, delta);
                if (batch.isDrawing()) {
                    batch.end();
                }
                return;
            }

            if (entity.components.contains(DrawableComponent.class)) {
                drawDefault(h, batch, tempoTrascorso);
                if (entity instanceof Warrior w) {
                    drawWarrior(w);
                }
            }

        }
        batch.end();
    }

    public void drawWarrior(Warrior w) {
        w.getSkillset().draw(batch, tempoTrascorso);
    }

    public void drawBullet(GameObject object) {
        if (object instanceof Bullet b) {
            b.effect.setPosition(object.get(PhysicsComponent.class).getPosition().x, object.get(PhysicsComponent.class).getPosition().y); // o qualsiasi posizione iniziale
            b.effect.update(object.engine.delta);
            b.effect.draw(batch);
        }
        float radius = object.components.get(RadiusComponent.class).getRadius();
        Sprite sprite = new Sprite(object.texture); // Crea uno sprite per il proiettile
        sprite.setColor(object.components.get(ColorComponent.class).color);
        sprite.setSize(radius * 2, radius * 2); // Imposta la dimensione in base al raggio
        sprite.setPosition(object.get(PhysicsComponent.class).getPosition().x - sprite.getWidth() / 2, object.get(PhysicsComponent.class).getPosition().y - sprite.getHeight() / 2); // Posiziona lo sprite
        sprite.draw(batch); // Disegna lo sprite
    }

    public void drawDespawnAnimation(Entity entity, SpriteBatch batch, float delta) {
        entity.components.get(DespawnAnimationComponent.class).accumulator += delta;

        // Calcola il progresso interpolato
        float progress = Math.min(entity.components.get(DespawnAnimationComponent.class).accumulator / entity.components.get(DespawnAnimationComponent.class).dissolve_duration, 1f);
        float alpha = Interpolation.fade.apply(1f - progress);

        // Applica trasparenza
        batch.setColor(1, 1, 1, alpha);
        batch.draw(entity.components.get(CustomAnimationComponent.class).getAnimation().play(entity, "default", tempoTrascorso),
            entity.get(PhysicsComponent.class).getPosition().x - entity.get(ConfigComponent.class).getConfig().imageWidth / 2,
            entity.get(PhysicsComponent.class).getPosition().y - entity.get(ConfigComponent.class).getConfig().imageHeight / 2,
            entity.get(ConfigComponent.class).getConfig().imageWidth, entity.get(ConfigComponent.class).getConfig().imageHeight);
        batch.setColor(1, 1, 1, 1);

        // Despawn quando finisce
        if (entity.components.get(DespawnAnimationComponent.class).accumulator >= entity.components.get(DespawnAnimationComponent.class).dissolve_duration) {
            entity.unregister();
        }
    }

    public void drawDefault(Entity entity, SpriteBatch batch, float tempoTrascorso) {
        boolean applied = false;
        if (entity instanceof Humanoid h) {
            if (h.getHumanStates().hasBeenHit) {
                Flash.getInstance().apply(batch, Color.RED);
                applied = true;
            }
        }

        if (entity.components.contains(ShadowComponent.class)) {
            batch.draw(entity.components.get(ShadowComponent.class).animation.play(entity, "default", tempoTrascorso),
                entity.get(PhysicsComponent.class).getPosition().x - entity.get(ConfigComponent.class).getConfig().imageWidth / 2,
                entity.get(PhysicsComponent.class).getPosition().y - entity.get(ConfigComponent.class).getConfig().imageHeight / 2,
                entity.get(ConfigComponent.class).getConfig().imageWidth, entity.get(ConfigComponent.class).getConfig().imageHeight);
        }

        batch.draw(entity.components.get(CustomAnimationComponent.class).getAnimation().play(entity, "default", tempoTrascorso),
            entity.get(PhysicsComponent.class).getPosition().x - entity.get(ConfigComponent.class).getConfig().imageWidth / 2,
            entity.get(PhysicsComponent.class).getPosition().y - entity.get(ConfigComponent.class).getConfig().imageHeight / 2,
            entity.get(ConfigComponent.class).getConfig().imageWidth, entity.get(ConfigComponent.class).getConfig().imageHeight);

        if (applied) {
            batch.setShader(null);
        }
    }
}
