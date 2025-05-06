package progetto.ECS.components.specific.general.skills.specific.boss;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import progetto.ECS.components.specific.base.PhysicsComponent;
import progetto.ECS.components.specific.general.AttributeComponent;
import progetto.ECS.components.specific.general.skills.specific.CombatSkill;
import progetto.ECS.entities.specific.living.Humanoid;
import progetto.factories.EntityFactory;

public class LichFireDomain extends CombatSkill {

    float offset = 0f;

    public LichFireDomain(Humanoid entity, String name, String description, float damage) {
        super(entity, name, description, damage);
    }

    /**
     * Metodo che aggiorna lo stato della skill.
     */
    @Override
    public void update(float delta) {

    }

    /**
     * Metodo per disegnare la skill.
     *
     * @param batch SpriteBatch utilizzato per il rendering.
     */
    @Override
    public void draw(SpriteBatch batch) {

    }

    /**
     * Metodo per eseguire la skill.
     */
    @Override
    public void execute() {
        if (owner.get(AttributeComponent.class).health < owner.get(AttributeComponent.class).health/2){
            offset+=36* Gdx.graphics.getDeltaTime();
        }
        for (int i = 0; i < 10; i++) {
            float angle = (float) (2 * Math.PI * i / 10) + offset;
            Vector2 direction = new Vector2((float) Math.cos(angle), (float) Math.sin(angle)).nor();
            owner.entityEngine.summon(EntityFactory.createBullet(owner.get(PhysicsComponent.class).getPosition().x + direction.x, owner.get(PhysicsComponent.class).getPosition().y + direction.y, direction, 0.1f, 2f, 10, owner.entityEngine.player()));
        }
    }
}
