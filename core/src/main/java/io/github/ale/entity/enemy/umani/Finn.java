package io.github.ale.entity.enemy.umani;

import io.github.ale.entity.abstractEntity.EntityConfig;
import io.github.ale.entity.enemy.Nemico;

public final class Finn extends Nemico{

    public Finn(EntityConfig config){
        super(config);
        create();
    }
    
    public void updateEntityType(){
        
    }
    
    @Override
    public void create() {
        
        setAree(5.5f, 2f);
        //////////////ADADADADA
    }
}
