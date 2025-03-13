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
        
        setAree(5.5f, 2f);
        //////////////ADADADADA
    }

    @Override
    public void attack(Player p) {
        if (cooldownAttack <= 0) {
                
            System.out.println("Finn attacca il giocatore!");

            p.getStatistiche().inflictDamage(getStatistiche().getAttackDamage());
            p.getStatistiche().direzioneDanno=getDirezione();
            System.out.println(p.getStatistiche().getHealth());
            
        
            cooldownAttack = ATTACK_COOLDOWN;
        }
    }
}
