package progetto.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import progetto.core.game.GameScreen;
import progetto.player.Player;
import progetto.ui.ProgressBar;

public class Gui {
    private final GameScreen gamescreen;
    private final SpriteBatch batch;
    @SuppressWarnings("unused")
    Player player;

    ProgressBar progressBar;

    // Constanti
    public final int larghezza = 16;
    public final int altezza = 9;
    private final float larghezzaBlocco;

    private float healthPercentage;

    public Gui(GameScreen gamescreen) {
        this.gamescreen = gamescreen;
        batch = new SpriteBatch();
        progressBar = new ProgressBar(new Texture("WindowUi.png"), new Texture("ProgressBar2.png"));

        larghezzaBlocco = (float) Gdx.graphics.getWidth() / larghezza;

        progressBar.setPosition(larghezzaBlocco*0.5f, larghezzaBlocco*(9-0.75f-0.5f));
        progressBar.setSize(new Vector2(larghezzaBlocco*4, larghezzaBlocco*0.75f));
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
    }
}
