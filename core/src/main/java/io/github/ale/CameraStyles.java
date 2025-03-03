package io.github.ale;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class CameraStyles {
    public static void lerpTo(OrthographicCamera camera, Vector2 coords){
        float x = camera.position.x + (coords.x - camera.position.x) * .05f;
        float y = camera.position.y + (coords.y - camera.position.y) * .05f;
        camera.position.set(x, y, 0);
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