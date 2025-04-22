package progetto.gameplay.entities.skills.specific.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import progetto.gameplay.entities.components.specific.sensors.InRangeListComponent;
import progetto.gameplay.entities.skills.specific.CombatSkill;
import progetto.gameplay.entities.specific.specific.living.combat.Warrior;
import progetto.gameplay.player.ManagerCamera;
import progetto.gameplay.player.Player;

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
        owner.engine.info.screen.setTimeScale(1f, 1f);
        cooldown.reset();
        Array<Warrior> inRange = owner.components.get(InRangeListComponent.class).inRange;
        for (Warrior warrior : inRange) {
            warrior.hit(owner, damage, 2);
        }
        direction.set(owner.getDirection().x, owner.getDirection().y).setLength(((Player) owner).getRangeRadius());
        angle = owner.getDirection().angleDeg() - 90;
        isBeingUsed = true;
    }

}
