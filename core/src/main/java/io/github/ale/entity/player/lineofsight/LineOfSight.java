package io.github.ale.entity.player.lineofsight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Segment;

import io.github.ale.entity.abstractEntity.Entity;
import io.github.ale.maps.Map;

public class LineOfSight {
    private Circle[][] lineOfSightCircles;
    private boolean[][] lineOfSight;

    private final Circle playerCircle;
    private Circle[][] losArea;
    private Segment[][] linea;
    
    private int mapWidth;
    private int mapHeight;

    /**
     * obbligatoriamente da fare senza il metodo create
     */
    public LineOfSight(Map map, OrthographicCamera cam) {
        mapWidth = Map.getWidth();
        mapHeight = Map.getHeight();
        lineOfSightCircles = new Circle[mapWidth][mapHeight];
        lineOfSight = new boolean[mapWidth][mapHeight];
        losArea = new Circle[mapWidth][mapHeight];
        linea = new Segment[mapWidth][mapHeight];
        playerCircle = new Circle();
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                lineOfSightCircles[i][j] = new Circle(i + 0.5f, j + 0.5f, 0.1f);
                losArea[i][j] = new Circle(i + 0.5f, j + 0.5f, 3.5f);
                lineOfSight[i][j] = false;
                linea[i][j] = new Segment(new Vector3(i + 0.5f, j + 0.5f, 0), new Vector3(0, 0, 0));
            }
        }
    }

    /**
     * aggiorna la visione in base al cambiamento della mappa e al player
     * @param entity
     */
    public void update(Entity e) {

        if (mapWidth != Map.getWidth() || mapHeight != Map.getHeight()) {
            System.err.println("AAAA");
            create();
        }

        updatePlayerCircle(e);
        updateLosValue(e);
        
    }

    public void draw(ShapeRenderer renderer) {
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                if (!lineOfSight[i][j]) {
                    renderer.setColor(Color.WHITE);
                }else{
                    renderer.setColor(Color.YELLOW);
                    renderer.circle(lineOfSightCircles[i][j].x, lineOfSightCircles[i][j].y, lineOfSightCircles[i][j].radius,
                            40);
                    
                } 
            }
        }
    }

    public void create() {
        mapWidth = Map.getWidth();
        mapHeight = Map.getHeight();
        lineOfSightCircles = new Circle[mapWidth][mapHeight];
        lineOfSight = new boolean[mapWidth][mapHeight];
        losArea = new Circle[mapWidth][mapHeight];
        linea = new Segment[mapWidth][mapHeight];
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                lineOfSightCircles[i][j] = new Circle(i + 0.5f, j + 0.5f, 0.1f);
                losArea[i][j] = new Circle(i + 0.5f, j + 0.5f, 3.5f);
                lineOfSight[i][j] = false;
                linea[i][j] = new Segment(new Vector3(i + 0.5f, j + 0.5f, 0), new Vector3(0, 0, 0));
            }
        }
    }

    private void updatePlayerCircle(Entity e){
        playerCircle.x = e.getX()+e.getSize().getWidth()/2;
        playerCircle.y = e.getY()+e.getSize().getHeight()/2;
        playerCircle.radius = 0.5f;
    }

    private void updateLosValue(Entity e){
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                linea[i][j].b.x = e.getX()+e.getSize().getWidth()/2;
                linea[i][j].b.y = e.getY()+e.getSize().getHeight()/2;
                if (playerCircle.overlaps(losArea[i][j])) {
                    lineOfSight[i][j] = !Map.checkLineCollision(new Vector2(linea[i][j].a.x, linea[i][j].a.y), new Vector2(linea[i][j].b.x, linea[i][j].b.y));
                    
                }else lineOfSight[i][j] = false;
                
            }
        }
    }
}
