package progetto;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import progetto.entity.Engine;
import progetto.entity.entities.specific.EntityInstance;
import progetto.world.map.MapManager;

public class AutoSave extends Thread {

    private boolean running = false;
    private final Engine engine;
    private final MapManager mapManager;

    public AutoSave(Engine engine, MapManager mapManager) {
        this.engine = engine;
        this.mapManager = mapManager;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        while (running) {
            try {
                sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (engine!=null){
                Array<EntityInstance> entities = engine.save();

                FileHandle fileHandle = Gdx.files.local("/save/maps/" + mapManager.getMap().nome + ".json");

                Json json = new Json();
                json.setOutputType(JsonWriter.OutputType.json);

                String string = json.prettyPrint(entities);
                try {
                    fileHandle.writeString(string, false);
                }catch (Exception e){
                    System.err.println("Errore salvando entità");
                }
            }
        }
    }
}
