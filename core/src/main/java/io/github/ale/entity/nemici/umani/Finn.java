package io.github.ale.entity.nemici.umani;

import io.github.ale.entity.abstractEntity.EntityConfig;
import io.github.ale.entity.nemici.Nemico;

public final class Finn extends Nemico{

    public Finn(EntityConfig config){
        super(config);
        create();
    }
    
    public void updateEntityType(){
        
    }
    
    @Override
    public void create() {
        
        setAree(5f, 2f);
        
    }
}
