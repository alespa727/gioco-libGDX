package io.github.ale.screens.game.entities.skill.skillist.enemy;

import io.github.ale.screens.game.entities.skill.skillist.skillType.CombatSkill;
import io.github.ale.screens.game.entities.entityTypes.combat.CombatEntity;

public class Slash extends CombatSkill {

    public Slash(CombatEntity entity, String name, String description, float damage) {
        super(entity, name, damage,  description);
    }

    /**Disegna l'evento*/
    public void draw() {

    }

    @Override
    public void execute() {
        System.out.println("Melee execute");
        draw();
        entity.manager.player().hit((CombatEntity) entity, damage);
    }

}
