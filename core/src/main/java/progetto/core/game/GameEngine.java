package progetto.core.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import progetto.core.Core;
import progetto.core.settings.model.ModelImpostazioni;
import progetto.entity.Engine;
import progetto.entity.components.specific.base.StateComponent;
import progetto.entity.entities.base.Entity;
import progetto.entity.entities.base.EntityConfig;
import progetto.entity.systems.specific.*;
import progetto.factories.EntityConfigFactory;
import progetto.factories.EntityFactory;
import progetto.input.DebugWindow;
import progetto.input.KeyHandler;
import progetto.player.ManagerCamera;
import progetto.player.Player;
import progetto.core.defeat.DefeatScreen;
import progetto.core.pause.PauseScreen;
import progetto.world.WorldManager;
import progetto.world.map.MapManager;

public class GameEngine {

    public static final float STEP = 1 / 60f;

    private Core core;
    private GameTime time;
    public GameScreen game;
    public Engine engine;
    public MapManager map;
    private Player player;

    public GameEngine(Core core, GameScreen game) {
        this.core = core;
        this.game = game;
        this.time = new GameTime();
    }

    public Engine getEntityEngine() {
        return engine;
    }

    public MapManager getMap() {
        return map;
    }

    public Player getPlayer() {
        return player;
    }

    public GameTime getTime() {
        return time;
    }

    private void loadAssets() {
        // Carica gli asset necessari
        Core.assetManager.load("entities/Finn.png", Texture.class);
        Core.assetManager.load("particle/particle.png", Texture.class);
        Core.assetManager.load("sounds/gunshot.mp3", Sound.class);
        Core.assetManager.load("sounds/fireball.mp3", Sound.class);
        Core.assetManager.load("entities/Lich.png", Texture.class);
        Core.assetManager.load("entities/nemico.png", Texture.class);
        Core.assetManager.load("entities/Finn/attack/sword.png", Texture.class);
        Core.assetManager.finishLoading();
    }

    public void update(float delta) {

        time.update(delta); // Tempo aggiornato
        SpriteBatch batch = core.batch;

        if (Gdx.input.isKeyJustPressed(ModelImpostazioni.getComandiModificabili().getHashMap().get("FERMA GIOCO"))) {
            core.setScreen(new PauseScreen(core, game));
        }

        // Aggiorna il gioco finché necessario
        while (time.getAccumulator() >= STEP) {
            float scaledTime = STEP * time.getTimeScale();
            WorldManager.getInstance().step(scaledTime, 8, 8);
            // Disegna il gioco
            step(scaledTime);
            WorldManager.update();
            time.setAccumulator(time.getAccumulator() - STEP);
            KeyHandler.input();
        }

        game.getGameDrawer().draw(batch);
        if (DebugWindow.renderHitboxes()) game.getRenderer().draw();


    }

    /**
     * Aggiorna lo stato di gioco
     * @param delta tempo trascorso dall'ultimo frame
     */
    public void step(float delta) {

        //engine.summon(EntityFactory.createSword(10, 10, 0.2f, 1f, new Vector2(0, -0.5f), 50, engine, null));

        Player player = engine.player();
        StateComponent state = player.components.get(StateComponent.class);

        if (!state.isAlive()) {
            game.core.setScreen(new DefeatScreen(core));
        }

        // Aggiorna la logica delle entità
        engine.render(delta);

        // Aggiorna la mappa
        map.render();

        // Aggiorna la telecamera
        ManagerCamera.update(engine, game.viewport, delta, false);

        this.core.batch.setProjectionMatrix(ManagerCamera.getInstance().combined);
        this.core.renderer.setProjectionMatrix(ManagerCamera.getInstance().combined);

    }

    public void initializeEntityEngine() {
        this.loadAssets();
        this.engine = new Engine(game);
        EntityConfig p = EntityConfigFactory.createPlayerConfig();
        this.player = new Player(p, engine);

        engine.summon(player);

//        for (int i = 0; i < 5; i++) {
//            for (int j = 0; j < 5; j++) {
//
//            }
//        }
//        EntityConfig config = EntityConfigFactory.createEntityConfig("Finn", 10, 10);
//        Entity e = EntityFactory.createEnemy("Finn", config, engine);
//        engine.summon(e);

        engine.summon(EntityFactory.createSword(10, 10, 0.2f, 1f, new Vector2(0, -0.5f), 50, engine, null));

        engine.addSystem(
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
            new ItemHoldingSystem()
        );

        engine.render(0);

        this.map = new MapManager(game.viewport, engine, 1);
    }

}
