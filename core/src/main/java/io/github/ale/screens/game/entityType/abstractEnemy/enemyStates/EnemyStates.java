package io.github.ale.screens.game.entityType.abstractEnemy.enemyStates;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import io.github.ale.screens.game.entities.player.Player;
import io.github.ale.screens.game.entityType.abstractEnemy.Enemy;
import io.github.ale.screens.game.entityType.abstractEntity.Entity;
import io.github.ale.screens.game.maps.Map;

public enum EnemyStates implements State<Enemy> {

    ATTACKING {
        @Override
        public void enter(Enemy entity) {
            //System.out.println(entity.nome()+ " id."+entity.id()+" is Attacking");
        }

        @Override
        public void update(Enemy entity) {
            entity.direzione().set(calculateVector(entity.coordinateCentro(), entity.manager.player().coordinateCentro()));
            entity.setIsAttacking(true);
            entity.movement().reset();
            if (entity.direzione().x !=0f && (entity.direzione().x == 1f || entity.direzione().x == -1f)){
                entity.direzione().scl(0.5f, 1f);
            }
            if(entity.direzione().y !=0f && (entity.direzione().y == 1f || entity.direzione().y == -1f)){
                entity.direzione().scl(1f, 0.5f);
            }
            if (entity.manager.player().coordinateCentro().dst(entity.coordinateCentro())>1.5f) {
                entity.statemachine().changeState(EnemyStates.PURSUE);
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

        @Override
        public void enter(Enemy entity) {
            System.out.println(entity.nome()+ " id."+entity.id()+" in Pursuing");

        }

        @Override
        public void update(Enemy entity) {
            if (entity.manager.player().coordinateCentro().dst(entity.coordinateCentro()) < 1.5f) {
                entity.statemachine().changeState(EnemyStates.ATTACKING);
            }

            entity.pathfinder().renderPath(entity.manager.player().coordinateCentro().x, entity.manager.player().coordinateCentro().y, entity.delta);
            if(entity.pathfinder().getPath().getCount()> entity.pursueMaxDistance){
                entity.statemachine.changeState(PATROLLING);
            }
            entity.checkIfDead();

            //AGGIORNAMENTO MOVEMENT
            entity.movement().update(entity, entity.delta);

        }

        @Override
        public void exit(Enemy entity) {
            entity.pathfinder().clear();
            entity.body.setLinearVelocity(0, 0);
        }

        @Override
        public boolean onMessage(Enemy entity, Telegram telegram) {
            return false;
        }
    },

    PATROLLING{

        @Override
        public void enter(Enemy entity) {
            System.out.println(entity.nome()+ " id."+entity.id()+" in Patrolling");
        }

        @Override
        public void update(Enemy entity) {
            Player player = entity.manager.player();

            entity.movement().reset();

            Vector2 direction = entity.direzione();

            if (direction.x == 1f || direction.x == -1f) {
                direction.scl(0.5f, 1f);
            }
            if(direction.y == 1f || direction.y == -1f){
                direction.scl(1f, 0.5f);
            }

            entity.pathfinder().renderPath(player.coordinateCentro().x, player.coordinateCentro().y, entity.delta);

            if(entity.pathfinder().getPath().getCount()<4){
                entity.statemachine.changeState(PURSUE);
            }else{
                entity.pathfinder().clear();
                entity.movement().reset();
                entity.movement().update(entity, entity.delta);
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
