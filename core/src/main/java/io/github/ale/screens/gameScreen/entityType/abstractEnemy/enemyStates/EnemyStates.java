package io.github.ale.screens.gameScreen.entityType.abstractEnemy.enemyStates;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import io.github.ale.screens.gameScreen.entityType.abstractEnemy.Enemy;

public enum EnemyStates implements State<Enemy> {
    IDLE {
        @Override
        public void enter(Enemy entity) {
            System.out.println(entity.nome()+ " id."+entity.id()+" in Idle");
        }

        @Override
        public void update(Enemy entity) {
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
    },

    PURSUE {

        @Override
        public void enter(Enemy entity) {
            System.out.println(entity.nome()+ " id."+entity.id()+" in Pursuing");

        }

        @Override
        public void update(Enemy entity) {
            if (entity.manager.player().coordinateCentro().dst(entity.coordinateCentro()) < 1.5f) {
                entity.statemachine().changeState(EnemyStates.IDLE);
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
            entity.movement().clear();
        }

        @Override
        public boolean onMessage(Enemy entity, Telegram telegram) {
            return false;
        }
    };
}
