package io.github.ale.entity.player.lineofsight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;

import io.github.ale.entity.abstractEntity.Entity;
import io.github.ale.maps.Map;

public class LineOfSight {
    
    Circle[][] lineOfSightCircles;
    boolean[][] lineOfSight;
    
    int mapWidth = Map.getWidth();
    int mapHeight = Map.getHeight();

    public LineOfSight(){
        mapWidth = Map.getWidth();
        mapHeight = Map.getHeight();
        lineOfSightCircles = new Circle[mapWidth][mapHeight];
        lineOfSight = new boolean[mapWidth][mapHeight];
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                lineOfSightCircles[i][j] = new Circle(i+0.5f, j+0.5f, 0.1f);
                lineOfSight[i][j]=false;
            } 
        }
    }

    public void update(Entity entity){
        if (mapWidth!=Map.getWidth() || mapWidth!=Map.getHeight()) {
            mapWidth = Map.getWidth();
            mapHeight = Map.getHeight();
            lineOfSightCircles = new Circle[mapWidth][mapHeight];
            lineOfSight = new boolean[mapWidth][mapHeight];
            for (int i = 0; i < mapWidth; i++) {
                for (int j = 0; j < mapHeight; j++) {
                    lineOfSightCircles[i][j] = new Circle(i+0.5f, j+0.5f, 0.1f);
                    lineOfSight[i][j]=false;
                } 
            }
        }
        //CODICE PER LA VISIONE DEL PLAYER 
    }
    
    public void draw(ShapeRenderer renderer){
        renderer.setColor(Color.BLACK);
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
            
                renderer.circle(lineOfSightCircles[i][j].x, lineOfSightCircles[i][j].y, lineOfSightCircles[i][j].radius, 40);
                
            } 
        }
        renderer.setColor(Color.BLACK);
    }
}
