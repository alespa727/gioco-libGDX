

package io.github.ale.screens.gameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import io.github.ale.screens.settings.Settings;

public enum GameStates implements State<GameScreen> {

    PLAYING {
        @Override
        public void enter(GameScreen screen) {
            System.out.println("GameScreen.PLAYING");
        }

        @Override
        public void update(GameScreen screen) {
            if (Gdx.input.isKeyPressed(Settings.getPulsanti()[7]))
                screen.stateMachine().changeState(GameStates.PAUSED);
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
                screen.stateMachine().changeState(GameStates.PLAYING);
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
