package progetto.gameplay.entity.components;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;

public class BodyComponent {

    public Object userdata;
    public BodyDef bodyDef;
    public FixtureDef fixtureDef;
    public Shape shape;

}
