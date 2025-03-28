package io.github.ale.screens.game.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;

import io.github.ale.screens.game.entityType.EntityManager;
import io.github.ale.screens.game.maps.Map;
import io.github.ale.screens.game.maps.MapManager;

public class CameraManager {
    private static OrthographicCamera camera;
    private static Vector3[] frustumCorners;
    /**
     * inizializza la telecamera
     */

    public CameraManager(){
        camera = new OrthographicCamera(); // telecamera
        camera.update(); // aggiornamento camera
        frustumCorners = new Vector3[4];
        for (int i = 0; i < 4; i++) {
            frustumCorners[i] = new Vector3(camera.frustum.planePoints[i]);
        }
    }

    public void smoothTransitionTo(Vector2 coords){
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

    public void update(MapManager maps, EntityManager entities, FitViewport viewport, boolean boundaries) {

        float x = camera.viewportWidth / 2;
        float y = camera.viewportHeight / 2;

        smoothTransitionTo(new Vector2(entities.player().coordinateCentro().x, entities.player().coordinateCentro().y));
        if (boundaries) boundaries(new Vector3(x, y, 0), Map.width() - x * 2, Map.height() - y * 2);
        viewport.apply();
        camera.update();


        for (int i = 0; i < 4; i++) {
            frustumCorners[i] = new Vector3(camera.frustum.planePoints[i]);
        }
        //System.out.println();
    }

    public static Vector3[] getFrustumCorners(){
        return frustumCorners;
    }
    public static Vector2 getCenter(){ return new Vector2(frustumCorners[0].x+camera.viewportWidth/2, frustumCorners[0].y+camera.viewportHeight/2);}

    public static boolean isWithinFrustumBounds(float x, float y) {
        // Check if x and y are within the frustum's bounds
        return x > frustumCorners[0].x-2f && y > frustumCorners[0].y-2f && x < frustumCorners[2].x+2f && y < frustumCorners[2].y+2f;
    }

    public OrthographicCamera get() {
        return camera;
    }

    public void setPositionToTarget(Vector2 coords) {
        camera.position.set(coords.x, coords.y, 0);
        camera.update();
    }

    public float getViewportWidth() {return camera.viewportWidth;}
    public float getViewportHeight() {return camera.viewportHeight;}
}
//player.getWorldX() + 2f / 2
