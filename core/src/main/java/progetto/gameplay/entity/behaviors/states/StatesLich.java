package progetto.gameplay.entity.behaviors.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.MathUtils;
import progetto.gameplay.entity.components.BodyComponent;
import progetto.gameplay.entity.types.living.combat.boss.Lich;
import progetto.gameplay.entity.components.entity.Cooldown;
import progetto.gameplay.map.Map;
import progetto.gameplay.player.Player;
import progetto.utils.TerminalCommand;

public enum StatesLich implements State<Lich> {
    FIREDOMAIN{
        private final Cooldown useFireDomain = new Cooldown(0);
        @Override
        public void enter(Lich entity) {
            useFireDomain.reset(MathUtils.random(3f, 5f));
        }

        @Override
        public void update(Lich entity) {
            if (!entity.searchPathIdle(entity.manager.player())){
                entity.getStateMachine().changeState(StatesLich.IDLE);
                return;
            }
            useFireDomain.update(entity.manager.delta);
            fireDomain(entity);
        }

        @Override
        public void exit(Lich entity) {
            entity.prepareFireDomain.reset(MathUtils.random(14, 20));
        }

        @Override
        public boolean onMessage(Lich entity, Telegram telegram) {
            return false;
        }

        public void fireDomain(Lich entity){
            entity.fireDomain();
            if (useFireDomain.isReady){
                entity.manager.clearQueue();
                entity.getStateMachine().changeState(StatesLich.LONG_RANGE_ATTACKS);
            }
        }

    },

    FIREBALL{
        private final Cooldown useFireball = new Cooldown(0);
        @Override
        public void enter(Lich entity) {
            useFireball.reset(MathUtils.random(1.2f, 1.5f));
        }

        @Override
        public void update(Lich entity) {
            if (!entity.searchPathIdle(entity.manager.player())){
                entity.getStateMachine().changeState(StatesLich.IDLE);
                return;
            }
            useFireball.update(entity.manager.delta);
            if (useFireball.isReady){
                fireball(entity);
            }
        }

        @Override
        public void exit(Lich entity) {
            entity.prepareToFireball.reset(MathUtils.random(4f, 7f));
        }

        @Override
        public boolean onMessage(Lich entity, Telegram telegram) {
            return false;
        }

        public void fireball(Lich entity) {
            entity.fireball();
            entity.getStateMachine().changeState(StatesLich.LONG_RANGE_ATTACKS);
        }
    },


    LONG_RANGE_ATTACKS {
        @Override
        public void enter(Lich entity) {
            entity.getMovementManager().setAwake(true);
        }

        @Override
        public void update(Lich entity) {
            entity.getMovementManager().update(entity.manager.delta);

            entity.getStateMachine().changeState(StatesLich.CHOOSING_STATE);

            // ATTACCO AD AREA SPARANDO PROIETTILI IN TUTTE LE DIREZIONI
            entity.prepareFireDomain.update(entity.manager.delta);
            initiateFireDomain(entity);

            // ATTACCO CON LA FIREBALL
            entity.prepareToFireball.update(entity.manager.delta);
            initiateFireball(entity);

            Player player = entity.manager.player();
            entity.searchPath(player);

        }

        @Override
        public void exit(Lich entity) {
            entity.getMovementManager().setAwake(false);
        }

        @Override
        public boolean onMessage(Lich entity, Telegram telegram) {
            return false;
        }

        public void initiateFireDomain(Lich entity){
            if (entity.prepareFireDomain.isReady){
                entity.getStateMachine().changeState(StatesLich.FIREDOMAIN);
            }
        }

        public void initiateFireball(Lich entity) {
            if (entity.prepareToFireball.isReady){
                entity.getStateMachine().changeState(StatesLich.FIREBALL);
            }
        }

    },

    CLOSE_RANGE_ATTACKS {
        @Override
        public void enter(Lich entity) {
        }

        @Override
        public void update(Lich entity) {
            entity.getStateMachine().changeState(StatesLich.CHOOSING_STATE);
        }

        @Override
        public void exit(Lich entity) {

        }

        @Override
        public boolean onMessage(Lich entity, Telegram telegram) {
            return false;
        }
    },

    CHOOSING_STATE{
        @Override
        public void enter(Lich entity) {
            if (entity.prepareToChangeStates.isReady){
                entity.prepareToChangeStates.reset(1);
                if(Map.isGraphLoaded) entity.searchPath(entity.manager.player());
                if(!entity.getPathFinder().success){
                    entity.getStateMachine().changeState(StatesLich.IDLE);
                    return;
                }
                if (entity.getPosition().dst(entity.manager.player().getPosition()) > 2f) {
                    entity.getStateMachine().changeState(StatesLich.LONG_RANGE_ATTACKS);
                }else{
                    entity.getStateMachine().changeState(StatesLich.CLOSE_RANGE_ATTACKS);
                }
            }else{
                entity.getStateMachine().revertToPreviousState();
            }
        }

        @Override
        public void update(Lich entity) {

        }

        @Override
        public void exit(Lich entity) {

        }

        @Override
        public boolean onMessage(Lich entity, Telegram telegram) {
            return false;
        }
    },

    IDLE {
        @Override
        public void enter(Lich entity) {
            entity.getMovementManager().setReady();
        }

        @Override
        public void update(Lich entity) {
            boolean success = entity.searchPathIdle(entity.manager.player());
            if(!success) return;

            entity.getStateMachine().changeState(StatesLich.CHOOSING_STATE);
        }

        @Override
        public void exit(Lich entity) {
        }

        @Override
        public boolean onMessage(Lich entity, Telegram telegram) {
            return false;
        }
    };
}
