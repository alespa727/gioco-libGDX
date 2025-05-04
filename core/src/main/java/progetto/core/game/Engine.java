package progetto.core.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import progetto.core.App;
import progetto.core.main.MainMenu;
import progetto.core.settings.model.ModelImpostazioni;
import progetto.ECS.EntityEngine;
import progetto.ECS.components.specific.base.StateComponent;
import progetto.ECS.entities.specific.EntityConfig;
import progetto.ECS.systems.specific.*;
import progetto.factories.EntityConfigFactory;
import progetto.factories.EntityFactory;
import progetto.graphics.shaders.specific.ColorFilter;
import progetto.input.KeyHandler;
import progetto.core.CameraManager;
import progetto.core.game.player.Player;
import progetto.core.pause.Pause;
import progetto.world.WorldManager;
import progetto.world.map.MapManager;

public class Engine {

    public static final float STEP = 1 / 60f;

    private App app;
    private Time time;
    public GameScreen game;
    public EntityEngine entityEngine;
    public MapManager map;
    private Player player;

    public Engine(App app, GameScreen game) {
        this.app = app;
        this.game = game;
        this.time = new Time();
    }

    public EntityEngine getEntityEngine() {
        return entityEngine;
    }

    public MapManager getMap() {
        return map;
    }

    public Player getPlayer() {
        return player;
    }

    public Time getTime() {
        return time;
    }

    public void update(float delta) {

        time.update(delta); // Tempo aggiornato
        SpriteBatch batch = app.batch;

        if (Gdx.input.isKeyJustPressed(ModelImpostazioni.getComandiModificabili().getHashMap().get("FERMA GIOCO"))) {
            app.setScreen(new Pause(app, "   Pausa", new Color(0.3f, 0.3f, 0.4f, 1)));
        }

        // Aggiorna il gioco finché necessario
        while (time.getAccumulator() >= STEP) {
            float scaledTime = STEP * time.getTimeScale();
            WorldManager.getInstance().step(scaledTime, 8, 8);
            step(scaledTime);
            WorldManager.update();
            time.setAccumulator(time.getAccumulator() - STEP);
            KeyHandler.input();
        }


        game.getGameDrawer().draw(batch, delta);


    }

    /**
     * Aggiorna lo stato di gioco
     * @param delta tempo trascorso dall'ultimo frame
     */
    public void step(float delta) {

        //entityEngine.summon(EntityFactory.createSword(10, 10, 0.2f, 1f, new Vector2(0, -0.5f), 50, entityEngine, null));

        Player player = entityEngine.player();
        StateComponent state = player.components.get(StateComponent.class);

        if (!state.isAlive()) {
            MainMenu main = new MainMenu(app, "Sei morto", new Color(0.85f, 0.2f, 0.2f, 1));
            game.app.setScreen(main);
        }

        // Aggiorna la logica delle entità
        entityEngine.render(delta);

        // Aggiorna la mappa
        map.render(delta);

        // Aggiorna la telecamera
        CameraManager.update(entityEngine, game.viewport, delta, false);

        this.app.batch.setProjectionMatrix(CameraManager.getInstance().combined);
        this.app.renderer.setProjectionMatrix(CameraManager.getInstance().combined);

    }

    public void initializeEntityEngine() {
        this.entityEngine = new EntityEngine(game);
        EntityConfig p = EntityConfigFactory.createPlayerConfig();
        this.player = new Player(p, entityEngine);

        entityEngine.summon(player);

        entityEngine.addSystem(
            new CullingSystem(),
            new CooldownSystem(),
            new UserInputSystem(),
            new PlayerSystem(),
            new DeathSystem(),
            new MovementSystem(),
            new SpeedLimiterSystem(),
            new NodeTrackerSystem(),
            new StatemachineSystem(),
            new SkillSystem(),
            new RangeSystem(),
            new HitSystem(),
            new KnockbackSystem(),
            new ItemHoldingSystem(),
            new FallingSystem()
        );

        entityEngine.render(0);

        this.map = new MapManager(game, game.viewport, entityEngine, 1);

    }

}
