package progetto.gameplay.systems.specific;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;
import progetto.gameplay.entities.skills.specific.player.PlayerDash;
import progetto.gameplay.entities.skills.specific.player.PlayerRangedAttack;
import progetto.gameplay.entities.skills.specific.player.PlayerSwordAttack;
import progetto.gameplay.entities.specific.base.Entity;
import progetto.gameplay.player.Player;
import progetto.gameplay.systems.base.System;

public class PlayerSystem extends System {

    @Override
    public void update(float delta, Array<Entity> list) {
        for (Entity e : list) {
            if (!(e instanceof Player)) {
                continue;
            }
            Player p = (Player) e;

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
}
