package progetto.gameplay.entity.skills;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import progetto.gameplay.entity.components.IteratableComponent;

public class SkillSet extends IteratableComponent {

    // === CAMPi ===

    private final Array<Skill> skillList;    // Lista delle skill nel set
    private boolean active;                   // Stato del set (attivo o disattivo)

    // === COSTRUTTORE ===

    public SkillSet() {
        skillList = new Array<>();
        active = true; // Imposta il set di skill come attivo inizialmente
    }

    // === METODI ===

    /**
     * Aggiorna lo stato di tutte le skill nel set.
     * Viene chiamato ogni frame per aggiornare il comportamento delle skill.
     */
    @Override
    public void update(float delta) {
        for (Skill skill : skillList) {
            skill.update(); // Aggiorna ogni skill
        }
    }

    /**
     * Disegna le skill che sono in esecuzione sullo schermo.
     * @param batch SpriteBatch per il rendering
     * @param elapsedTime Tempo trascorso per l'animazione delle skill
     */
    public void draw(SpriteBatch batch, float elapsedTime) {
        for (Skill skill : skillList) {
            if(skill.isBeingUsed()) {
                skill.draw(batch); // Disegna solo le skill in uso
            }
        }
    }

    /**
     * Aggiunge una skill al set.
     * @param skill La skill da aggiungere
     */
    public void add(Skill skill) {
        skillList.add(skill);
    }

    /**
     * Rimuove una skill dal set.
     * @param skill La skill da rimuovere
     */
    public void remove(Skill skill) {
        skillList.removeValue(skill, false);
    }

    /**
     * Restituisce una skill in base alla sua classe.
     * @param skillClass La classe della skill da cercare
     * @return La skill corrispondente alla classe specificata, o null se non trovata
     */
    public Skill getSkill(Class<? extends Skill> skillClass) {
        if (!active) return null;

        for (Skill skill : skillList) {
            if (skill.getClass().equals(skillClass)) {
                return skill; // Restituisce la skill se trovata
            }
        }
        return null; // Restituisce null se la skill non è nel set
    }

    /**
     * Restituisce la lista delle skill nel set.
     * @return Lista di tutte le skill nel set
     */
    public Array<Skill> getSkillList() {
        return skillList;
    }

    /**
     * Disabilita il set di skill, impedendo l'esecuzione di nuove skill.
     */
    private void disable() {
        if (active) active = false;
    }

    /**
     * Abilita il set di skill, permettendo l'esecuzione di skill.
     */
    private void enable() {
        if (!active) active = true;
    }

    /**
     * Esegue una skill specificata dalla sua classe.
     * @param skillClass La classe della skill da eseguire
     */
    public void execute(Class<? extends Skill> skillClass) {
        Skill skill = getSkill(skillClass); // Ottiene la skill dal set
        if (skill != null) {
            skill.execute(); // Esegue la skill se trovata
        }
    }
}

