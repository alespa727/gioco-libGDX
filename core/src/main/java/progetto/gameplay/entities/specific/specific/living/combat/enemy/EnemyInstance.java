package progetto.gameplay.entities.specific.specific.living.combat.enemy;

import progetto.gameplay.entities.specific.specific.living.HumanoidInstances;

public class EnemyInstance extends HumanoidInstances {

    // === ATTRIBUTI ===
    public float viewDistance;
    public float pursueMaxDistance;

    // === COSTRUTTORE ===
    public EnemyInstance(Enemy e) {
        super(e);
        // Inizializzazione dei valori specifici dell'EnemyInstance
        this.viewDistance = e.viewDistance;
        this.pursueMaxDistance = e.pursueMaxDistance;
    }

    public EnemyInstance(){
        super();
    }
}
