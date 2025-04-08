package progetto.gameplay.entity.skills.boss;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import progetto.gameplay.entity.skills.CombatSkill;
import progetto.gameplay.entity.types.living.Humanoid;
import progetto.gameplay.entity.types.living.combat.player.Player;

public class LichFireball extends CombatSkill {
    private final float speed;
    private Player player;

    public LichFireball(Humanoid entity, String name, String description, float damage, float speed) {
        super(entity, name, description, damage);
        this.speed = speed;
        this.player = entity.manager.player();
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(SpriteBatch batch) {

    }

    @Override
    public void execute() {
        Vector2 direction = new Vector2(player.getPosition().x - entity.getPosition().x, player.getPosition().y - entity.getPosition().y);
        direction.nor();
        Vector2 position = new Vector2(entity.getPosition()).add(direction);

        entity.manager.createBullet(position.x, position.y, direction, 1f, this.speed, this.damage, entity);
    }


}
