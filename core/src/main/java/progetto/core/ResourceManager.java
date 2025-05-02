package progetto.core;

import com.badlogic.gdx.assets.AssetManager;

public class ResourceManager{
    private static final AssetManager assetManager = new AssetManager();

    public static AssetManager get() {
        return assetManager;
    }
}
