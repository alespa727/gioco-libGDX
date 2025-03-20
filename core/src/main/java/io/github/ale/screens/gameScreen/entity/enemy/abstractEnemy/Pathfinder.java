package io.github.ale.screens.gameScreen.entity.enemy.abstractEnemy;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.ale.screens.gameScreen.maps.Map;
import io.github.ale.screens.gameScreen.maps.MapManager;
import io.github.ale.screens.gameScreen.pathfinding.HeuristicDistance;
import io.github.ale.screens.gameScreen.pathfinding.Node;

public class Pathfinder {
    private Node startNode;
    private Node endNode;
    private int map=-1;
    private Heuristic<Node> heuristic;
    private boolean hasLoadedGraph = false;
    private IndexedAStarPathFinder<Node> pathFinder;
    private final DefaultGraphPath<Node> path;

    public Pathfinder(){
        this.path = new DefaultGraphPath<>();
    }
    
    public void renderPath(float x, float y, Nemico enemy) {
        // Carica il grafo una volta che è stato caricato

        if (hasLoadedGraph && map!=MapManager.currentmap()) {
            map = MapManager.currentmap();
            pathFinder = new IndexedAStarPathFinder<>(Map.getGraph());
            hasLoadedGraph = true;
            System.out.println("Caricato il grafo!");
        }
        
        if (!hasLoadedGraph && Map.isGraphLoaded) {
            map = MapManager.currentmap();
            pathFinder = new IndexedAStarPathFinder<>(Map.getGraph());
            hasLoadedGraph = true;
            System.out.println("Caricato il grafo!");
            calcolaPercorso(x, y, enemy);
            enemy.getMovementManager().setGoal(path.get(0), path.get(1));
        }

        if (enemy.getMovementManager().sulNodo) {
            calcolaPercorso(x, y, enemy);
            if (path.getCount() > 1)
                enemy.getMovementManager().setGoal(path.get(0), path.get(1));
        }

        // System.out.println(path.getCount());
        if (path.getCount() > 1 && enemy.coordinateCentro().dst(x, y)<20f) {
            // Aggiorna il movimento del nemico
            enemy.getMovementManager().update(enemy);
        } else {
            enemy.getMovementManager().setFermo(enemy);
        }
    }

    public void calcolaPercorso(float x, float y, Nemico enemy) {
        path.clear();
        startNode = Map.getGraph().getClosestNode(enemy.hitbox().x + enemy.hitbox().width / 2, enemy.hitbox().y + enemy.hitbox().height / 2);
        endNode = Map.getGraph().getClosestNode(x, y);
        //System.out.println(Map.getGraph().getNodeCount());
        heuristic = new HeuristicDistance();

        boolean success = pathFinder.searchNodePath(startNode, endNode, heuristic, path);
        if (success && path.getCount() < 14) {
            //System.out.println("Percorso trovato!");
        } else {
            if (path.getCount() == 0 || path.get(path.getCount() - 1) != endNode) {
                path.add(endNode);
            }
            //System.out.println("Percorso non trovato");
        }
        
    }

    
    public void drawPath(ShapeRenderer shapeRenderer) {
        // Controlla se il percorso è vuoto o nullo
        if (path == null || path.nodes.isEmpty()) {
            return; // Nessun percorso da disegnare
        }
        // Colore delle linee

        shapeRenderer.setColor(Color.BLACK);
        
        shapeRenderer.setColor(Color.RED);
        Node previousNode = null; // Nodo precedente inizializzato a null
        for (Node node : path.nodes) {
            if (previousNode != null) {
                // Disegna una linea dal nodo precedente al nodo corrente
                shapeRenderer.rectLine(previousNode.getX(), previousNode.getY(), node.getX(), node.getY(), 0.1f);
                shapeRenderer.circle(previousNode.x + 0.5f, previousNode.y + 0.5f, 0.2f, 10);
            }
            previousNode = node; // Aggiorna il nodo precedente
        }
        shapeRenderer.circle(startNode.x + 0.5f, startNode.y + 0.5f, 0.2f, 10);
        shapeRenderer.circle(endNode.x + 0.5f, endNode.y + 0.5f, 0.2f, 10);
        shapeRenderer.setColor(Color.BLACK);
    }
}
