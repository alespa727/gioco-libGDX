package progetto.ui.listeners;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.function.Supplier;

/**
 * Questa classe rappresenta un listener personalizzato che esegue un'azione
 * (fornita tramite un {@link Supplier}) quando avviene un cambiamento su un {@link Actor}.
 *
 * @param <I> parametro generico che rappresenta il tipo restituito dal {@link Supplier}.
 * @see ChangeListener
 * @see Supplier
 * @see Actor
 *
 * @author Ferrarese Tommaso
 */
public class CustomChangeListener <I> extends ChangeListener {

    /**
     * Il {@link Supplier} utilizzato per eseguire un'azione al cambiamento.
     */
    private final Supplier<I> supplier;

    /**
     * Costruttore della classe.
     *
     * @param supplier il metodo da eseguire al cambiamento; restituisce un valore di tipo {@code I}.
     */
    public CustomChangeListener(final Supplier<I> supplier){
        // Ho solo un supplier perche' ricevo solo un metodo void
        this.supplier = supplier;
    }

    /**
     * Metodo chiamato automaticamente quando l'attore genera un evento di tipo {@link ChangeEvent}.
     *
     * @param event evento generato.
     * @param actor attore che ha generato l'evento.
     */
    @Override
    public void changed(ChangeEvent event, Actor actor) {
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
