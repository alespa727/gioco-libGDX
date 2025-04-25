package progetto.statemachines;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import progetto.core.CoreConfig;
import progetto.core.game.GameScreen;
import progetto.core.game.GameTime;
import progetto.input.KeyHandler;
import progetto.screens.PauseScreen;
import progetto.world.WorldManager;

import static progetto.core.game.GameScreen.STEP;

public enum ManagerGame implements State<GameScreen> {

    PLAYING {
        @Override
        public void enter(GameScreen screen) {

        }

        @Override
        public void update(GameScreen screen) {

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
