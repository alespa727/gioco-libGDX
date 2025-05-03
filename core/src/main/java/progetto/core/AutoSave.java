package progetto.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import progetto.entity.EntityEngine;
import progetto.entity.entities.specific.EntityInstance;
import progetto.world.map.MapManager;

public class AutoSave extends Thread {

    private boolean running = false;
    private final EntityEngine entityEngine;
    private final MapManager mapManager;

    public AutoSave(EntityEngine entityEngine, MapManager mapManager) {
        this.entityEngine = entityEngine;
        this.mapManager = mapManager;
    }

    public void stopSaving() {
        this.running = false;
        this.interrupt();
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            System.out.println("AutoSave");
            try {
                sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("Fermato durante lo sleep");
            }

            if (entityEngine !=null){
                Array<EntityInstance> entities = entityEngine.save();

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
