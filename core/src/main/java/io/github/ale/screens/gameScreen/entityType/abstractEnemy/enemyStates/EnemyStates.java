package io.github.ale.screens.gameScreen.entityType.abstractEnemy.enemyStates;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import io.github.ale.cooldown.Cooldown;
import io.github.ale.screens.gameScreen.entityType.abstractEnemy.Enemy;

public enum EnemyStates implements State<Enemy> {
    ATTACKING {
        @Override
        public void enter(Enemy entity) {
            //System.out.println(entity.nome()+ " id."+entity.id()+" is Attacking");
        }

        @Override
        public void update(Enemy entity) {
            entity.setIsAttacking(true);
            entity.movement().reset();
            if (entity.direzione().x !=0f && (entity.direzione().x == 1f || entity.direzione().x == -1f)){
                entity.direzione().scl(0.5f, 1f);
            }
            if(entity.direzione().y !=0f && (entity.direzione().y == 1f || entity.direzione().y == -1f)){
                entity.direzione().scl(1f, 0.5f);
            }
            if (!entity.manager.player().hitbox().overlaps(entity.range())) {
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


        private float roundToOne(float value) {
            return value >= 0 ? 1 : -1;
        }

    },

    PURSUE {

        @Override
        public void enter(Enemy entity) {
            System.out.println(entity.nome()+ " id."+entity.id()+" in Pursuing");

        }

        @Override
        public void update(Enemy entity) {
            if (entity.manager.player().hitbox().overlaps(entity.range())) {
                entity.statemachine().changeState(EnemyStates.ATTACKING);
            }

            entity.pathfinder().renderPath(entity.manager.player().coordinateCentro().x, entity.manager.player().coordinateCentro().y, entity.delta);
            if(entity.pathfinder().getPath().getCount()>10){
                entity.statemachine.changeState(PATROLLING);
            }
            entity.checkIfDead();

            //AGGIORNAMENTO MOVEMENT
            entity.movement().update(entity, entity.delta);

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
            System.out.println(entity.nome()+ " id."+entity.id()+" in Patrolling");
        }

        @Override
        public void update(Enemy entity) {
            if (entity.direzione().x == 1f || entity.direzione().x == -1f) {
                entity.direzione().scl(0.5f, 1f);
            }
            if(entity.direzione().y == 1f || entity.direzione().y == -1f){
                entity.direzione().scl(1f, 0.5f);
            }
            float randomx=entity.coordinateCentro().x;
            float randomy=entity.coordinateCentro().y;
            if(entity.isPatrolling()){

            }
            entity.pathfinder().renderPath(randomx, randomy, entity.delta);
            entity.movement().update(entity, entity.delta);

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
