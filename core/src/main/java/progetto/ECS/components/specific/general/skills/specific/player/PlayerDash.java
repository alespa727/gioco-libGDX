package progetto.ECS.components.specific.general.skills.specific.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import progetto.core.CameraManager;
import progetto.ECS.components.specific.base.Cooldown;
import progetto.ECS.components.specific.base.PhysicsComponent;
import progetto.ECS.components.specific.general.skills.base.Skill;
import progetto.ECS.components.specific.sensors.InRangeListComponent;
import progetto.ECS.entities.specific.living.Humanoid;
import progetto.core.game.player.Player;

public class PlayerDash extends Skill {
    private final float dodgeSpeed;

    public PlayerDash(Humanoid entity, String name, String description, float dodgeSpeed) {
        super(entity, name, description);
        this.dodgeSpeed = dodgeSpeed;
        cooldown = new Cooldown(2f);
    }

    @Override
    public void update(float delta) {
        if (isBeingUsed) {
            elapsedTime += delta;
            cooldown.update(delta);
            if (owner.components.get(InRangeListComponent.class).inRange.size > 0) {
                owner.entityEngine.game.setTimeScale(0.1f, 1f);
                isBeingUsed = false;
            }
            if (cooldown.isReady) {
                isBeingUsed = false;
                elapsedTime = 0;
            }
        }
    }

    @Override
    public void draw(SpriteBatch batch) {

    }


    @Override
    public void execute() {
        float speed = owner.getMaxSpeed();
        Body body = owner.components.get(PhysicsComponent.class).getBody();
        Vector3 mouseScreenPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        Vector3 mouseWorldPos = CameraManager.getInstance().unproject(mouseScreenPos);
        Vector2 playerPos = body.getPosition().cpy();
        Vector2 mousePos = new Vector2(mouseWorldPos.x, mouseWorldPos.y);
        System.out.println(body.getLinearVelocity().len() +"/"+speed);
        Vector2 direction = mousePos.sub(playerPos).nor().scl(body.getLinearVelocity().len()/speed);
        Vector2 currentVelocity = body.getLinearVelocity();
        Vector2 newVelocity = currentVelocity.add(direction.scl(dodgeSpeed));
        body.setLinearVelocity(newVelocity);
        cooldown.reset(0.5f);
        if (((Player) owner).components.get(InRangeListComponent.class).inRange.size == 0) setBeingUsed(true);
    }
}
