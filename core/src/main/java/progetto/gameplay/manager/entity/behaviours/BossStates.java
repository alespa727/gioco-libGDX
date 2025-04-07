package progetto.gameplay.manager.entity.behaviours;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import progetto.gameplay.entity.types.humanEntity.combatEntity.boss.Boss;

public enum BossStates implements State<Boss> {
    PURSUE{
        @Override
        public void enter(Boss entity) {

        }

        @Override
        public void update(Boss entity) {

        }

        @Override
        public void exit(Boss entity) {

        }

        @Override
        public boolean onMessage(Boss entity, Telegram telegram) {
            return false;
        }
    },

    IDLE{
        @Override
        public void enter(Boss entity) {

        }

        @Override
        public void update(Boss entity) {

        }

        @Override
        public void exit(Boss entity) {

        }

        @Override
        public boolean onMessage(Boss entity, Telegram telegram) {
            return false;
        }
    }
}
