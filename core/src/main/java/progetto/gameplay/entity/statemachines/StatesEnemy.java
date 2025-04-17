package progetto.gameplay.entity.statemachines;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import progetto.gameplay.entity.types.living.combat.enemy.Enemy;
import progetto.gameplay.player.Player;

public enum StatesEnemy implements State<Enemy> {

    ATTACKING {
        Player player;

        @Override
        public void enter(Enemy entity) {
            entity.getPhysics().getBody().setLinearDamping(20f);
        }

        @Override
        public void update(Enemy entity) {
            if (player == null)
                player = entity.manager.player();

            entity.attack();

            entity.getDirection().set(calculateVector(entity.getPosition(), entity.manager.player().getPosition()));
            if (entity.getDirection().x != 0f && (entity.getDirection().x == 1f || entity.getDirection().x == -1f)) {
                entity.getDirection().scl(0.5f, 1f);
            }
            if (entity.getDirection().y != 0f && (entity.getDirection().y == 1f || entity.getDirection().y == -1f)) {
                entity.getDirection().scl(1f, 0.5f);
            }

            entity.getStateMachine().changeState(CHOOSE_STATE);
        }

        @Override
        public void exit(Enemy entity) {

        }

        @Override
        public boolean onMessage(Enemy entity, Telegram telegram) {
            return false;
        }


        public Vector2 calculateVector(Vector2 from, Vector2 to) {
            // Differenza tra i due punti
            float deltaX = to.x - from.x;
            float deltaY = to.y - from.y;

            // Calcola l'angolo in radianti
            float angleRadians = MathUtils.atan2(deltaY, deltaX);

            // Calcola il vettore unitario

            return new Vector2(Math.round(MathUtils.cos(angleRadians)), Math.round(MathUtils.sin(angleRadians)));
        }

    },

    PURSUE {
        Player player;

        @Override
        public void enter(Enemy entity) {
            entity.getMovementManager().setAwake(true);
        }

        @Override
        public void update(Enemy entity) {
            if (player == null)
                player = entity.manager.player();

            entity.getPhysics().getBody().setLinearDamping(3f);

            entity.getStateMachine().changeState(CHOOSE_STATE);
        }

        @Override
        public void exit(Enemy entity) {
            entity.getMovementManager().setAwake(false);
        }

        @Override
        public boolean onMessage(Enemy entity, Telegram telegram) {
            return false;
        }
    },

    PATROLLING {
        Player player;
        float accumulator = 0;

        @Override
        public void enter(Enemy entity) {
        }

        @Override
        public void update(Enemy entity) {
            if (player == null)
                player = entity.manager.player();

            accumulator+=entity.manager.delta;

            Vector2 direction = entity.getDirection();

            if (direction.x == 1f || direction.x == -1f) {
                direction.scl(0.5f, 1f);
            }
            if (direction.y == 1f || direction.y == -1f) {
                direction.scl(1f, 0.5f);
            }

            entity.getStateMachine().changeState(CHOOSE_STATE);
        }

        @Override
        public void exit(Enemy entity) {
        }

        @Override
        public boolean onMessage(Enemy entity, Telegram telegram) {
            return false;
        }

    },

    CHOOSE_STATE {
        @Override
        public void enter(Enemy entity) {
        }

        @Override
        public void update(Enemy entity) {
            entity.searchPathIdle(entity.manager.player());
            if(!entity.getPathFinder().success){
                entity.getStateMachine().changeState(StatesEnemy.PATROLLING);
                return;
            }
            if (entity.getPosition().dst(entity.manager.player().getPosition()) > entity.getRangeRadius()) {
                entity.getStateMachine().changeState(StatesEnemy.PURSUE);
            }else{
                entity.getStateMachine().changeState(StatesEnemy.ATTACKING);
            }
        }

        @Override
        public void exit(Enemy entity) {

        }

        @Override
        public boolean onMessage(Enemy entity, Telegram telegram) {
            return false;
        }

        Player player;

    }
}
