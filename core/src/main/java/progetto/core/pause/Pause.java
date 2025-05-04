package progetto.core.pause;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import progetto.core.App;
import progetto.core.game.GameScreen;
import progetto.core.loading.Loading;
import progetto.core.settings.model.ModelImpostazioni;
import progetto.ECS.components.specific.base.Cooldown;
import progetto.ECS.components.specific.base.PhysicsComponent;
import progetto.graphics.shaders.specific.ColorFilter;
import progetto.input.DebugWindow;
import progetto.core.CameraManager;
import progetto.core.main.MainMenu;
import progetto.world.WorldManager;

public class Pause extends MainMenu {

    public Pause(App app, String title, Color color) {
        super(app, title, color);
    }

    public Pause(App app, String title) {
        super(app, title);
    }

    @Override
    protected void createPlayButton(BitmapFont font, NinePatchDrawable background) {
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.BLACK.cpy().mul(0.40f);
        buttonStyle.up = background;

        TextButton play = new TextButton("Riprendi", buttonStyle);
        play.getLabel().setAlignment(Align.center);
        play.setSize(500, 100);
        play.setPosition(viewport.getWorldWidth() / 2 - play.getWidth() / 2,
                viewport.getWorldHeight() / 2 - play.getHeight() / 2 + 90);

        play.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                app.setScreen(app.gameScreen);
            }
        });

        group.addActor(play);
    }
}
