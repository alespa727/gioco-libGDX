package progetto.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.awt.Toolkit;

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
public class ViewImpostazioni implements Screen {

    // ATTRIBUTI-------------------------------------------------------------------------------

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
        stage = new Stage(new ScreenViewport());
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
        // Può essere implementato per inizializzare gli attori sulla scena.
    }

    /**
     * Metodo chiamato per disegnare la schermata e aggiornare la scena.
     * Viene chiamato ogni frame.
     *
     * @param delta il tempo trascorso dall'ultimo frame
     */
    @Override
    public void render(float delta) {
        // Pulisce lo schermo con un colore di sfondo chiaro
        Gdx.gl.glClearColor(0.9f, 0.9f, 0.9f, 0.9f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (stage != null) {
            stage.act();
            stage.draw();
        }
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
            stage.getViewport().update(width, height, true);
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
}
