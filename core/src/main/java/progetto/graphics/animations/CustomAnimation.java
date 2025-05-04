package progetto.graphics.animations;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import progetto.ECS.entities.Entity;

import java.util.HashMap;

/**
 * Classe che gestisce una serie di animazioni personalizzate.
 * Ogni animazione è associata a una texture e viene eseguita tramite un nome specificato.
 */
public class CustomAnimation {

    private final HashMap<String, DefaultAnimationSet> textures; // Mappa delle animazioni

    /**
     * Costruttore
     */
    public CustomAnimation(final String[] animation_name, Texture[] img) {
        textures = new HashMap<>();
        for (int i = 0; i < img.length; i++) {
            textures.put(animation_name[i], new DefaultAnimationSet(img[i]));
        }
    }

    /**
     * Esegue l'animazione specificata per un'entità.
     * @param e l'entità per la quale eseguire l'animazione.
     * @param animation_name il nome dell'animazione da eseguire.
     * @param time il tempo trascorso dall'inizio dell'animazione.
     * @return il frammento di animazione corrente.
     */
    public TextureRegion play(Entity e, final String animation_name, final float time) {
        return textures.get(animation_name).play(e, time);
    }
}


