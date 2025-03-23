package io.github.ale.screens.gameScreen.entityType.abstractEntity;

import com.badlogic.gdx.math.Vector2;

public class EntityConfig {
    public int id;
    public float x, y;
    public String imgpath;
    public float width, height;
    public Vector2 direzione;
    public String nome, descrizione;
    public boolean isAlive, inCollisione, isMoving;
    public float offsetX, offsetY;
    public float hp, speed, attackdmg;
    public float imageWidth, imageHeight;

    public EntityConfig(){}

    public EntityConfig(EntityConfig config){
        this.id = config.id;
        this.x = config.x;
        this.y = config.y;
        this.imgpath = config.imgpath;
        this.width = config.width;
        this.height = config.height;
        this.direzione = config.direzione;
        this.nome = config.nome;
        this.descrizione = config.descrizione;
        this.isAlive = config.isAlive;
        this.offsetX = config.offsetX;
        this.offsetY = config.offsetY;
        this.inCollisione = config.inCollisione;
        this.isMoving = config.isMoving;
        this.hp = config.hp;
        this.speed = config.speed;
        this.attackdmg = config.attackdmg;
        this.imageWidth = config.imageWidth;
        this.imageHeight = config.imageHeight;
    }
}
