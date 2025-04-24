package progetto.statemachines;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import progetto.entity.components.specific.base.PhysicsComponent;
import progetto.entity.components.specific.movement.DirectionComponent;
import progetto.entity.entities.specific.living.combat.enemy.Enemy;
import progetto.player.Player;
import progetto.world.map.Map;

public enum StatesEnemy implements State<Enemy> {

    ATTACKING {
        Player player;

        @Override
        public void enter(Enemy entity) {
            entity.components.get(PhysicsComponent.class).getBody().setLinearDamping(20f);
        }

        @Override
        public void update(Enemy entity) {
            if (player == null)
                player = entity.engine.player();

            entity.attack();

            entity.get(DirectionComponent.class).direction.set(calculateVector(entity.get(PhysicsComponent.class).getPosition(), entity.engine.player().get(PhysicsComponent.class).getPosition()));
            if (entity.get(DirectionComponent.class).direction.x != 0f && (entity.get(DirectionComponent.class).direction.x == 1f || entity.get(DirectionComponent.class).direction.x == -1f)) {
                entity.get(DirectionComponent.class).direction.scl(0.5f, 1f);
            }
            if (entity.get(DirectionComponent.class).direction.y != 0f && (entity.get(DirectionComponent.class).direction.y == 1f || entity.get(DirectionComponent.class).direction.y == -1f)) {
                entity.get(DirectionComponent.class).direction.scl(1f, 0.5f);
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
                player = entity.engine.player();

            entity.components.get(PhysicsComponent.class).getBody().setLinearDamping(3f);

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
        @Override
        public void enter(Enemy entity) {
        }

        @Override
        public void update(Enemy entity) {
            if (player == null)
                player = entity.engine.player();

            Vector2 direction = entity.get(DirectionComponent.class).direction;

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
            Player p = entity.engine.player();
            if (Map.isGraphLoaded) entity.searchPathIdle(p);

            if (!entity.getPathFinder().success) {
                entity.getStateMachine().changeState(StatesEnemy.PATROLLING);
                return;
            }
            if (entity.get(PhysicsComponent.class).getPosition().dst(entity.engine.player().get(PhysicsComponent.class).getPosition()) > entity.getRangeRadius()) {
                entity.getStateMachine().changeState(StatesEnemy.PURSUE);
            } else {
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
    }
}
