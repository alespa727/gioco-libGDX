package progetto.gameplay.entity.components.humanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import progetto.gameplay.entity.components.Component;
import progetto.gameplay.entity.types.Entity;


public class Despawn extends Component{
    private Entity owner;
    private float accumulator = 0f;
    private float dissolve_speed = 0.4f;

    public Despawn(Entity e) {
        this.owner = e;
    }

    public void draw(SpriteBatch batch, TextureRegion texture, float x, float y, float width, float height) {
        if(accumulator < 1.0f){
            accumulator = Interpolation.smooth.apply(accumulator, 1.0f, dissolve_speed);
            batch.setColor(1, 1, 1, 1-accumulator);
            batch.draw(texture, x, y, width, height);
            accumulator+=0.1f* Gdx.graphics.getDeltaTime();
        }else{

            owner.despawn();
        }
    }
}
