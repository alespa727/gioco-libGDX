package progetto.gameplay.systems.specific;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Array;
import progetto.gameplay.entities.components.specific.ColorComponent;
import progetto.gameplay.entities.components.specific.ShadowComponent;
import progetto.gameplay.entities.components.specific.DespawnAnimationComponent;
import progetto.gameplay.entities.components.specific.DrawableComponent;
import progetto.gameplay.entities.specific.base.Entity;
import progetto.gameplay.entities.specific.specific.living.Humanoid;
import progetto.gameplay.systems.base.System;
import progetto.graphics.shaders.specific.Flash;

public class DrawingSystem extends System {
    private SpriteBatch batch;
    private float tempoTrascorso=0;

    public DrawingSystem(SpriteBatch batch) {
        this.batch = batch;
    }

    public void draw(SpriteBatch batch, float tempoTrascorso) {
        this.batch = batch;
        this.tempoTrascorso = tempoTrascorso;
    }

    @Override
    public void update(float delta, Array<Entity> list) {
        tempoTrascorso += delta;
        batch.begin();
        for (Entity entity : list) {
            if (entity instanceof Humanoid h) {
                if (entity.componentManager.contains(DespawnAnimationComponent.class)) {
                    drawDespawnAnimation(h, batch, delta);
                    continue;
                }

                if (entity.componentManager.contains(DrawableComponent.class)) {
                    drawDefault(h, batch, tempoTrascorso);
                }
            }
        }
        batch.end();
    }

    public void drawDespawnAnimation(Humanoid entity, SpriteBatch batch, float delta) {
        entity.componentManager.get(DespawnAnimationComponent.class).accumulator += delta;

        // Calcola il progresso interpolato
        float progress = Math.min(entity.componentManager.get(DespawnAnimationComponent.class).accumulator / entity.componentManager.get(DespawnAnimationComponent.class).dissolve_duration, 1f);
        float alpha = Interpolation.fade.apply(1f - progress);

        java.lang.System.out.println(alpha);
        // Applica trasparenza
        batch.setColor(1, 1, 1, alpha);
        batch.draw(entity.getTextures().play(entity, "default" ,tempoTrascorso),
            entity.getPosition().x - entity.getConfig().imageWidth / 2,
            entity.getPosition().y - entity.getConfig().imageHeight / 2,
            entity.getConfig().imageWidth, entity.getConfig().imageHeight);
        batch.setColor(1, 1, 1, 1);

        // Despawn quando finisce
        if (entity.componentManager.get(DespawnAnimationComponent.class).accumulator >= entity.componentManager.get(DespawnAnimationComponent.class).dissolve_duration) {
            entity.despawn();
        }
    }

    public void drawDefault(Humanoid entity, SpriteBatch batch, float tempoTrascorso) {
        boolean applied = false;
        if (entity.getHumanStates().hasBeenHit) {
            Flash.getInstance().apply(batch, Color.RED);
            applied = true;
        }

        batch.draw(entity.getTextures().play(entity, "default" ,tempoTrascorso),
            entity.getPosition().x - entity.getConfig().imageWidth / 2,
            entity.getPosition().y - entity.getConfig().imageHeight / 2,
            entity.getConfig().imageWidth, entity.getConfig().imageHeight);

        if (entity.componentManager.contains(ShadowComponent.class)) {
            batch.draw(entity.componentManager.get(ShadowComponent.class).animation.play(entity, "default" ,tempoTrascorso),
                entity.getPosition().x - entity.getConfig().imageWidth / 2,
                entity.getPosition().y - entity.getConfig().imageHeight / 2,
                entity.getConfig().imageWidth, entity.getConfig().imageHeight);
        }

        if(applied) {
            batch.setShader(null);
        }
    }
}
