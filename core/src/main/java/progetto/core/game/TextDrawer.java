package progetto.core.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class TextDrawer{
    private SpriteBatch batch;
    private BitmapFont font;
    private GlyphLayout layout;
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;

    private String text = "";

    public TextDrawer(){
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/myfont2.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20 * Gdx.graphics.getWidth()/1280;
        parameter.color = Color.WHITE.cpy().mul(0.9f);
        this.font = generator.generateFont(parameter);
        this.batch = new SpriteBatch();
        this.layout = new GlyphLayout();
    }

    public BitmapFont generateFont(int size) {
        parameter.size = size;
        return generator.generateFont(parameter);
    }

    public void setText(String text){
        this.text = text;
    }

    public String getText(){
        return text;
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


}
