package progetto.entity.components.specific.general.skills.specific.boss;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import progetto.audio.AudioManager;
import progetto.entity.components.specific.base.PhysicsComponent;
import progetto.entity.components.specific.general.skills.specific.CombatSkill;
import progetto.entity.entities.specific.living.Humanoid;
import progetto.factories.EntityFactory;
import progetto.player.Player;

public class LichFireball extends CombatSkill {
    private final float speed;
    private final Player player;
    private AudioManager mng;

    public LichFireball(Humanoid entity, String name, String description, float damage, float speed) {
        super(entity, name, description, damage);
        this.speed = speed;
        this.player = entity.engine.player();
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void draw(SpriteBatch batch) {

    }

    @Override
    public void execute() {
        Vector2 direction = new Vector2(player.get(PhysicsComponent.class).getPosition().x - owner.get(PhysicsComponent.class).getPosition().x, player.get(PhysicsComponent.class).getPosition().y - owner.get(PhysicsComponent.class).getPosition().y);
        direction.nor();
        Vector2 position = new Vector2(owner.get(PhysicsComponent.class).getPosition()).add(direction);
        owner.engine.summon(EntityFactory.createBullet(position.x, position.y, direction, 0.2f, this.speed, this.damage, player));
    }


}
