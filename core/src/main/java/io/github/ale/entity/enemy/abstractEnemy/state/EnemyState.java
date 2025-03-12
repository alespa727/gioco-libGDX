package io.github.ale.entity.enemy.abstractEnemy.state;

public class EnemyState {
    private boolean inAreaInseguimento;
    private boolean inRange;
    private boolean idle;
    private boolean pursuing;
    private boolean outOfPursuing;

    public boolean isInAreaInseguimento() {
        return inAreaInseguimento;
    }

    public void setInAreaInseguimento(boolean inAreaInseguimento) {
        this.inAreaInseguimento = inAreaInseguimento;
    }

    public boolean isInRange() {
        return inRange;
    }

    public void setInRange(boolean inRange) {
        this.inRange = inRange;
    }

    public boolean isIdle() {
        return idle;
    }

    public void setIdle(boolean idle) {
        this.idle = idle;
    }

    public boolean isPursuing() {
        return pursuing;
    }

    public void setPursuing(boolean pursuing) {
        this.pursuing = pursuing;
    }

    public boolean isOutOfPursuing() {
        return outOfPursuing;
    }

    public void setOutOfPursuing(boolean outOfPursuing) {
        this.outOfPursuing = outOfPursuing;
    }
}
