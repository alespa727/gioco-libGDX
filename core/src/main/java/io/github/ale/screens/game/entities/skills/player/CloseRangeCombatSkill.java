package io.github.ale.screens.game.entities.skills.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import io.github.ale.screens.game.entities.types.combat.CombatEntity;
import io.github.ale.screens.game.entities.types.player.Player;
import io.github.ale.screens.game.entities.skills.skillType.CombatSkill;

public class CloseRangeCombatSkill extends CombatSkill {
    private Array<CombatEntity> inRange;

    public CloseRangeCombatSkill(Player entity, String name, String description, float damage) {
        super(entity, name, damage, description);
    }

    @Override
    public void draw(SpriteBatch batch) {

    }

    @Override
    public void execute() {
        inRange = ((Player) entity).getInRange();
        for (CombatEntity combatEntity : inRange) {
            combatEntity.hit((CombatEntity) entity, damage);
        }
    }
}
