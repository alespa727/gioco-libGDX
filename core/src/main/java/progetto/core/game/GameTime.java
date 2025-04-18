package progetto.core.game;

public class GameTime {
    public float delta;
    public float accumulator;
    private final float timeScale;

    public GameTime() {
        delta=0;
        accumulator = 0f;
        timeScale = 1f;
    }

    public void update(final float delta) {
        this.delta = delta;
        accumulator += delta;
    }

    public float getDelta() {
        return delta;
    }

    public float getAccumulator() {
        return accumulator;
    }

    public void setAccumulator(final float accumulator) {
        this.accumulator = accumulator;
    }

    public float getTimeScale() {
        return timeScale;
    }
}
