package io.github.ale.screens.defeatScreen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.ale.MyGame;

public class DefeatScreen implements Screen{
    MyGame game;

    public DefeatScreen(MyGame game){
        this.game = game;
    }
    @Override
    public void show() {
       
    }

    @Override
    public void render(float delta) {
       ScreenUtils.clear(Color.BLACK);
    }

    @Override
    public void resize(int width, int height) {

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
