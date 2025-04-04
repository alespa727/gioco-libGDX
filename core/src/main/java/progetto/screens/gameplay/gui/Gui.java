package progetto.screens.gameplay.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import progetto.screens.gameplay.GameScreen;

public class Gui {
    private final GameScreen gamescreen;
    private final ShapeRenderer shapeRenderer;
    @SuppressWarnings("unused")
    private final SpriteBatch batch;

    public Gui(GameScreen gamescreen) {
        this.gamescreen = gamescreen;
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
    }

    public void draw() {
        shapeRenderer.begin(ShapeType.Filled);
        barravita();
        skill();
        shapeRenderer.end();
    }

    public void barravita() {
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(Gdx.graphics.getHeight() * 0.05f, Gdx.graphics.getHeight() - Gdx.graphics.getHeight() * 0.05f, 220, -50);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(Gdx.graphics.getHeight() * 0.05f + 10, Gdx.graphics.getHeight() - Gdx.graphics.getHeight() * 0.05f - 10, 200 * gamescreen.getEntityManager().player().getHealth() / 100, -30);
    }

    public void skill() {
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(Gdx.graphics.getHeight() * 0.05f, Gdx.graphics.getHeight() - Gdx.graphics.getHeight() * 0.05f - 60, 220, -30);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(Gdx.graphics.getHeight() * 0.05f + 10, Gdx.graphics.getHeight() - Gdx.graphics.getHeight() * 0.05f - 70, 200, -10);
    }
}
