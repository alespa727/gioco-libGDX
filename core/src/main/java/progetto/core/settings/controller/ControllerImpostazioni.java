package progetto.core.settings.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

import progetto.core.Core;
import progetto.core.settings.view.ViewImpostazioni;
import progetto.core.settings.model.ModelImpostazioni;
import progetto.core.main.MainScreen;
import progetto.ui.CustomChangeListener;
import progetto.ui.CustomClickListener;
import progetto.ui.CustomLinkedHashMap;
import progetto.ui.CustomTappedPane;
import progetto.ui.DatiSlider;
import progetto.ui.Style;

/**
 * La classe ControllerImpostazioni implementa il pattern MVC per la gestione delle impostazioni.
 * Contiene riferimenti privati a un model e a una view, oltre ai costruttori e ai metodi necessari per il suo funzionamento.
 *
 * @author Ferrarese Tommaso
 */
public class ControllerImpostazioni {

    // ATTRIBUTI------------------------------------------------------------------------------------------------------

    /**
     * Riferimento al Core del game.
     *
     * @see Core
     */
    private final Core game;

    /**
     * Riferimento al modello delle impostazioni.
     *
     * @see ModelImpostazioni
     */
    private ModelImpostazioni modelImpostazioni;

    /**
     * Riferimento alla vista delle impostazioni.
     *
     * @see ViewImpostazioni
     */
    private ViewImpostazioni viewImpostazioni;

    // COSTRUTTORI------------------------------------------------------------------------------------------------------

    /**
     * Costruttore predefinito.
     * Inizializza solo il riferimento al {@link Core} senza creare né il model né la view delle impostazioni.
     *
     * @param game istanza principale del gioco.
     * @see Core
     */
    public ControllerImpostazioni(final Core game){
        this.game = game;
    }

    /**
     * Costruttore che inizializza il model e la view utilizzando un oggetto {@link Style}.
     * <p>
     * Crea una nuova istanza di {@link ModelImpostazioni} passando lo stile specificato
     * e una nuova istanza di {@link ViewImpostazioni}.
     * </p>
     *
     * @param game istanza principale del gioco.
     * @param style oggetto che rappresenta gli stili grafici da applicare; parametro immutabile.
     * @see Core
     * @see Style
     */
    public ControllerImpostazioni(final Core game, final Style style){
        this.game = game;
        this.modelImpostazioni = new ModelImpostazioni(style);
        this.viewImpostazioni = new ViewImpostazioni();
    }

    /**
     * Costruttore che inizializza il controller utilizzando model e view già esistenti.
     * <p>
     * Permette di associare direttamente delle istanze preesistenti di {@link ModelImpostazioni} e {@link ViewImpostazioni}.
     * </p>
     *
     * @param game istanza principale del gioco.
     * @param modelImpostazioni oggetto contenente i dati e la logica delle impostazioni; parametro immutabile.
     * @param viewImpostazioni oggetto che gestisce la visualizzazione grafica delle impostazioni; parametro immutabile.
     * @see Core
     * @see ModelImpostazioni
     * @see ViewImpostazioni
     */
    public ControllerImpostazioni(final Core game, final ModelImpostazioni modelImpostazioni, final ViewImpostazioni viewImpostazioni){
        this.game = game;
        this.modelImpostazioni = modelImpostazioni;
        this.viewImpostazioni = viewImpostazioni;
    }

    // METODI------------------------------------------------------------------------------------------------------

    /**
     * Crea una view contenente le impostazioni di gioco.
     * Utilizza il controller per garantire che il model e la view non condividano dati direttamente.
     *
     * @param modelImpostazioni modello contenente i dati delle impostazioni.
     * @return una nuova istanza di {@link ViewImpostazioni} inizializzata.
     * @see ModelImpostazioni
     * @see ViewImpostazioni
     */
    public ViewImpostazioni creaImpostazioni(ModelImpostazioni modelImpostazioni){
        ViewImpostazioni viewImpostazioni = new ViewImpostazioni();

        viewImpostazioni.setRoot(this.creaTabella(modelImpostazioni.getStyle().getSkin(), true, false));

        Label label = this.creaLabel("Impostazioni", false, Align.center, modelImpostazioni.getStyle());
        this.aggiungiActorATabella(viewImpostazioni.getRoot(), label, 5);

        viewImpostazioni.getRoot().row().fill().expand();

        CustomTappedPane tappedPane = this.creaTappedPane(false);

        tappedPane.aggiungiTab("Grafica", this.creaScrollPane(aggiungiParteGrafica(modelImpostazioni), modelImpostazioni.getStyle()), modelImpostazioni.getStyle());
        tappedPane.aggiungiTab("Comandi", this.creaScrollPane(aggiungiParteComandi(modelImpostazioni, viewImpostazioni.getStage()), modelImpostazioni.getStyle()), modelImpostazioni.getStyle());
        tappedPane.aggiungiTab("Audio", this.creaScrollPane(aggiungiParteAudio(modelImpostazioni), modelImpostazioni.getStyle()), modelImpostazioni.getStyle());

        this.aggiungiActorATabella(viewImpostazioni.getRoot(), tappedPane, 5);

        viewImpostazioni.getRoot().row();

        this.aggiungiActorATabella(
            viewImpostazioni.getRoot(),
            this.creaBackButton(modelImpostazioni.getStyle()),
            5
        );

        return viewImpostazioni;
    }

    /**
     * Crea una tabella contenente le impostazioni relative alla grafica di gioco.
     *
     * @param modelImpostazioni modello contenente i dati grafici.
     * @return una {@link Table} con i controlli grafici.
     * @see ModelImpostazioni
     * @see Table
     */
    public Table aggiungiParteGrafica(final ModelImpostazioni modelImpostazioni){
        Table tabellaEsterna = creaTabella(modelImpostazioni.getStyle().getSkin(), false, false);

//        {
//            List<Dimension> list = new ArrayList<>(ModelImpostazioni.getValoriSchermo().getHashMap().values());
//            this.creaSliderConBottoni(
//                tabellaEsterna,
//                "Risoluzione",
//                (" " + Gdx.graphics.getWidth() + " x " + Gdx.graphics.getHeight() + " "),
//                modelImpostazioni.getStyle(),
//                ModelImpostazioni.getValoriSchermo(),
//                dim -> Gdx.graphics.setWindowedMode(dim.width, dim.height),
//                dim -> dim.width + " x " + dim.height
//            );
//        }

        tabellaEsterna.row();

        {
            List<Integer> list = new ArrayList<>(ModelImpostazioni.getValoriFrameRate().getHashMap().values());
            this.creaSliderConBottoni(
                tabellaEsterna,
                "Frame Rate",
                (" " + list.get(ModelImpostazioni.getValoriFrameRate().getIndice()) + " "),
                modelImpostazioni.getStyle(),
                ModelImpostazioni.getValoriFrameRate(),
                Gdx.graphics::setForegroundFPS,
                Objects::toString
            );
        }

        tabellaEsterna.row();

        {
            this.creaSliderConLabel(tabellaEsterna, "Luminosita'", modelImpostazioni.getStyle(), ModelImpostazioni.getLUMINOSITA());
        }

        return tabellaEsterna;
    }

    /**
     * Crea una tabella contenente le impostazioni relative ai comandi modificabili.
     *
     * @param modelImpostazioni modello contenente i dati dei comandi.
     * @param stage lo stage su cui applicare il nuovo input processor per la modifica dei comandi.
     * @return una {@link Table} con i controlli dei comandi.
     * @see ModelImpostazioni
     * @see Table
     */
    public Table aggiungiParteComandi(final ModelImpostazioni modelImpostazioni, final Stage stage){
        Table tabellaEsterna = creaTabella(modelImpostazioni.getStyle().getSkin(), false, false);

        {
            Table table = creaTabella(modelImpostazioni.getStyle().getSkin(), false, false);
            LinkedHashMap<String, Integer> hashMap = ModelImpostazioni.getComandiModificabili().getHashMap();
            List<String> listNomi = new ArrayList<>(hashMap.keySet());
            final List<Integer> valori = new ArrayList<>(hashMap.values());

            for(int i = 0; i < hashMap.size(); i++){

                final String nomeLabel = listNomi.get(i);
                Label label = creaLabel(nomeLabel, false, Align.center, modelImpostazioni.getStyle());
                this.aggiungiActorATabella(table, label, 5);

                final String nomeComando = Input.Keys.toString(valori.get(i));
                Label labelComando = creaLabel(nomeComando, true, Align.center, modelImpostazioni.getStyle());

                this.aggiungiListener(
                    labelComando,
                    new ClickListener() { // Aggiungo il listener al bottone
                        @Override
                        public void clicked(InputEvent event, float x, float y) { // Appena clicco il bottone
                            Gdx.input.setInputProcessor(new InputAdapter() { // Aspetto
                                @Override
                                public boolean keyDown(int keycode) { // Appena clicco un tasto della tastiera
                                    Gdx.input.setInputProcessor(stage); // Do un nuovo processore di input
                                    labelComando.setText(Input.Keys.toString(keycode)); // Cambio il nome del bottone premuto col tasto cliccato
                                    hashMap.put(nomeLabel, keycode); // Modifico l'array che contiene i comandi col nuovo tasto cliccato
                                    return true; // Esco
                                }
                            });
                        }
                    }
                );

                this.aggiungiActorATabella(table, labelComando, 5);

                table.row();
            }

            this.aggiungiActorATabella(tabellaEsterna, table, 5);
        }

        return tabellaEsterna;
    }

    /**
     * Crea una tabella contenente le impostazioni audio e un pulsante di test.
     *
     * @param modelImpostazioni modello contenente i dati audio.
     * @return una {@link Table} con i controlli relativi all'audio.
     * @see ModelImpostazioni
     * @see Table
     */
    public Table aggiungiParteAudio(final ModelImpostazioni modelImpostazioni){
        Table tabellaEsterna = creaTabella(modelImpostazioni.getStyle().getSkin(), false, false);

        {
            this.creaSliderConLabel(tabellaEsterna, "Musica", modelImpostazioni.getStyle(), ModelImpostazioni.getMUSICA());
        }

        tabellaEsterna.row();

        {
            this.creaSliderConLabel(tabellaEsterna, "Audio", modelImpostazioni.getStyle(), ModelImpostazioni.getAUDIO());
        }

        tabellaEsterna.row();

        {
            this.creaSliderConLabel(tabellaEsterna, "Suoni", modelImpostazioni.getStyle(), ModelImpostazioni.getSUONI());
        }

        tabellaEsterna.row();

        {
            Label label = creaLabel("Prova", false, Align.center, modelImpostazioni.getStyle());
            this.aggiungiActorATabella(tabellaEsterna, label, 5);

            TextButton textButton = creaTextBottone("Prova", false, modelImpostazioni.getStyle());
            this.aggiungiListener(
                textButton,
                new CustomClickListener<>(
                    () -> {
                        System.out.println("Cliccato prova musica");
                        return null;
                    }
                )
            );

            this.aggiungiActorATabella(tabellaEsterna, textButton, 5);
        }

        return tabellaEsterna;
    }

    /**
     * Crea una label con le specifiche impostate.
     *
     * @param nomeComando testo da visualizzare.
     * @param isModificabile true se la label è modificabile, false altrimenti.
     * @param allineamento allineamento del testo (costante Align).
     * @param style stile grafico da applicare.
     * @return la {@link Label} creata.
     * @see String
     * @see Style
     * @see Label
     */
    public Label creaLabel(final String nomeComando, final boolean isModificabile, final int allineamento, final Style style){

        // Creo la label
        Label label;

        if(style.controllaEsistenzaStyle(style::getLabelStyle)){
            label = new Label(nomeComando, style.getLabelStyle());
        }else{
            label = new Label(nomeComando, style.getSkin());
        }

        // Controllo se puo' essere modificabile o no
        this.setModificabile(label, isModificabile);

        // Gli aggiungo un allineamento per il testo
        label.setAlignment(allineamento);

        // Gli aggiungo il debug
        this.setDebugAttore(label);

        // Ritorno la label
        return label;
    }

    /**
     * Crea un bottone di testo con le specifiche impostate.
     *
     * @param nomeBottone testo da visualizzare.
     * @param isModificabile true se il bottone è modificabile, false altrimenti.
     * @param style stile grafico da applicare.
     * @return il {@link TextButton} creato.
     * @see String
     * @see Style
     * @see TextButton
     */
    public TextButton creaTextBottone(final String nomeBottone, final boolean isModificabile, final Style style){

        // Creo il textButton
        TextButton textButton;

        if(style.controllaEsistenzaStyle(style::getButtonStyle)){
            textButton = new TextButton(nomeBottone, style.getButtonStyle());
        }else{
            textButton = new TextButton(nomeBottone, style.getSkin());
        }

        // Controllo se puo' essere modifiabile o no
        this.setModificabile(textButton, isModificabile);

        // Gli aggiungo il dubug
        this.setDebugAttore(textButton);

        // Ritorno il textButton
        return textButton;
    }

    /**
     * Crea uno slider con i parametri specificati.
     *
     * @param isModificabile true se lo slider è modificabile, false altrimenti.
     * @param min valore minimo.
     * @param max valore massimo.
     * @param stepSize passo tra un valore e l'altro.
     * @param isVerticale true se lo slider è verticale, false se orizzontale.
     * @param style stile grafico da applicare.
     * @return lo {@link Slider} creato.
     * @see Style
     * @see Slider
     */
    public Slider creaSlider(final boolean isModificabile, final float min, final float max, final float stepSize, final boolean isVerticale, final Style style){

        // Creo lo slider
        Slider slider;

        if(style.controllaEsistenzaStyle(style::getSliderStyle)){
            slider = new Slider(min, max, stepSize, isVerticale, style.getSliderStyle());
        }else{
            slider = new Slider(min, max, stepSize, isVerticale, style.getSkin());
        }

        // Controllo se puo' essere modificabile o no
        this.setModificabile(slider, isModificabile);

        // Gli aggiungo il debug
        this.setDebugAttore(slider);

        // Ritorno lo slider
        return slider;
    }

    /**
     * Crea uno scroll pane che contiene un attore.
     *
     * @param actor attore da visualizzare all'interno dello scroll pane.
     * @param style stile grafico da applicare.
     * @return lo {@link ScrollPane} creato.
     * @see Actor
     * @see Style
     * @see ScrollPane
     */
    public ScrollPane creaScrollPane(final Actor actor, final Style style){

        // Creo lo ScroolPane
        ScrollPane scrollPane;

        if(style.controllaEsistenzaStyle(style::getScrollPaneStyle)){
            scrollPane = new ScrollPane(actor, style.getScrollPaneStyle());
        }else{
            scrollPane = new ScrollPane(actor, style.getSkin());
        }

        // Rende in automatico le barre visibili se servono
        scrollPane.setFadeScrollBars(false);

        // Abilita la possibilita dello scrool orizzontale e verticale
        scrollPane.setScrollingDisabled(false, false);

        // Ritorno lo ScroolPane
        return scrollPane;
    }

    /**
     * Crea una tabella con le specifiche impostate.
     *
     * @param skin skin da applicare alla tabella.
     * @param isFillParent true se la tabella deve riempire il contenitore.
     * @param isModificabile true se la tabella è modificabile, false altrimenti.
     * @return la {@link Table} creata.
     * @see Skin
     * @see Table
     */
    public Table creaTabella(final Skin skin, final boolean isFillParent, final boolean isModificabile){

        // Creo la tabella
        Table table;

        if(skin != null){
             table = new Table(skin);
        }else{
            table = new Table();
        }

        table.setFillParent(isFillParent);

        // Controllo se puo' essere modificabile o no
        this.setModificabile(table, isModificabile);

        // Gli aggiungo il debug
        this.setDebugAttore(table);

        // Ritorno la tabella
        return table;
    }

    /**
     * Crea un pannello a tab con le specifiche impostate.
     *
     * @param isModificabile true se il pannello è modificabile, false altrimenti.
     * @return il {@link CustomTappedPane} creato.
     * @see CustomTappedPane
     */
    public CustomTappedPane creaTappedPane(final boolean isModificabile){

        // Creo la tappedPane
        CustomTappedPane tappedPane = new CustomTappedPane();

        // Controllo se puo' essere modificabile o no
        this.setModificabile(tappedPane, isModificabile);

        // Gli aggiungo il debug
        this.setDebugAttore(tappedPane);

        // Ritorno la tappedPane
        return tappedPane;
    }

    /**
     * Crea uno slider con bottoni laterali per scorrere tra diversi valori.
     *
     * @param <I> il tipo generico degli oggetti gestiti.
     * @param table la tabella in cui aggiungere gli attori creati.
     * @param nomeLabel il testo della label principale.
     * @param nomeInizialeLabelModificabile il testo iniziale della label modificabile.
     * @param style lo stile grafico da applicare (classe custom non modificabile).
     * @param linkedHashMap contenitore dei dati, gestito tramite indice interno.
     * @param consumer operazione da eseguire per aggiornare un valore selezionato.
     * @param function funzione che trasforma un valore selezionato in testo da visualizzare.
     * @see Table
     * @see String
     * @see Style
     * @see CustomLinkedHashMap
     * @see Consumer
     * @see Function
     */
    public <I> void creaSliderConBottoni(Table table, final String nomeLabel, final String nomeInizialeLabelModificabile, final Style style, final CustomLinkedHashMap<I> linkedHashMap, final Consumer<I> consumer, final Function<I, String> function){

        final Label label = creaLabel(nomeLabel, false, Align.center, style);

        this.aggiungiActorATabella(table, label, 5);

        Table tableInterna = creaTabella(style.getSkin(), false, false);

        TextButton textButtonSinistra = creaTextBottone(" [ < ] ", false, style);
        Label labelTraPulsanti = creaLabel(nomeInizialeLabelModificabile, false, Align.center, style);
        TextButton textButtonDestra = creaTextBottone(" [ > ] ", false, style);

        this.aggiungiListener(
            textButtonSinistra,
            creaCustomClickListenerSliderConBottoni(linkedHashMap, false, consumer, function, labelTraPulsanti)
        );

        this.aggiungiListener(
            textButtonDestra,
            creaCustomClickListenerSliderConBottoni(linkedHashMap, true, consumer, function, labelTraPulsanti)
        );

        this.aggiungiActorATabella(tableInterna, textButtonSinistra, 5);
        this.aggiungiActorATabella(tableInterna, labelTraPulsanti, 5);
        this.aggiungiActorATabella(tableInterna, textButtonDestra, 5);

        this.aggiungiActorATabella(table, tableInterna, 5);
    }

    /**
     * Crea un listener personalizzato per modificare una label al click dei bottoni dello slider.
     *
     * @param <I> il tipo generico degli oggetti gestiti.
     * @param linkedHashMap contenitore dei dati con indice interno.
     * @param avanti true se bisogna scorrere in avanti, false per indietro.
     * @param consumer operazione da eseguire sul valore selezionato.
     * @param function funzione per ottenere il testo da visualizzare partendo dal valore selezionato.
     * @param label la label da aggiornare al click.
     * @return un {@link CustomClickListener} configurato.
     * @see CustomLinkedHashMap
     * @see Consumer
     * @see Function
     * @see Label
     * @see CustomClickListener
     */
    public <I> CustomClickListener<I> creaCustomClickListenerSliderConBottoni(CustomLinkedHashMap<I> linkedHashMap, final boolean avanti, final Consumer<I> consumer, final Function<I, String> function, Label label){
        return new CustomClickListener<>(
            () -> {
                final List<String> chiavi = new ArrayList<>(linkedHashMap.getHashMap().keySet());
                int indiceCorrente = linkedHashMap.getIndice();
                int nuovoIndice = avanti ? avanti(chiavi, indiceCorrente) : indietro(chiavi, indiceCorrente);

                linkedHashMap.setIndice(nuovoIndice);

                final List<I> valoriLista = new ArrayList<>(linkedHashMap.getHashMap().values());
                final I valoreSelezionato = valoriLista.get(nuovoIndice);

                consumer.accept(valoreSelezionato);

                label.setText(" " + function.apply(valoreSelezionato) + " ");

                return null;
            }
        );
    }

    /**
     * Decrementa un indice in una lista, ciclato all'indietro.
     *
     * @param <O> il tipo degli elementi della lista.
     * @param <LI> il tipo della lista estesa da {@link List}.
     * @param list la lista su cui lavorare.
     * @param indice l'indice attuale.
     * @return l'indice decrementato, o il massimo se si oltrepassa l'inizio.
     * @see List
     */
    public  <O, LI extends List<O>> int indietro(final LI list, int indice){
        if(indice - 1 < 0){
            indice = list.size();
        }
        indice--;

        return indice;
    }

    /**
     * Incrementa un indice in una lista, ciclato in avanti.
     *
     * @param <O> il tipo degli elementi della lista.
     * @param <LI> il tipo della lista estesa da {@link List}.
     * @param list la lista su cui lavorare.
     * @param indice l'indice attuale.
     * @return l'indice incrementato, o 0 se si oltrepassa la fine.
     * @see List
     */
    public  <O, LI extends List<O>> int avanti(final LI list, int indice){
        if(indice + 1 >= list.size()){
            indice = -1;
        }
        indice++;

        return indice;
    }

    /**
     * Crea uno slider numerico con una label che mostra il valore corrente.
     *
     * @param <DS> il tipo generico che estende {@link DatiSlider}.
     * @param table la tabella in cui aggiungere gli attori creati.
     * @param nomeLabel il testo della label principale.
     * @param style lo stile grafico da applicare (classe custom non modificabile).
     * @param datiSlider i dati di configurazione dello slider.
     * @see DatiSlider
     * @see Table
     * @see String
     * @see Style
     */
    public <DS extends DatiSlider> void creaSliderConLabel(Table table, final String nomeLabel, final Style style, DS datiSlider){
        final Label label = creaLabel(nomeLabel, false, Align.center, style);

        this.aggiungiActorATabella(table, label, 5);

        Table tableInterna = creaTabella(style.getSkin(), false, false);

        Label labelTesto = creaLabel(String.format(" %.2f ", datiSlider.getPosizione()), false, Align.center, style);
        Slider slider = creaSlider(true, datiSlider.getBoundariesSinistra(), datiSlider.getBoundariesDestra(), datiSlider.getStepSize(), false, style);
        slider.setValue(datiSlider.getPosizione());

        this.aggiungiListener(
            slider,
            new CustomChangeListener<>(() -> {

                datiSlider.setPosizione(slider.getValue());

                labelTesto.setText(String.format(" %.2f ", datiSlider.getPosizione()));

                return null;
            })
        );

        tableInterna.add(labelTesto).pad(5).size(100, 50);
        tableInterna.add(slider).pad(5).size(100, 50);

        this.aggiungiActorATabella(table, tableInterna, 5);
    }

    /**
     * Crea un bottone "Go Back" con un listener predefinito per tornare alla pagina precedente.
     *
     * @param style lo stile grafico da applicare (classe custom non modificabile).
     * @return un {@link TextButton} configurato.
     * @see Style
     * @see TextButton
     */
    public TextButton creaBackButton(final Style style) {
        TextButton back = creaTextBottone("Go Back", false, style);
        back.addListener(new ClickListener() { // Aggiungo un listener al bottone per il click
            @Override
            public void clicked(InputEvent event, float x, float y) { // Quando c'è il click del bottone
                game.setScreen(new MainScreen(game));
            }
        });

        return  back;
    }

    /**
     * Aggiunge un attore a una tabella con un padding specificato.
     *
     * @param table la tabella a cui aggiungere l'attore.
     * @param actor l'attore da aggiungere.
     * @param padding il valore del padding in pixel.
     * @see Table
     * @see Actor
     */
    public void aggiungiActorATabella(Table table, final Actor actor, final int padding) {
        table.add(actor).pad(padding);
    }

    /**
     * Aggiunge un listener a un attore.
     *
     * @param <G> il tipo generico del listener che estende {@link EventListener}.
     * @param actor l'attore su cui impostare il listener.
     * @param eventListener il listener da aggiungere.
     * @see Actor
     * @see EventListener
     */
    public <G extends EventListener> void aggiungiListener(Actor actor, final G eventListener){
        actor.addListener(eventListener);
    }

    /**
     * Imposta se un attore può essere modificato (interagibile) o meno.
     *
     * @param actor l'attore su cui agire.
     * @param isModificabile true se modificabile, false se non interagibile.
     * @see Actor
     */
    public void setModificabile(Actor actor, final boolean isModificabile){
        if(!isModificabile){
            actor.setTouchable(null);
        }
    }

    /**
     * Abilita il debug visivo per un attore.
     *
     * @param actor l'attore su cui abilitare il debug.
     * @see Actor
     */
    public void setDebugAttore(Actor actor){
        actor.setDebug(true);
    }

    // GETTER E SETTER------------------------------------------------------------------------------------------------------

    /**
     * Restituisce il {@link ModelImpostazioni} associato a questa classe.
     *
     * @return il modello delle impostazioni.
     * @see ModelImpostazioni
     */
    public ModelImpostazioni getModelImpostazioni() {
        return modelImpostazioni;
    }

    /**
     * Imposta il {@link ModelImpostazioni} associato a questa classe.
     *
     * @param modelImpostazioni il modello delle impostazioni da assegnare.
     * @see ModelImpostazioni
     */
    public void setModelImpostazioni(ModelImpostazioni modelImpostazioni) {
        this.modelImpostazioni = modelImpostazioni;
    }

    /**
     * Restituisce la {@link ViewImpostazioni} associata a questa classe.
     *
     * @return la view delle impostazioni.
     * @see ViewImpostazioni
     */
    public ViewImpostazioni getViewImpostazioni() {
        return viewImpostazioni;
    }

    /**
     * Imposta la {@link ViewImpostazioni} associata a questa classe.
     *
     * @param viewImpostazioni la view delle impostazioni da assegnare.
     * @see ViewImpostazioni
     */
    public void setViewImpostazioni(ViewImpostazioni viewImpostazioni) {
        this.viewImpostazioni = viewImpostazioni;
    }
}
