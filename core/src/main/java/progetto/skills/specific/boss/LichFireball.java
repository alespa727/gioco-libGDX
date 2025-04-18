package progetto.skills.specific.boss;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import progetto.audio.AudioManager;
import progetto.factories.EntityFactory;
import progetto.skills.specific.CombatSkill;
import progetto.entity.specific.specific.living.Humanoid;
import progetto.manager.player.Player;

public class LichFireball extends CombatSkill {
    private final float speed;
    private final Player player;
    private AudioManager mng;
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
        Vector2 direction = new Vector2(player.getPosition().x - owner.getPosition().x, player.getPosition().y - owner.getPosition().y);
        direction.nor();
        Vector2 position = new Vector2(owner.getPosition()).add(direction);
        owner.manager.summon(EntityFactory.createBullet(position.x, position.y, direction, 0.2f, this.speed, this.damage, player));
    }


}
