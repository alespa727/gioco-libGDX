package progetto.gameplay.player.inventoty;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

import progetto.CoreConfig;
import progetto.gameplay.GameScreen;

public class Inventory extends Window {
    Array<Item> items;
    Stage stage;
    Table table;

    final boolean[][] slots;
    int itemCount=0;
    final int width = 5, height = 5;
    final int itemSize = 100;

    public Inventory(GameScreen screen, String title, Skin skin) {
        super(title, skin);
        slots = new boolean[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                slots[i][j] = false;
            }
        }
        setMovable(true);
        setKeepWithinStage(false);
        setSize(width*itemSize, height*itemSize);
        setVisible(false);
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera()));
        stage.addActor(this);
        table = new Table();
        addActor(table);
        table.setFillParent(true);
        table.setDebug(true);
        items = new Array<>();
        Gdx.input.setInputProcessor(stage);
        addItem(new Item("", "", new Texture(Gdx.files.internal("lich.jpg"))));
        addItem(new Item("", "", new Texture(Gdx.files.internal("lich.jpg"))));
        addItem(new Item("", "", new Texture(Gdx.files.internal("lich.jpg"))));
        addItem(new Item("", "", new Texture(Gdx.files.internal("lich.jpg"))));
        addItem(new Item("", "", new Texture(Gdx.files.internal("lich.jpg"))));
        addItem(new Item("", "", new Texture(Gdx.files.internal("lich.jpg"))));
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
        int totalCells = width*height;
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

    public void show(){
        setVisible(true);
    }

    public void hide(){
        setVisible(false);
    }

    public void update() {
        if (Gdx.input.isKeyJustPressed(CoreConfig.getAPRIINVENTARIO())){
            setVisible(!isVisible());
        }
        stage.getViewport().apply();
        stage.act();
        stage.draw();
    }
}
