package progetto.gameplay.entity.types.living.combat.npc;

import progetto.gameplay.entity.types.EntityConfig;
import progetto.gameplay.entity.types.living.HumanoidInstances;
import progetto.gameplay.manager.ManagerEntity;

public class ProvaNpc extends NotPlayableCharacter {

    public ProvaNpc(HumanoidInstances instance, ManagerEntity managerEntity, String[] dialoghi, WindowDialogo windowDialogo) {
        super(instance, managerEntity, dialoghi, windowDialogo);
    }

    public ProvaNpc(EntityConfig config, ManagerEntity manager, String[] dialoghi, WindowDialogo windowDialogo) {
        super(config, manager, dialoghi, windowDialogo);
    }
}
