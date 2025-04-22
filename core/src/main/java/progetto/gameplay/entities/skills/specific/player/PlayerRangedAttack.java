package progetto.gameplay.entities.skills.specific.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import progetto.factories.EntityFactory;
import progetto.gameplay.entities.skills.specific.CombatSkill;
import progetto.gameplay.entities.specific.specific.living.Humanoid;
import progetto.gameplay.entities.specific.specific.living.combat.Warrior;
import progetto.gameplay.entities.specific.specific.notliving.Bullet;
import progetto.gameplay.player.ManagerCamera;
import progetto.manager.input.KeyHandler;

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
            Vector3 mouse = ManagerCamera.getInstance().unproject(KeyHandler.mouse);
            Vector2 direction = new Vector2(mouse.x - owner.getPosition().x, mouse.y - owner.getPosition().y);
            direction.nor();
            Vector2 position = new Vector2(owner.getPosition()).add(direction);

            Bullet bullet = (Bullet) owner.engine.summon(EntityFactory.createBullet(position.x, position.y, direction, 0.1f, this.speed, this.damage, owner.engine, Warrior.class));
        } else {
            System.out.println("FINITI I PROIETTILI");
        }


    }
}
