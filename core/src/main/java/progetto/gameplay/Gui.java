package progetto.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import progetto.gameplay.entity.types.humanEntity.combatEntity.player.Player;

public class Gui {
    private final Game gamescreen; // Riferimento allo schermo di gioco
    private final ShapeRenderer shapeRenderer; // Strumento per disegnare forme (come la barra della vita)
    private float height; // Altezza della finestra
    private float width;  // Larghezza della finestra

    Player player; // Riferimento al giocatore
    private float healthPercentage; // Percentuale di vita del giocatore
    @SuppressWarnings("unused")
    private final SpriteBatch batch; // Batch per disegnare le immagini (non usato per ora)

    // Costruttore che prepara la GUI
    public Gui(Game gamescreen) {
        this.gamescreen = gamescreen;
        shapeRenderer = new ShapeRenderer(); // Inizializza lo strumento per disegnare forme
        width = Gdx.app.getGraphics().getWidth(); // Prende la larghezza della finestra
        height = Gdx.app.getGraphics().getHeight(); // Prende l'altezza della finestra
        batch = new SpriteBatch(); // Inizializza il batch per disegnare le immagini
    }

    // Metodo per disegnare gli oggetti sulla schermata
    public void draw() {
        // Se il giocatore non è stato trovato, lo ottiene
        if (player == null) {
            player = gamescreen.getEntityManager().player();
        } else {
            // Calcola la percentuale di salute del giocatore
            float health = player.getHealth() / player.getMaxHealth();
            healthPercentage = MathUtils.lerp(healthPercentage, health, 0.5f); // Passaggio fluido tra le percentuali
        }

        // Rinfresca le dimensioni della finestra
        width = Gdx.app.getGraphics().getWidth();
        height = Gdx.app.getGraphics().getHeight();

        // Inizia a disegnare le forme (barra della vita e altro)
        shapeRenderer.begin(ShapeType.Filled);

        // Disegna la barra della vita
        barravita();
        skill(); // Questo è per le skill, che non sono implementate ancora

        // Fine del disegno delle forme
        shapeRenderer.end();
    }

    // Metodo per disegnare la barra della vita
    public void barravita() {
        float offset = width / 100; // Spazio extra per il bordo della barra

        // Disegna il contorno della barra della vita (grigio scuro)
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(width / 25, height - height / 20, width / 5, -height / 20);

        // Disegna il bordo interno della barra (nero)
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(width / 25 + offset, height - height / 20 - offset, width / 5, -height / 20);

        // Disegna la parte rossa che rappresenta la vita
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(width / 25 + offset, height - height / 20 - offset, width / 5 * healthPercentage, -height / 20);
    }

    // Metodo per disegnare le skill del giocatore (da implementare in futuro)
    public void skill() {
        // Questo è il posto dove aggiungere le skill del giocatore
    }
}
