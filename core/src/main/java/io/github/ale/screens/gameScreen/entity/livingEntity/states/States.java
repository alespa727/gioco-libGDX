package io.github.ale.screens.gameScreen.entity.livingEntity.states;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

import io.github.ale.screens.gameScreen.entity.livingEntity.LivingEntity;

public enum States implements State<LivingEntity> {
    IDLE {
        @Override
        public void enter(LivingEntity entity) {
            System.out.println(entity.nome()+ " id."+entity.id()+" in Idle");
        }

        @Override
        public void update(LivingEntity entity) {
            if (entity.direzione().x == 1f || entity.direzione().x == -1f) {
                entity.direzione().scl(0.5f, 1f);
            }
            if(entity.direzione().y == 1f || entity.direzione().y == -1f){
                entity.direzione().scl(1f, 0.5f);
            }
            if (entity.manager.player().coordinateCentro().dst(entity.coordinateCentro()) > 1.5f) {
                entity.statemachine().changeState(States.PURSUE);
            }
        }

        @Override
        public void exit(LivingEntity entity) {
            
        }

        @Override
        public boolean onMessage(LivingEntity entity, Telegram telegram) {
            return false;
        }
    },

    PURSUE {

        @Override
        public void enter(LivingEntity entity) {
            System.out.println(entity.nome()+ " id."+entity.id()+" in Pursuing");
        }

        @Override
        public void update(LivingEntity entity) {
            if (entity.manager.player().coordinateCentro().dst(entity.coordinateCentro()) < 1.5f) {
                entity.statemachine().changeState(States.IDLE);
            }
            entity.pathfinder.renderPath(entity.manager.player().coordinateCentro().x, entity.manager.player().coordinateCentro().y);
            entity.checkIfDead();
                        
            //AGGIORNAMENTO MOVEMENT
            entity.movement().update(entity);
        }

        @Override
        public void exit(LivingEntity entity) {
            entity.pathfinder.clear();
        }

        @Override
        public boolean onMessage(LivingEntity entity, Telegram telegram) {
            return false;
        }
    };


}
