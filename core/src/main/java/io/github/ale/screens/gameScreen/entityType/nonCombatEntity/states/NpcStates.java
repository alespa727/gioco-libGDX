package io.github.ale.screens.gameScreen.entityType.nonCombatEntity.states;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

import io.github.ale.screens.gameScreen.entityType.nonCombatEntity.NonCombatEntity;

public enum NpcStates implements State<NonCombatEntity> {
    IDLE {
        @Override
        public void enter(NonCombatEntity entity) {
            System.out.println(entity.nome()+ " id."+entity.id()+" in Idle");
        }

        @Override
        public void update(NonCombatEntity entity) {
            if (entity.direzione().x == 1f || entity.direzione().x == -1f) {
                entity.direzione().scl(0.5f, 1f);
            }
            if(entity.direzione().y == 1f || entity.direzione().y == -1f){
                entity.direzione().scl(1f, 0.5f);
            }
            if (entity.manager.player().coordinateCentro().dst(entity.coordinateCentro()) > 1.5f) {
                entity.statemachine().changeState(NpcStates.FOLLOW);
            }
        }

        @Override
        public void exit(NonCombatEntity entity) {

        }

        @Override
        public boolean onMessage(NonCombatEntity entity, Telegram telegram) {
            return false;
        }
    },

    FOLLOW {

        @Override
        public void enter(NonCombatEntity entity) {
            System.out.println(entity.nome()+ " id."+entity.id()+" in Pursuing");

        }

        @Override
        public void update(NonCombatEntity entity) {
            if (entity.manager.player().coordinateCentro().dst(entity.coordinateCentro()) < 1.5f) {
                entity.statemachine().changeState(NpcStates.IDLE);
            }
            entity.pathfinder().renderPath(entity.manager.player().coordinateCentro().x, entity.manager.player().coordinateCentro().y);
            entity.checkIfDead();

            //AGGIORNAMENTO MOVEMENT
            entity.movement().update(entity);
        }

        @Override
        public void exit(NonCombatEntity entity) {
            entity.pathfinder().clear();
            entity.movement().clear();
        }

        @Override
        public boolean onMessage(NonCombatEntity entity, Telegram telegram) {
            return false;
        }
    };


}
