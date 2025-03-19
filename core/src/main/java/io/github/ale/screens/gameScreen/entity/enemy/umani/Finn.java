package io.github.ale.screens.gameScreen.entity.enemy.umani;

import io.github.ale.screens.gameScreen.entity.abstractEntity.EntityConfig;
import io.github.ale.screens.gameScreen.entity.enemy.abstractEnemy.Nemico;
import io.github.ale.screens.gameScreen.entity.player.Player;

public final class Finn extends Nemico {

    public Finn(EntityConfig config, Player p) {
        super(config, p);
        create();
    }

    @Override
    public void updateEntityType() {
        pursue(player().predizioneCentro(this).x, player().predizioneCentro(this).y);
    }


    
    @Override
    public void create() {
        setAree(5.5f, 1f);
    }

    @Override
    public void attack() {
        if (atkCooldown() <= 0) {
            System.out.println("Finn attacca il giocatore!");

            player().statistiche().inflictDamage(statistiche().getAttackDamage(), player().stati().immortality());

            float angolo = calcolaAngolo(coordinateCentro().x, coordinateCentro().y, player().coordinateCentro().x,
                    player().coordinateCentro().y);

            System.out.println("Angolo di attacco: " + angolo + "°");

            player().knockbackStart(angolo);// startKnockback(angolo);
            System.out.println(player().statistiche().getHealth());

            setAtkCooldown(ATTACK_COOLDOWN);
        }
    }

}
