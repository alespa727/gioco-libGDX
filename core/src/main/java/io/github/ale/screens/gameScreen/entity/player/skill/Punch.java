package io.github.ale.screens.gameScreen.entity.player.skill;

import com.badlogic.gdx.utils.Array;

import io.github.ale.screens.gameScreen.entity.abstractEntity.Entity;
import io.github.ale.screens.gameScreen.entity.abstractEntity.caratteristiche.Skill;
import io.github.ale.screens.gameScreen.entity.player.KeyHandlerPlayer;

public class Punch extends Skill{
    KeyHandlerPlayer keyH;
    Entity entity;
    float countdown = 0;
    float cooldown = 0.2f;
    Array<Entity> inRange;
    public boolean hit=false;
    public Punch(Entity entity){
        this.entity = entity;
        keyH = new KeyHandlerPlayer();
    }

    @Override
    public void attack(){
        hit=false;
        if(countdown > 0){
                countdown-=entity.delta;
        }else{
            keyH.input();
            if(keyH.f){
                countdown=cooldown;
                inRange = entity.manager.entita(entity.range.x, entity.range.y, entity.range.width, entity.range.height);
                System.out.println(entity.range.x);
                for(int i=0; i<inRange.size; i++){
                    
                    if(inRange.get(i)!=entity){
                        inRange.get(i).statistiche().inflictDamage(60, false);
                        if (!hit) {
                            hit=true;
                        }
                    } 
                }
            }
        }
    }
}
