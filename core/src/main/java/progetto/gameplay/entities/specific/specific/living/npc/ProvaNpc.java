package progetto.gameplay.entities.specific.specific.living.npc;

import progetto.gameplay.entities.specific.base.EntityConfig;
import progetto.gameplay.entities.specific.specific.living.HumanoidInstances;
import progetto.manager.entities.EntityManager;

public class ProvaNpc extends NotPlayableCharacter {

    public ProvaNpc(HumanoidInstances instance, EntityManager entityManager, String[] dialoghi, WindowDialogo windowDialogo) {
        super(instance, entityManager, dialoghi, windowDialogo);
    }

    public ProvaNpc(EntityConfig config, EntityManager manager, String[] dialoghi, WindowDialogo windowDialogo) {
        super(config, manager, dialoghi, windowDialogo);
        windowDialogo.setVisible(true);
        windowDialogo.setSize(1000, 1000);
    }
}
