package io.github.ale.screens.defeatScreen;

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

import io.github.ale.MyGame;

public class DefeatScreen implements Screen{
    private final MyGame game;
    private Skin skin;
    private Stage stage;
    private Table root;

    
    public DefeatScreen(MyGame game) {
        this.game = game;
        
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        root = new Table();
        root.setFillParent(true);
        skin = new Skin(Gdx.files.internal("metal-ui.json"));
        stage.addActor(root);
    
        TextButton button1 = new TextButton("Play again", skin);
        button1.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(game.gameScreen);
                return true;
	    }
        });
        root.add(button1).fill().pad(300);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.9f, 0.9f, 0.9f, 0.9f);
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
