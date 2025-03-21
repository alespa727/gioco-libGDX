package io.github.ale.screens.gameScreen.entity.abstractEntity.caratteristiche;

import io.github.ale.screens.gameScreen.entity.abstractEntity.Entity;

public abstract class Skill {
    
    protected Entity entity;
    private final String name;
    private final String description;

    public float countdown = 0;
    public float cooldown = 0.8f;
    public boolean executed=false;

    public Skill(String name, String description){
        this.name=name;
        this.description=description;
    }

    public String description() {
        return description;
    }

    public String setName() {
        return name;
    }

    public abstract void execute();
}
