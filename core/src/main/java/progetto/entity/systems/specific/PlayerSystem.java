package progetto.entity.systems.specific;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import progetto.entity.components.base.ComponentFilter;
import progetto.entity.components.specific.general.PlayerComponent;
import progetto.entity.components.specific.general.skills.specific.player.PlayerDash;
import progetto.entity.components.specific.general.skills.specific.player.PlayerRangedAttack;
import progetto.entity.components.specific.general.skills.specific.player.PlayerSwordAttack;
import progetto.entity.entities.base.Entity;
import progetto.entity.systems.base.IterableSystem;
import progetto.player.Player;

public class PlayerSystem extends IterableSystem {

    public PlayerSystem() {
        super(ComponentFilter.all(PlayerComponent.class));
    }

    @Override
    public void processEntity(Entity entity, float delta) {
        if (!(entity instanceof Player)) {
            return;
        }
        Player p = (Player) entity;

        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            if (p.getAttackCooldown().isReady) {
                p.getSkillset().getSkill(PlayerSwordAttack.class).execute();
                p.getAttackCooldown().reset();
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            if (p.getAttackCooldown().isReady) {
                p.getSkillset().getSkill(PlayerRangedAttack.class).execute();
                p.getAttackCooldown().reset();
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.F)) {
            if (p.getDashCooldown().isReady) {
                p.getSkillset().getSkill(PlayerDash.class).execute();
                p.getDashCooldown().reset();
            }
        }
    }
}
