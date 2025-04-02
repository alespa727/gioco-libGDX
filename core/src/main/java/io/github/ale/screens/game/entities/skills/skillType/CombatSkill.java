package io.github.ale.screens.game.entities.skills.skillType;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.ale.screens.game.entities.types.mobs.LivingEntity;
import io.github.ale.screens.game.entities.skills.Skill;

public abstract class CombatSkill extends Skill {
    public final float damage;

    public CombatSkill(LivingEntity entity, String name, String description, float damage) {
        super(entity, name, description);
        this.damage = damage;
    }

}
