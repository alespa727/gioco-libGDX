package progetto.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

public class ProgressBar {
    private Vector2 pos;
    private Vector2 size;

    private final NinePatchDrawable background;
    private final NinePatchDrawable foreground;

    private float progress;
    private float max=1f;

    public ProgressBar(Texture background, Texture progress) {
        NinePatch patch = new NinePatch(background, 7, 7, 7, 7);
        this.background = new NinePatchDrawable(patch);
        NinePatch patch2 = new NinePatch(progress, 7, 7, 7, 7);
        this.foreground = new NinePatchDrawable(patch2);
    }

    public void setPosition(float x, float y) {
        pos = new Vector2(x, y);
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }

    public void setWidth(float width) {
        this.size.x = width;
    }

    public void setHeight(float height) {
        this.size.y = height;
    }

    public void setProgress(float progress) {
        if (progress > max) {
            progress = max;
        }
        this.progress = progress;
    }

    public float getProgress() {
        return progress;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public void draw(Batch batch) {
        background.draw(batch, pos.x, pos.y, size.x, size.y);
        foreground.draw(batch, pos.x, pos.y, progress*size.x, size.y);
    }

}
