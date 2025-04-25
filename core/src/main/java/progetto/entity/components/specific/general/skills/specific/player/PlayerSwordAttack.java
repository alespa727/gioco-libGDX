package progetto.entity.components.specific.general.skills.specific.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import progetto.entity.components.specific.base.PhysicsComponent;
import progetto.entity.components.specific.general.skills.specific.CombatSkill;
import progetto.entity.components.specific.movement.DirectionComponent;
import progetto.entity.components.specific.sensors.InRangeListComponent;
import progetto.entity.entities.specific.living.combat.Warrior;
import progetto.player.ManagerCamera;
import progetto.player.Player;

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
                System.out.println("FINITA");
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
        ManagerCamera.shakeTheCamera(0.1f, 0.01f);
        owner.engine.game.setTimeScale(1f, 1f);
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
