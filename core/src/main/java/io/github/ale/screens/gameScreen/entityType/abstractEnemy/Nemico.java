package io.github.ale.screens.gameScreen.entityType.abstractEnemy;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import io.github.ale.screens.gameScreen.entityType.EntityManager;
import io.github.ale.screens.gameScreen.entityType.abstractEntity.EntityConfig;
import io.github.ale.screens.gameScreen.entityType.combatEntity.CombatEntity;

public abstract class Nemico extends CombatEntity {

    public Nemico(EntityConfig config, EntityManager manager, float attackcooldown) {
        super(config, manager, attackcooldown);
        this.range = new Rectangle(0, 0, 1.5f, 1.5f);
    }

    @Override
    public void cooldown() {
        damagecooldown();
        attackcooldown();
    }

    public void despawn() {
        super.despawn();
    }

    @Override
    public void drawRange(ShapeRenderer renderer) {
        renderer.rect(range.x, range.y, range.width, range.height);
        renderer.setColor(Color.BLACK);
    }

    public void evade(float x, float y) {
        float directionX = x - coordinateCentro().x;
        float directionY = y - coordinateCentro().y;

        Vector2 oppositeDirection = new Vector2(-directionX, -directionY);

        pathfinder().renderPath(coordinateCentro().x + oppositeDirection.x, coordinateCentro().y + oppositeDirection.y);
    }
}
