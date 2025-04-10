package progetto.gameplay.entity.behaviors.states;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import progetto.gameplay.manager.ManagerWorld;
import progetto.gameplay.entity.types.living.combat.enemy.Enemy;
import progetto.gameplay.entity.types.living.combat.player.Player;
import progetto.gameplay.manager.entity.ManagerEntity;

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

            Body enemyBody = entity.getPhysics().getBody();
            Body playerBody = player.getPhysics().getBody();

            entity.attack();
            entity.searchPath(player);

            entity.getDirection().set(calculateVector(entity.getPosition(), entity.manager.player().getPosition()));

            RayCastCallback callback = getRayCastCallback(entity, enemyBody.getPosition(), playerBody.getPosition());
            ManagerWorld.getInstance().rayCast(callback, enemyBody.getPosition(), playerBody.getPosition());

            if (entity.getDirection().x != 0f && (entity.getDirection().x == 1f || entity.getDirection().x == -1f)) {
                entity.getDirection().scl(0.5f, 1f);
            }
            if (entity.getDirection().y != 0f && (entity.getDirection().y == 1f || entity.getDirection().y == -1f)) {
                entity.getDirection().scl(1f, 0.5f);
            }

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
            entity.movement().setAwake(true);
        }

        @Override
        public void update(Enemy entity) {
            Body enemyBody = entity.getPhysics().getBody();
            Body playerBody = player.getPhysics().getBody();
            enemyBody.setLinearDamping(3f);
            if (player == null)
                player = entity.manager.player();

            RayCastCallback callback = getRayCastCallback(entity, enemyBody.getPosition(), playerBody.getPosition());
            ManagerWorld.getInstance().rayCast(callback, enemyBody.getPosition(), playerBody.getPosition());

            entity.getPathFinder().renderPath(entity.manager.player().getPosition().x, entity.manager.player().getPosition().y, entity.manager.delta);
            if (!entity.getPathFinder().success) entity.statemachine.changeState(PATROLLING);
        }

        @Override
        public void exit(Enemy entity) {
            entity.movement().setAwake(false);
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

            Body enemyBody = entity.getPhysics().getBody();
            Body playerBody = player.getPhysics().getBody();

            accumulator+=entity.manager.delta;
            if (accumulator > 0.5f){
                accumulator = 0f;
                RayCastCallback callback = getRayCastCallback(entity, enemyBody.getPosition(), playerBody.getPosition());
                ManagerWorld.getInstance().rayCast(callback, enemyBody.getPosition(), playerBody.getPosition());
            }


            Vector2 direction = entity.getDirection();

            if (direction.x == 1f || direction.x == -1f) {
                direction.scl(0.5f, 1f);
            }
            if (direction.y == 1f || direction.y == -1f) {
                direction.scl(1f, 0.5f);
            }
        }

        @Override
        public void exit(Enemy entity) {
        }

        @Override
        public boolean onMessage(Enemy entity, Telegram telegram) {
            return false;
        }

    };

    private static RayCastCallback getRayCastCallback(Enemy entity, Vector2 start, Vector2 end) {

        return new RayCastCallback() {

            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                Filter filter = fixture.getFilterData();
                Object userData = fixture.getBody().getUserData();

                if (userData == null) {
                    return -1; // Ignora il fixture e continua il raycasting
                }

                if (userData.equals("map")) {
                    return -1;
                }

                if (userData instanceof Player && filter.categoryBits != ManagerEntity.RANGE) {
                    if (start.dst(end) > entity.rangeRadius() && entity.statemachine.getCurrentState() != PURSUE) {
                        entity.statemachine.changeState(PURSUE);
                        return fraction;
                    }
                    if (start.dst(end) < entity.rangeRadius() && entity.statemachine.getCurrentState() != ATTACKING) {
                        entity.statemachine.changeState(ATTACKING);
                        return fraction;
                    }

                }

                return 0;
            }
        };


    }
}
