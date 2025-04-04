package progetto.screens.gameplay.entities.skills.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import progetto.screens.gameplay.entities.skills.skillType.CombatSkill;
import progetto.screens.gameplay.entities.types.combat.CombatEntity;
import progetto.screens.gameplay.entities.types.entity.Entity;
import progetto.screens.gameplay.entities.types.player.Player;
import progetto.utils.camera.CameraManager;

public class CloseRangeCombatSkill extends CombatSkill {
    private Array<CombatEntity> inRange;

    private Vector2 direction;
    float angle;

    public CloseRangeCombatSkill(Player entity, String name, String description, float damage) {
        super(entity, name, description, damage);
        loadTexture("skills/sword_attack", 8, 20);
        direction = new Vector2();
        cooldown.reset(0);
    }

    @Override
    public void update() {
        if (isBeingUsed) {
            elapsedTime += entity.delta;
            cooldown.update(entity.delta);
            if (cooldown.isReady){
                isBeingUsed=false;
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
        CameraManager.shakeTheCamera(0.1f, 0.01f);
        entity.manager.gameInfo.screen.setTimeScale(1f, 1f);
        cooldown.reset();
        inRange = ((Player) entity).getInRange();
        for (CombatEntity combatEntity : inRange) {
            combatEntity.hit((CombatEntity) entity, damage);
        }
        direction.set(entity.direzione().x, entity.direzione().y).setLength(((Player) entity).rangeRadius());
        angle = entity.direzione().angleDeg()-90;
        isBeingUsed=true;
    }

    public Vector2 attackDirection(Entity player, Array<CombatEntity> entities) {
        float sommax=0, sommay=0, count=0;
        for (CombatEntity combatEntity : entities) {
            count++;
            sommax += combatEntity.getPosition().x;
            sommay += combatEntity.getPosition().y;
        }
        Vector2 media = new Vector2(sommax/count, sommay/count);
        return media.sub(player.getPosition());
    }
}
