package progetto.gameplay.entity.types.living.npc;

import progetto.gameplay.entity.types.EntityConfig;
import progetto.gameplay.entity.types.living.HumanoidInstances;
import progetto.gameplay.manager.entity.EntityManager;

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
