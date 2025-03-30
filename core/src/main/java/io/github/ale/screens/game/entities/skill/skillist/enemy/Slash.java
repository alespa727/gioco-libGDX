package io.github.ale.screens.game.entities.skill.skillist.enemy;

import io.github.ale.cooldown.Cooldown;
import io.github.ale.screens.game.entities.skill.skillist.CombatSkill;
import io.github.ale.screens.game.entityType.combat.CombatEntity;

public class Slash extends CombatSkill {
    private final float damage;
    Cooldown rangeCooldown;

    public Slash(CombatEntity entity, String name, String description, float damage) {
        super(entity, name, description);
        this.damage=damage;
        rangeCooldown=new Cooldown(0.5f);
    }

    public void draw() {

    }

    @Override
    public void execute() {
        System.out.println("Melee execute");
        draw();
        entity.manager.player().hit(entity, damage);
    }

}
