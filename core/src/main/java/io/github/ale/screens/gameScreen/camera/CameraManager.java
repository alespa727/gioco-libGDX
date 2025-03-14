package io.github.ale.screens.gameScreen.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;

import io.github.ale.screens.gameScreen.entity.EntityManager;
import io.github.ale.screens.gameScreen.maps.Map;
import io.github.ale.screens.gameScreen.maps.MapManager;

public class CameraManager {
    private OrthographicCamera camera;
    /**
     * inizializza la telecamera
     */

    public CameraManager(){
        camera = new OrthographicCamera(); // telecamera
        camera.update(); // aggiornamento camera
    
    }

    public void lerpTo(Vector2 coords){
        Vector3 position = new Vector3();
        position.x = camera.position.x + (coords.x - camera.position.x) * .1f;
        position.y = camera.position.y + (coords.y - camera.position.y) * .1f;
        position.z = 0;
        camera.position.set(position);
    }
    public void boundaries(Vector3 start, float width, float height){
        Vector3 position = camera.position;
        if (position.x < start.x){
            position.x = start.x;
        }
        if (position.y < start.y){
            position.y = start.y;
        }
        if (position.x > start.x + width){
            position.x = start.x + width;
        }
        if (position.y > start.y + height){
            position.y = start.y + height;
        }
        camera.position.set(position);
    }
    
    /***
     * aggiorna cosa la telecamera deve seguire/modalità della telecamera
     */

    public void update(MapManager maps, EntityManager entities, FitViewport viewport) {

        float x = camera.viewportWidth / 2;
        float y = camera.viewportHeight / 2;
        if (!maps.getAmbiente()) { // tipo di telecamera

            lerpTo(new Vector2(Map.getWidth() / 2f, entities.player().getY() + 2f / 2));
            boundaries(new Vector3(x, y, 0), Map.getWidth() - x * 2, Map.getHeight() - y * 2);

            camera.update();
            viewport.apply();

        } else {

            lerpTo(new Vector2(entities.player().getX() + 2f / 2, entities.player().getY() + 2f / 2));
            boundaries(new Vector3(x, y, 0), Map.getWidth() - x * 2, Map.getHeight() - y * 2);
            viewport.apply();
            camera.update();

        }

    }

    public OrthographicCamera get() {
        return camera;
    }
}
//player.getWorldX() + 2f / 2