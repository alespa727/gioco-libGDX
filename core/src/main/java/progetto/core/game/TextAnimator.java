package progetto.core.game;

import com.badlogic.gdx.math.MathUtils;

/**
 * Anima un testo facendolo apparire lettera per lettera nel tempo.
 */
public class TextAnimator {
    private boolean hasFinished = true;
    private String text;
    private final StringBuilder currentText;
    private float timeAccumulator;
    private float timePerLetter;

    public TextAnimator() {
        this.currentText = new StringBuilder();
        this.timeAccumulator = 0;
    }

    public boolean isReady(){
        return hasFinished;
    }

    public void setText(String text) {
        this.text = text;
        this.currentText.setLength(0);
        this.hasFinished = false;

        this.timePerLetter = 0.045f;
    }

    public void update(float delta) {
        timeAccumulator += delta;

        if (timeAccumulator >= timePerLetter) {
            int nextIndex = currentText.length();
            if (nextIndex < text.length()) {
                currentText.append(text.charAt(nextIndex));
            }else hasFinished = true;
            timeAccumulator -= timePerLetter;
        }
    }

    public String getCurrentText() {
        return currentText.toString();
    }
}
