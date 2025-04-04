package progetto.utils;

/**
 * @author alesp
 */
public class Cooldown {
    public float time;
    public float maxTime;
    public boolean isReady;

    public Cooldown(float maxTime) {
        this.maxTime = maxTime;
        this.time = maxTime;
        this.isReady = true;
    }

    public void update(float delta) {
        if (!isReady) {
            time -= delta;
            time = Math.max(0, time);
            if (time <= 0) {
                isReady = true;
            }
        }
    }

    public void reset() {
        isReady = false;
        time = maxTime;
    }

    public void reset(float time) {
        isReady = false;
        this.time = time;
    }

    public float getTimer() {
        return time;
    }

    public float getMaxTime() {
        return maxTime;
    }
}
