package io.github.ale.screens.game.entities.skill.skillist;

import com.badlogic.gdx.utils.Array;

import io.github.ale.cooldown.Cooldown;
import io.github.ale.screens.game.entityType.combat.CombatEntity;

public class Melee extends CombatSkill {
    private final float damage;
    Array<CombatEntity> inRange;
    Cooldown rangeCooldown;
    public Melee(CombatEntity entity, String name, String description, float damage) {
        super(entity, name, description);
        this.damage=damage;
        rangeCooldown=new Cooldown(0.5f);
    }

    public void draw() {

    }

    @Override
    public void execute() {
        draw();
        entity.manager.player().hit(entity, damage);
    }

}
