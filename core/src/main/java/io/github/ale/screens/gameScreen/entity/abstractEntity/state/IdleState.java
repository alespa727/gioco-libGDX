package io.github.ale.screens.gameScreen.entity.abstractEntity.state;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

import io.github.ale.screens.gameScreen.entity.abstractEntity.Entity;

public class IdleState implements State<Entity>{

    @Override
    public void enter(Entity entity) {
       
    }

    @Override
    public void update(Entity entity) {

    }

    @Override
    public void exit(Entity entity) {
       
    }

    @Override
    public boolean onMessage(Entity entity, Telegram telegram) {
        return false;
    }

}
