package io.github.ale.screens.gameScreen.entity.player.lineofsight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Segment;
import com.badlogic.gdx.utils.Array;

import io.github.ale.screens.gameScreen.entity.abstractEntity.Entity;
import io.github.ale.screens.gameScreen.entity.enemy.abstractEnemy.Nemico;
import io.github.ale.screens.gameScreen.maps.Map;

public class LineOfSight {
    private final Array<Vector2> temp;
    private final Array<Vector2> puntiComuni;
    private int minIndex;

    private final Array<Vector2> v;

    private Vector3[][] centroCerchio;
    private Segment[][] linea;
    private boolean[][] lineOfSight;

    private final Circle playerCircle;
    private final Vector2 entityPosition = new Vector2();

    private final float centroRaggio = 0.1f;
    private final float losRaggio = 5.5f;

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

        v = new Array<>();
        for (int index = 0; index < 5; index++) {
            v.add(new Vector2());
        }
        puntiComuni = new Array<>();
        temp = new Array<>();

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

    public float getRaggio() {
        return losRaggio;
    }

    /**
     * aggiorna la visione in base al cambiamento della mappa e al player
     * 
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
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                if (!lineOfSight[i][j]) {
                    renderer.setColor(Color.YELLOW);
                } else {
                    p1 = new Vector2(linea[i][j].a.x, linea[i][j].a.y);

                    if (minIndex < puntiComuni.size && puntiComuni.contains(p1, false)) {
                        renderer.setColor(Color.BLACK);
                    } else
                        renderer.setColor(Color.WHITE);
                    renderer.circle(centroCerchio[i][j].x, centroCerchio[i][j].y, centroRaggio,
                            40);
                    // renderer.line(p1, new Vector2(linea[i][j].b.x, linea[i][j].b.y));
                    // renderer.circle(playerCircle.x, playerCircle.y, playerCircle.radius,
                    // 40);
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

    private void updatePlayerCircle(Entity e) {
        playerCircle.x = e.getX() + e.getSize().getWidth() / 2;
        playerCircle.y = e.getY() + e.getSize().getHeight() / 2;
        playerCircle.radius = 0.5f;
    }

    private void updateLosValue(Entity e) {
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                linea[i][j].b.x = e.getX() + e.getSize().getWidth() / 2;
                linea[i][j].b.y = e.getY() + e.getSize().getHeight() / 2;
                circle.x = centroCerchio[i][j].x;
                circle.y = centroCerchio[i][j].y;
                circle.radius = losRaggio;
                if (playerCircle.overlaps(circle)) {
                    v.get(0).set(linea[i][j].a.x, linea[i][j].a.y);
                    v.get(1).set(linea[i][j].b.x, linea[i][j].b.y);
                    lineOfSight[i][j] = !Map.checkLineCollision(v.get(0), v.get(1));
                } else
                    lineOfSight[i][j] = false;
            }
        }
    }

    public Vector2 mutualLineOfSight(Entity e, Entity e2, float area) { // e -> entita e2-> player
        puntiComuni.clear();
        
        boolean los;
        Vector2 objective = null;
        entityPosition.set(e.getX() + e.getSize().getWidth() / 2, e.getY() + e.getSize().getHeight() / 2);
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                if (lineOfSight[i][j]) {
                    los = !Map.checkLineCollision(new Vector2(linea[i][j].a.x, linea[i][j].a.y), entityPosition);
                    if (los && entityPosition.dst(new Vector2(linea[i][j].a.x, linea[i][j].a.y)) < area) {
                        objective = new Vector2(linea[i][j].a.x, linea[i][j].a.y);
                        puntiComuni.add(objective);
                    }
                }
            }
        }

        if (!puntiComuni.isEmpty()) {
            objective = minimo(e, e2);
        }

        // System.out.println(objective);

        return objective;

    }

    public Vector2 mutualLineOfSight(Nemico nemico, Entity e2, float area) {
        temp.clear();
    
        boolean los;
        Vector2 objective = null;
        entityPosition.set(nemico.getX() + nemico.getSize().getWidth() / 2, nemico.getY() + nemico.getSize().getHeight() / 2);
    
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                if (lineOfSight[i][j]) {
                    Vector2 point = new Vector2(linea[i][j].a.x, linea[i][j].a.y);
                    
                    los = checkFullLineOfSight(nemico, point) && 
                          !Map.checkRectangleCollision(
                              point.x - nemico.getAwareness().getRange().width / 2,
                              point.y - nemico.getAwareness().getRange().height / 2,
                              nemico.getAwareness().getRange().width,
                              nemico.getAwareness().getRange().height);
    
                    if (los && entityPosition.dst(point) < area) {
                        temp.add(point);
                    }
                }
            }
        }
    
        if (!temp.isEmpty()) {
            objective = minimo(nemico, e2);
        }
    
        addPunti();

        return objective;
    }

    private boolean checkFullLineOfSight(Nemico nemico, Vector2 target) {
        float x = nemico.hitbox().x;
        float y = nemico.hitbox().y;
        float w = nemico.hitbox().width;
        float h = nemico.hitbox().height;
    
        // Quattro punti della hitbox
        Vector2 topLeft = new Vector2(x, y + h);
        Vector2 topRight = new Vector2(x + w, y + h);
        Vector2 bottomLeft = new Vector2(x, y);
        Vector2 bottomRight = new Vector2(x + w, y);
    
        // Controlla se almeno un angolo ha una linea libera fino al target
        return !Map.checkLineCollision(topLeft, target) &&
               !Map.checkLineCollision(topRight, target) &&
               !Map.checkLineCollision(bottomLeft, target) &&
               !Map.checkLineCollision(bottomRight, target);
    }
    
    

    public Vector2 minimo(Entity e1, Entity e2) {
        Vector2 minimo = new Vector2();
        float min = Float.MAX_VALUE;
        for (Vector2 puntiComuni1 : temp) {
            float distanzaTotale = puntiComuni1.dst(e1.coordinateCentro()) + puntiComuni1.dst(e2.coordinateCentro());
            if (distanzaTotale <= min) {
                min = puntiComuni1.dst(e1.coordinateCentro()) + puntiComuni1.dst(e2.coordinateCentro());
                minimo = puntiComuni1;
            } else if (Math.abs(distanzaTotale - min) <= 0.2f) {
                float distanzaDaGiocatore = puntiComuni1.dst(e2.coordinateCentro());
                float distanzaDaNemico = puntiComuni1.dst(e1.coordinateCentro());

                if (distanzaDaGiocatore < distanzaDaNemico) {
                    minimo = puntiComuni1;
                }
            }
        }
        return minimo;
    }

    public void clearPuntiComuni(){
        puntiComuni.clear();
    }

    private void addPunti(){
        puntiComuni.addAll(temp);
    }
}