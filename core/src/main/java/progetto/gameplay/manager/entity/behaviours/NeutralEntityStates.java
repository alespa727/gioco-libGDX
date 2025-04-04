package progetto.gameplay.manager.entity.behaviours;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import progetto.gameplay.entities.types.NeutralEntity;

public enum NeutralEntityStates implements State<NeutralEntity> {
    IDLE {
        @Override
        public void enter(NeutralEntity entity) {
        }

        @Override
        public void update(NeutralEntity entity) {
            if (entity.direzione().x == 1f || entity.direzione().x == -1f) {
                entity.direzione().scl(0.5f, 1f);
            }
            if (entity.direzione().y == 1f || entity.direzione().y == -1f) {
                entity.direzione().scl(1f, 0.5f);
            }
            if (entity.manager.player().getPosition().dst(entity.getPosition()) > 1.5f) {
                entity.statemachine().changeState(NeutralEntityStates.FOLLOW);
            }
        }

        @Override
        public void exit(NeutralEntity entity) {

        }

        @Override
        public boolean onMessage(NeutralEntity entity, Telegram telegram) {
            return false;
        }
    },

    FOLLOW {
        @Override
        public void enter(NeutralEntity entity) {
        }

        @Override
        public void update(NeutralEntity entity) {
            if (entity.manager.player().getPosition().dst(entity.getPosition()) < 1.5f) {
                entity.statemachine().changeState(NeutralEntityStates.IDLE);
            }
            entity.pathfinder().renderPath(entity.manager.player().getPosition().x, entity.manager.player().getPosition().y, entity.delta);
            entity.checkIfDead();

            //AGGIORNAMENTO MOVEMENT
            entity.movement().update();
        }

        @Override
        public void exit(NeutralEntity entity) {
            entity.pathfinder().clear();
        }

        @Override
        public boolean onMessage(NeutralEntity entity, Telegram telegram) {
            return false;
        }
    };


}
