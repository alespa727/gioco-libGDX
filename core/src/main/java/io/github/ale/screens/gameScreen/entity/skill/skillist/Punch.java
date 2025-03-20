package io.github.ale.screens.gameScreen.entity.skill.skillist;

import com.badlogic.gdx.utils.Array;

import io.github.ale.screens.gameScreen.entity.abstractEntity.Entity;
import io.github.ale.screens.gameScreen.entity.abstractEntity.caratteristiche.Skill;
import io.github.ale.screens.gameScreen.entity.player.KeyHandlerPlayer;

public class Punch extends Skill{
    private final KeyHandlerPlayer keyH;
    private final float damage = 20;
    private Array<Entity> inRange;

    public Punch(Entity entity, String name, String description){
        super(name, description);
        this.entity = entity;
        keyH = new KeyHandlerPlayer();
    }

    @Override
    public void execute(){
        executed=false;
        if(countdown > 0){
                countdown-=entity.delta;
        }else{
            keyH.input();
            if(keyH.left_click){
                countdown=cooldown;
                inRange = entity.manager.entita(entity.range().x, entity.range().y, entity.range().width, entity.range().height);
                for(int i=0; i<inRange.size; i++){
                    
                    if(inRange.get(i)!=entity){
                        inRange.get(i).statistiche().inflictDamage(damage, false);
                        inRange.get(i).knockbackStart(entity.calcolaAngolo(entity.coordinateCentro().x, entity.coordinateCentro().y, inRange.get(i).coordinateCentro().x, inRange.get(i).coordinateCentro().y));
                        System.out.println("Pugno a " + inRange.get(i).getClass() + " " + inRange.get(i).id());
                        executed=true;
                        
                    } 
                }
            }
        }
    }
}
