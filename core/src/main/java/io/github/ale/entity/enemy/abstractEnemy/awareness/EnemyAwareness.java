package io.github.ale.entity.enemy.abstractEnemy.awareness;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Segment;

public class EnemyAwareness {

    private final Circle areaInseguimento;
    private final Rectangle range;
    private final Segment visione;
    private final Vector2 obbiettivo;
    private final Vector2 obbiettivoDrawCoord;

    public EnemyAwareness(){
        obbiettivo = new Vector2();
        obbiettivoDrawCoord = new Vector2();
        range = new Rectangle();
        visione = new Segment(new Vector3(0, 0, 0), new Vector3(0, 0, 0));
        areaInseguimento = new Circle();
    }

    public Segment getVisione() {
        return visione;
    }

    public void setVisione(float x1, float y1, float x2, float y2) {
        this.visione.a.set(x1, y1, 0);
        this.visione.b.set(x2, y2, 0);
    }

    public Vector2 getObbiettivo() {
        return obbiettivo;
    }

    public void setObbiettivo(float x, float y) {
        this.obbiettivo.set(x, y);
    }

    public Vector2 getObbiettivoDrawCoord() {
        return obbiettivoDrawCoord;
    }

    public void setObbiettivoDrawCoord(float x, float y) {
        this.obbiettivoDrawCoord.set(x, y);
    }

    public Rectangle getRange() {
        return range;
    }

    public void setRange(float x, float y, float width, float height) {
        this.range.set(x, y, width, height);
    }

    public Circle getAreaInseguimento() {
        return areaInseguimento;
    }

    public void setAreaInseguimento(float x, float y, float radius) {
        this.areaInseguimento.set(x, y, radius);
    }
}
