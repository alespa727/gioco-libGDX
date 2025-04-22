package progetto.gameplay.entities.components.base;

/**
 * Classe astratta base per tutti i componenti nel paradigma ECS (Entity-Component-System).
 * I componenti contengono solo dati e vengono associati a entità.
 * <p>
 * Il flag {@code awake} consente di abilitare o disabilitare temporaneamente il componente.
 */
public abstract class Component {

    /**
     * Indica se il componente è attivo (awake).
     * Se false, il sistema può ignorare temporaneamente questo componente.
     */
    private boolean awake = true;

    /**
     * Verifica se il componente è attivo.
     *
     * @return true se il componente è attivo, false altrimenti.
     */
    public boolean isAwake() {
        return awake;
    }

    /**
     * Imposta lo stato attivo del componente.
     *
     * @param awake true per attivare il componente, false per disattivarlo.
     */
    public void setAwake(boolean awake) {
        this.awake = awake;
    }
}

