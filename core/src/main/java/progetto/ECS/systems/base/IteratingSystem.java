package progetto.ECS.systems.base;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import progetto.ECS.components.base.ComponentFilter;
import progetto.ECS.components.specific.base.StateComponent;
import progetto.ECS.entities.Entity;

/**
 * Sistema automatico che gestisce solo le entità con un certo tipo di componenti.
 * <p>
 * In pratica, questo sistema filtra le entità che servono e le salva in una lista,
 * così non deve controllarle tutte ogni volta. Questo migliora le prestazioni.
 * </p>
 * Chi estende questa classe deve solo scrivere cosa succede nel metodo
 * {@link #processEntity(Entity, float)}.
 */
public abstract class IteratingSystem extends System {

    /**
     * Tempo totale passato dall'inizio dell'esecuzione del sistema.
     */
    private float elapsedTime;

    /**
     * Entità che hanno tutti i componenti richiesti e che vengono gestite da questo sistema.
     */
    private final ObjectSet<Entity> filteredEntities;

    /**
     * Entità che devono essere rimosse da {@code filteredEntities} al prossimo update.
     */
    private final ObjectSet<Entity> entitiesToRemove;

    /**
     * Lista dei tipi di componenti richiesti per poter gestire un'entità.
     */
    private final ComponentFilter filter;

    /**
     * Costruttore senza componenti richiesti. Il sistema funzionerà su tutte le entità.
     */
    public IteratingSystem() {
        filteredEntities = new ObjectSet<>();
        entitiesToRemove = new ObjectSet<>();
        filter = null;
    }

    /**
     * Costruttore con una lista di componenti: solo le entità che li hanno verranno gestite.
     *
     * @param filter componenti che un'entità deve avere per essere gestita
     */
    public IteratingSystem(ComponentFilter filter) {
        filteredEntities = new ObjectSet<>();
        this.filter = filter;
        entitiesToRemove = new ObjectSet<>();
    }

    /**
     * Metodo chiamato ogni frame per aggiornare il sistema.
     * <p>
     * Rimuove prima le entità da {@code filteredEntities} se servono, poi chiama {@link #processEntity(Entity, float)}
     * su tutte le entità rilevanti.
     * </p>
     *
     * @param delta    tempo passato dal frame precedente
     * @param entities tutte le entità attive nel gioco
     */
    public void update(float delta, Array<Entity> entities) {
        for (Entity entity : entitiesToRemove) {
            filteredEntities.remove(entity);
        }
        entitiesToRemove.clear();

        elapsedTime += delta;

        if (filter == null) {
            for (int i = 0; i < entities.size; i++) {
                Entity e = entities.get(i);
                processEntity(e, delta);
            }
            return;
        }

        for (Entity entity : filteredEntities) {
            if (entity.get(StateComponent.class).shouldBeUpdated()){
                processEntity(entity, delta);
            }
        }
    }

    /**
     * Ritorna il tempo totale passato da quando il sistema è attivo.
     *
     * @return tempo in secondi
     */
    public float getElapsedTime() {
        return elapsedTime;
    }

    /**
     * Aggiunge una o più entità da gestire, se hanno tutti i componenti richiesti.
     *
     * @param entities entità da aggiungere
     */
    public void addEntity(Entity... entities) {
        if (filter == null) return;

        for (Entity e : entities) {
            if (filter.matches(e)) {
                //java.lang.System.out.println(e.getClass().getSimpleName() + " " + this.getClass().getSimpleName());
                filteredEntities.add(e);
            }
        }
    }

    /**
     * Rimuove un'entità dal sistema, se ha i componenti richiesti.
     * La rimozione avviene nel prossimo {@code update}.
     *
     * @param e entità da rimuovere
     */
    public void removeEntity(Entity e) {
        if (filter == null) return;

        if (filter.matches(e)) {
            //java.lang.System.out.println(e.getClass().getSimpleName() + " " + this.getClass().getSimpleName());
            entitiesToRemove.add(e);
        }
    }

    /**
     * Metodo da implementare: contiene la logica per elaborare ogni entità gestita dal sistema.
     *
     * @param entity entità da processare
     * @param delta  tempo passato dal frame precedente
     */
    public abstract void processEntity(Entity entity, float delta);
}

