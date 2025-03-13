package io.github.ale.screens.gameScreen.entity.abstractEntity.stats;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Stats {
    private final Health health;

    private final float baseAttackDamage;
    private float attackMultiplier=1f;
    private float attackDamage;

    private final float baseSpeed;
    private float speedMultiplier=1f;
    private float speed;
    
    public Stats(float health, float speed, float attackdmg){
        this.health = new Health(health);
        this.baseAttackDamage = attackdmg;
        this.baseSpeed = speed;
    }

    public Health getHealthObject(){ return this.health; } 
    public float getHealth(){ return this.health.getHp(); } 
    public void regenHealth(float delta){ this.health.setHp(this.health.getHp()+delta);}
    public void regenHealthTo(float health){ this.health.setHp(health);}
    public void inflictDamage(float delta){ this.health.setHp(this.health.getHp()-delta);}

    public void drawHealth(SpriteBatch hud){ this.health.draw(hud);}

    public void setAttackDamageBuff( float attackMultiplier) { this.attackMultiplier = attackMultiplier; }
    public void setSpeedBuff( float speedMultiplier) { this.speedMultiplier = speedMultiplier; }

    public float getSpeed() { speed = speedMultiplier*baseSpeed; return speed; }
    public float getAttackDamage() { attackDamage = attackMultiplier * baseAttackDamage; return attackDamage; }
    public float getSpeedBuff(){ return speedMultiplier;}
}
