package progetto.entity.components.specific.general;

import progetto.entity.components.base.Component;
import progetto.entity.components.specific.base.Cooldown;

public class BulletComponent extends Component {
    /**
     * Danno che il proiettile infligge alle altre entità
     */
    public final float damage;

    /**
     * Velocità di movimento del proiettile
     */
    public final float velocity;

    /**
     * Raggio del proiettile
     */
    public final float radius;

    public final Cooldown cooldown = new Cooldown(3f);


    public BulletComponent(float damage, float velocity, float radius) {
        this.damage = damage;
        this.velocity = velocity;
        this.radius = radius;
        cooldown.reset();
    }
}
