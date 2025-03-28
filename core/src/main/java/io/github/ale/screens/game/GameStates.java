

package io.github.ale.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import io.github.ale.screens.pause.PauseScreen;
import io.github.ale.screens.settings.Settings;

import static io.github.ale.screens.game.GameScreen.STEP;

public enum GameStates implements State<GameScreen> {

    PLAYING {
        @Override
        public void enter(GameScreen screen) {
            System.out.println("GameScreen.PLAYING");
        }

        @Override
        public void update(GameScreen screen) {
            if (Gdx.input.isKeyPressed(Settings.getPulsanti()[7]))
                screen.gameState().changeState(GameStates.PAUSED);

            if (!screen.isPaused) {
                screen.accumulator += screen.delta;

                // Aggiorna il gioco finché necessario
                while (screen.accumulator >= STEP) {
                    screen.update(STEP, true);
                    screen.accumulator -= STEP;

                    screen.world.step(STEP, 6, 2); // Fixed timestep simulation
                }
            }

            // Disegna il gioco
            screen.draw(screen.delta);

            // Pausa
            if (Gdx.input.isKeyJustPressed(Settings.getPulsanti()[7]) && !screen.isPaused) {
                screen.isPaused = true;
                screen.game.setScreen(new PauseScreen(screen.game, screen));
            }
        }

        @Override
        public void exit(GameScreen screen) {

        }

        @Override
        public boolean onMessage(GameScreen entity, Telegram telegram) {
            return false;
        }
    },

    PAUSED {
        @Override
        public void enter(GameScreen screen) {
            System.out.println("GameScreen.PAUSED");
        }

        @Override
        public void update(GameScreen screen) {
            if (!screen.isPaused)
                screen.gameState().changeState(GameStates.PLAYING);
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
