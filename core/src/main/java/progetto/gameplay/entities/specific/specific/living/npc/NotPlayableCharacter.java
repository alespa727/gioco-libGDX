package progetto.gameplay.entities.specific.specific.living.npc;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import progetto.gameplay.entities.specific.base.EntityConfig;
import progetto.gameplay.entities.specific.base.EntityInstance;
import progetto.gameplay.entities.specific.specific.living.Humanoid;
import progetto.gameplay.entities.specific.specific.living.HumanoidInstances;
import progetto.manager.entities.EntityManager;
import progetto.manager.world.WorldManager;
import progetto.manager.input.TerminalCommand;

public abstract class NotPlayableCharacter extends Humanoid {

    private final String[] dialoghi;
    private final WindowDialogo windowDialogo;
    private int indexDialogo = 0;

    private boolean listenerAggiunto = false;
    private boolean isTyping = false;

    public NotPlayableCharacter(HumanoidInstances instance, EntityManager entityManager, String[] dialoghi, WindowDialogo windowDialogo) {
        super(instance, entityManager);
        this.dialoghi = dialoghi;
        this.windowDialogo = windowDialogo;
    }

    public NotPlayableCharacter(EntityConfig config, EntityManager manager, String[] dialoghi, WindowDialogo windowDialogo) {
        super(config, manager);
        this.dialoghi = dialoghi;
        this.windowDialogo = windowDialogo;
    }

    public void talkTuah() {
        if (dialoghi == null || dialoghi.length == 0) return;

        showDialogLine(indexDialogo);

        if (!listenerAggiunto) {
            windowDialogo.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (!isTyping) {
                        if (indexDialogo < dialoghi.length - 1) {
                            indexDialogo++;
                            showDialogLine(indexDialogo);
                        } else {
                            windowDialogo.setVisible(false);
                        }
                    }
                }
            });
            listenerAggiunto = true;
        }
    }

    private void showDialogLine(int index) {
        if (index >= dialoghi.length) return;

        String fullText = dialoghi[index];
        final StringBuilder displayedText = new StringBuilder();
        isTyping = true;

        Timer.schedule(new Timer.Task() {
            int charIndex = 0;

            @Override
            public void run() {
                if (charIndex < fullText.length()) {
                    displayedText.append(fullText.charAt(charIndex++));
                    windowDialogo.setText(displayedText.toString());
                } else {
                    isTyping = false;
                    this.cancel();
                }
            }
        }, 0, 0.05f);
    }

    @Override
    public void cooldown(float delta) {

    }

    @Override
    public void updateEntityType(float delta) {

    }

    @Override
    public EntityInstance despawn() {
        TerminalCommand.printError("Deleting entity" + getConfig().nome);
        manager.remove(this);

        WorldManager.destroyBody(getPhysics().getBody());

        return new HumanoidInstances(this);
    }
}
