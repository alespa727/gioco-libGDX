package progetto.core.game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TextDrawer{
    SpriteBatch batch;
    BitmapFont font;
    GlyphLayout layout;

    String text = "ciao";

    public TextDrawer(){
        this.font = new BitmapFont();
        this.batch = new SpriteBatch();
        this.layout = new GlyphLayout();
    }

    public void drawText(float x, float y){
        layout.setText(font, text);
        float textWidth = layout.width;
        float centeredX = x - textWidth / 2f;
        this.batch.begin();
        this.font.draw(batch, text, centeredX, y);
        this.batch.end();
    }
}
