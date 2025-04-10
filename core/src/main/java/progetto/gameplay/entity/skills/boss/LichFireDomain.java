package progetto.gameplay.entity.skills.boss;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import progetto.factories.EntityFactory;
import progetto.gameplay.entity.skills.CombatSkill;
import progetto.gameplay.entity.types.living.Humanoid;

public class LichFireDomain extends CombatSkill {
    private float offset=0;

    public LichFireDomain(Humanoid entity, String name, String description, float damage) {
        super(entity, name, description, damage);
    }

    /**
     * Metodo che aggiorna lo stato della skill.
     */
    @Override
    public void update() {

    }

    /**
     * Metodo per disegnare la skill.
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
        System.out.println("Executing skill: " + name);
        for (int i = 0; i < 10; i++) {
            float angle = (float)(2 * Math.PI * i / 10);
            Vector2 direction = new Vector2((float)Math.cos(angle), (float)Math.sin(angle)).nor();

            System.out.println("Creating bullet " + i);
            owner.manager.summon(EntityFactory.createBullet(owner.getPosition().x + direction.x, owner.getPosition().y + direction.y, direction, 0.1f, 2f, 10, owner));
        }
        System.out.println("Skill execution finished");
    }
}
