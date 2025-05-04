package progetto.core.game;

import com.badlogic.gdx.math.MathUtils;

/**
 * Anima un testo facendolo apparire lettera per lettera nel tempo.
 */
public class TextAnimator {
    private boolean hasFinished = true;
    private String text;
    private StringBuilder currentText;
    private float timeAccumulator;
    private float baseTimePerLetter;
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
        int length = text.length();
        float minSpeed = 0.001f;
        float maxSpeed = 0.4f;

        baseTimePerLetter = MathUtils.clamp(
                maxSpeed - (length / 40f) * (maxSpeed - minSpeed),
                minSpeed,
                maxSpeed
        );

        randomizeTimePerLetter();
    }

    public void update(float delta) {
        timeAccumulator += delta;

        if (timeAccumulator >= timePerLetter) {
            int nextIndex = currentText.length();
            if (nextIndex < text.length()) {
                currentText.append(text.charAt(nextIndex));
                randomizeTimePerLetter();
            }else hasFinished = true;
            timeAccumulator -= timePerLetter;
        }
    }

    private void randomizeTimePerLetter() {
        this.timePerLetter = Math.min(baseTimePerLetter, 0.4f);
    }

    public String getCurrentText() {
        return currentText.toString();
    }
}
