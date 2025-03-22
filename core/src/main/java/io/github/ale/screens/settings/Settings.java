package io.github.ale.screens.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.awt.GridLayout;

import io.github.ale.MyGame;

public class Settings implements Screen {
    private final MyGame game;
    private Skin skin;
    private Stage stage;
    private Table root;
    private Table table;

    public Settings(MyGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        root = new Table();
        root.setFillParent(true);
        skin = new Skin(Gdx.files.internal("metal-ui.json"));
        table = new Table();


        Label label = new Label("Comandi", skin);
        label.setTouchable(null); // Rende il testo non editabile
        table.add(label);
        table.row();

        final String[] comandi = new String[]{
            "avanti (NORD)",
            "sinistra (OVEST)",
            "sotto (SUD)",
            "destra (EST)",
            "dash (corri)",
            "cambio mappa",
            "attacco",
            "pausa",
            "play"
        };

        final String[] pulsanti = new String[]{
            "W",
            "A",
            "S",
            "D",
            "Maiuscolo (SHIFT)",
            "E",
            "R",
            "SPACE",
            "esc"
        };

        Table t = new Table();
        for (int i = 0; i < comandi.length; i++) {
            Label label1 = new Label("          " + comandi[i] + "          ", skin);
            label1.setTouchable(null);
            Label label2 = new Label("          " + pulsanti[i] + "          ", skin);
            label2.setTouchable(null);
            t.add(label1);
            t.add(label2);
            t.row();
        }

        table.add(t);

        table.row(); // Va a capo per la riga successiva

        /*
        System.out.println("Java Heap: " + Gdx.app.getJavaHeap() / (1024 * 1024) + " MB");
        System.out.println("Native Heap: " + Gdx.app.getNativeHeap() / (1024 * 1024) + " MB");
         */

        // Add some UI elements
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(game.mainScreen);
            }
        });

        /*
        System.out.println("Java Heap: " + Gdx.app.getJavaHeap() / (1024 * 1024) + " MB");
        System.out.println("Native Heap: " + Gdx.app.getNativeHeap() / (1024 * 1024) + " MB");
         */

        table.add(backButton);
        root.add(table);
        stage.addActor(root);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.9f, 0.9f, 0.9f, 0.9f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (stage != null) {
            stage.act();
            stage.draw();
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
        // Do not dispose here, just clear input processor
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        if (stage != null) {
            stage.dispose();
            stage = null;
        }
        if (skin != null) {
            skin.dispose();
            skin = null;
        }
    }
}
