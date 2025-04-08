package progetto.gameplay.entity.behaviors.manager.entity.behaviours;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import progetto.gameplay.entity.types.living.combat.boss.Lich;
import progetto.utils.Cooldown;

public enum LichStates implements State<Lich> {
    PURSUE1 {
        Cooldown prepareToFireball = new Cooldown(2f);
        Cooldown useFireball = new Cooldown(0.6f);

        boolean isPrepareToFireball = false;

        @Override
        public void enter(Lich entity) {
            System.out.println("LichStates.PURSUE");
            useFireball.reset();
            prepareToFireball.reset();
        }

        @Override
        public void update(Lich entity) {

            // AGGIORNAMENTO MOVEMENT
            if (!isPrepareToFireball){
                prepareToFireball.update(entity.delta);
                if (prepareToFireball.isReady){
                    System.out.println("Preparing..");
                    useFireball.reset();
                    isPrepareToFireball = true;
                }
                entity.pathfinder().renderPath(entity.manager.player().getPosition().x, entity.manager.player().getPosition().y, entity.delta);
                entity.move();
            }else{
                useFireball.update(entity.delta);
                if (useFireball.isReady){
                    isPrepareToFireball = false;
                    System.out.println("FIREBALL");
                    entity.fireball();
                    useFireball.reset();
                    prepareToFireball.reset();
                }
            }

            if (!entity.pathfinder().success) entity.getStateMachine().changeState(IDLE1);
        }

        @Override
        public void exit(Lich entity) {

        }

        @Override
        public boolean onMessage(Lich entity, Telegram telegram) {
            return false;
        }
    },
    IDLE1 {
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
