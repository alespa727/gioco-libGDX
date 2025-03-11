package io.github.ale.entity.player.lineofsight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Segment;

import io.github.ale.entity.abstractEntity.Entity;
import io.github.ale.maps.Map;

public class LineOfSight {

    private Vector3[][] centroCerchio;
    private Segment[][] linea;
    private boolean[][] lineOfSight;

    private final Circle playerCircle;
    
    private final float centroRaggio = 0.1f;
    private final float losRaggio = 3.5f;

    private final Circle circle;
    
    private int mapWidth;
    private int mapHeight;

    /**
     * obbligatoriamente da fare senza il metodo create
     */
    public LineOfSight() {
        mapWidth = Map.getWidth();
        mapHeight = Map.getHeight();

        circle = new Circle();

        centroCerchio = new Vector3[mapWidth][mapHeight];
        lineOfSight = new boolean[mapWidth][mapHeight];
        linea = new Segment[mapWidth][mapHeight];

        playerCircle = new Circle();
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                centroCerchio[i][j] = new Vector3(i + 0.5f, j + 0.5f, 0);
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
            create();
        }

        updatePlayerCircle(e);
        updateLosValue(e);
        
    }

    public void draw(ShapeRenderer renderer) {
        Vector2 p1;
        Vector2 p2;
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                if (!lineOfSight[i][j]) {
                    renderer.setColor(Color.WHITE);
                    
                }else{
                    p1 = new Vector2(linea[i][j].a.x, linea[i][j].a.y);
                    p2 = new Vector2(linea[i][j].b.x, linea[i][j].b.y);
                    renderer.setColor(Color.YELLOW);
                    renderer.circle(centroCerchio[i][j].x, centroCerchio[i][j].y, centroRaggio,
                            40);
                    renderer.line(p2, p1);
                    //renderer.circle(centroCerchio[7][7].x, centroCerchio[7][7].y, losRaggio,
                    //        40);
                    renderer.line(p2, p1);
                    renderer.circle(playerCircle.x, playerCircle.y, playerCircle.radius,
                                    40);
                } 
            }
        }
    }

    public void create() {
        mapWidth = Map.getWidth();
        mapHeight = Map.getHeight();

        centroCerchio = new Vector3[mapWidth][mapHeight];
        lineOfSight = new boolean[mapWidth][mapHeight];
        linea = new Segment[mapWidth][mapHeight];

        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                centroCerchio[i][j] = new Vector3(i + 0.5f, j + 0.5f, 0);
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
                circle.x = centroCerchio[i][j].x;
                circle.y = centroCerchio[i][j].y;
                circle.radius = losRaggio;
                if (playerCircle.overlaps(circle)) {
                    lineOfSight[i][j] = !Map.checkLineCollision(new Vector2(linea[i][j].a.x, linea[i][j].a.y), new Vector2(linea[i][j].b.x, linea[i][j].b.y));
                    
                }else lineOfSight[i][j] = false;
                
            }
        }
    }
}
