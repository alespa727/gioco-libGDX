package progetto.core;

import com.badlogic.gdx.Screen;

public abstract class CustomScreen implements Screen {

    /**
     * Metodo in cui inserire il rendering a schermo
     * @param delta tempo passato dall'ultimo frame
     */
    public abstract void draw(float delta);

}
