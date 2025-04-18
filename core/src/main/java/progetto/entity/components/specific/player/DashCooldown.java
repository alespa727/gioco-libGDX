package progetto.entity.components.specific.player;

import progetto.entity.components.specific.entity.Cooldown;

public class DashCooldown extends Cooldown{
    /**
     * Costruttore che inizializza il cooldown con un tempo massimo.
     * Imposta il tempo rimanente uguale al tempo massimo e imposta isReady su true.
     *
     * @param maxTime Durata del cooldown
     */
    public DashCooldown(float maxTime) {
        super(maxTime);
    }
}
