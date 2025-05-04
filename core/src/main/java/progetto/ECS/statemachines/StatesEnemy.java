package progetto.ECS.statemachines;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import progetto.ECS.components.specific.base.PhysicsComponent;
import progetto.ECS.components.specific.movement.DirectionComponent;
import progetto.ECS.entities.specific.living.combat.enemy.BaseEnemy;
import progetto.core.game.player.Player;
import progetto.world.map.Map;

public enum StatesEnemy implements State<BaseEnemy> {

    ATTACKING {
        Player player;

        @Override
        public void enter(BaseEnemy entity) {
            entity.components.get(PhysicsComponent.class).getBody().setLinearDamping(20f);
        }

        @Override
        public void update(BaseEnemy entity) {
            if (player == null)
                player = entity.entityEngine.player();

            entity.attack();

            entity.get(DirectionComponent.class).direction.set(calculateVector(entity.get(PhysicsComponent.class).getPosition(), entity.entityEngine.player().get(PhysicsComponent.class).getPosition()));
            if (entity.get(DirectionComponent.class).direction.x != 0f && (entity.get(DirectionComponent.class).direction.x == 1f || entity.get(DirectionComponent.class).direction.x == -1f)) {
                entity.get(DirectionComponent.class).direction.scl(0.5f, 1f);
            }
            if (entity.get(DirectionComponent.class).direction.y != 0f && (entity.get(DirectionComponent.class).direction.y == 1f || entity.get(DirectionComponent.class).direction.y == -1f)) {
                entity.get(DirectionComponent.class).direction.scl(1f, 0.5f);
            }

            entity.getStateMachine().changeState(CHOOSE_STATE);
        }

        @Override
        public void exit(BaseEnemy entity) {

        }

        @Override
        public boolean onMessage(BaseEnemy entity, Telegram telegram) {
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
        public void enter(BaseEnemy entity) {
            entity.getMovementManager().setAwake(true);
        }

        @Override
        public void update(BaseEnemy entity) {
            if (player == null)
                player = entity.entityEngine.player();

            entity.components.get(PhysicsComponent.class).getBody().setLinearDamping(3f);

            entity.getStateMachine().changeState(CHOOSE_STATE);
        }

        @Override
        public void exit(BaseEnemy entity) {
            entity.getMovementManager().setAwake(false);
        }

        @Override
        public boolean onMessage(BaseEnemy entity, Telegram telegram) {
            return false;
        }
    },

    PATROLLING {
        Player player;
        @Override
        public void enter(BaseEnemy entity) {
        }

        @Override
        public void update(BaseEnemy entity) {
            if (player == null)
                player = entity.entityEngine.player();

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
        public void exit(BaseEnemy entity) {
        }

        @Override
        public boolean onMessage(BaseEnemy entity, Telegram telegram) {
            return false;
        }

    },

    CHOOSE_STATE {
        @Override
        public void enter(BaseEnemy entity) {

        }

        @Override
        public void update(BaseEnemy entity) {
            Player p = entity.entityEngine.player();
            if (Map.isGraphLoaded) entity.searchPathIdle(p);

            if (!entity.getPathFinder().success) {
                entity.getStateMachine().changeState(StatesEnemy.PATROLLING);
                return;
            }
            if (entity.get(PhysicsComponent.class).getPosition().dst(entity.entityEngine.player().get(PhysicsComponent.class).getPosition()) > entity.getRangeRadius()) {
                entity.getStateMachine().changeState(StatesEnemy.PURSUE);
            } else {
                entity.getStateMachine().changeState(StatesEnemy.ATTACKING);
            }
        }

        @Override
        public void exit(BaseEnemy entity) {

        }

        @Override
        public boolean onMessage(BaseEnemy entity, Telegram telegram) {
            return false;
        }
    }
}
