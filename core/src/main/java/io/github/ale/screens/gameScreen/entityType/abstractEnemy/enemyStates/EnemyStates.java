package io.github.ale.screens.gameScreen.entityType.abstractEnemy.enemyStates;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

import com.badlogic.gdx.math.Vector2;
import io.github.ale.screens.gameScreen.entityType.abstractEnemy.Enemy;

public enum EnemyStates implements State<Enemy> {
    ATTACKING {
        Vector2 playerDirection;

        @Override
        public void enter(Enemy entity) {
            System.out.println(entity.nome()+ " id."+entity.id()+" is Attacking");
        }

        @Override
        public void update(Enemy entity) {
            playerDirection = entity.manager.player().coordinateCentro().sub(entity.coordinateCentro()).nor();
            playerDirection.set(roundToOneDecimal(playerDirection.x), roundToOneDecimal(playerDirection.y));
            entity.direzione().set(playerDirection);
            entity.movement().reset();
            if (entity.direzione().x == 1f || entity.direzione().x == -1f) {
                entity.direzione().scl(0.5f, 1f);
            }
            if(entity.direzione().y == 1f || entity.direzione().y == -1f){
                entity.direzione().scl(1f, 0.5f);
            }
            if (entity.manager.player().coordinateCentro().dst(entity.coordinateCentro()) > 1.5f) {
                entity.statemachine().changeState(EnemyStates.PURSUE);
            }
        }

        @Override
        public void exit(Enemy entity) {

        }

        @Override
        public boolean onMessage(Enemy entity, Telegram telegram) {
            return false;
        }


        private float roundToOneDecimal(float value) {
            return Math.round(value) / 1f;
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

            entity.setIsAttacking(entity.manager.isAnyDifferentEntityInRect(entity, entity.range().x, entity.range().y, entity.range().width, entity.range().height));
            entity.pathfinder().renderPath(entity.manager.player().coordinateCentro().x, entity.manager.player().coordinateCentro().y);
            entity.checkIfDead();

            //AGGIORNAMENTO MOVEMENT
            entity.movement().update(entity);

        }

        @Override
        public void exit(Enemy entity) {
            entity.pathfinder().clear();
        }

        @Override
        public boolean onMessage(Enemy entity, Telegram telegram) {
            return false;
        }
    },

    PATROLLING{
        @Override
        public void enter(Enemy entity) {

        }

        @Override
        public void update(Enemy entity) {

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
