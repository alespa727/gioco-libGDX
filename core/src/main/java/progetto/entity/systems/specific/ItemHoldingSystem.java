package progetto.entity.systems.specific;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import progetto.entity.components.base.ComponentFilter;
import progetto.entity.components.specific.base.PhysicsComponent;
import progetto.entity.components.specific.item.ItemComponent;
import progetto.entity.entities.base.Entity;
import progetto.entity.systems.base.IteratingSystem;
import progetto.player.ManagerCamera;

public class ItemHoldingSystem extends IteratingSystem {

    public ItemHoldingSystem() {
        super(ComponentFilter.all(ItemComponent.class));
    }

    @Override
    public void processEntity(Entity entity, float delta) {
        ItemComponent item = entity.get(ItemComponent.class);
        if (item.getEntity()==null){
            entity.get(PhysicsComponent.class).getBody().getFixtureList().get(0).setSensor(false);
            return;
        }else{
            entity.get(PhysicsComponent.class).getBody().getFixtureList().get(0).setSensor(true);
        }
        Vector2 pos = item.getEntity().get(PhysicsComponent.class).getPosition();

        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        Vector3 proj = ManagerCamera.getInstance().unproject(mousePos);
        Vector3 diff = proj.sub(new Vector3(item.getEntity().get(PhysicsComponent.class).getPosition(), 0));
        Vector2 angolo = new Vector2(diff.x, diff.y);

        item.setAngleDeg(angolo.angleDeg());



        float distance = 0.75f;

        float posx = pos.x + distance * MathUtils.cosDeg(item.getAngleDeg());
        float posy = pos.y + distance * MathUtils.sinDeg(item.getAngleDeg());

        entity.get(PhysicsComponent.class).getBody().setTransform(posx, posy, 0);
    }
}
