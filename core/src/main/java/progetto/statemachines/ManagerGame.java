package progetto.statemachines;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import progetto.core.CoreConfig;
import progetto.core.game.GameScreen;
import progetto.core.game.GameTime;
import progetto.manager.input.KeyHandler;
import progetto.manager.world.WorldManager;
import progetto.screens.PauseScreen;

import static progetto.core.game.GameScreen.STEP;

public enum ManagerGame implements State<GameScreen> {

    PLAYING {
        @Override
        public void enter(GameScreen screen) {

        }

        @Override
        public void update(GameScreen screen) {
            SpriteBatch batch = screen.getInfo().core.batch;

            GameTime time = screen.getTime();
            if (Gdx.input.isKeyJustPressed(CoreConfig.getFERMAGIOCO())) {
                screen.getInfo().core.setScreen(new PauseScreen(screen.getInfo().core, screen));
            }

            // Aggiorna il gioco finché necessario
            while (time.getAccumulator() >= STEP) {
                float scaledTime = STEP * screen.getTimeScale();
                WorldManager.getInstance().step(scaledTime, 8, 8);
                // Disegna il gioco
                screen.update(scaledTime);
                screen.updateWorld();
                time.setAccumulator(time.getAccumulator() - STEP);
                KeyHandler.input();
            }

            screen.getGameDrawer().draw(batch);
        }

        @Override
        public void exit(GameScreen screen) {

        }

        @Override
        public boolean onMessage(GameScreen entity, Telegram telegram) {
            return false;
        }
    }
}
