package io.github.ale.screens.gameScreen.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import io.github.ale.screens.gameScreen.entity.abstractEntity.Entity;

public class Gui{
    
    private final ShapeRenderer shapeRenderer;
    @SuppressWarnings("unused")
    private final SpriteBatch batch;

    public Gui(){
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
    }

    public void draw(Entity e){
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(Gdx.graphics.getHeight()*0.05f, Gdx.graphics.getHeight()-Gdx.graphics.getHeight()*0.05f, 220, -50);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(Gdx.graphics.getHeight()*0.05f+10, Gdx.graphics.getHeight()-Gdx.graphics.getHeight()*0.05f-10, 200*e.statistiche().getHealth()/100, -30);
        shapeRenderer.end();
    }
}
