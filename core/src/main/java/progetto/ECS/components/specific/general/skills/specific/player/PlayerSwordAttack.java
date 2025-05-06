package progetto.ECS.components.specific.general.skills.specific.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import progetto.ECS.components.specific.base.PhysicsComponent;
import progetto.ECS.components.specific.general.skills.specific.CombatSkill;
import progetto.ECS.components.specific.movement.DirectionComponent;
import progetto.ECS.components.specific.sensors.InRangeListComponent;
import progetto.ECS.entities.specific.living.combat.Warrior;
import progetto.core.CameraManager;
import progetto.core.game.player.Player;

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
    public void update(float delta) {
        if (isBeingUsed) {
            elapsedTime += delta;
            cooldown.update(delta);
            if (cooldown.isReady) {
                isBeingUsed = false;
                elapsedTime = 0;
                //System.out.println("FINITA");
            }
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(animation.getKeyFrame(elapsedTime),
            owner.get(PhysicsComponent.class).getPosition().x + direction.x - 1f,
            owner.get(PhysicsComponent.class).getPosition().y + direction.y - 1f,
            1f, 1f, // Origin for rotation
            2f, 2f, // Width and height
            1f, 1f, // Scaling factors
            angle);   // Rotation in degrees
    }

    @Override
    public void execute() {
        CameraManager.shakeTheCamera(0.1f, 0.01f);
        owner.entityEngine.game.setTimeScale(1f, 1f);
        cooldown.reset();
        Array<Warrior> inRange = owner.components.get(InRangeListComponent.class).inRange;
        for (Warrior warrior : inRange) {
            warrior.hit(owner, damage, 2);
        }
        direction.set(owner.get(DirectionComponent.class).direction.x, owner.get(DirectionComponent.class).direction.y).setLength(((Player) owner).getRangeRadius());
        angle = owner.get(DirectionComponent.class).direction.angleDeg() - 90;
        isBeingUsed = true;
    }

}
