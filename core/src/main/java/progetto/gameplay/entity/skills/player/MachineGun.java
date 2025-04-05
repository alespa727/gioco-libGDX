package progetto.gameplay.entity.skills.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import progetto.gameplay.entity.skills.skillType.CombatSkill;
import progetto.gameplay.entity.types.humanEntity.HumanEntity;
import progetto.utils.KeyHandler;
import progetto.gameplay.manager.camera.CameraManager;

public class MachineGun extends CombatSkill {
    final float speed;

    public MachineGun(HumanEntity entity, String name, String description, float damage, float speed) {
        super(entity, name, description, damage);
        this.speed = speed;
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(SpriteBatch batch) {

    }

    @Override
    public void execute() {

        Vector3 mouse = CameraManager.getInstance().unproject(KeyHandler.mouse);
        Vector2 direction=new Vector2(mouse.x-entity.getPosition().x, mouse.y-entity.getPosition().y);
        direction.nor();
        Vector2 position=new Vector2(entity.getPosition()).add(direction);

        entity.manager.createBullet(position.x, position.y, direction, this.speed, this.damage);
    }
}
