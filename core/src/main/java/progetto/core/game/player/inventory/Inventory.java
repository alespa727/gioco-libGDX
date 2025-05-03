package progetto.core.game.player.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

import progetto.core.settings.model.ModelImpostazioni;

public class Inventory extends Window {
    final Array<Item> items;
    final Stage stage;
    final Table table;

    final boolean[][] slots;
    final int width = 1, height = 5;
    final int itemSize = 100;
    int itemCount = 0;

    public Inventory(WindowStyle skin) {
        super("Inventory", skin);

        slots = new boolean[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                slots[i][j] = false;
            }
        }
        setKeepWithinStage(false);
        getTitleLabel().setVisible(false);     // nasconde il testo
        getTitleTable().clear();               // rimuove la barra
        setSize(width * itemSize, height * itemSize);
        setVisible(false);
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera()));
        setPosition(stage.getWidth() / 2 - getWidth()/2, stage.getHeight() / 2 - getHeight()/2);
        stage.addActor(this);
        table = new Table();
        addActor(table);
        table.setFillParent(true);
        table.setDebug(true);
        items = new Array<>();
        Gdx.input.setInputProcessor(stage);
    }

    public void addItem(Item item) {
        items.add(item);
        rebuild();
    }

    public void removeItem(Item item) {
        items.removeValue(item, true);
    }

    public void rebuild() {
        table.clear();  // Pulisce la tabella
        itemCount = 0;
        // Aggiungi gli item alla tabella
        for (Item item : items) {
            itemCount++;
            table.add(item.getImage()).size(itemSize, itemSize);  // Aggiungi l'immagine dell'item
            if (itemCount % width == 0) {
                table.row();  // Passa alla riga successiva dopo aver aggiunto l'ultimo item in una colonna
            }
        }

        // Calcola il numero di celle vuote da aggiungere per arrivare a 50
        int totalCells = width * height;
        int cellsToAdd = totalCells - itemCount;  // Celle vuote da aggiungere

        // Se ci sono celle da aggiungere
        for (int i = 0; i < cellsToAdd; i++) {
            table.add().size(itemSize, itemSize);  // Aggiungi una cella vuota
            if ((itemCount + i + 1) % width == 0) {
                table.row();  // Passa alla riga successiva se necessario
            }
        }
    }

    public Stage getStage() {
        return stage;
    }

    public void show() {
        setVisible(true);
    }

    public void hide() {
        setVisible(false);
    }

    public void update() {
        if (Gdx.input.isKeyJustPressed(ModelImpostazioni.getComandiModificabili().getHashMap().get("APRI INVENTARIO"))) {
            setVisible(!isVisible());
        }
        stage.getViewport().apply();
        stage.act();
        stage.draw();
    }
}
