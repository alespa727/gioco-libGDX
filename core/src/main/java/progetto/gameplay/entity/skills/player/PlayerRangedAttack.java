package progetto.gameplay.entity.skills.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import progetto.gameplay.entity.skills.skillType.CombatSkill;
import progetto.gameplay.entity.types.living.Humanoid;
import progetto.utils.KeyHandler;
import progetto.gameplay.entity.behaviors.manager.camera.CameraManager;

public class PlayerRangedAttack extends CombatSkill {
    final float speed;
    final int maxBullets;
    int bullets;

    public PlayerRangedAttack(Humanoid entity, String name, String description, int bullets, float damage, float speed) {
        super(entity, name, description, damage);
        this.maxBullets = bullets;
        this.bullets = bullets;
        this.speed = speed;
    }

    public void reloadBullets() {
        this.bullets = this.maxBullets;
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(SpriteBatch batch) {

    }

    @Override
    public void execute() {
        if (bullets > 0) {
            bullets--;
            Vector3 mouse = CameraManager.getInstance().unproject(KeyHandler.mouse);
            Vector2 direction=new Vector2(mouse.x-entity.getPosition().x, mouse.y-entity.getPosition().y);
            direction.nor();
            Vector2 position=new Vector2(entity.getPosition()).add(direction);

            entity.manager.createBullet(position.x, position.y, direction, 0.1f, this.speed, this.damage, entity);
        }else{
            System.out.println("FINITI I PROIETTILI");
        }


    }
}
