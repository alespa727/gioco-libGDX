package io.github.ale.screens.game.entities.entityTypes.player.movement;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import io.github.ale.screens.game.entities.entityTypes.player.Player;

public enum MovementState implements State<Player> {
    OPPOSTOX{
        @Override
        public void enter(Player entity) {

        }

        @Override
        public void update(Player entity) {
            entity.getMovement().oppostoX();
        }

        @Override
        public void exit(Player entity) {

        }

        @Override
        public boolean onMessage(Player entity, Telegram telegram) {
            return false;
        }
    },

    OPPOSTOY{
        @Override
        public void enter(Player entity) {

        }

        @Override
        public void update(Player entity) {
            entity.getMovement().oppostoY();
        }

        @Override
        public void exit(Player entity) {

        }

        @Override
        public boolean onMessage(Player entity, Telegram telegram) {
            return false;
        }
    },

    MOVING{
        @Override
        public void enter(Player entity) {

        }

        @Override
        public void update(Player entity) {
            entity.getMovement().moving();
        }

        @Override
        public void exit(Player entity) {

        }

        @Override
        public boolean onMessage(Player entity, Telegram telegram) {
            return false;
        }
    },

    NOTMOVING{
        @Override
        public void enter(Player entity) {

        }

        @Override
        public void update(Player entity) {
            entity.getMovement().notMoving();
        }

        @Override
        public void exit(Player entity) {

        }

        @Override
        public boolean onMessage(Player entity, Telegram telegram) {
            return false;
        }
    };

}
