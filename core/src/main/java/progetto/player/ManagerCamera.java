package progetto.player;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import progetto.entity.Engine;
import progetto.entity.components.specific.base.Cooldown;
import progetto.entity.components.specific.base.PhysicsComponent;
import progetto.world.map.Map;

public class ManagerCamera {
    private static OrthographicCamera camera;
    private static Vector3[] frustumCorners;

    private static Cooldown cooldown;
    private static float shakeForce;

    /**
     * inizializza la telecamera
     */

    public static void init() {
        camera = new OrthographicCamera(); // telecamera
        camera.update(); // aggiornamento camera
        frustumCorners = new Vector3[4];
        for (int i = 0; i < 4; i++) {
            frustumCorners[i] = new Vector3(camera.frustum.planePoints[i]);
        }
        cooldown = new Cooldown(0.2f);
    }

    public static Vector3[] getFrustumCorners() {
        return frustumCorners;
    }

    public static void shakeTheCamera(float time, float shakeForce) {
        cooldown.reset(time);
        ManagerCamera.shakeForce = shakeForce;
    }

    public static boolean isWithinFrustumBounds(float x, float y) {
        float offset = 5f;
        // Check if x and y are within the frustum's bounds
        return x > frustumCorners[0].x - offset && y > frustumCorners[0].y - offset && x < frustumCorners[2].x + offset && y < frustumCorners[2].y + offset;
    }

    public static void smoothTransitionTo(Vector2 coords) {
        Vector3 position = new Vector3();
        position.x = camera.position.x + (coords.x - camera.position.x) * 0.06f;
        position.y = camera.position.y + (coords.y - camera.position.y) * 0.06f;
        position.z = 0;
        camera.position.set(position);
    }

    public static void boundaries(Vector3 start, float width, float height) {
        Vector3 position = camera.position;
        if (position.x < start.x) {
            position.x = start.x;
        }
        if (position.y < start.y) {
            position.y = start.y;
        }
        if (position.x > start.x + width) {
            position.x = start.x + width;
        }
        if (position.y > start.y + height) {
            position.y = start.y + height;
        }
        camera.position.set(position);
    }

    /***
     * aggiorna cosa la telecamera deve seguire/modalità della telecamera
     */

    public static void update(Engine entities, FitViewport viewport, float delta, boolean boundaries) {

        float x = camera.viewportWidth / 2;
        float y = camera.viewportHeight / 2;
        cooldown.update(delta);

        if (!cooldown.isReady) shake();
        if (entities.player().contains(PhysicsComponent.class)) {
            smoothTransitionTo(new Vector2(entities.player().get(PhysicsComponent.class).getPosition().x, entities.player().get(PhysicsComponent.class).getPosition().y));
        }
        if (boundaries) boundaries(new Vector3(x, y, 0), Map.width() - x * 2, Map.height() - y * 2);
        viewport.apply();
        camera.update();


        for (int i = 0; i < 4; i++) {
            frustumCorners[i] = new Vector3(camera.frustum.planePoints[i]);
        }
        //System.out.println();
    }

    private static void shake() {
        float offsetX = (float) (Math.random() * 2 - 1) * shakeForce;
        float offsetY = (float) (Math.random() * 2 - 1) * shakeForce;
        camera.position.add(offsetX, offsetY, 0);
        camera.update();
    }

    public static OrthographicCamera getInstance() {
        return camera;
    }

    public static float getViewportWidth() {
        return camera.viewportWidth;
    }

    public static float getViewportHeight() {
        return camera.viewportHeight;
    }
}
//player.getWorldX() + 2f / 2
