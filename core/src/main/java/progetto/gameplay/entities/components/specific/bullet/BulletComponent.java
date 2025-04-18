package progetto.gameplay.entities.components.specific.bullet;

import progetto.gameplay.entities.components.base.Component;

public class BulletComponent extends Component{
    /** Danno che il proiettile infligge alle altre entità */
    public final float damage;

    /** Velocità di movimento del proiettile */
    public final float velocity;

    /** Raggio del proiettile */
    public final float radius;


    public BulletComponent(float damage, float velocity, float radius) {
        this.damage = damage;
        this.velocity = velocity;
        this.radius = radius;
    }
}
