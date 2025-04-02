package io.github.ale.screens.gameplay.entities.types.enemy.enemyStates;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import io.github.ale.screens.gameplay.entities.types.enemy.Enemy;
import io.github.ale.screens.gameplay.entities.types.player.Player;
import io.github.ale.screens.gameplay.manager.entity.EntityManager;

public enum EnemyStates implements State<Enemy> {

    ATTACKING {
        Player player;

        @Override
        public void enter(Enemy entity) {
            entity.body.setLinearDamping(20f);
        }

        @Override
        public void update(Enemy entity) {
            if (player == null)
                player = entity.manager.player();

            entity.attack();

            entity.pathfinder().renderPath(entity.manager.player().getPosition().x, entity.manager.player().getPosition().y, entity.delta);

            entity.direzione().set(calculateVector(entity.getPosition(), entity.manager.player().getPosition()));

            RayCastCallback callback = getRayCastCallback(entity, entity.body.getPosition(), player.body.getPosition());
            entity.manager.world.rayCast(callback, entity.body.getPosition(), player.body.getPosition());

            if (entity.direzione().x != 0f && (entity.direzione().x == 1f || entity.direzione().x == -1f)) {
                entity.direzione().scl(0.5f, 1f);
            }
            if (entity.direzione().y != 0f && (entity.direzione().y == 1f || entity.direzione().y == -1f)) {
                entity.direzione().scl(1f, 0.5f);
            }

            entity.checkIfDead();
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
            System.out.println(entity.nome() + " id." + entity.id() + " in Pursuing");
            entity.body.setLinearDamping(3f);
        }

        @Override
        public void update(Enemy entity) {
            if (player == null)
                player = entity.manager.player();

            RayCastCallback callback = getRayCastCallback(entity, entity.body.getPosition(), player.body.getPosition());
            entity.manager.world.rayCast(callback, entity.body.getPosition(), player.body.getPosition());

            entity.pathfinder().renderPath(entity.manager.player().getPosition().x, entity.manager.player().getPosition().y, entity.delta);


            entity.checkIfDead();

            //AGGIORNAMENTO MOVEMENT
            entity.movement().update();

            if (!entity.pathfinder().success) entity.statemachine.changeState(PATROLLING);
        }

        @Override
        public void exit(Enemy entity) {
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
            System.out.println(entity.nome() + " id." + entity.id() + " in Patrolling");
        }

        @Override
        public void update(Enemy entity) {
            if (player == null)
                player = entity.manager.player();

            accumulator+=player.delta;
            if (accumulator > 1f){
                accumulator = 0f;
                RayCastCallback callback = getRayCastCallback(entity, entity.body.getPosition(), player.body.getPosition());
                entity.manager.world.rayCast(callback, entity.body.getPosition(), player.body.getPosition());
            }


            Vector2 direction = entity.direzione();

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
                    System.out.println("MURO");
                    return -1;
                }

                if (userData instanceof Player && filter.categoryBits != EntityManager.RANGE) {
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
