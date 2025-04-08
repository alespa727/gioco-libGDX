package progetto.gameplay.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector2;
import progetto.CoreConfig;
import progetto.gameplay.Game;
import progetto.gameplay.manager.map.WorldManager;
import progetto.gameplay.map.events.SpawnEntityEvent;
import progetto.menu.PauseScreen;

import static progetto.gameplay.Game.STEP;

public enum GameManager implements State<Game> {

    PLAYING {
        @Override
        public void enter(Game screen) {
            System.out.println("GameScreen.PLAYING");
        }

        @Override
        public void update(Game screen) {
            if (Gdx.input.isKeyJustPressed(CoreConfig.getFERMAGIOCO())) {
                screen.getGameInfo().game.setScreen(new PauseScreen(screen.getGameInfo().game, screen));
            }

            screen.accumulator += screen.delta;

            // Aggiorna il gioco finché necessario
            while (screen.accumulator >= STEP) {
                float scaledTime = STEP * screen.getTimeScale();
                screen.update(scaledTime);
                screen.accumulator -= STEP;
                WorldManager.getInstance().step(scaledTime, 5, 5);
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
