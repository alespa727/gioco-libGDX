package io.github.ale.screens.gameScreen.entity.skill.skillist;

import com.badlogic.gdx.utils.Array;

import io.github.ale.screens.gameScreen.entity.abstractEntity.caratteristiche.Skill;
import io.github.ale.screens.gameScreen.entity.combatEntity.CombatEntity;
import io.github.ale.screens.gameScreen.entity.livingEntity.LivingEntity;

public class Punch extends Skill {
    private final float damage = 20;
    private Array<LivingEntity> inRange;

    public Punch(CombatEntity entity, String name, String description) {
        super(entity, name, description);
    }

    @Override
    public void execute() {
        inRange = entity.manager.entitaviventi(entity.range().x, entity.range().y, entity.range().width,
                entity.range().height);
        for (int i = 0; i < inRange.size; i++) {
            if (!inRange.get(i).getClass().equals(entity.getClass())) {
                inRange.get(i).hit(entity.manager.calcolaAngoloAttacco(entity, inRange.get(i)), damage);
                System.out.println("Pugno a " + inRange.get(i).getClass() + " " + inRange.get(i).id());
            } 
        }
    }
}
