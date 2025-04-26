package progetto.core.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class TextDrawer{
    SpriteBatch batch;
    BitmapFont font;
    GlyphLayout layout;

    String text = "ciao";

    public TextDrawer(){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/myfont2.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40;
        this.font = generator.generateFont(parameter);
        this.batch = new SpriteBatch();
        this.layout = new GlyphLayout();
    }

    public void setColor(Color color){
        font.setColor(color);
    }

    public void drawCenteredText(float x, float y){
        layout.setText(font, text);
        float textWidth = layout.width;
        float centeredX = x - textWidth / 2f;
        this.batch.begin();
        this.font.draw(batch, text, centeredX, y);
        this.batch.end();
    }

    public void drawCenteredText(Batch batch, String text, float x, float y){
        layout.setText(font, text);
        float textWidth = layout.width;
        float centeredX = x - textWidth / 2f;
        batch.begin();
        this.font.draw(batch, text, centeredX, y);
        batch.end();
    }

    public void drawText(Batch batch, String text, float x, float y){
        batch.begin();
        this.font.draw(batch, text, x, y);
        batch.end();
    }
}
