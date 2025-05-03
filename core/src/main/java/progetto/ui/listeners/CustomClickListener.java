package progetto.ui.listeners;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.function.Supplier;

/**
 * Questa classe rappresenta un listener personalizzato che esegue un'azione
 * (fornita tramite un {@link Supplier}) quando viene rilevato un click su un {@link com.badlogic.gdx.scenes.scene2d.Actor}.
 *
 * @param <I> parametro generico che rappresenta il tipo restituito dal {@link Supplier}.
 * @see ClickListener
 * @see Supplier
 * @see InputEvent
 *
 * @author Ferrarese Tommaso
 */
public class CustomClickListener<I> extends ClickListener {

    /**
     * Il {@link Supplier} utilizzato per eseguire un'azione al click.
     */
    private final Supplier<I> supplier;

    /**
     * Costruttore della classe.
     *
     * @param supplier il metodo da eseguire al click; restituisce un valore di tipo {@code I}.
     */
    public CustomClickListener(final Supplier<I> supplier){
        // Ho solo un supplier perche' ricevo solo un metodo void
        this.supplier = supplier;
    }

    /**
     * Metodo chiamato automaticamente quando viene rilevato un click sull'attore.
     *
     * @param event evento generato dal click.
     * @param x coordinata X del punto di click relativo all'attore.
     * @param y coordinata Y del punto di click relativo all'attore.
     */
    @Override
    public void clicked (InputEvent event, float x, float y) {
        this.supplier.get();
    }

    /**
     * Restituisce il {@link Supplier} associato a questo listener.
     *
     * @return il supplier memorizzato.
     * @see Supplier
     */
    public Supplier<I> getSupplier() {
        return supplier;
    }
}
