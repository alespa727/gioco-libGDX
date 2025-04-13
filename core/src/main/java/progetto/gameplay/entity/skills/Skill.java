package progetto.gameplay.entity.skills;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import progetto.gameplay.entity.components.entity.Cooldown;
import progetto.gameplay.entity.types.living.Humanoid;

public abstract class Skill {

    // === CAMPI ===

    protected Texture[] texture;               // Array per i frame della texture
    protected TextureRegion[] textureRegion;    // Array per le regioni della texture
    protected Animation<TextureRegion> animation; // Animazione basata su TextureRegion

    public final String name;                  // Nome della skill
    public final String description;           // Descrizione della skill
    protected final Humanoid owner;        // Entità che possiede la skill

    protected boolean isBeingUsed = false;     // Flag per verificare se la skill è in uso
    protected Cooldown cooldown;               // Gestore del cooldown per la skill

    protected float elapsedTime;               // Tempo trascorso dall'inizio dell'uso della skill

    // === COSTRUTTORE ===

    public Skill(Humanoid owner, String name, String description) {
        this.owner = owner;
        this.name = name;
        this.description = description;
    }

    // === METODI ===

    /**
     * Verifica se la skill è attualmente in uso.
     */
    public boolean isBeingUsed() {
        return isBeingUsed;
    }

    /**
     * Carica la texture e prepara l'animazione della skill.
     * @param path Percorso delle immagini per i frame della skill.
     * @param frames Numero di frame dell'animazione.
     * @param framesPerSecond Numero di fotogrammi al secondo per l'animazione.
     */
    public void loadTexture(String path, int frames, int framesPerSecond) {
        texture = new Texture[frames];
        textureRegion = new TextureRegion[frames];

        // Caricamento delle texture per ogni frame
        for (int i = 0; i < texture.length; i++) {
            texture[i] = new Texture(path + (i + 1) + ".png");
        }

        // Creazione delle regioni della texture
        for (int i = 0; i < frames; i++) {
            textureRegion[i] = new TextureRegion(texture[i]);
        }

        // Creazione dell'animazione con i frame caricati
        animation = new Animation<>(1f / framesPerSecond, this.textureRegion);
        cooldown = new Cooldown((float) frames / framesPerSecond);
        animation.setPlayMode(Animation.PlayMode.LOOP);  // Imposta la modalità di loop per l'animazione
    }

    /**
     * Ottiene l'animazione della skill.
     */
    public Animation<TextureRegion> getAnimation() {
        return animation;
    }

    /**
     * Imposta se la skill è in uso.
     */
    public void setBeingUsed(boolean isBeingUsed) {
        this.isBeingUsed = isBeingUsed;
    }

    // === METODI ABSTRACT ===

    /**
     * Metodo che aggiorna lo stato della skill.
     */
    public abstract void update();

    /**
     * Metodo per disegnare la skill.
     * @param batch SpriteBatch utilizzato per il rendering.
     */
    public abstract void draw(SpriteBatch batch);

    /**
     * Metodo per eseguire la skill.
     */
    public abstract void execute();

    @Override
    public String toString() {
        return "Skill{" +
            "type='" + getClass().getSimpleName() + '\'' +
            '}';
    }
}
