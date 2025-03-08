package io.github.ale.entity.abstractEnity.movement;

import java.util.ArrayList;
import java.util.Collections;

import io.github.ale.ComandiAzioni;
import io.github.ale.entity.abstractEnity.Entity;

public class EntityMovementManager {

    ArrayList<ComandiAzioni> azioni;
    boolean flag=false;

    int count=0;
    public EntityMovementManager(){
        azioni = new ArrayList<>();
    }
    /**
     * aggiorna i vari parametri in base al movimento
     * @param p
     */

    public void update(Entity entity){
        movimento(entity);
    }

    public void updateAddAzione(ComandiAzioni[] azione){
        Collections.addAll(azioni, azione);
    }

    public void clearAzioni(){
        azioni.clear();
    }

    public void movimento(Entity entity){


        if (azioni.isEmpty()) {
            return;
        }

        entity.setIsMoving(true);

        switch (azioni.get(count).getAzione()) {
            case spostaX -> EntityMovement.spostaX(entity, azioni.get(count).getCoord());
            case spostaY ->EntityMovement.spostaY(entity, azioni.get(count).getCoord());
            case dashX -> EntityMovement.dashX(entity, azioni.get(count).getCoord());
            case dashY -> EntityMovement.dashY(entity, azioni.get(count).getCoord());
            default -> {
            }
        }

        
        if (count==0 && !flag) {
            stampaAzioni();
            flag=true;
        }

        if (count != azioni.size() - 1) {
            if (entity.getIsMoving()==false) {
                count++;
                stampaAzioni();
            }
            
        } 
 
    }
    
    private void stampaAzioni(){
        System.out.println(azioni.get(count).getAzione());
        System.out.println(count);
        System.out.println(azioni.size() -1);
    }
}
