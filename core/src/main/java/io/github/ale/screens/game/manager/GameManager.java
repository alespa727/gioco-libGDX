package io.github.ale.screens.game.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import io.github.ale.ComandiGioco;
import io.github.ale.screens.game.GameScreen;
import io.github.ale.screens.pause.PauseScreen;

import static io.github.ale.screens.game.GameScreen.STEP;

public enum GameManager implements State<GameScreen> {

    PLAYING {
        @Override
        public void enter(GameScreen screen) {
            System.out.println("GameScreen.PLAYING");
        }

        @Override
        public void update(GameScreen screen) {
            if (Gdx.input.isKeyJustPressed(ComandiGioco.getFERMAGIOCO())) {
                screen.game.setScreen(new PauseScreen(screen.game, screen));
            }


            screen.accumulator += screen.delta;

            // Aggiorna il gioco finché necessario
            while (screen.accumulator >= STEP) {
                screen.update(STEP);
                screen.accumulator -= STEP;

                screen.getWorld().step(STEP, 1, 1);
            }


            // Disegna il gioco
            screen.draw(screen.delta);

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
