package progetto.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Questa classe rappresenta una tabella personalizzata che gestisce un insieme di "tabs" (schede),
 * ciascuna associata a un contenuto. Ogni tab può essere selezionato per visualizzare un contenuto diverso.
 * La struttura è composta da una tabella per i bottoni delle tab e un {@link Stack} per il contenuto associato.
 *
 * @see Table
 * @see Stack
 * @see TextButton
 *
 * @author Ferrarese Tommaso
 */
public class CustomTappedPane extends Table {

    /**
     * Tabella che contiene i bottoni delle tab
     *
     * @see Table
     */
    private final Table tabButtons = new Table();

    /**
     * Stack che contiene il contenuto associato ad ogni tab
     *
     * @see Stack
     */
    private final Stack contentStack = new Stack();

    /**
     *  Lo stile per la personalizzazione del componente
     *
     * @see Style
     */

    /**
     * Costruttore che inizializza la struttura della {@link CustomTappedPane}.
     * La tabella viene strutturata con i bottoni in alto a sinistra e il contenuto che si espande sotto.
     *
     * Viene utilizzato lo stile fornito nella creazione del componente.
     */
    public CustomTappedPane() {
        this.top().left();
        this.add(tabButtons).expandX().fillX().row();
        this.add(contentStack).expand().fill();
    }

    /**
     * Aggiunge una nuova tab con un contenuto associato. Ogni tab è rappresentato da un {@link TextButton}
     * e il contenuto è un {@link Actor} che viene mostrato quando la tab viene selezionata.
     *
     * @param titoloParte il titolo del bottone della tab.
     * @param contenuto il contenuto da visualizzare quando la tab è selezionata.
     * @param style lo stile da utilizzare per la personalizzazione dei bottoni e della skin.
     */
    public void aggiungiTab(String titoloParte, Actor contenuto, Style style) {

        TextButton tab;

        if(style.controllaEsistenzaStyle(style::getButtonStyle)){
            tab = new TextButton(titoloParte, style.getButtonStyle());
        }else{
            tab = new TextButton(titoloParte, style.getSkin());
        }

        tabButtons.add(tab).pad(20);

        contentStack.add(contenuto);

        contenuto.setVisible(contentStack.getChildren().size == 1); // mostra solo il primo inizialmente

        tab.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for (Actor child : contentStack.getChildren()) {
                    child.setVisible(false);
                }
                contenuto.setVisible(true);
            }
        });
    }
}
