package progetto.graphics.shaders.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Classe astratta che rappresenta uno shader da applicare.
 * Gestisce il programma dello shader.
 */
public abstract class Shader {

    /**
     * Il programma dello shader che definisce gli effetti grafici.
     */
    protected ShaderProgram program;

    /**
     * Il framebuffer su cui applicare lo shader, usato per renderizzare temporaneamente l'immagine.
     */
    protected FrameBuffer frameBuffer;

    /**
     * Indica se lo shader è attivo.
     */
    private boolean active = false;

    /**
     * Inizia l'applicazione dello shader. Viene chiamato prima di disegnare gli oggetti con questo shader.
     */
    public abstract void begin();

    /**
     * Termina l'applicazione dello shader. Viene chiamato dopo aver disegnato gli oggetti con questo shader.
     */
    public abstract void end();

    /**
     * Disegna un oggetto con lo shader applicato.
     *
     * @param batch il batch di SpriteBatch da usare per il disegno.
     */
    public abstract void draw(SpriteBatch batch);

    /**
     * Disegna un oggetto con lo shader applicato, specificando la posizione e le dimensioni.
     */
    public abstract void draw(SpriteBatch batch, float x, float y, float width, float height);

    /**
     * Restituisce lo stato di attivazione dello shader.
     *
     * @return true se lo shader è attivo, altrimenti false.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Imposta lo stato di attivazione dello shader.
     *
     * @param active true per attivare lo shader, false per disattivarlo.
     */
    public void setActive(boolean active) {
        this.active = active;
    }
}
