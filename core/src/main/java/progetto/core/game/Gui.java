package progetto.core.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import progetto.core.game.player.Player;
import progetto.ui.components.ProgressBar;

public class Gui {

    public final int WIDTH = 16;

    private final GameScreen gamescreen;
    private final SpriteBatch batch;
    private final ProgressBar progressBar;

    @SuppressWarnings("unused")
    private Player player;
    private float healthPercentage;

    public Gui(GameScreen gamescreen) {
        this.gamescreen = gamescreen;
        batch = new SpriteBatch();
        progressBar = new ProgressBar(new Texture("WindowUi.png"), new Texture("ProgressBar2.png"));

        float larghezzaBlocco = (float) Gdx.graphics.getWidth() / WIDTH;

        progressBar.setPosition(larghezzaBlocco *0.5f, larghezzaBlocco *(9-0.75f-0.5f));
        progressBar.setSize(new Vector2(larghezzaBlocco *4, larghezzaBlocco *0.75f));
        progressBar.setProgress(1);
    }

    public void draw() {
        if (player == null) {
            player = gamescreen.getEntityManager().player();
        } else {
            float health = player.getHealth() / player.getStats().maxHealth;
            healthPercentage = Interpolation.fastSlow.apply(healthPercentage, health, 0.1f);

            progressBar.setProgress(healthPercentage);
        }

        barravita();
    }

    public void barravita() {
        batch.begin();
        progressBar.draw(batch, Color.RED, 1);
        batch.end();
    }

    public void skill() {
        // NON IMPLEMENTATA
    }
}
