package progetto.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import progetto.Game;
import progetto.GameConfig;

import java.util.LinkedHashMap;

public class Settings implements Screen {
    private final Game game;
    private TextButton.TextButtonStyle buttonStyle;
    private Label.LabelStyle labelStyle;
    private Skin skin;
    private Stage stage;
    private Table root;
    private Table table;

    private LinkedHashMap<String, Integer> comandi = new LinkedHashMap<>();

    public Settings(Game game) {
        this.game = game;

        comandi.put("Su", GameConfig.getDirezioneNord());
        comandi.put("Sinistra", GameConfig.getDirezioneOvest());
        comandi.put("Giu", GameConfig.getDirezioneSud());
        comandi.put("Destra", GameConfig.getDirezioneEst());
        comandi.put("Corri", GameConfig.getCORRI());
        comandi.put("Interagisci", GameConfig.getUSA());
        comandi.put("Attacca", GameConfig.getATTACCO());
        comandi.put("Pausa", GameConfig.getFERMAGIOCO());
        comandi.put("Riprendi", GameConfig.getRIPRENDIGIOCO());

    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage); // Setto il processore di input

        root = new Table(); // Creo la tabella che conterra' quella principale
        root.setFillParent(true);
        skin = new Skin(Gdx.files.internal("metal-ui.json")); // Creo la skin che conterra' i dati

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/myfont.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        BitmapFont font;
        parameter.size = 25;
        font = generator.generateFont(parameter); // font size 12 pixels
        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.BLACK;
        labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.BLACK;
        table = new Table(); // Creo una tabella principale
        table.setSize(500, 500); // Assegno la dimensione che avra'
        table.setDebug(true); // per vedere come e' fatta la tabella

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

        String[] keys = comandi.keySet().toArray(new String[0]); // Ottengo un array delle chiavi
        for (int i = 0; i < keys.length; i++) {
            String nome = keys[i]; // Ottengo il nome del comando
            int keycode = comandi.get(nome); // Ottengo il tasto associato
            creaBottone(t, nome, keycode);
        }

        table.add(t); // Aggiungo la tabella dei comandi a quella principale
        table.row(); // Vado a capo
    }

    private void creaBottone(Table t, String nome, int keycode) {
        Label label = creaLabel(nome);
        final TextButton bottone = new TextButton(Input.Keys.toString(keycode), this.buttonStyle);

        bottone.addListener(new ClickListener() { // Aggiungo il listener al bottone
            @Override
            public void clicked(InputEvent event, float x, float y) { // Appena clicco il bottone
                Gdx.input.setInputProcessor(new InputAdapter() { // Aspetto
                    @Override
                    public boolean keyDown(int keycode) { // Appena clicco un tasto della tastiera
                        Gdx.input.setInputProcessor(stage); // Do un nuovo processore di input
                        bottone.setText(Input.Keys.toString(keycode)); // Cambio il nome del bottone premuto col tasto cliccato
                        comandi.put(nome, keycode); // Modifico l'array che contiene i comandi col nuovo tasto cliccato
                        aggiornaComando(nome, keycode);
                        return true; // Esco
                    }
                });
            }
        });

        t.add(label).expand().fill().pad(5); // Aggiungo alla tabella la label col testo + setto una dimensione fissa che deve avere
        t.add(bottone).expand().fill().pad(5); // Aggiungo alla tabella il bottone col suo listener + setto una dimensione fissa che deve avere
        t.row(); // Vado a capo
        t.setDebug(true); //per vedere come e' fatta la tabella
    }

    private Label creaLabel(String testo) {
        Label label = new Label(testo, labelStyle); // Lable che conterra' il testo
        label.setTouchable(null); // Rende il testo non editabile
        return label;
    }

    private void aggiornaComando(String nome, int keycode) {
        switch (nome) {
            case "Su":
                GameConfig.setDirezioneNord(keycode);
                break;
            case "Sinistra":
                GameConfig.setDirezioneOvest(keycode);
                break;
            case "Giu":
                GameConfig.setDirezioneSud(keycode);
                break;
            case "Destra":
                GameConfig.setDirezioneEst(keycode);
                break;
            case "Corri":
                GameConfig.setCORRI(keycode);
                break;
            case "Interagisci":
                GameConfig.setUSA(keycode);
                break;
            case "Attacca":
                GameConfig.setATTACCO(keycode);
                break;
            case "Pausa":
                GameConfig.setFERMAGIOCO(keycode);
                break;
            case "Riprendi":
                GameConfig.setRIPRENDIGIOCO(keycode);
                break;
        }
    }

    private void creaBackButton(Table table) {
        TextButton back = new TextButton("Back", buttonStyle); // Creo un bottone
        back.addListener(new ClickListener() { // Aggiungo un listener al bottone per il click
            @Override
            public void clicked(InputEvent event, float x, float y) { // Quando c'è il click del bottone
                game.setScreen(new MainScreen(game)); // Modifico quello che si vede (Serve per ritornare alla pagina principale)
            }
        });

        table.add(back); // LO aggiungo alla tabella
    }
}
