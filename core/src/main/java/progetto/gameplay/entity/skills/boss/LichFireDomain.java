package progetto.gameplay.entity.skills.boss;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import progetto.gameplay.entity.skills.Skill;
import progetto.gameplay.entity.skills.skillType.CombatSkill;
import progetto.gameplay.entity.types.Entity;
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
        for (int i = 0; i < 15; i++) {
            float angle = (float)(2 * Math.PI * i / 15) +offset; // da 0 a 2π in 15 passi
            offset++;
            Vector2 direction = new Vector2((float)Math.cos(angle), (float)Math.sin(angle)).nor(); // vettore normalizzato

            entity.manager.createBullet(entity.getPosition().x, entity.getPosition().y, direction, 0.1f, 1f, 20, entity);
        }
    }
}
