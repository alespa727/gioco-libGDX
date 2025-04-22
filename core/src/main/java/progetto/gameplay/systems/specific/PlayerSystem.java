package progetto.gameplay.systems.specific;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;
import progetto.gameplay.entities.components.specific.general.PlayerComponent;
import progetto.gameplay.entities.skills.specific.player.PlayerDash;
import progetto.gameplay.entities.skills.specific.player.PlayerRangedAttack;
import progetto.gameplay.entities.skills.specific.player.PlayerSwordAttack;
import progetto.gameplay.entities.specific.base.Entity;
import progetto.gameplay.player.Player;
import progetto.gameplay.systems.base.AutomaticSystem;

public class PlayerSystem extends AutomaticSystem {

    public PlayerSystem() {
        super(Array.with(new PlayerComponent()));
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
