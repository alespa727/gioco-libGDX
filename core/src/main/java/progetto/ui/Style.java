package progetto.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.function.Supplier;

/**
 * La classe {@code Style} gestisce la configurazione degli stili grafici per l'interfaccia utente.
 * Contiene vari stili per etichette, bottoni, slider, barre di progresso e pannelli di scorrimento.
 * Fornisce anche metodi per la gestione dinamica delle skin e degli stili, nonché per il recupero dei file.
 *
 * @author Ferrarese Tommaso
 */
public final class Style {

    // ATTRIBUTI---------------------------------------------------------------------------------------

    /**
     * La skin di default per la UI
     *
     * @see Skin
     */
    private Skin skin;

    /**
     * Lo stile per le etichette (Label)
     *
     * @see Label.LabelStyle
     */
    private Label.LabelStyle labelStyle;

    /**
     * Lo stile per i bottoni (TextButton)
     *
     * @see TextButton.TextButtonStyle
     */
    private TextButton.TextButtonStyle buttonStyle;

    /**
     * Lo stile per gli slider (Slider)
     *
     * @see Slider.SliderStyle
     */
    private Slider.SliderStyle sliderStyle;

    /**
     * Lo stile per gli scrollPane
     *
     * @see ScrollPane.ScrollPaneStyle
     */
    private ScrollPane.ScrollPaneStyle scrollPaneStyle;

    /**
     * Lo stile per le barre di progresso (ProgressBar)
     *
     * @see ProgressBar.ProgressBarStyle
     */
    private ProgressBar.ProgressBarStyle progressBarStyle;

    // COSTRUTTORI---------------------------------------------------------------------------------------

    /**
     * Costruttore che inizializza la skin di default utilizzando un percorso.
     *
     * @param percorsoSkin il percorso della skin.
     */
    public Style(final String percorsoSkin) {
        if(percorsoSkin != null){
            ricercaPercorsoSkin(percorsoSkin);
        }
    }

    /**
     * Costruttore che inizializza la classe {@code Style} con una {@link Skin} predefinita.
     *
     * @param skin la skin predefinita.
     */
    public Style(final Skin skin) {
        this.skin = skin;
    }

    /**
     * Costruttore che inizializza diversi stili per i componenti UI (Label, TextButton, Slider, etc.).
     * Accetta percorsi per le risorse grafiche relative a ciascun componente.
     *
     * @param percorsoSkin il percorso della skin.
     * @param percorsoLabel il percorso per la font della label.
     * @param sizeLabel la dimensione della font per la label.
     * @param colorLabelStyle il colore della font per la label.
     * @param percorsoTextButton il percorso della font per il textButton.
     * @param sizeTextButton la dimensione della font per il textButton.
     * @param colorTextButtonStyle il colore della font per il textButton.
     * @param percorsoSlider i percorsi per le risorse grafiche degli slider.
     * @param percorsoScrollPane i percorsi per le risorse grafiche degli scrollPane.
     * @param percorsoProgressBar i percorsi per le risorse grafiche delle barre di progresso.
     */
    public Style(final String percorsoSkin, final String percorsoLabel, final Integer sizeLabel, final Color colorLabelStyle, final String percorsoTextButton, final Integer sizeTextButton, final Color colorTextButtonStyle, final String[] percorsoSlider, final String[] percorsoScrollPane, final String[] percorsoProgressBar) {
        // Inizializzazione della skin
        if(percorsoSkin != null){
            ricercaPercorsoSkin(percorsoSkin);
        } else {
            messaggioErroreRicercaFile(percorsoSkin);
        }

        // Inizializzazione dello stile della label
        if(percorsoLabel != null) {
            this.labelStyle = new Label.LabelStyle();
            this.labelStyle.font = recuperaFont(percorsoLabel, sizeLabel);
            this.labelStyle.fontColor = colorLabelStyle;
        }

        // Inizializzazione dello stile del TextButton
        if(percorsoTextButton != null) {
            this.buttonStyle = new TextButton.TextButtonStyle();
            this.buttonStyle.font = recuperaFont(percorsoTextButton, sizeTextButton);
            this.buttonStyle.fontColor = colorTextButtonStyle;
        }

        // Inizializzazione dello stile dello Slider
        if(percorsoSlider != null) {
            try {
                Texture background = new Texture(ottieniPercorso(percorsoSlider[0]));
                Texture knob = new Texture(ottieniPercorso(percorsoSlider[1]));
                Texture before = new Texture(ottieniPercorso(percorsoSlider[2]));

                this.sliderStyle = new Slider.SliderStyle();
                this.sliderStyle.background = new TextureRegionDrawable(new TextureRegion(background));
                this.sliderStyle.knob = new TextureRegionDrawable(new TextureRegion(knob));
                this.sliderStyle.knobBefore = new TextureRegionDrawable(new TextureRegion(before));

            } catch (Exception e) {
                String percorsi = "File non trovati: ";
                for(String i : percorsoSlider){
                    percorsi += "\n" + percorsoSlider;
                }
                messaggioErroreRicercaFile(percorsi);
            }
        }

        // Inizializzazione dello stile dello ScrollPane
        if(percorsoScrollPane != null) {
            try {
                Texture background = new Texture(ottieniPercorso(percorsoScrollPane[0]));
                Texture hScroll = new Texture(ottieniPercorso(percorsoScrollPane[1]));
                Texture vScroll = new Texture(ottieniPercorso(percorsoScrollPane[2]));
                Texture hKnob = new Texture(ottieniPercorso(percorsoScrollPane[3]));
                Texture vKnob = new Texture(ottieniPercorso(percorsoScrollPane[4]));

                this.scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
                this.scrollPaneStyle.background = new TextureRegionDrawable(new TextureRegion(background));
                this.scrollPaneStyle.hScroll = new TextureRegionDrawable(new TextureRegion(hScroll));
                this.scrollPaneStyle.vScroll = new TextureRegionDrawable(new TextureRegion(vScroll));
                this.scrollPaneStyle.hScrollKnob = new TextureRegionDrawable(new TextureRegion(hKnob));
                this.scrollPaneStyle.vScrollKnob = new TextureRegionDrawable(new TextureRegion(vKnob));

            } catch (Exception e) {
                String percorsi = "File non trovati: ";
                for(String i : percorsoScrollPane){
                    percorsi += "\n" + percorsoScrollPane;
                }
                messaggioErroreRicercaFile(percorsi);
            }
        }

        // Inizializzazione dello stile della ProgressBar
        if(percorsoProgressBar != null) {
            try {
                Texture background = new Texture(ottieniPercorso(percorsoProgressBar[0]));
                Texture knob = new Texture(ottieniPercorso(percorsoProgressBar[1]));

                this.progressBarStyle = new ProgressBar.ProgressBarStyle();
                this.progressBarStyle.background = new TextureRegionDrawable(new TextureRegion(background));
                this.progressBarStyle.knob = new TextureRegionDrawable(new TextureRegion(knob));

            } catch (Exception e) {
                String percorsi = "File non trovati: ";
                for(String i : percorsoProgressBar){
                    percorsi += "\n" + percorsoProgressBar;
                }
                messaggioErroreRicercaFile(percorsi);
            }
        }
    }

    // METODI------------------------------------------------------------------------------------------------

    /**
     * Recupera il percorso di un file di risorsa.
     *
     * @param percorso il percorso del file.
     * @return il {@link FileHandle} per il file specificato.
     */
    public FileHandle ottieniPercorso(String percorso) {
        return Gdx.files.internal(percorso);
    }

    /**
     * Mostra un messaggio di errore nel caso in cui un file non venga trovato.
     *
     * @param percorso il percorso del file non trovato.
     */
    public void messaggioErroreRicercaFile(String percorso) {
        System.out.println("Non si è trovato il file nel percorso: " + Gdx.files.internal(percorso));
        Gdx.app.exit();
    }

    /**
     * Cerca e carica la skin da un percorso specificato.
     *
     * @param percorsoSkin il percorso della skin.
     */
    public void ricercaPercorsoSkin(String percorsoSkin) {
        try {
            this.skin = new Skin(ottieniPercorso(percorsoSkin));
        } catch (Exception e) {
            messaggioErroreRicercaFile(percorsoSkin);
        }
    }

    /**
     * Recupera un font da un percorso specificato con la dimensione desiderata.
     *
     * @param percorso il percorso del file del font.
     * @param size la dimensione del font.
     * @return il {@link BitmapFont} caricato.
     */
    public BitmapFont recuperaFont(final String percorso, int size) {
        try {
            final FreeTypeFontGenerator generator = new FreeTypeFontGenerator(ottieniPercorso(percorso));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = size;
            return generator.generateFont(parameter);
        } catch (Exception e) {
            messaggioErroreRicercaFile(percorso);
        }
        return null;
    }

    /**
     * Controlla se uno stile esiste.
     *
     * @param supplier il getter per l'oggetto style.
     * @param <T> il tipo dell'oggetto.
     * @return true se lo stile esiste, false altrimenti.
     */
    public <T> boolean controllaEsistenzaStyle(final Supplier<T> supplier) {
        return supplier.get() != null;
    }

    // GETTER E SETTER---------------------------------------------------------------------------------------------

    /**
     * Restituisce la skin corrente.
     *
     * @return la skin corrente.
     */
    public Skin getSkin() {
        return skin;
    }

    /**
     * Imposta una nuova skin.
     *
     * @param skin la nuova skin da impostare.
     */
    public void setSkin(Skin skin) {
        this.skin = skin;
    }

    /**
     * Restituisce lo stile della label corrente.
     *
     * @return lo stile della label corrente.
     */
    public Label.LabelStyle getLabelStyle() {
        return labelStyle;
    }

    /**
     * Imposta un nuovo stile per le label.
     *
     * @param labelStyle il nuovo stile da impostare per le label.
     */
    public void setLabelStyle(Label.LabelStyle labelStyle) {
        this.labelStyle = labelStyle;
    }

    /**
     * Restituisce lo stile del bottone corrente.
     *
     * @return lo stile del bottone corrente.
     */
    public TextButton.TextButtonStyle getButtonStyle() {
        return buttonStyle;
    }

    /**
     * Imposta un nuovo stile per i bottoni.
     *
     * @param buttonStyle il nuovo stile da impostare per i bottoni.
     */
    public void setButtonStyle(TextButton.TextButtonStyle buttonStyle) {
        this.buttonStyle = buttonStyle;
    }

    /**
     * Restituisce lo stile dello slider corrente.
     *
     * @return lo stile dello slider corrente.
     */
    public Slider.SliderStyle getSliderStyle() {
        return sliderStyle;
    }

    /**
     * Imposta un nuovo stile per gli slider.
     *
     * @param sliderStyle il nuovo stile da impostare per gli slider.
     */
    public void setSliderStyle(Slider.SliderStyle sliderStyle) {
        this.sliderStyle = sliderStyle;
    }

    /**
     * Restituisce lo stile dello scroll pane corrente.
     *
     * @return lo stile dello scroll pane corrente.
     */
    public ScrollPane.ScrollPaneStyle getScrollPaneStyle() {
        return scrollPaneStyle;
    }

    /**
     * Imposta un nuovo stile per lo scroll pane.
     *
     * @param scrollPaneStyle il nuovo stile da impostare per lo scroll pane.
     */
    public void setScrollPaneStyle(ScrollPane.ScrollPaneStyle scrollPaneStyle) {
        this.scrollPaneStyle = scrollPaneStyle;
    }

    /**
     * Restituisce lo stile della progress bar corrente.
     *
     * @return lo stile della progress bar corrente.
     */
    public ProgressBar.ProgressBarStyle getProgressBarStyle() {
        return progressBarStyle;
    }

    /**
     * Imposta un nuovo stile per la progress bar.
     *
     * @param progressBarStyle il nuovo stile da impostare per la progress bar.
     */
    public void setProgressBarStyle(ProgressBar.ProgressBarStyle progressBarStyle) {
        this.progressBarStyle = progressBarStyle;
    }
}
