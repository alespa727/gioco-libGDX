package io.github.ale.entity.abstractEntity.movement;

import java.util.ArrayList;
import java.util.Collections;

import io.github.ale.entity.abstractEntity.Entity;

public class EntityMovementManager {

    private ArrayList<ComandiAzioni> azioni;
    private boolean flag=false;

    private int count=0;
    public EntityMovementManager(){
        inizializzaListaAzioni();
    }

    private void inizializzaListaAzioni(){
        azioni = new ArrayList<>();
    }
    /**
     * aggiorna i vari parametri in base al movimento
     * @param p
     */

    public void update(Entity entity){
        movimentoSuLista(entity);
    }

    public void addAzione(ComandiAzioni[] azione){
        Collections.addAll(azioni, azione);
    }

    public void clearAzioni(){
        azioni.clear();
    }

    public void movimentoSuLista(Entity entity){


        if (azioni.isEmpty()) {
            return;
        }

        entity.getStati().setIsMoving(true);

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
            if (entity.getStati().isMoving()==false) {
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
