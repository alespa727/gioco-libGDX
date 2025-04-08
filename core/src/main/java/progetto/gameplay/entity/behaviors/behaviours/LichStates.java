package progetto.gameplay.entity.behaviors.manager.entity.behaviours;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.MathUtils;
import progetto.gameplay.entity.types.living.combat.boss.Lich;
import progetto.utils.Cooldown;

public enum LichStates implements State<Lich> {

    PURSUE {
        Cooldown prepareToFireball = new Cooldown(2f);
        Cooldown useFireball = new Cooldown(0.6f);

        Cooldown prepareFireDomain = new Cooldown(0);
        Cooldown useFireDomain = new Cooldown(5f);

        boolean isPrepareToFireball = false;
        boolean isPreparedToFireDomain = false;

        @Override
        public void enter(Lich entity) {
            System.out.println("LichStates.PURSUE");
            useFireball.reset();
            prepareToFireball.reset();
            prepareFireDomain.reset(MathUtils.random(14, 20));
            useFireDomain.reset(MathUtils.random(4f, 5f));
        }

        @Override
        public void update(Lich entity) {

            // ATTACCO AD AREA SPARANDO PROIETTILI IN TUTTE LE DIREZIONI
            if(!isPreparedToFireDomain){
                prepareFireDomain.update(entity.delta);
                prepareToFireball(entity);
            }else{
                useFireDomain.update(entity.delta);
                fireDomain(entity);
                return;
            }

            // AGGIORNAMENTO MOVEMENT
            if (!isPrepareToFireball){
                prepareToFireball.update(entity.delta);
                if (prepareToFireball.isReady){
                    useFireball.reset();
                    isPrepareToFireball = true;
                }
            }else{
                useFireball.update(entity.delta);
                if (useFireball.isReady) fireball(entity);
                return;
            }

            entity.pathfinder().renderPath(entity.manager.player().getPosition().x, entity.manager.player().getPosition().y, entity.delta);
            entity.move();

            if (!entity.pathfinder().success) entity.getStateMachine().changeState(IDLE);
        }

        @Override
        public void exit(Lich entity) {

        }

        @Override
        public boolean onMessage(Lich entity, Telegram telegram) {
            return false;
        }

        public void fireball(Lich entity) {
            isPrepareToFireball = false;
            System.out.println("FIREBALL");
            entity.fireball();
            useFireball.reset();
            prepareToFireball.reset();
        }

        public void fireDomain(Lich entity){
            entity.fireDomain();
            if (useFireDomain.isReady){
                System.out.println("FireDomain is over");
                isPreparedToFireDomain=false;
                prepareFireDomain.reset(MathUtils.random(14, 20));
                entity.manager.clearQueue();
            }
        }

        public void prepareToFireball(Lich entity){
            if (prepareFireDomain.isReady){
                System.out.println("FireDomain ready");
                isPreparedToFireDomain=true;
                useFireDomain.reset(MathUtils.random(2f, 3f));
            }
        }
    },
    IDLE {
        @Override
        public void enter(Lich entity) {
            System.out.println("LichStates.IDLE");
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
    };
}
