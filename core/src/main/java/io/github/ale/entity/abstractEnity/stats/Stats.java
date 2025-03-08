package io.github.ale.entity.abstractEnity.stats;

public class Stats {
    private Health health;

    private final float baseAttackDamage;
    private float attackMultiplier;
    private float attackDamage;

    private final float baseSpeed;
    private float speedMultiplier;
    private float speed;
    
    public Stats(float health, float speed, float attackdmg){
        this.health = new Health(health);
        this.baseAttackDamage = attackdmg;
        this.baseSpeed = speed;
    }

    public float getHealth(){ return this.health.getHp(); } 
    public void regenHealth(float delta){ this.health.setHp(this.health.getHp()+delta);}
    public void inflictDamage(float delta){ this.health.setHp(this.health.getHp()-delta);}

    public void setAttackMultiplier( float attackMultiplier) { this.attackMultiplier = attackMultiplier; }
    public void setSpeedMultiplier( float speedMultiplier) { this.speedMultiplier = speedMultiplier; }

    public float getSpeed() { speed = speedMultiplier*baseSpeed; return speed; }
    public float getAttackDamage() { attackDamage = attackMultiplier * baseAttackDamage; return attackDamage; }

}
