package progetto.core.settings.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import progetto.audio.AudioEngine;
import progetto.core.CustomScreen;
import progetto.core.ScreenRenderer;
import progetto.graphics.shaders.specific.ColorFilter;
import progetto.graphics.shaders.specific.Vignette;

/**
 * La classe {@code ViewImpostazioni} implementa l'interfaccia {@code Screen} di LibGDX
 * e rappresenta la schermata delle impostazioni del gioco.
 * <p>
 * Questa schermata gestisce la visualizzazione e l'interazione con gli elementi dell'interfaccia
 * utente come bottoni, slider, e altre componenti che possono essere utilizzate per configurare
 * le impostazioni del gioco (come la luminosità, il volume, ecc.).
 * </p>
 * La schermata è composta da un {@code Stage} che gestisce l'intero layout e i componenti visivi,
 * e un {@code Table} che organizza i componenti dell'interfaccia utente.
 *
 * @author Ferrarese Tommaso
 */
public class ViewImpostazioni extends CustomScreen {

    /**
     * Oggetto per disegnare lo schermo
     */
    private ScreenRenderer renderer;

    /**
     * Oggetto per disegnare a schermo
     */
    private SpriteBatch batch;

    /**
     * La scena che contiene gli attori (UI components).
     *
     * @see Stage
     */
    private Stage stage;

    /**
     * La tabella che organizza gli attori nella scena.
     *
     * @see Table
     */
    private Table root;

    // COSTRUTTORI-------------------------------------------------------------------------------

    /**
     * Costruttore predefinito della schermata delle impostazioni.
     * Inizializza la scena e imposta l'input processor su questa scena.
     */
    public ViewImpostazioni(){
        stage = new Stage(new FitViewport(1600, 900, new OrthographicCamera()));

        renderer = new ScreenRenderer(this);

        batch = new SpriteBatch();

        renderer.addShader(ColorFilter.getInstance());
        renderer.addShader(Vignette.getInstance());
        Gdx.input.setInputProcessor(this.stage);
    }

    // METODI-------------------------------------------------------------------------------

    /**
     * Aggiunge un attore alla scena.
     *
     * @param actor l'attore da aggiungere alla scena
     */
    public void setActorStage(Actor actor){
        this.stage.addActor(actor);
    }

    /**
     * Metodo chiamato quando la schermata viene mostrata.
     * Può essere utilizzato per inizializzare la schermata e i componenti.
     */
    @Override
    public void show() {

    }

    /**
     * Metodo chiamato per disegnare la schermata e aggiornare la scena.
     * Viene chiamato ogni frame.
     *
     * @param delta il tempo trascorso dall'ultimo frame
     */
    @Override
    public void render(float delta) {
        renderer.draw(batch, delta, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    /**
     * Metodo chiamato quando la finestra del gioco viene ridimensionata.
     * Aggiorna la vista della scena in base alle nuove dimensioni.
     *
     * @param width  la nuova larghezza della finestra
     * @param height la nuova altezza della finestra
     */
    @Override
    public void resize(int width, int height) {
        if (stage != null) {
            stage.getViewport().getCamera().update();
            stage.getViewport().update(width, height, true);
            stage.getViewport().apply();
        }
    }

    /**
     * Metodo chiamato quando il gioco viene messo in pausa.
     * Può essere utilizzato per salvare lo stato o mettere in pausa le animazioni.
     */
    @Override
    public void pause() {}

    /**
     * Metodo chiamato quando il gioco viene ripreso dopo la pausa.
     * Può essere utilizzato per riprendere le animazioni o lo stato.
     */
    @Override
    public void resume() {}

    /**
     * Metodo chiamato quando la schermata viene nascosta.
     * Può essere utilizzato per pulire le risorse o fermare l'input.
     */
    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    /**
     * Metodo chiamato quando la schermata viene distrutta.
     * Pulisce tutte le risorse utilizzate dalla schermata.
     */
    @Override
    public void dispose() {
        if(this.stage != null){
            this.stage.dispose();
        }
    }

    // GETTER E SETTER-------------------------------------------------------------------------------

    /**
     * Ottiene la scena corrente.
     *
     * @return la scena {@code Stage}
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Ottiene la tabella root della scena.
     *
     * @return la tabella {@code Table}
     */
    public Table getRoot() {
        return root;
    }

    /**
     * Imposta la tabella root della scena.
     *
     * @param root la tabella da impostare
     */
    public void setRoot(Table root) {
        this.root = root;
    }

    @Override
    public void draw(float delta) {
        // Pulisce lo schermo con un colore di sfondo chiaro
        ScreenUtils.clear(Color.WHITE);
        if (stage != null) {
            stage.act();
            stage.draw();
        }
    }
}
