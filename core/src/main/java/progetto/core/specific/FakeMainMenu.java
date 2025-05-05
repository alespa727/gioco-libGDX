package progetto.core.specific;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import progetto.core.App;
import progetto.core.loading.Loading;
import progetto.core.main.MainMenu;

public class FakeMainMenu extends MainMenu {
    public FakeMainMenu(App app, String title, Color color, Runnable runnable) {
        super(app, title, color);
        runnable.run();
        app.setScreen(this);
    }

    @Override
    protected void createPlayButton(BitmapFont font, NinePatchDrawable background) {
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.BLACK.cpy().mul(0.40f);
        buttonStyle.up = background;

        TextButton play = new TextButton("Gioca", buttonStyle);
        play.getLabel().setAlignment(Align.center);
        play.setSize(300, 100);
        play.setPosition(viewport.getWorldWidth() / 2 - play.getWidth() / 2,
                viewport.getWorldHeight() / 2 - play.getHeight() / 2 + 90);

        play.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                app.setScreen(new Loading(app, app.gameScreen, 3, 4.5f));
            }
        });

        group.addActor(play);
    }
}
