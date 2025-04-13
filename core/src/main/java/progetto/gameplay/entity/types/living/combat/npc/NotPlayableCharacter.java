package progetto.gameplay.entity.types.living.combat.npc;

import progetto.gameplay.entity.types.EntityConfig;
import progetto.gameplay.entity.types.EntityInstance;
import progetto.gameplay.entity.types.living.Humanoid;
import progetto.gameplay.entity.types.living.HumanoidInstances;
import progetto.gameplay.manager.ManagerEntity;

public abstract class NotPlayableCharacter extends Humanoid {

    private String[] dialoghi;
    private WindowDialogo windowDialogo;

    public NotPlayableCharacter(HumanoidInstances instance, ManagerEntity managerEntity, String[] dialoghi, WindowDialogo windowDialogo) {
        super(instance, managerEntity);
        this.dialoghi = dialoghi;
        this.windowDialogo = windowDialogo;
    }

    public NotPlayableCharacter(EntityConfig config, ManagerEntity manager, String[] dialoghi, WindowDialogo windowDialogo) {
        super(config, manager);
        this.dialoghi = dialoghi;
        this.windowDialogo = windowDialogo;
    }

    public void talkTuah(){

    }

    @Override
    public void cooldown(float delta) {

    }

    @Override
    public void updateEntityType(float delta) {

    }

    @Override
    public EntityInstance despawn() {
        return null;
    }
}
