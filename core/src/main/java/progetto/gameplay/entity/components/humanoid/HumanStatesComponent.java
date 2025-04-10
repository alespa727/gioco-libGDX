package progetto.gameplay.entity.components.humanoid;

import progetto.gameplay.entity.components.entity.Cooldown;
import progetto.gameplay.entity.components.IteratableComponent;
import progetto.gameplay.entity.types.living.Humanoid;

public class HumanStatesComponent extends IteratableComponent {
    private final Humanoid humanoid;

    private boolean hasBeenHit = false;
    private boolean hasAnyBuff = false;
    private boolean invulnerable = false;

    private final Cooldown cooldown;

    public HumanStatesComponent(Humanoid humanoid) {
        this.humanoid = humanoid;
        this.cooldown = new Cooldown(.273f);
        this.cooldown.reset();
    }

    /**
     * Verifica se l'umano è stato colpito.
     *
     * @return true se l'umano è stato colpito, false altrimenti.
     */
    public boolean hasBeenHit() {
        return hasBeenHit;
    }

    public void setHasBeenHit(boolean hasBeenHit) {
        this.hasBeenHit = hasBeenHit;
    }

    public boolean hasAnyBuffs() {
        return hasAnyBuff;
    }

    public void setHasAnyBuff(boolean hasAnyBuff) {
        this.hasAnyBuff = hasAnyBuff;
    }

    public boolean isInvulnerable() {
        return invulnerable;
    }

    public void setInvulnerable(boolean invulnerable) {
        this.invulnerable = invulnerable;
    }

    /**
     * Verifica se l'umanoide ha dei buff di salute attivi.
     */
    public void checkIfHaveBuff() {
        hasAnyBuff = humanoid.getHealth() < humanoid.getMaxHealth();
    }

    @Override
    public void update(float delta) {
        checkIfHaveBuff();
        if (hasBeenHit()) {
            cooldown.update(delta);
            if (cooldown.isReady){
                cooldown.reset();
                hasBeenHit = false;
            }
        }
    }
}
