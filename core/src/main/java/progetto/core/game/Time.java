package progetto.core.game;

import java.util.Timer;
import java.util.TimerTask;

public class Time {
    private float timeScale;
    public float delta;
    public float accumulator;

    public Time() {
        delta = 0;
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

    public void setTimeScale(float timeScale, float time) {
        this.timeScale = timeScale;

        TimerTask task = new TimerTask() {
            public void run() {
                resetTimeScale();
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, (long) (time*1000));
    }

    private void resetTimeScale() {
        this.timeScale = 1f;
    }
}
