package progetto.gameplay.entity.types.living.combat.npc;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class WindowDialogo extends Window {
    public WindowDialogo(String title, Skin skin) {
        super(title, skin);
    }

    public WindowDialogo(String title, Skin skin, String styleName) {
        super(title, skin, styleName);
    }

    public WindowDialogo(String title, WindowStyle style) {
        super(title, style);
    }
}
