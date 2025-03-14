package io.github.ale.screens.gameScreen.entity.abstractEntity.movement;

import java.util.ArrayList;
import java.util.Collections;

import io.github.ale.screens.gameScreen.entity.abstractEntity.Entity;

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

    public void addAzione(ComandiAzioni azione){
        Collections.addAll(azioni, azione);
    }

    public void clearAzioni(){
        azioni.clear();
        count=0;
    }

    public void addizioneAzioniCoordinate(){
        if (count < azioni.size() - 1) {
            count = azioni.size() -1;
        }
    }


    public void movimentoSuLista(Entity entity){


        if (azioni.isEmpty()) {
            return;
        }

        entity.stati().setIsMoving(true);

        addizioneAzioniCoordinate();

        switch (azioni.get(count).getAzione()) {
            case sposta -> EntityMovement.sposta(entity, azioni.get(count).getX(), azioni.get(count).getY());
            default -> {
            }
        }

        
        if (count==0 && !flag) {
            stampaAzioni();
            flag=true;
        }

        if (count != azioni.size() - 1) {
            if (entity.stati().isMoving()==false) {
                count++;
                //stampaAzioni();
            }
            
        } 
 
    }
    
    private void stampaAzioni(){
        System.out.println(azioni.get(count).getAzione());
        System.out.println(count);
        System.out.println(azioni.size() -1);
    }
}
