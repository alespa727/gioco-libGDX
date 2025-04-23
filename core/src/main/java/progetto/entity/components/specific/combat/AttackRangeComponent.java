package progetto.entity.components.specific.combat;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import progetto.entity.components.base.Component;

public class AttackRangeComponent extends Component {

    public Body directionalRange;
    public BodyDef bodyDef = new BodyDef();
    public FixtureDef fixtureDef;
    public Shape shape;

    public float rangeRadius;

    public AttackRangeComponent() {
    }


    public Body getDirectionalRange() {
        return directionalRange;
    }

    public BodyDef getBodyDef() {
        return bodyDef;
    }

    public FixtureDef getFixtureDef() {
        return fixtureDef;
    }

    public Shape getShape() {
        return shape;
    }

    public float getRangeRadius() {
        return rangeRadius;
    }
}
