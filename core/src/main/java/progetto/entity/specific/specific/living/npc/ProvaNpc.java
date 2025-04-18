package progetto.entity.specific.specific.living.npc;

import progetto.entity.specific.base.EntityConfig;
import progetto.entity.specific.specific.living.HumanoidInstances;
import progetto.rendering.entity.EntityManager;

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
