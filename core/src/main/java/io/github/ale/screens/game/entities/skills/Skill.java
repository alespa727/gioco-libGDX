package io.github.ale.screens.game.entities.skills;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.ale.screens.game.entities.types.mobs.LivingEntity;

public abstract class Skill {

    private Texture texture;
    
    public final String name;
    public final String description;
    protected LivingEntity entity;
    private boolean isBeingUsed = false;

    public Skill(LivingEntity entity, String name, String description) {
        this.entity = entity;
        this.name = name;
        this.description = description;
    }

    public boolean isBeingUsed() {
        return isBeingUsed;
    }

    public void setBeingUsed(boolean isBeingUsed) {
        this.isBeingUsed = isBeingUsed;
    }

    public abstract void draw(SpriteBatch batch);
    public abstract void execute();
}
