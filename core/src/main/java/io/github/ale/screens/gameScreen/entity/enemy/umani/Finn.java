package io.github.ale.screens.gameScreen.entity.enemy.umani;

import io.github.ale.screens.gameScreen.entity.abstractEntity.EntityConfig;
import io.github.ale.screens.gameScreen.entity.enemy.Nemico;
import io.github.ale.screens.gameScreen.entity.player.Player;

public final class Finn extends Nemico{

    public Finn(EntityConfig config, Player p){
        super(config, p);
        create();
    }
    
    public void updateEntityType(){
        
    }
    
    @Override
    public void create() {
        
        setAree(5.5f, 1.5f);
        //////////////ADADADADA
    }

    @Override
    public void attack() {
        if (getAtkCooldown() <= 0) {
                
            System.out.println("Finn attacca il giocatore!");

            player().getStatistiche().inflictDamage(getStatistiche().getAttackDamage());
            player().getStatistiche().direzioneDanno=getDirezione();
            System.out.println(player().getStatistiche().getHealth());
            
        
            setAtkCooldown(ATTACK_COOLDOWN);
        }
    }
}
