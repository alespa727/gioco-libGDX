package io.github.ale.entity.player.lineofsight;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Segment;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.BooleanArray;

import io.github.ale.entity.abstractEntity.Entity;

public class LineOfSightData {
    private Array<Vector3> centroCerchio;
    private Array<Segment> linea;
    private BooleanArray lineOfSight;

    private int mapWidth;
    private int mapHeight;

    public LineOfSightData(int x) {
        this.centroCerchio = new Array<>();
        this.linea = new Array<>();
        this.lineOfSight = new BooleanArray();
    }

    public void addLinea(Entity e){
        
    }
}
