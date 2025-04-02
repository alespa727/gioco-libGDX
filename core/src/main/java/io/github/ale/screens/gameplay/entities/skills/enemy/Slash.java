package io.github.ale.screens.gameplay.entities.skills.enemy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.ale.screens.gameplay.entities.types.combat.CombatEntity;
import io.github.ale.screens.gameplay.entities.skills.skillType.CombatSkill;

public class Slash extends CombatSkill {

    public Slash(CombatEntity entity, String name, String description, float damage) {
        super(entity, name, description,  damage);
    }


    @Override
    public void update() {

    }

    @Override
    public void draw(SpriteBatch batch) {

    }


    @Override
    public void execute() {
        System.out.println("Melee execute");
        entity.manager.player().hit((CombatEntity) entity, damage);
    }

}
