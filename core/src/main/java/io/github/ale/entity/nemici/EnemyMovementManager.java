package io.github.ale.entity.nemici;

import java.util.ArrayList;
import java.util.Collections;

import io.github.ale.ComandiAzioni;

public class EnemyMovementManager {

    ArrayList<ComandiAzioni> azioni;
    boolean flag=false;

    int count=0;
    public EnemyMovementManager(){
        azioni = new ArrayList<>();
    }
    /**
     * aggiorna i vari parametri in base al movimento
     * @param p
     */

    public void update(Nemico enemy){
        movimento(enemy);
    }

    public void updateAddAzione(ComandiAzioni[] azione){
        Collections.addAll(azioni, azione);
    }

    public void clearAzioni(){
        azioni.clear();
    }

    public void movimento(Nemico enemy){


        if (azioni.isEmpty()) {
            return;
        }
        
        switch (azioni.get(count).getAzione()) {
            case spostaX -> enemy.spostaX(azioni.get(count).getCoord());
            case spostaY -> enemy.spostaY(azioni.get(count).getCoord());
            case dashX -> enemy.dashX(azioni.get(count).getCoord());
            case dashY -> enemy.dashY(azioni.get(count).getCoord());
            default -> {
            }
        }
        
        if (count==0 && !flag) {
            stampaAzioni();
            flag=true;
        }

        if (enemy.getHasFinishedMoving() && count != azioni.size() - 1) {
            count++;
            stampaAzioni();
        } 
 
    }
    
    private void stampaAzioni(){
        System.out.println(azioni.get(count).getAzione());
        System.out.println(count);
        System.out.println(azioni.size() -1);
    }
}
