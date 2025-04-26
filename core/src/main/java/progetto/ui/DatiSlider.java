package progetto.ui;

/**
 * La classe {@code DatiSlider} rappresenta i dati necessari per gestire uno slider,
 * inclusi i confini (sinistro e destro), la posizione corrente e la dimensione del passo
 * per le modifiche. Questi dati sono utili per il controllo del movimento e l'interazione
 * con uno slider.
 *
 * @author Ferrarese Tommaso
 */
public class DatiSlider {

    /** Il confine sinistro dello slider */
    private float boundariesSinistra;

    /** Il confine destro dello slider */
    private float boundariesDestra;

    /** La posizione attuale dello slider */
    private float posizione;

    /** La dimensione del passo per la modifica della posizione dello slider */
    private float stepSize;

    /**
     * Costruttore della classe {@code DatiSlider}.
     * Inizializza i confini sinistro e destro, la posizione e la dimensione del passo.
     *
     * @param boundariesSinistra il confine sinistro dello slider.
     * @param boundariesDestra il confine destro dello slider.
     * @param stepSize la dimensione del passo per la modifica della posizione.
     * @param posizione la posizione iniziale dello slider.
     */
    public DatiSlider(final float boundariesSinistra, final float boundariesDestra, final float stepSize, final float posizione){
        this.boundariesSinistra = boundariesSinistra;
        this.boundariesDestra = boundariesDestra;
        this.stepSize = stepSize;
        this.posizione = posizione;
    }

    /**
     * Restituisce il confine sinistro dello slider.
     *
     * @return il confine sinistro dello slider.
     */
    public float getBoundariesSinistra() {
        return boundariesSinistra;
    }

    /**
     * Imposta il confine sinistro dello slider.
     *
     * @param boundariesSinistra il nuovo valore del confine sinistro.
     */
    public void setBoundariesSinistra(float boundariesSinistra) {
        this.boundariesSinistra = boundariesSinistra;
    }

    /**
     * Restituisce il confine destro dello slider.
     *
     * @return il confine destro dello slider.
     */
    public float getBoundariesDestra() {
        return boundariesDestra;
    }

    /**
     * Imposta il confine destro dello slider.
     *
     * @param boundariesDestra il nuovo valore del confine destro.
     */
    public void setBoundariesDestra(float boundariesDestra) {
        this.boundariesDestra = boundariesDestra;
    }

    /**
     * Restituisce la posizione attuale dello slider.
     *
     * @return la posizione attuale dello slider.
     */
    public float getPosizione() {
        return posizione;
    }

    /**
     * Imposta la posizione dello slider.
     *
     * @param posizione la nuova posizione dello slider.
     */
    public void setPosizione(float posizione) {
        this.posizione = posizione;
    }

    /**
     * Restituisce la dimensione del passo per la modifica della posizione dello slider.
     *
     * @return la dimensione del passo.
     */
    public float getStepSize() {
        return stepSize;
    }

    /**
     * Imposta la dimensione del passo per la modifica della posizione dello slider.
     *
     * @param stepSize la nuova dimensione del passo.
     */
    public void setStepSize(float stepSize) {
        this.stepSize = stepSize;
    }
}
