package progetto.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import progetto.gameplay.GameScreen;
import progetto.gameplay.map.Map;

public class DebugWindow extends Window {

    private final Stage stage;
    private final Label fpsLabel;
    private final Label memoryLabel;
    private final TextField xCoord;
    private final TextField yCoord;

    private static boolean entityAI=true;
    private static boolean hitboxes=false;
    private static boolean pathfinding=false;


    public DebugWindow(GameScreen gameScreen, String title, Skin skin) {
        super(title, skin);
        setKeepWithinStage(true);
        setDebug(true);

        // Crea la viewport
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera()));
        // Aggiunge la finestra
        stage.addActor(this);

        // Impostazioni della finestra
        setMovable(true);

        // Posiziona la finestra in basso (spazio per il titolo) e dai un margine
        int marginTop = 50;  // Distanza dal bordo superiore per il titolo
        setPosition(10, Gdx.graphics.getHeight() - 150 - marginTop); // Aggiungi spazio sopra

        // Crea i TextField, TextButton e Label
        xCoord = new TextField("", skin);
        yCoord = new TextField("", skin);
        xCoord.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
        yCoord.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());

        TextButton teleport = new TextButton("Teleport player", skin);
        teleport.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                float coordY = -1;
                float coordX = -1;
                if (!xCoord.getText().isEmpty() && !yCoord.getText().isEmpty()) {
                    coordX = Float.parseFloat(xCoord.getText());
                    coordY = Float.parseFloat(yCoord.getText());
                }

                if (coordX > 0 && coordY > 0 && coordX < Map.width() && coordY < Map.height()) {
                    gameScreen.getEntityManager().player().teleport(new Vector2(coordX, coordY));
                } else {
                    xCoord.setMessageText("Errore di coordinate");
                    yCoord.setMessageText("Errore di coordinate");
                    System.err.println(Map.width() + " " + Map.height());
                    System.err.println("Coordinate non valide");
                }
            }
        });

        fpsLabel = new Label("FPS: 0", skin);
        memoryLabel = new Label("Memory: 0MB", skin);

        TextButton ai = new TextButton("Entity AI interruttore", skin);
        ai.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                entityAI = !entityAI;
            }
        });

        TextButton hitboxesButton = new TextButton("Attiva debug hitbox", skin);
        hitboxesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hitboxes = !hitboxes;
            }
        });

        TextButton pathfindingButton = new TextButton("Pathfinding", skin);
        pathfindingButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pathfinding = !pathfinding;
            }
        });

        // Organizza gli elementi in due tabelle separate per un layout più chiaro
        Table debugTable = new Table();
        debugTable.top().left();
        debugTable.add(fpsLabel).expandX().fillX().padBottom(10).row();
        debugTable.add(memoryLabel).expandX().fillX().padBottom(10).row();
        debugTable.add(ai).expandX().fillX().padBottom(10).row();
        debugTable.add(hitboxesButton).expandX().fillX().padBottom(10).row();
        debugTable.add(pathfindingButton).expandX().fillX().padBottom(10).row();

        Table teleportTable = new Table();
        teleportTable.top().left();
        teleportTable.add(xCoord).expandX().fillX().padBottom(10).row();
        teleportTable.add(yCoord).expandX().fillX().padBottom(10).row();
        teleportTable.add(teleport).expandX().fillX().padBottom(10).row();

        // Tabella principale che contiene le due tabelle
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.add(debugTable).expandY().top().pad(45).row();  // Sezione di debug
        mainTable.add(teleportTable).expandY().top().pad(10).row();  // Sezione teleport

        // Aggiungi la tabella principale alla finestra
        add(mainTable).expand().fill().pad(5).row();

        // Input processor per il stage
        Gdx.input.setInputProcessor(stage);
        pack();

        // Imposta una dimensione iniziale per la finestra
        setSize(300, 400); // Imposta la larghezza e l'altezza iniziale
    }




    public static boolean renderEntities() {
        return entityAI;
    }

    public static boolean renderHitboxes() {
        return hitboxes;
    }

    public static boolean renderPathfinding() {
        return pathfinding;
    }

    public static void setDebugMode(boolean state) {
        if (hitboxes==state) {
            TerminalCommand.printError("Hitboxes mode is already active.");
        }else{
            TerminalCommand.printMessage("Debug mode is now active.");
            hitboxes = state;
        }

        if (pathfinding==state) {
            TerminalCommand.printError("Pathfinding mode is already active.");
        }else{
            TerminalCommand.printMessage("Pathfinding mode is now active.");
            pathfinding = state;
        }

    }

    public static void setEntityAI(boolean entityAI) {
        DebugWindow.entityAI = entityAI;
    }

    public void draw() {
        stage.getViewport().apply();
        stage.act(Gdx.graphics.getDeltaTime()); // Update the stage (for animations or actions)
        stage.draw(); // Draw the stage
    }

    public void updateDebugInfo(float fps, long memory) {
        fpsLabel.setText("FPS: " + fps);
        memoryLabel.setText("Memory: " + memory / 1024 / 1024 + "MB");
    }

}


