package progetto.ECS.systems.specific;

import com.badlogic.gdx.Gdx;

import progetto.core.settings.model.ModelImpostazioni;
import progetto.ECS.components.base.ComponentFilter;
import progetto.ECS.components.specific.general.PlayerComponent;
import progetto.ECS.components.specific.general.skills.specific.player.PlayerDash;
import progetto.ECS.components.specific.general.skills.specific.player.PlayerRangedAttack;
import progetto.ECS.components.specific.general.skills.specific.player.PlayerSwordAttack;
import progetto.ECS.entities.Entity;
import progetto.ECS.systems.base.IteratingSystem;
import progetto.core.game.player.Player;

public class PlayerSystem extends IteratingSystem {

    public PlayerSystem() {
        super(ComponentFilter.all(PlayerComponent.class));
    }

    @Override
    public void processEntity(Entity entity, float delta) {
        if (!(entity instanceof Player)) {
            return;
        }
        Player p = (Player) entity;

        if (Gdx.input.isKeyPressed(ModelImpostazioni.getComandiModificabili().getHashMap().get("ATTACCO SPADA"))) {
            if (p.getAttackCooldown().isReady) {
                p.getSkillset().getSkill(PlayerSwordAttack.class).execute();
                p.getAttackCooldown().reset();
            }
        }

        if (Gdx.input.isKeyPressed(ModelImpostazioni.getComandiModificabili().getHashMap().get("SPARA"))) {
            if (p.getAttackCooldown().isReady) {
                p.getSkillset().getSkill(PlayerRangedAttack.class).execute();
                p.getAttackCooldown().reset();
            }
        }

        if (Gdx.input.isKeyPressed(ModelImpostazioni.getComandiModificabili().getHashMap().get("CORRI"))) {
            if (p.getDashCooldown().isReady) {
                p.getSkillset().getSkill(PlayerDash.class).execute();
                p.getDashCooldown().reset();
            }
        }
    }
}
