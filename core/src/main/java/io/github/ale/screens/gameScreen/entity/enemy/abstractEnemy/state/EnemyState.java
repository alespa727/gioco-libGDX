package io.github.ale.screens.gameScreen.entity.enemy.abstractEnemy.state;

public class EnemyState {
    private boolean inAreaInseguimento;
    private boolean inRange;
    private boolean idle;
    private boolean pursuing;
    private boolean searching;

    public boolean inAreaInseguimento() {
        return inAreaInseguimento;
    }

    public void setInAreaInseguimento(boolean inAreaInseguimento) {
        this.inAreaInseguimento = inAreaInseguimento;
    }

    public boolean inRange() {
        return inRange;
    }

    public void setInRange(boolean inRange) {
        this.inRange = inRange;
    }

    public boolean idle() {
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

    public boolean searching() {
        return searching;
    }

    public void setSearching(boolean searching) {
        this.searching = searching;
    }

}
