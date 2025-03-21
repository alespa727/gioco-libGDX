package io.github.ale.screens.gameScreen.entity.enemy.abstractEnemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import io.github.ale.screens.gameScreen.entity.EntityManager;
import io.github.ale.screens.gameScreen.entity.abstractEntity.Entity;
import io.github.ale.screens.gameScreen.entity.abstractEntity.EntityConfig;
import io.github.ale.screens.gameScreen.entity.abstractEntity.movement.EntityMovementManager;
import io.github.ale.screens.gameScreen.entity.enemy.abstractEnemy.awareness.EnemyAwareness;
import io.github.ale.screens.gameScreen.entity.enemy.abstractEnemy.state.EnemyState;
import io.github.ale.screens.gameScreen.entity.player.Player;
import io.github.ale.screens.gameScreen.pathfinding.Pathfinder;

public abstract class Nemico extends Entity {
    private final Player player;
    private final Pathfinder pathfinder;

    private final float maxDamageTime = 0.273f;
    private float countdownDamage = 0.273f;

    private final EntityMovementManager movement;
    private final EnemyState stati;
    private EnemyAwareness awareness;

    public final float ATTACK_COOLDOWN = 2f;

    public Nemico(EntityConfig config, EntityManager manager, Player player) {
        super(config);
        this.manager = manager;
        this.player = player;
        this.movement = new EntityMovementManager();
        this.stati = new EnemyState();
        this.awareness = new EnemyAwareness();
        this.range = new Rectangle(0, 0, 1.5f, 1.5f);
        this.pathfinder = new Pathfinder(this);
    }

    public void cooldown() {
        if (statistiche().gotDamaged) {
            countdownDamage -= delta;
            if (countdownDamage <= 0) {
                countdownDamage = maxDamageTime;
                statistiche().gotDamaged = false;
            }
        }
        if (atkCooldown() >= 0) {
            setAtkCooldown(atkCooldown() - delta);
        } else {
            if (manager.entita(range.x, range.y, range.width, range.height).size > 0) {
                setAtkCooldown(1.3f);
                if (manager.isentityinrect(0, range.x, range.y, range.width, range.height)) {
                    attack();
                }
            }
        }
    }

    @Override
    public void updateEntity() {
        delta = Gdx.graphics.getDeltaTime();

        collisionientita();
        cooldown();
        limiti();
        adjustHitbox();
        knockback();

        if (direzione().x > 0)
            range.x = coordinateCentro().x + (float) Math.ceil(direzione().x) - getSize().getWidth() / 2;
        else
            range.x = coordinateCentro().x + (float) Math.floor(direzione().x) - getSize().getWidth() / 2;

        if (direzione().y > 0)
            range.y = coordinateCentro().y + (float) Math.ceil(direzione().y) - getSize().getWidth() / 2;
        else
            range.y = coordinateCentro().y + (float) Math.floor(direzione().y) - getSize().getWidth() / 2;
    }

    @Override
    public void drawRange(ShapeRenderer renderer) {
        renderer.rect(range.x, range.y, range.width, range.height);
        renderer.setColor(Color.BLACK);
    }

    public boolean checkIfDead() {
        if (statistiche().getHealth() <= 0) {
            this.stati().setIsAlive(false);
            despawn();
        }
        return this.stati().isAlive();
    }

    public void drawPath(ShapeRenderer shapeRenderer) {
        pathfinder.drawPath(shapeRenderer);
    }

    public void pursue(float x, float y) {
        pathfinder.renderPath(x, y);
    }

    public void evade(float x, float y) {
        float directionX = x - coordinateCentro().x;
        float directionY = y - coordinateCentro().y;

        Vector2 oppositeDirection = new Vector2(-directionX, -directionY);

        pathfinder.renderPath(coordinateCentro().x + oppositeDirection.x, coordinateCentro().y + oppositeDirection.y);
    }

    public EnemyState states() {
        return stati;
    }

    public EntityMovementManager movement() {
        return movement;
    }

    protected Player player() {
        return player;
    }

    public abstract void attack();

    public EnemyAwareness getAwareness() {
        return awareness;
    }

    public void setAwareness(EnemyAwareness awareness) {
        this.awareness = awareness;
    }

    @Override
    public void drawHitbox(ShapeRenderer renderer) {
        renderer.rect(hitbox().x, hitbox().y, hitbox().width, hitbox().height);
    }
}
