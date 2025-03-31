package io.github.ale;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.ale.dialogPkg.toplayerDialog.Dialog;
import io.github.ale.screens.game.GameScreen;
import io.github.ale.screens.mainmenu.MainScreen;

import javax.swing.*;

public class MyGame extends com.badlogic.gdx.Game{

    public SpriteBatch batch;
    public ShapeRenderer renderer;

    public GameScreen gameScreen = new GameScreen(this);
    public MainScreen mainScreen = new MainScreen(this);
    @Override
    public void create() {
        batch = new SpriteBatch(); // praticamente la cosa per disegnare
        renderer = new ShapeRenderer(); // disegna forme
        setScreen(mainScreen);
        Dialog dialog = new Dialog();
    }

    @Override
    public void dispose(){
        batch.dispose();
        renderer.dispose();
    }



}
