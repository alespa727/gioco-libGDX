package progetto.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
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
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import progetto.core.Core;
import progetto.core.CoreConfig;
import progetto.core.game.GameScreen;
import progetto.entity.components.specific.base.Cooldown;
import progetto.entity.components.specific.base.PhysicsComponent;
import progetto.graphics.shaders.specific.ColorFilter;
import progetto.player.ManagerCamera;

public class PauseScreen implements Screen {
    final Core game;
    final GameScreen gameScreen;
    final FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/myfont.ttf"));
    final FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
    private final ColorFilter darken;
    private final float duration = 1f; // quanto deve durare l'interpolazione in secondi
    boolean resumeRequest;
    boolean pauseRequest;
    Cooldown pause;
    Cooldown resume;
    FitViewport viewport;
    TextButton MainMenu;
    BitmapFont font;
    float alpha;
    Vector3[] corners;
    float x = 0;
    float y = 0;
    private Texture background;
    private Texture overlay;
    private Stage stage;
    private Table root;
    private Table table;
    private float transitionTime = 0; // quanto tempo è passato

    public PauseScreen(Core game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.darken = ColorFilter.getInstance();
    }

    @Override
    public void show() {
        viewport = gameScreen.viewport;

        stage = new Stage(new ScreenViewport());
        viewport.setCamera(ManagerCamera.getInstance());
        viewport.apply(false);
        Gdx.input.setInputProcessor(stage);
        root = new Table();
        root.setFillParent(true);
        table = new Table();
        table.setSize(stage.getWidth(), stage.getHeight());
        stage.addActor(root);
        root.add(table).pad(20).bottom().fill().expand();

        Table table1 = new Table();

        parameter.size = 50;
        font = generator.generateFont(parameter); // font size 12 pixels
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.LIGHT_GRAY;
        MainMenu = new TextButton("Menu principale", buttonStyle);
        MainMenu.getColor().a = 0;
        MainMenu.addAction(Actions.fadeIn(0.3f, Interpolation.linear));
        MainMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainScreen(game));
            }

            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                MainMenu.getColor().a = 0.7f;
            }

            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                MainMenu.getColor().a = 1f;
            }
        });


        table1.add().fill().expand().row();
        table1.add().fill().expand().row();
        table1.add().fill().expand().row();
        table1.add().fill().expand().row();
        table1.add(MainMenu).expand().row();


        table.add(table1).pad(20).fill().expand();

        System.out.println("PauseScreen loaded");
        resumeRequest = false;
        pauseRequest = true;
        resume = new Cooldown(0.3f);
        pause = new Cooldown(0.7f);
        pause.reset();
        resume.reset();
        gameScreen.getEntityManager().player().getHumanStates().hasBeenHit = false;

        stage.setDebugAll(true);
    }

    @Override
    public void render(float delta) {
        corners = ManagerCamera.getFrustumCorners();
        drawBackground(delta);
    }

    public void drawBackground(float delta) {
        if (pauseRequest) {
            transitionIn(delta);
            return;
        }

        if (resumeRequest) {
            transitionOut(delta);
            return;
        }

        drawPauseMenu(delta);

    }

    public void transitionOut(float delta) {
        resume.update(delta);
        gameScreen.render(delta);

        gameScreen.getGameDrawer().draw(game.batch);

        if (resume.isReady) {
            game.setScreen(gameScreen);
            ManagerCamera.getInstance().position.set(gameScreen.getEntityManager().player().get(PhysicsComponent.class).getPosition(), 0);
            ManagerCamera.getInstance().update();
            resumeRequest = false;
            Gdx.graphics.setForegroundFPS(Gdx.graphics.getDisplayMode().refreshRate);
        }
    }

    public void drawPauseMenu(float delta) {
        if (Gdx.input.isKeyJustPressed(CoreConfig.getRIPRENDIGIOCO())) {
            resumeRequest = true;
        }

        ScreenUtils.clear(0, 0, 0, 1); // Pulisce lo schermo con nero

        gameScreen.getGameDrawer().draw(game.batch);

        stage.act();
        stage.draw();
    }

    public void transitionIn(float delta) {
        // Incrementa il tempo passato
        transitionTime += delta;

        // Calcola il progresso (da 0 a 1, clampato)
        float progress = Math.min(transitionTime / duration, 1f);

        // Applica l'interpolazione tra 0 e 1
        alpha = Interpolation.smoother.apply(0f, 1f, progress);

        pause.update(delta);
        gameScreen.getGameDrawer().draw(game.batch);

        if (pause.isReady) {
            pauseRequest = false;
            System.out.println("Pause request is ready");
        }
    }

    @Override
    public void resize(int width, int height) {
        if (stage != null) {
            stage.getViewport().update(width, height, true);
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

}
