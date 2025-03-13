package io.github.ale;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.ale.screens.gameScreen.GameScreen;

public class MyGame extends com.badlogic.gdx.Game{

    public SpriteBatch batch;
    public ShapeRenderer renderer;

    GameScreen gameScreen = new GameScreen(this);
    @Override
    public void create() {
        batch = new SpriteBatch(); // praticamente la cosa per disegnare
        renderer = new ShapeRenderer(); // disegna forme
        gameScreen.create(1f);
        setScreen(gameScreen);
    }

    @Override
    public void dispose(){
        
    }

    

}
