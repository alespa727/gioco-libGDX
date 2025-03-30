package io.github.ale.screens.game.entities.skill.skillist.player;

import com.badlogic.gdx.utils.Array;
import io.github.ale.screens.game.entities.player.Player;
import io.github.ale.screens.game.entities.skill.skillist.CombatSkill;
import io.github.ale.screens.game.entityType.combat.CombatEntity;

public class CloseRangeCombatSkill extends CombatSkill {
    Player player;
    public final float damage;
    Array<CombatEntity> inRange;

    public CloseRangeCombatSkill(Player entity, String name, String description, float damage) {
        super(entity, name, description);
        this.player = entity;
        this.damage = damage;
    }

    @Override
    public void execute() {
        inRange = player.getInRange();
        for (CombatEntity combatEntity : inRange) {
            combatEntity.hit(player, damage);
        }
    }
}
