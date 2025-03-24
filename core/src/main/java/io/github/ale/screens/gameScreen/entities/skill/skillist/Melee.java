package io.github.ale.screens.gameScreen.entities.skill.skillist;

import com.badlogic.gdx.utils.Array;

import io.github.ale.screens.gameScreen.entities.skill.CombatSkill;
import io.github.ale.screens.gameScreen.entityType.combatEntity.CombatEntity;

public class Melee extends CombatSkill {
    private final float damage;
    Array<CombatEntity> inRange;
    public Melee(CombatEntity entity, String name, String description, float damage) {
        super(entity, name, description);
        this.damage=damage;
    }

    public void draw() {

    }

    @Override
    public void execute() {
        draw();
        inRange = entity.manager.combatEntity(entity.range());
        System.out.println(inRange.size);
        for (int i = 0; i < inRange.size; i++) {
            if (!inRange.get(i).getClass().equals(entity.getClass())) {
                inRange.get(i).hit(entity.manager.getAngleToTarget(entity, inRange.get(i)), damage);
                System.out.println("Pugno a " + inRange.get(i).getClass() + " " + inRange.get(i).id());
            }
        }
    }

}
