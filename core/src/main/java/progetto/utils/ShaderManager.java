package progetto.utils;

import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.utils.ArrayMap;

public class ShaderManager {

    final ArrayMap<String, Shader> shaders;

    public ShaderManager() {
        shaders = new ArrayMap<>();
    }

    public void addShader(String name, Shader shader) {
        shaders.put(name, shader);
    }

}
