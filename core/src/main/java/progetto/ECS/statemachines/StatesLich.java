package progetto.ECS.statemachines;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.MathUtils;
import progetto.ECS.components.specific.base.Cooldown;
import progetto.ECS.components.specific.base.PhysicsComponent;
import progetto.ECS.components.specific.combat.MultiCooldownComponent;
import progetto.ECS.entities.specific.living.combat.boss.Lich;
import progetto.core.game.player.Player;
import progetto.world.map.Map;

public enum StatesLich implements State<Lich> {
    /**
     * Stato in cui il Lich usa l'abilità "Fire Domain".
     * Il Lich lancia un'area di attacco e, al termine, cambia stato per eseguire un altro attacco a lunga distanza.
     * Gestisce anche i tempi di cooldown per l'abilità.
     */
    FIREDOMAIN {
        private final Cooldown useFireDomain = new Cooldown(0);
        private final Cooldown interval = new Cooldown(0.33f);

        @Override
        public void enter(Lich entity) {
            useFireDomain.reset(MathUtils.random(3f, 5f));
        }

        @Override
        public void update(Lich entity) {
            if (!entity.searchPathIdle(entity.entityEngine.player())) {
                entity.getStateMachine().changeState(StatesLich.IDLE);
                return;
            }
            interval.update(entity.entityEngine.delta);
            useFireDomain.update(entity.entityEngine.delta);
            fireDomain(entity);
        }

        @Override
        public void exit(Lich entity) {
            entity.components.get(MultiCooldownComponent.class).getCooldown("firedomain").reset(MathUtils.random(14, 20));
        }

        @Override
        public boolean onMessage(Lich entity, Telegram telegram) {
            return false;
        }

        public void fireDomain(Lich entity) {
            DefaultStateMachine<Lich, StatesLich> stateMachine = entity.getStateMachine();
            if (interval.isReady) {
                entity.fireDomain();
                interval.reset();
            }
            if (useFireDomain.isReady) {
                entity.entityEngine.clearQueue();
                entity.getStateMachine().changeState(StatesLich.LONG_RANGE_ATTACKS);
            }
        }

    },

    /**
     * Stato in cui il Lich usa l'abilità "Fireball".
     * Il Lich lancia una palla di fuoco come attacco a distanza.
     * Gestisce il tempo di cooldown per l'abilità "Fireball".
     */
    FIREBALL {
        private final Cooldown useFireball = new Cooldown(0);

        @Override
        public void enter(Lich entity) {
            useFireball.reset(MathUtils.random(1.2f, 1.5f));
        }

        @Override
        public void update(Lich entity) {
            if (!entity.searchPathIdle(entity.entityEngine.player())) {
                entity.getStateMachine().changeState(StatesLich.IDLE);
                return;
            }
            useFireball.update(entity.entityEngine.delta);
            if (useFireball.isReady) {
                fireball(entity);
            }
        }

        @Override
        public void exit(Lich entity) {
            entity.components.get(MultiCooldownComponent.class).getCooldown("fireball").reset(MathUtils.random(4f, 7f));
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

    /**
     * Stato in cui il Lich esegue attacchi a lunga distanza.
     * Include l'uso delle abilità "Fire Domain" e "Fireball" durante l'attacco a distanza.
     * Il Lich si muove e cerca di attaccare il giocatore.
     */
    LONG_RANGE_ATTACKS {
        @Override
        public void enter(Lich entity) {
            entity.getMovementManager().setAwake(true);
        }

        @Override
        public void update(Lich entity) {
            entity.getStateMachine().changeState(StatesLich.CHOOSING_STATE);

            // ATTACCO AD AREA SPARANDO PROIETTILI IN TUTTE LE DIREZIONI
            entity.components.get(MultiCooldownComponent.class).getCooldown("firedomain").update(entity.entityEngine.delta);
            initiateFireDomain(entity);

            // ATTACCO CON LA FIREBALL
            entity.components.get(MultiCooldownComponent.class).getCooldown("fireball").update(entity.entityEngine.delta);
            initiateFireball(entity);

            Player player = entity.entityEngine.player();
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

        public void initiateFireDomain(Lich entity) {
            if (entity.components.get(MultiCooldownComponent.class).getCooldown("firedomain").isReady) {
                entity.getStateMachine().changeState(StatesLich.FIREDOMAIN);
            }
        }

        public void initiateFireball(Lich entity) {
            if (entity.components.get(MultiCooldownComponent.class).getCooldown("fireball").isReady) {
                entity.getStateMachine().changeState(StatesLich.FIREBALL);
            }
        }

    },

    /**
     * Stato in cui il Lich esegue attacchi a distanza ravvicinata.
     * In questo stato, il Lich si avvicina al giocatore e cerca di infliggere danni a breve distanza.
     * Questo stato viene scelto dopo aver deciso la strategia dell'attacco.
     */
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

    /**
     * Stato in cui il Lich decide quale stato successivo intraprendere.
     * In base alla distanza dal giocatore, il Lich decide se attaccare a lunga distanza, a corta distanza o rimanere inattivo.
     */
    CHOOSING_STATE {
        @Override
        public void enter(Lich entity) {
            if (entity.components.get(MultiCooldownComponent.class).getCooldown("changestates").isReady) {
                entity.components.get(MultiCooldownComponent.class).getCooldown("changestates").reset(1);
                if (Map.isGraphLoaded) entity.searchPath(entity.entityEngine.player());
                if (!entity.getPathFinder().success) {
                    entity.getStateMachine().changeState(StatesLich.IDLE);
                    return;
                }
                if (entity.get(PhysicsComponent.class).getPosition().dst(entity.entityEngine.player().get(PhysicsComponent.class).getPosition()) > 2f) {
                    entity.getStateMachine().changeState(StatesLich.LONG_RANGE_ATTACKS);
                } else {
                    entity.getStateMachine().changeState(StatesLich.CLOSE_RANGE_ATTACKS);
                }
            } else {
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

    /**
     * Stato in cui il Lich è inattivo e si prepara per il prossimo movimento.
     * In questo stato, il Lich cerca di determinare se il giocatore è nelle vicinanze e se deve cambiare stato.
     */
    IDLE {
        @Override
        public void enter(Lich entity) {
            entity.getMovementManager().setReady(true);
        }

        @Override
        public void update(Lich entity) {
            boolean success = entity.searchPathIdle(entity.entityEngine.player());
            if (!success) return;

            entity.getStateMachine().changeState(StatesLich.CHOOSING_STATE);
        }

        @Override
        public void exit(Lich entity) {
        }

        @Override
        public boolean onMessage(Lich entity, Telegram telegram) {
            return false;
        }
    }
}
