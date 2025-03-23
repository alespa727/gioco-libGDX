package io.github.ale.screens.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.ale.MyGame;

public class Settings implements Screen {
    private final MyGame game;
    private Skin skin;
    private Stage stage;
    private Table root;
    private Table table;

    // I pulsanti sono static final int

    private static int[] pulsanti = new int[]{ // Tasti dei pulsanti
        Input.Keys.W,
        Input.Keys.A,
        Input.Keys.S,
        Input.Keys.D,
        Input.Keys.SHIFT_LEFT,
        Input.Keys.E,
        Input.Keys.F,
        Input.Keys.SPACE,
        Input.Keys.ESCAPE
    };
    final String[] comandi = new String[]{ // Array descrizione tasti
        "Su",
        "Sinistra",
        "Giu",
        "Destra",
        "Sprint",
        "Interagisci",
        "Attacca",
        "pausa",
        "play"
    };

    private TextButton bottoni[] = new TextButton[comandi.length]; // Bottoni per cambiare

    public Settings(MyGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage); // Setto il processore di input

        root = new Table(); // Creo la tabella che conterra' quella principale
        root.setFillParent(true);
        skin = new Skin(Gdx.files.internal("metal-ui.json")); // Creo la skin che conterra' i dati
        table = new Table(); // Creo una tabella principale
        table.setSize(500, 500); // Assegno la dimensione che avra'
        //table.setDebug(true); // per vedere come e' fatta la tabella

        creaTitolo(table); // Creo il titolo per la tabella principale

        creaTabellaPulsanti(table); // Creo una tabella che conterra' i pulsanti per quella principale

        creaBackButton(table); // Creo il bottone per tornare alla pagina principale

        root.add(table); // Aggiungo alla tabella quella principale
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

    // Mostra i valori della memoria heap usati in byte
    public void mostraCalcoloHeap() {
        // Calcola la memoria Heap in byte
        System.out.println("Java Heap: " + Gdx.app.getJavaHeap() / (1024 * 1024) + " MB"); // Memoria Heap usata dal java
        System.out.println("Native Heap: " + Gdx.app.getNativeHeap() / (1024 * 1024) + " MB"); // Memoria Heap usata dal dispositivo
    }

    private Label creaLabel(String testo) {
        Label label = new Label(testo, skin); // Lable che conterra' il testo
        label.setTouchable(null); // Rende il testo non editabile
        return label;
    }

    // Metodo per creare il titolo
    private void creaTitolo(Table table) {
        Label label = creaLabel("Comandi");
        table.add(label); // Aggiungo alla tabella la leble col testo
        label.setAlignment(5); // Setto il suo allineamento
        table.row(); // Vado a capo
    }

    // Metodo per creare la tabella dei pulsanti
    private void creaTabellaPulsanti(Table table) {
        Table t = new Table(); // Creo una tabella che conterra' i comandi
        for (int i = 0; i < comandi.length; i++) { // Vado a creare tutti i comandi esistenti di base
            Label label = creaLabel(comandi[i]); // Creo la label che conterra' i dati del comando

            final int ii = i; // faccio una variabile int final per la classe anonima

            bottoni[i] = new TextButton(Input.Keys.toString(pulsanti[i]), skin); // Creo un bottone
            bottoni[i].addListener(new ClickListener() { // Aggiungo il listener al bottone
                @Override
                public void clicked(InputEvent event, float x, float y) { // Appena clicco il bottone
                    Gdx.input.setInputProcessor(new InputAdapter() { // Aspetto
                        @Override
                        public boolean keyDown(int keycode) { // Appena clicco un tasto della tastiera
                            Gdx.input.setInputProcessor(stage); // Do un nuovo processore di input
                            bottoni[ii].setText(Input.Keys.toString(keycode)); // Cambio il nome del bottone premuto col tasto cliccato
                            pulsanti[ii] = keycode; // Modifico l'array che contiene i comandi col nuovo tasto cliccato
                            return true; // Esco
                        }
                    });
                }
            });

            t.add(label).expand().fill().pad(5); // Aggiungo alla tabella la label col testo + setto una dimensione fissa che deve avere
            t.add(bottoni[i]).expand().fill().pad(5); // Aggiungo alla tabella il bottone col suo listener + setto una dimensione fissa che deve avere
            t.row(); // Vado a capo
            //t.setDebug(true); per vedere come e' fatta la tabella
        }

        table.add(t); // Aggiungo la tabella dei comandi a quella principale
        table.row(); // Vado a capo
    }

    private void creaBackButton(Table table){
        TextButton back = new TextButton("Back", skin); // Creo un bottone
        back.addListener(new ClickListener() { // Aggiungo un listener al bottone per il click
            @Override
            public void clicked(InputEvent event, float x, float y) { // Quando c'è il click del bottone
                game.setScreen(game.mainScreen); // Modifico quello che si vede (Serve per ritornare alla pagina principale)
            }
        });

        table.add(back); // LO aggiungo alla tabella
    }

    public static int[] getPulsanti() {
        return pulsanti;
    }

    public static void setPulsanti(int[] pulsanti) {
        Settings.pulsanti = pulsanti;
    }
}
