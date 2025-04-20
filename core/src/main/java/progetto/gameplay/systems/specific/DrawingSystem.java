package progetto.gameplay.systems.specific;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Array;
import progetto.gameplay.entities.components.specific.*;
import progetto.gameplay.entities.specific.base.Entity;
import progetto.gameplay.entities.specific.specific.living.Humanoid;
import progetto.gameplay.entities.specific.specific.living.combat.Warrior;
import progetto.gameplay.entities.specific.specific.notliving.Bullet;
import progetto.gameplay.systems.base.System;
import progetto.graphics.shaders.specific.Flash;
import progetto.manager.input.DebugWindow;

public class DrawingSystem extends System {
    private SpriteBatch batch;
    private float tempoTrascorso=0;

    private float freezeTime=0;

    public DrawingSystem(SpriteBatch batch) {
        this.batch = batch;
    }

    public void draw(SpriteBatch batch, float tempoTrascorso) {
        this.batch = batch;
        this.tempoTrascorso = tempoTrascorso;
    }

    @Override
    public void update(float delta, Array<Entity> list) {
        if (!DebugWindow.renderEntities()){
            batch.begin();
            for (Entity entity : list) {
                if (!entity.shouldRender()) continue;

                if (entity.componentManager.contains(DrawableComponent.class)) {
                    drawDefault(entity, batch, tempoTrascorso);
                }
            }
            batch.end();
            return;
        }
        tempoTrascorso += delta;
        batch.begin();
        for (Entity entity : list) {
            if (!entity.shouldRender()) continue;

            if (entity instanceof Bullet bullet){
                drawBullet(bullet);
                continue;
            }

            if (entity instanceof Humanoid h) {
                if (entity.componentManager.contains(DespawnAnimationComponent.class)) {
                    drawDespawnAnimation(h, batch, delta);
                    continue;
                }

                if (entity.componentManager.contains(DrawableComponent.class)) {
                    drawDefault(h, batch, tempoTrascorso);
                    if (entity instanceof Warrior w) {
                        drawWarrior(w);
                    }
                }

            }
        }
        batch.end();
    }

    public void drawWarrior(Warrior w) {
        Sprite sprite = new Sprite(w.weapon);
        sprite.setPosition(w.getPosition().x-1f, w.getPosition().y-1f);
        sprite.setSize(2f, 2f);
        sprite.draw(batch);
        w.getSkillset().draw(batch, tempoTrascorso);
    }

    public void drawBullet(Bullet bullet) {
        bullet.effect.setPosition(bullet.getPosition().x, bullet.getPosition().y); // o qualsiasi posizione iniziale
        bullet.effect.update(bullet.manager.delta);
        bullet.effect.draw(batch);
        float radius = bullet.componentManager.get(BulletComponent.class).radius;
        Sprite sprite = new Sprite(bullet.texture); // Crea uno sprite per il proiettile
        sprite.setColor(bullet.componentManager.get(ColorComponent.class).color);
        sprite.setSize(radius * 2, radius * 2); // Imposta la dimensione in base al raggio
        sprite.setPosition(bullet.getPosition().x - sprite.getWidth() / 2, bullet.getPosition().y - sprite.getHeight() / 2); // Posiziona lo sprite
        sprite.draw(batch); // Disegna lo sprite
    }

    public void drawDespawnAnimation(Entity entity, SpriteBatch batch, float delta) {
        entity.componentManager.get(DespawnAnimationComponent.class).accumulator += delta;

        // Calcola il progresso interpolato
        float progress = Math.min(entity.componentManager.get(DespawnAnimationComponent.class).accumulator / entity.componentManager.get(DespawnAnimationComponent.class).dissolve_duration, 1f);
        float alpha = Interpolation.fade.apply(1f - progress);

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

    public void drawDefault(Entity entity, SpriteBatch batch, float tempoTrascorso) {
        boolean applied = false;
        if (entity instanceof Humanoid h) {
            if (h.getHumanStates().hasBeenHit) {
                Flash.getInstance().apply(batch, Color.RED);
                applied = true;
            }
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
