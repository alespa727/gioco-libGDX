package progetto.gameplay.entity.skills.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import progetto.gameplay.entity.skills.CombatSkill;
import progetto.gameplay.entity.types.living.combat.Warriors;
import progetto.gameplay.entity.types.Entity;
import progetto.gameplay.entity.types.living.combat.player.Player;
import progetto.gameplay.manager.ManagerCamera;

public class PlayerSwordAttack extends CombatSkill {

    private final Vector2 direction;
    float angle;

    public PlayerSwordAttack(Player entity, String name, String description, float damage) {
        super(entity, name, description, damage);
        loadTexture("skills/sword_attack", 8, 20);
        direction = new Vector2();
        cooldown.reset(0);
    }

    @Override
    public void update() {
        if (isBeingUsed) {
            elapsedTime += owner.delta;
            cooldown.update(owner.delta);
            if (cooldown.isReady){
                isBeingUsed=false;
                elapsedTime = 0;
            }
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(animation.getKeyFrame(elapsedTime),
            owner.getPosition().x + direction.x - 1f,
            owner.getPosition().y + direction.y - 1f,
            1f, 1f, // Origin for rotation
            2f, 2f, // Width and height
            1f, 1f, // Scaling factors
            angle);   // Rotation in degrees
    }

    @Override
    public void execute() {
        ManagerCamera.shakeTheCamera(0.1f, 0.01f);
        owner.manager.gameInfo.screen.setTimeScale(1f, 1f);
        cooldown.reset();
        Array<Warriors> inRange = ((Player) owner).getInRange();
        for (Warriors warriors : inRange) {
            warriors.hit((Warriors) owner, damage, 2);
        }
        direction.set(owner.direzione().x, owner.direzione().y).setLength(((Player) owner).rangeRadius());
        angle = owner.direzione().angleDeg()-90;
        isBeingUsed=true;
    }

    public Vector2 attackDirection(Entity player, Array<Warriors> entities) {
        float sommax=0, sommay=0, count=0;
        for (Warriors warriors : entities) {
            count++;
            sommax += warriors.getPosition().x;
            sommay += warriors.getPosition().y;
        }
        Vector2 media = new Vector2(sommax/count, sommay/count);
        return media.sub(player.getPosition());
    }
}
