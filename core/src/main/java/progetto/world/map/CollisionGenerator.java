package progetto.world.map;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import progetto.entity.Engine;
import progetto.factories.BodyFactory;

public class CollisionGenerator {

    Map map;

    boolean[][] collision;

    public CollisionGenerator(TiledMapTileLayer collisionLayer, Map map) {
        collision = new boolean[Map.width()][Map.height()];
        this.map = map;
        for (int i = 0; i < Map.width(); i++) {
            for (int j = 0; j < Map.height(); j++) {
                TiledMapTileLayer.Cell tile = collisionLayer.getCell(i, j);
                if (tile != null && tile.getTile() != null) {
                    collision[i][j] = true;
                }
            }
        }
    }

    public void generateCollisions(MapLayer collisionLayer) {
        MapObjects objects = collisionLayer.getObjects();
        for (MapObject object : objects) {
            if (object instanceof RectangleMapObject) {  // Check if it's a rectangle, adjust if necessary
                float x = ((RectangleMapObject) object).getRectangle().x * MapManager.TILE_SIZE;
                float y = ((RectangleMapObject) object).getRectangle().y * MapManager.TILE_SIZE;
                float width = ((RectangleMapObject) object).getRectangle().width * MapManager.TILE_SIZE;
                float height = ((RectangleMapObject) object).getRectangle().height * MapManager.TILE_SIZE;
                BodyDef bodyDef = BodyFactory.createBodyDef(BodyDef.BodyType.StaticBody, x + width / 2, y + height / 2);
                Shape boxShape = BodyFactory.createPolygonShape(width / 2, height / 2);

                FixtureDef fixtureDef = BodyFactory.createFixtureDef(boxShape, 1f, 0.1f, 0.1f);
                fixtureDef.filter.groupIndex = Engine.WALL;

                BodyFactory.createBody(map, bodyDef, fixtureDef);
            }
        }
    }

    public boolean[][] getCollision() {
        return collision;
    }
}
