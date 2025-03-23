package io.github.ale.screens.gameScreen.entity.enemy.abstractEnemy;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import io.github.ale.screens.gameScreen.entity.EntityManager;
import io.github.ale.screens.gameScreen.entity.abstractEntity.EntityConfig;
import io.github.ale.screens.gameScreen.entity.combatEntity.CombatEntity;
import io.github.ale.screens.gameScreen.entity.enemy.abstractEnemy.states.States;
import io.github.ale.screens.gameScreen.entity.player.Player;

public abstract class Nemico extends CombatEntity {
    private final Player player;
    public DefaultStateMachine<Nemico, States> statemachine;

    public Nemico(EntityConfig config, EntityManager manager, float attackcooldown, Player player) {
        super(config, manager, attackcooldown);
        this.player = player;
        statemachine = new DefaultStateMachine<>(this);
        this.range = new Rectangle(0, 0, 1.5f, 1.5f);
    }

    @Override
    public void cooldown() {
        damagecooldown();
        attackcooldown();
    }

    @Override
    public void despawn() {
        pathfinder.dispose();
        System.out.println("Entità id " + id() + " despawnata");
        manager.despawn(this);
    }

    @Override
    public void drawRange(ShapeRenderer renderer) {
        renderer.rect(range.x, range.y, range.width, range.height);
        renderer.setColor(Color.BLACK);
    }

    public void drawPath(ShapeRenderer shapeRenderer) {
        pathfinder.drawPath(shapeRenderer);
    }

    public void evade(float x, float y) {
        float directionX = x - coordinateCentro().x;
        float directionY = y - coordinateCentro().y;

        Vector2 oppositeDirection = new Vector2(-directionX, -directionY);

        pathfinder.renderPath(coordinateCentro().x + oppositeDirection.x, coordinateCentro().y + oppositeDirection.y);
    }

    protected Player player() {
        return player;
    }

}
