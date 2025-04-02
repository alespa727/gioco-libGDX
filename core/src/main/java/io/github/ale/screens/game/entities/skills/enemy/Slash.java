package io.github.ale.screens.game.entities.skills.enemy;

import io.github.ale.screens.game.entities.types.combat.CombatEntity;
import io.github.ale.screens.game.entities.skills.skillType.CombatSkill;

public class Slash extends CombatSkill {

    public Slash(CombatEntity entity, String name, String description, float damage) {
        super(entity, name, damage, description);
    }

    /**
     * Disegna l'evento
     */
    public void draw() {

    }

    @Override
    public void execute() {
        System.out.println("Melee execute");
        draw();
        entity.manager.player().hit((CombatEntity) entity, damage);
    }

}
