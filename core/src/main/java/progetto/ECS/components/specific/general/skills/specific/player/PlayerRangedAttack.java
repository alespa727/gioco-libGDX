package progetto.ECS.components.specific.general.skills.specific.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import progetto.ECS.components.specific.base.PhysicsComponent;
import progetto.ECS.components.specific.general.skills.specific.CombatSkill;
import progetto.ECS.entities.specific.living.Humanoid;
import progetto.ECS.entities.specific.living.combat.Warrior;
import progetto.ECS.entities.specific.notliving.Bullet;
import progetto.factories.EntityFactory;
import progetto.input.KeyHandler;
import progetto.core.CameraManager;

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
    public void update(float delta) {

    }

    @Override
    public void draw(SpriteBatch batch) {

    }

    @Override
    public void execute() {
        if (bullets > 0) {
            bullets--;
            Vector3 mouse = CameraManager.getInstance().unproject(KeyHandler.mouse);
            Vector2 direction = new Vector2(mouse.x - owner.get(PhysicsComponent.class).getPosition().x, mouse.y - owner.get(PhysicsComponent.class).getPosition().y);
            direction.nor();
            Vector2 position = new Vector2(owner.get(PhysicsComponent.class).getPosition()).add(direction);

            Bullet bullet = (Bullet) owner.entityEngine.summon(EntityFactory.createBullet(position.x, position.y, direction, 0.1f, this.speed, this.damage, owner.entityEngine, Warrior.class));
        } else {
            System.out.println("FINITI I PROIETTILI");
        }


    }
}
