package progetto.core.settings;

import com.badlogic.gdx.Input;

import java.awt.Dimension;

import progetto.ui.CustomLinkedHashMap;
import progetto.ui.CustomMapComandiModificabili;
import progetto.ui.DatiSlider;
import progetto.ui.Style;

/**
 * La classe {@code ModelImpostazioni} gestisce le impostazioni di configurazione per il gioco,
 * come la risoluzione dello schermo, il framerate, la luminosità, l'audio, i suoni e la musica,
 * nonché i comandi modificabili per il controllo del gioco.
 *
 * Questa classe contiene anche le strutture dati necessarie per gestire i valori di queste impostazioni
 * e metodi getter e setter per modificarli.
 *
 * @author Ferrarese Tommaso
 */
public class ModelImpostazioni {

    // ATTRIBUTI

    /** L'oggetto Style che definisce l'aspetto grafico delle impostazioni */
    private Style style;

    /** Una mappa che contiene le diverse risoluzioni dello schermo */
    private static CustomLinkedHashMap<Dimension> valoriSchermo = new CustomLinkedHashMap<Dimension>(3);

    static {
        // Valori schermo e' statico quindi mi serve inserire i valori in maniera statica
        valoriSchermo.getHashMap().put("Schermo 240p (SD)", new Dimension(426, 240)); //0
        valoriSchermo.getHashMap().put("Schermo 360p (SD)", new Dimension(640, 360)); //1
        valoriSchermo.getHashMap().put("Schermo 480p (SD)", new Dimension(854, 480)); //2
        valoriSchermo.getHashMap().put("Schermo 720p (HD)", new Dimension(1280, 720)); //3
        valoriSchermo.getHashMap().put("Schermo 1080p (HD)", new Dimension(1920, 1080)); //4
        valoriSchermo.getHashMap().put("Schermo 1440p (2k)", new Dimension(2560, 1440)); //5
        valoriSchermo.getHashMap().put("Schermo 2160p (4k)", new Dimension(3840, 2160)); //6
        valoriSchermo.getHashMap().put("Schermo 4320p (8k)", new Dimension(7680, 4320)); //7
    }

    /** Una mappa che contiene i diversi valori del framerate */
    private static CustomLinkedHashMap<Integer> valoriFrameRate = new CustomLinkedHashMap<Integer>(2);

    static {
        // Valori framerate e' statico quindi mi serve inserire i valori in maniera statica
        valoriFrameRate.getHashMap().put("30 Frame Per Secondo", 30); //0
        valoriFrameRate.getHashMap().put("60 Frame Per Secondo", 60); //1
        valoriFrameRate.getHashMap().put("120 Frame Per Secondo", 120); //2
        valoriFrameRate.getHashMap().put("165 Frame Per Secondo", 165); //3
        valoriFrameRate.getHashMap().put("240 Frame Per Secondo", 240); //4
    }

    /** Impostazione per la luminosità della GPU, da 0 (0%) a 2 (200%) */
    private static DatiSlider LUMINOSITA = new DatiSlider(0f, 2f, 0.01f, 1f);

    /** Impostazione per l'audio, da 0 (0%) a 1 (100%) */
    private static DatiSlider AUDIO = new DatiSlider(0f, 1f, 0.01f, 1f);

    /** Impostazione per i suoni, da 0 (0%) a 1 (100%) */
    private static DatiSlider SUONI = new DatiSlider(0f, 1f, 0.01f, 1f);

    /** Impostazione per la musica, da 0 (0%) a 1 (100%) */
    private static DatiSlider MUSICA  = new DatiSlider(0f, 1f, 0.01f, 1f);

    /** Mappa dei comandi modificabili per il gioco */
    private static CustomMapComandiModificabili<Integer> comandiModificabili = new CustomMapComandiModificabili<Integer>();

    static {
        // Valori comandi modificabili e' statico quindi mi serve inserire i valori in maniera statica

        /**
         * <p>MOVIMENTI---------------------------------------------------------------------------------</p>
         */
        comandiModificabili.getHashMap().put("DIREZIONE NORD", Input.Keys.W); //0
        comandiModificabili.getHashMap().put("DIREZIONE SUD", Input.Keys.S); //1
        comandiModificabili.getHashMap().put("DIREZIONE EST", Input.Keys.D); //2
        comandiModificabili.getHashMap().put("DIREZIONE OVEST", Input.Keys.A); //3

        /**
         * <p>ATTACCO BASE, MODIFICABILE----------------------------------------------------</p>
         */
        comandiModificabili.getHashMap().put("ATTACCO SPADA", Input.Keys.F); //4

        /**
         * <p>ATTACCO AVANZATO, MODIFICABILE----------------------------------------------------</p>
         */
        comandiModificabili.getHashMap().put("SPARA", Input.Keys.Q); //5

        /**
         * <p>USO OGGETTO, MODIFICABILE--------------------------------------------</p>
         */
        comandiModificabili.getHashMap().put("USA", Input.Keys.E); //6

        /**
         * <p>VELOCITÀ, MODIFICABILE-------------------------------------------</p>
         */
        comandiModificabili.getHashMap().put("CORRI", Input.Keys.SHIFT_LEFT); //7

        /**
         * <p>FERMA GIOCO, MODIFICABILE---------------------------------------</p>
         */
        comandiModificabili.getHashMap().put("FERMA GIOCO", Input.Keys.ESCAPE); //8

        /**
         * <p>RIPRENDI GIOCO, MODIFICABILE------------------------------------</p>
         */
        comandiModificabili.getHashMap().put("RIPRENDI GIOCO", Input.Keys.SPACE); //9

        /**
         * <p>INVENTARIO, MODIFICABILE----------------------------------------</p>
         */
        comandiModificabili.getHashMap().put("APRI INVENTARIO", Input.Keys.M); //10

        /**
         * <p>IMPOSTAZIONI DEBUG, MODIFICABILE--------------------------------</p>
         */
        comandiModificabili.getHashMap().put("IMPOSTAZIONI DEBUG", Input.Keys.CONTROL_RIGHT); //11
    }

    // COSTRUTTORI -----------------------------------------------------------------------------

    /**
     * Costruttore per inizializzare il modello delle impostazioni con uno specifico {@link Style}.
     *
     * @param style l'oggetto Style che definisce l'aspetto grafico.
     */
    public ModelImpostazioni(final Style style){
        this.style = style;
    }

    // METODI -----------------------------------------------------------------------------

    // GETTER E SETTER -----------------------------------------------------------------------------

    /**
     * Restituisce la mappa che contiene le diverse risoluzioni dello schermo.
     *
     * @return la mappa delle risoluzioni dello schermo.
     */
    public static CustomLinkedHashMap<Dimension> getValoriSchermo() {
        return valoriSchermo;
    }

    /**
     * Imposta i valori delle risoluzioni dello schermo.
     *
     * @param valoriSchermo la nuova mappa delle risoluzioni dello schermo.
     */
    public static void setValoriSchermo(CustomLinkedHashMap<Dimension> valoriSchermo) {
        ModelImpostazioni.valoriSchermo = valoriSchermo;
    }

    /**
     * Restituisce la mappa che contiene i valori del framerate.
     *
     * @return la mappa dei valori del framerate.
     */
    public static CustomLinkedHashMap<Integer> getValoriFrameRate() {
        return valoriFrameRate;
    }

    /**
     * Imposta i valori del framerate.
     *
     * @param valoriFrameRate la nuova mappa dei valori del framerate.
     */
    public static void setValoriFrameRate(CustomLinkedHashMap<Integer> valoriFrameRate) {
        ModelImpostazioni.valoriFrameRate = valoriFrameRate;
    }

    /**
     * Restituisce la mappa dei comandi modificabili.
     *
     * @return la mappa dei comandi modificabili.
     */
    public static CustomMapComandiModificabili<Integer> getComandiModificabili() {
        return comandiModificabili;
    }

    /**
     * Imposta i comandi modificabili.
     *
     * @param comandiModificabili la nuova mappa dei comandi modificabili.
     */
    public static void setComandiModificabili(CustomMapComandiModificabili<Integer> comandiModificabili) {
        ModelImpostazioni.comandiModificabili = comandiModificabili;
    }

    /**
     * Restituisce il dato relativo alla luminosità.
     *
     * @return l'oggetto {@link DatiSlider} relativo alla luminosità.
     */
    public static DatiSlider getLUMINOSITA() {
        return LUMINOSITA;
    }

    /**
     * Imposta il dato relativo alla luminosità.
     *
     * @param LUMINOSITA il nuovo valore per la luminosità.
     */
    public static void setLUMINOSITA(DatiSlider LUMINOSITA) {
        ModelImpostazioni.LUMINOSITA = LUMINOSITA;
    }

    /**
     * Restituisce il dato relativo all'audio.
     *
     * @return l'oggetto {@link DatiSlider} relativo all'audio.
     */
    public static DatiSlider getAUDIO() {
        return AUDIO;
    }

    /**
     * Imposta il dato relativo all'audio.
     *
     * @param AUDIO il nuovo valore per l'audio.
     */
    public static void setAUDIO(DatiSlider AUDIO) {
        ModelImpostazioni.AUDIO = AUDIO;
    }

    /**
     * Restituisce il dato relativo ai suoni.
     *
     * @return l'oggetto {@link DatiSlider} relativo ai suoni.
     */
    public static DatiSlider getSUONI() {
        return SUONI;
    }

    /**
     * Imposta il dato relativo ai suoni.
     *
     * @param SUONI il nuovo valore per i suoni.
     */
    public static void setSUONI(DatiSlider SUONI) {
        ModelImpostazioni.SUONI = SUONI;
    }

    /**
     * Restituisce il dato relativo alla musica.
     *
     * @return l'oggetto {@link DatiSlider} relativo alla musica.
     */
    public static DatiSlider getMUSICA() {
        return MUSICA;
    }

    /**
     * Imposta il dato relativo alla musica.
     *
     * @param MUSICA il nuovo valore per la musica.
     */
    public static void setMUSICA(DatiSlider MUSICA) {
        ModelImpostazioni.MUSICA = MUSICA;
    }

    /**
     * Restituisce lo {@link Style} dell'aspetto grafico.
     *
     * @return lo {@link Style} associato.
     */
    public Style getStyle() {
        return style;
    }

    /**
     * Imposta lo {@link Style} dell'aspetto grafico.
     *
     * @param style il nuovo valore per lo {@link Style}.
     */
    public void setStyle(Style style) {
        this.style = style;
    }
}
