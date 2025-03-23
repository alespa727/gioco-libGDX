package io.github.ale.screens.gameScreen.entity.enemy.abstractEnemy.states;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

import io.github.ale.screens.gameScreen.entity.enemy.abstractEnemy.Nemico;

public enum States implements State<Nemico> {
    IDLE {
        @Override
        public void enter(Nemico entity) {
            System.out.println(entity.nome()+ " id."+entity.id()+" in Idle");
        }

        @Override
        public void update(Nemico entity) {
            
        }

        @Override
        public void exit(Nemico entity) {
            
        }

        @Override
        public boolean onMessage(Nemico entity, Telegram telegram) {
            return false;
        }
    },

    PURSUE {

        @Override
        public void enter(Nemico entity) {
            System.out.println("Chasing");
        }

        @Override
        public void update(Nemico entity) {
            System.out.println("Chasing");
            entity.setIsAttacking(entity.manager.isAnyDifferentEntityInRect(entity, entity.range().x, entity.range().y, entity.range().width, entity.range().height));
            entity.pathfinder.renderPath(entity.manager.player().coordinateCentro().x, entity.manager.player().coordinateCentro().y);
            entity.checkIfDead();
        }

        @Override
        public void exit(Nemico entity) {
            
        }

        @Override
        public boolean onMessage(Nemico entity, Telegram telegram) {
            return false;
        }
    };


}
