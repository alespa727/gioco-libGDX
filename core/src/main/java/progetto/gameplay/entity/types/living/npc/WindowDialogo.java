package progetto.gameplay.entity.types.living.npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class WindowDialogo extends Window {
    private Label dialogLabel;

    public WindowDialogo(String title, Skin skin) {
        super(title, skin);
        dialogLabel = new Label("", skin);
        dialogLabel.setWrap(true); // fa andare a capo il testo
        this.add(dialogLabel).width(Gdx.graphics.getWidth()); // imposta larghezza fissa, opzionale
        this.pack();
    }

    public void setText(String text) {
        dialogLabel.setText(text);
    }

    public Label getDialogLabel() {
        return dialogLabel;
    }
}
