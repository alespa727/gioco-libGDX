package io.github.ale.screens.game.entities.skill.skillist.player;

import com.badlogic.gdx.utils.Array;
import io.github.ale.screens.game.entities.player.Player;
import io.github.ale.screens.game.entities.skill.skillist.CombatSkill;
import io.github.ale.screens.game.entityType.combat.CombatEntity;

public class CloseRangeCombatSkill extends CombatSkill {
    private Array<CombatEntity> inRange;

    public CloseRangeCombatSkill(Player entity, String name, String description, float damage) {
        super(entity, name, damage, description);
    }

    @Override
    public void execute() {
        inRange = ((Player) entity).getInRange();
        for (CombatEntity combatEntity : inRange) {
            combatEntity.hit((CombatEntity) entity, damage);
        }
    }
}
