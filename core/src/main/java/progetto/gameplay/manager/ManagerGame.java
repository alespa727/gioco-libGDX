package progetto.gameplay.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import progetto.CoreConfig;
import progetto.gameplay.Game;
import progetto.menu.PauseScreen;
import progetto.utils.KeyHandler;

import static progetto.gameplay.Game.STEP;

public enum ManagerGame implements State<Game> {

    PLAYING {
        @Override
        public void enter(Game screen) {
            System.out.println("GameScreen.PLAYING");
        }

        @Override
        public void update(Game screen) {
            if (Gdx.input.isKeyJustPressed(CoreConfig.getFERMAGIOCO())) {
                screen.getInfo().core.setScreen(new PauseScreen(screen.getInfo().core, screen));
            }

            screen.accumulator += screen.delta;

            // Aggiorna il gioco finché necessario
            while (screen.accumulator >= STEP) {
                float scaledTime = STEP * screen.getTimeScale();
                ManagerWorld.getInstance().step(scaledTime, 8, 8);
                screen.update(scaledTime);
                screen.accumulator -= STEP;
                KeyHandler.input();
            }

            // Disegna il gioco
            screen.draw();
        }

        @Override
        public void exit(Game screen) {

        }

        @Override
        public boolean onMessage(Game entity, Telegram telegram) {
            return false;
        }
    }
}
