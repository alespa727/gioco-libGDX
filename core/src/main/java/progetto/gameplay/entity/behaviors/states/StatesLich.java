package progetto.gameplay.entity.behaviors.states;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.MathUtils;
import progetto.gameplay.entity.types.living.combat.boss.Lich;
import progetto.utils.Cooldown;

public enum StatesLich implements State<Lich> {
    FIREDOMAIN{
        private final Cooldown useFireDomain = new Cooldown(0);
        @Override
        public void enter(Lich entity) {
            System.out.println("Launching fire domain");
            useFireDomain.reset(MathUtils.random(4f, 5f));
        }

        @Override
        public void update(Lich entity) {
            useFireDomain.update(entity.delta);
            fireDomain(entity);
        }

        @Override
        public void exit(Lich entity) {
            System.out.println("FireDomain is over");
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
            System.out.println("Preparing fireball..");
            useFireball.reset(MathUtils.random(1.2f, 1.5f));
        }

        @Override
        public void update(Lich entity) {
            useFireball.update(entity.delta);
            if (useFireball.isReady) fireball(entity);
        }

        @Override
        public void exit(Lich entity) {
            entity.prepareToFireball.reset(MathUtils.random(2f, 3f));
        }

        @Override
        public boolean onMessage(Lich entity, Telegram telegram) {
            return false;
        }

        public void fireball(Lich entity) {
            System.out.println("Using fireball..");
            entity.fireball();
            entity.getStateMachine().changeState(StatesLich.LONG_RANGE_ATTACKS);
        }
    },


    LONG_RANGE_ATTACKS {
        @Override
        public void enter(Lich entity) {
        }

        @Override
        public void update(Lich entity) {

            entity.getStateMachine().changeState(StatesLich.CHOOSING_STATE);

            // ATTACCO AD AREA SPARANDO PROIETTILI IN TUTTE LE DIREZIONI
            entity.prepareFireDomain.update(entity.delta);
            initiateFireDomain(entity);

            // ATTACCO CON LA FIREBALL
            entity.prepareToFireball.update(entity.delta);
            initiateFireball(entity);

            entity.searchPath(entity.manager.player());
            entity.move();
        }

        @Override
        public void exit(Lich entity) {

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
                if(!entity.pathfinder().success){
                    entity.getStateMachine().changeState(StatesLich.IDLE);
                }

                if (entity.getPosition().dst(entity.manager.player().getPosition()) > 2f) {
                    entity.getStateMachine().changeState(StatesLich.LONG_RANGE_ATTACKS);
                    System.out.println("Choosing long range attack...");
                }else{
                    entity.getStateMachine().changeState(StatesLich.CLOSE_RANGE_ATTACKS);
                    System.out.println("Choosing close range attack...");
                }
            }else{
                entity.getStateMachine().changeState(entity.getStateMachine().getPreviousState());
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
            System.out.println("LichStates.IDLE");
        }

        @Override
        public void update(Lich entity) {
            entity.pathfinder().renderPath(entity.manager.player().getPosition().x, entity.manager.player().getPosition().y, entity.delta);
            if(entity.pathfinder().success){
                entity.getStateMachine().changeState(StatesLich.CHOOSING_STATE);
            }
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
