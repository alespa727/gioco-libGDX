package io.github.ale.screens.gameplay.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import io.github.ale.GameConfig;
import io.github.ale.screens.gameplay.GameScreen;
import io.github.ale.screens.menu.PauseScreen;

import static io.github.ale.screens.gameplay.GameScreen.STEP;

public enum GameManager implements State<GameScreen> {

    PLAYING {

        @Override
        public void enter(GameScreen screen) {
            System.out.println("GameScreen.PLAYING");
        }

        @Override
        public void update(GameScreen screen) {
            if (Gdx.input.isKeyJustPressed(GameConfig.getFERMAGIOCO())) {
                screen.getGameInfo().game.setScreen(new PauseScreen(screen.getGameInfo().game, screen));
            }

            screen.accumulator += screen.delta;

            // Aggiorna il gioco finché necessario
            while (screen.accumulator >= STEP) {
                float scaledTime = STEP * screen.getTimeScale();
                screen.update(scaledTime);
                screen.accumulator -= STEP;

                screen.getWorld().step(scaledTime, 8, 8);
            }


            // Disegna il gioco
            screen.draw();
        }

        @Override
        public void exit(GameScreen screen) {

        }

        @Override
        public boolean onMessage(GameScreen entity, Telegram telegram) {
            return false;
        }
    };
}
