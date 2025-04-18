package progetto.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import progetto.core.Core;

public class MainScreen implements Screen {
    private final Core game;
    private Skin skin;
    private Stage stage;
    private Table root;
    private Table table;

    private boolean playRequest;

    public MainScreen(Core game) {
        this.game = game;
    }

    @Override
    public void show() { //come un costruttore praticamente
        stage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(stage);
        root = new Table();
        root.setFillParent(true);
        skin = new Skin(Gdx.files.internal("skins/metal-ui.json"));
        table = new Table();
        stage.addActor(root);
        root.add(table);
        //toplayerDialog dialog = new toplayerDialog(); //tolto momwnaneamente
        bottonePlay();

        bottoneSettings();


        TextButton button3 = new TextButton("Salvataggi", skin);
        table.add(button3);

        table.row();

        TextButton button4 = new TextButton("Crediti", skin);
        table.add(button4);
        TextButton button5 = new TextButton("Exit", skin);
        table.add(button5);
        button5.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
                return true;
            }
        });
        table.setDebug(true);
    }

    public void bottonePlay() {
        TextButton button = new TextButton("Play", skin);
        button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(game.gameScreen);
                return true;
            }
        });
        root.add(button).fill();
    }

    public void bottoneSettings() {
        TextButton button = new TextButton("Settings", skin);
        table.add(button);

        button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new Settings(game));
                return true;
            }
        });

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0.9f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        if (skin != null) skin.dispose();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

}
