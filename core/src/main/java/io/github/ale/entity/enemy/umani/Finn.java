package io.github.ale.entity.enemy.umani;

import io.github.ale.entity.abstractEntity.EntityConfig;
import io.github.ale.entity.enemy.abstractEnemy.Nemico;

public final class Finn extends Nemico{

    public Finn(EntityConfig config){
        super(config);
        create();
    }
    /**
     * inizializza il nemico
     */
    
    @Override
    public void create() {
        
        setAree(5f, 2f);
        
    }
}
