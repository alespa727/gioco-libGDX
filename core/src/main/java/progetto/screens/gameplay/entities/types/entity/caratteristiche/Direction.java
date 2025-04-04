package progetto.screens.gameplay.entities.types.entity.caratteristiche;

/**
 * Enum delle direzioni per semplificare il mapping e minimizzare errori.
 */
public enum Direction {
    UP(0, 1, false, false),
    DOWN(0, -1, false, false),
    LEFT(-1, 0, true, false),
    RIGHT(1, 0, false, false),

    LEFT_UP(-1, 1, true, false),
    LEFT_DOWN(-1, -1, true, false),
    RIGHT_UP(1, 1, false, false),
    RIGHT_DOWN(1, -1, false, false);

    private final int x, y;
    private final boolean flipX, flipY;

    Direction(int x, int y, boolean flipX, boolean flipY) {
        this.x = x;
        this.y = y;
        this.flipX = flipX;
        this.flipY = flipY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isFlipX() {
        return flipX;
    }

    public boolean isFlipY() {
        return flipY;
    }
}
