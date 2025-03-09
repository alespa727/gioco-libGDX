package io.github.ale.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class CameraStyles {
    public static void lerpTo(OrthographicCamera camera, Vector2 coords){
        Vector3 position = new Vector3();
        position.x = camera.position.x + (coords.x - camera.position.x) * .1f;
        position.y = camera.position.y + (coords.y - camera.position.y) * .1f;
        position.z = 0;
        camera.position.set(position);
    }
    public static void boundaries(OrthographicCamera camera, Vector3 start, float width, float height){
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
}
//player.getWorldX() + 2f / 2