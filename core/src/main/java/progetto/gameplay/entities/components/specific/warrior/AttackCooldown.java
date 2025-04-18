package progetto.gameplay.entities.components.specific.warrior;

import progetto.gameplay.entities.components.specific.entity.Cooldown;

public class AttackCooldown extends Cooldown{
    /**
     * Costruttore che inizializza il cooldown con un tempo massimo.
     * Imposta il tempo rimanente uguale al tempo massimo e imposta isReady su true.
     *
     * @param maxTime Durata del cooldown
     */
    public AttackCooldown(float maxTime) {
        super(maxTime);
    }
}
