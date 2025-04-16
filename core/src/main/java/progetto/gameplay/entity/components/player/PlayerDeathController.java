package progetto.gameplay.entity.components.player;

import progetto.gameplay.entity.components.IteratableComponent;
import progetto.gameplay.player.Player;

public class PlayerDeathController extends IteratableComponent {
    private final Player player;
    public PlayerDeathController(Player player) {
        this.player = player;
    }

    @Override
    public void update(float delta) {
        if (player.getHealth() <= 0) {
            player.setDead();
            player.getStats().setHealth(100);
        }
    }
}
