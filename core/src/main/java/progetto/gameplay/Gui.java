package progetto.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import progetto.gameplay.entities.types.Player;

public class Gui {
    private final GameScreen gamescreen;
    private final ShapeRenderer shapeRenderer;
    private float height;
    private float width;
    TextureRegion[][] health;
    Sprite[][] sprite;

    Player player;
    private float healthPercentage;
    @SuppressWarnings("unused")
    private final SpriteBatch batch;
    public Gui(GameScreen gamescreen) {
        this.gamescreen = gamescreen;
        shapeRenderer = new ShapeRenderer();
        width = Gdx.app.getGraphics().getWidth();
        height = Gdx.app.getGraphics().getHeight();
        batch = new SpriteBatch();
        Texture healthTexture = new Texture(Gdx.files.internal("menu/healthbar.png"));
        health = TextureRegion.split(healthTexture, healthTexture.getWidth()/3, healthTexture.getHeight()/3);
        sprite = new Sprite[health.length][health[0].length];
        for (int i = 0; i < health.length; i++) {
            for (int j = 0; j < health[0].length; j++) {
                sprite[i][j] = new Sprite(health[i][j]);
                sprite[i][j].setColor(Color.WHITE);
                sprite[i][j].setSize(health[0][0].getRegionWidth()*10, health[0][0].getRegionHeight()*10);
                sprite[i][j].setPosition(width/2+sprite[i][j].getWidth()*j, height/2);
            }
        }
    }

    public void draw() {
        if (player==null) {
            player = gamescreen.getEntityManager().player();
        }else{
            float health = player.getHealth() / player.getMaxHealth();
            healthPercentage = MathUtils.lerp(healthPercentage, health, 0.5f);
        }
        width = Gdx.app.getGraphics().getWidth();
        height = Gdx.app.getGraphics().getHeight();
        shapeRenderer.begin(ShapeType.Filled);
        barravita();
        skill();
        shapeRenderer.end();

    }

    public void barravita() {
        float offset = width/100;
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(width/25, height-height/20, width/5, -height/20);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(width/25+offset, height-height/20-offset, width/5, -height/20);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(width/25+offset, height-height/20-offset, width/5*healthPercentage, -height/20);

    }


    public void skill() {

    }
}
