package progetto.gameplay.entities.specific.specific.living.npc;

import progetto.gameplay.entities.specific.base.EntityConfig;
import progetto.gameplay.entities.specific.specific.living.HumanoidInstances;
import progetto.manager.entities.Engine;

public class ProvaNpc extends NotPlayableCharacter {

    public ProvaNpc(HumanoidInstances instance, Engine engine, String[] dialoghi, WindowDialogo windowDialogo) {
        super(instance, engine, dialoghi, windowDialogo);
    }

    public ProvaNpc(EntityConfig config, Engine manager, String[] dialoghi, WindowDialogo windowDialogo) {
        super(config, manager, dialoghi, windowDialogo);
        windowDialogo.setVisible(true);
        windowDialogo.setSize(1000, 1000);
    }
}
