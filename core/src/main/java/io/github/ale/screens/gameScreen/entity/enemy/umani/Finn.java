package io.github.ale.screens.gameScreen.entity.enemy.umani;

import io.github.ale.screens.gameScreen.entity.EntityManager;
import io.github.ale.screens.gameScreen.entity.abstractEntity.EntityConfig;
import io.github.ale.screens.gameScreen.entity.enemy.abstractEnemy.Nemico;
import io.github.ale.screens.gameScreen.entity.player.Player;

public final class Finn extends Nemico {

    public Finn(EntityConfig config, EntityManager manager, Player p) {
        super(config, manager, p);
        create();
    }

    @Override
    public void updateEntityType() {
        pursue(player().predizioneCentro(this).x, player().predizioneCentro(this).y);
        checkIfDead();
    }


    
    @Override
    public void create() {
    }

    @Override
    public void attack() {
        
        System.out.println("Finn attacca il giocatore!");

        player().statistiche().inflictDamage(statistiche().attackdamage(), player().stati().immortality());

        float angolo = calcolaAngolo(coordinateCentro().x, coordinateCentro().y, player().coordinateCentro().x,
                player().coordinateCentro().y);

        //System.out.println("Angolo di attacco: " + angolo + "°");

        player().hit(angolo, statistiche().attackdamage());// startKnockback(angolo);
        System.out.println(player().statistiche().health());
        
    }

}
