package progetto.gameplay.entity.types.living;

import com.badlogic.gdx.graphics.Texture;
import progetto.gameplay.entity.types.GenericEntityTextures;

import java.lang.reflect.Array;
import java.util.EnumMap;

public class HumanoidTextures{

    GenericEntityTextures[] textures;

    public HumanoidTextures(Texture[] img) {
        textures = new GenericEntityTextures[img.length];
        for (int i = 0; i < img.length; i++) {
            textures[i] = new GenericEntityTextures(img[i]);
        }
    }
}
