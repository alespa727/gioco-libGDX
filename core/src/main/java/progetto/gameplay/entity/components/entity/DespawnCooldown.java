package progetto.gameplay.entity.components.entity;

public class DespawnCooldown extends Cooldown {
    /**
     * Costruttore che inizializza il cooldown con un tempo massimo.
     * Imposta il tempo rimanente uguale al tempo massimo e imposta isReady su true.
     *
     * @param maxTime Durata del cooldown
     */
    public DespawnCooldown(float maxTime) {
        super(maxTime);
    }
}
