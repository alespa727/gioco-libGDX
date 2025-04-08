package progetto.gameplay.entity.behaviors.manager.entity.behaviours;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import progetto.gameplay.entity.types.living.combat.boss.Lich;
import progetto.gameplay.entity.types.living.combat.player.Player;
import progetto.gameplay.entity.behaviors.EntityManager;
import progetto.gameplay.entity.behaviors.manager.map.WorldManager;

public enum LichStates implements State<Lich> {
    PURSUE {
        private Player player;

        @Override
        public void enter(Lich entity) {
        }

        @Override
        public void update(Lich entity) {
            if (player == null)
                player = entity.manager.player();
            entity.pathfinder().renderPath(entity.manager.player().getPosition().x, entity.manager.player().getPosition().y, entity.delta);

            // AGGIORNAMENTO MOVEMENT
            entity.movement().update();

            RayCastCallback callback = getRayCastCallback(entity, entity.body.getPosition(), player.body.getPosition());
            WorldManager.getInstance().rayCast(callback, entity.body.getPosition(), player.body.getPosition());

            if (!entity.pathfinder().success) entity.getStateMachine().changeState(IDLE);
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

    private static RayCastCallback getRayCastCallback(Lich entity, Vector2 start, Vector2 end) {

        return new RayCastCallback() {

            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                Filter filter = fixture.getFilterData();
                Object userData = fixture.getBody().getUserData();

                if (userData == null) {
                    return -1; // Ignora il fixture e continua il raycasting
                }

                if (userData.equals("map")) {
                    return -1;
                }

                if (userData instanceof Player && filter.categoryBits != EntityManager.RANGE) {
                    if (start.dst(end) > entity.rangeRadius() && entity.getStateMachine().getCurrentState() != PURSUE) {
                        entity.getStateMachine().changeState(PURSUE);
                        return fraction;
                    }
                    if (start.dst(end) < entity.rangeRadius() && entity.getStateMachine().getCurrentState() != IDLE) {
                        entity.getStateMachine().changeState(IDLE);
                        return fraction;
                    }

                }

                return 0;
            }
        };
    }
}
