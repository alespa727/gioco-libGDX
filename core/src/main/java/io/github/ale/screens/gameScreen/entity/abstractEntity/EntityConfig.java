package io.github.ale.screens.gameScreen.entity.abstractEntity;

import com.badlogic.gdx.math.Vector2;

public class EntityConfig {
    public int id;
    public float x, y;
    public String imgpath;
    public float width, height;
    public Vector2 direzione;
    public String nome, descrizione;
    public boolean isAlive, inCollisione, isMoving;
    public float hp, speed, attackdmg;
    public float imageWidth, imageHeight;
}
