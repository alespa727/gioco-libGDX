package progetto.gameplay.entities.components.specific;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import progetto.gameplay.entities.components.base.Component;
import progetto.gameplay.entities.specific.base.Entity;
import progetto.gameplay.entities.specific.specific.living.Humanoid;
import progetto.graphics.animations.CustomAnimation;

public class ShadowComponent extends Component {
    private CustomAnimation anim;
    public ShadowComponent(Humanoid entity) {
        String[] string = new String[]{"default"};
        Texture texture = new Texture(Gdx.files.internal("entities/"+ entity.getClass().getSimpleName()+"_shadow.png"));
        Texture[] textures = new Texture[]{texture};
        anim = new CustomAnimation(string, textures);
    }

    public TextureRegion play(Entity e, String animation_name, float elapsedTime) {
        return anim.play(e, animation_name, elapsedTime);
    }
}
