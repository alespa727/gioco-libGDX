package io.github.ale.screens.game.entities.skills.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import io.github.ale.screens.game.GameScreen;
import io.github.ale.screens.game.entities.types.combat.CombatEntity;
import io.github.ale.screens.game.entities.types.player.Player;
import io.github.ale.screens.game.entities.skills.skillType.CombatSkill;

public class CloseRangeCombatSkill extends CombatSkill {
    private Array<CombatEntity> inRange;

    private Vector2 direction;
    float angle;

    public CloseRangeCombatSkill(Player entity, String name, String description, float damage) {
        super(entity, name, description, damage);
        loadTexture("skills/sword_attack", 8, 20);
        direction = new Vector2();
    }

    @Override
    public void update() {
        System.out.println(isBeingUsed);
        if (isBeingUsed) {
            elapsedTime += entity.delta;
            cooldown.update(entity.delta);
            if (cooldown.isReady){
                isBeingUsed=false;
                cooldown.reset();
                elapsedTime = 0;
            }
        }
    }

    @Override
    public void draw(SpriteBatch batch) {


        batch.draw(animation.getKeyFrame(elapsedTime),
            entity.getPosition().x + direction.x - 1f,
            entity.getPosition().y + direction.y - 1f,
            1f, 1f, // Origin for rotation
            2f, 2f, // Width and height
            1f, 1f, // Scaling factors
            angle);   // Rotation in degrees
    }

    @Override
    public void execute() {
        inRange = ((Player) entity).getInRange();
        for (CombatEntity combatEntity : inRange) {
            combatEntity.hit((CombatEntity) entity, damage);
        }
        direction.set(entity.direzione().x, entity.direzione().y).setLength(((Player) entity).rangeRadius());
        angle = entity.direzione().angleDeg()-90;
        isBeingUsed=true;
    }

}
