package progetto.entity.animations;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import progetto.entity.specific.base.Entity;

import java.util.HashMap;

public class AnimationSetMap {

    private final HashMap<String, DefaultAnimationSet> textures;

    public AnimationSetMap(final String[] animation_name, Texture[] img) {
        textures = new HashMap<>();
        for (int i = 0; i < img.length; i++) {
            textures.put(animation_name[i], new DefaultAnimationSet(img[i]));
        }
    }

    public TextureRegion play(Entity e, final String animation_name, final float time) {
        return textures.get(animation_name).play(e, time);
    }
}
