package progetto.gameplay.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import progetto.CoreConfig;
import progetto.gameplay.GameScreen;
import progetto.menu.PauseScreen;
import progetto.utils.GameTime;
import progetto.utils.KeyHandler;

import static progetto.gameplay.GameScreen.STEP;

public enum ManagerGame implements State<GameScreen> {

    PLAYING {
        @Override
        public void enter(GameScreen screen) {

        }

        @Override
        public void update(GameScreen screen) {
            GameTime time = screen.getTime();
            if (Gdx.input.isKeyJustPressed(CoreConfig.getFERMAGIOCO())) {
                screen.getInfo().core.setScreen(new PauseScreen(screen.getInfo().core, screen));
            }

            // Aggiorna il gioco finché necessario
            while (time.getAccumulator() >= STEP) {
                float scaledTime = STEP * screen.getTimeScale();
                ManagerWorld.getInstance().step(scaledTime, 8, 8);
                screen.update(scaledTime);
                screen.updateWorld();
                time.setAccumulator(time.getAccumulator() - STEP);
                KeyHandler.input();
            }

            // Disegna il gioco
            screen.getGameDrawer().draw(screen.getInfo().core.batch);
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
