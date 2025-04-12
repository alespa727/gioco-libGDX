package progetto.gameplay.player.inventoty;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Item{
    String name;
    String description;
    ImageButton image;

    public Item(String name, String description, Texture image) {
        this.name = name;
        this.description = description;
        TextureRegionDrawable region = new TextureRegionDrawable(image);
        this.image = new ImageButton(region);
        this.image.setSize(100, 100);
        this.image.setDebug(true);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public ImageButton getImage() {
        return image;
    }

}
