package io.github.ale.screens.gameScreen.pathfinding;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.ale.screens.gameScreen.entity.enemy.abstractEnemy.Nemico;
import io.github.ale.screens.gameScreen.maps.Map;
import io.github.ale.screens.gameScreen.maps.MapManager;

public class Pathfinder {
    Nemico enemy;
    private Node startNode;
    private Node endNode;
    private int map = -1;
    private Heuristic<Node> heuristic;
    private boolean hasLoadedGraph = false;
    private IndexedAStarPathFinder<Node> pathFinder;
    private final DefaultGraphPath<Node> path;

    private float cooldown = 0;

    public Pathfinder(Nemico enemy) {
        this.path = new DefaultGraphPath<>();
        this.enemy = enemy;
    }

    public void countdown() {
        if (cooldown > 0) {
            cooldown -= Gdx.graphics.getDeltaTime();
        } else {
            cooldown = 3f;
            enemy.movement().sulNodo = true;
        }
    }

    public void renderPath(float x, float y) {
        // Carica il grafo una volta che è stato caricato

        countdown();

        if (hasLoadedGraph && map != MapManager.currentmap()) {
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
            calcolaPercorso(x, y);
            enemy.movement().setGoal(path.get(0), path.get(1));
        }

        if (enemy.movement().sulNodo) {

            calcolaPercorso(x, y);

            if (path.getCount() > 1)
                enemy.movement().setGoal(path.get(0), path.get(1));
        }

        // System.out.println(path.getCount());
        if (path.getCount() > 1 && enemy.coordinateCentro().dst(x, y) < 20f) {
            // Aggiorna il movimento del nemico
            enemy.movement().update(enemy);
        } else {
            enemy.movement().setFermo(enemy);
        }

    }

    public void calcolaPercorso(float x, float y) {
        path.clear();
    
        // Get the closest nodes for the start and end positions
        startNode = Map.getGraph().getClosestNode(enemy.coordinateCentro().x, enemy.coordinateCentro().y);
        endNode = Map.getGraph().getClosestNode(x, y);
    
        // Validate the nodes
        if (startNode == null || endNode == null) {
            System.err.println("Start or end node is null!");
            return;
        }
    
        // Initialize the heuristic if not already done
        if (heuristic == null) {
            heuristic = new HeuristicDistance();
        }
    
        // Perform the pathfinding
        boolean success = pathFinder.searchNodePath(startNode, endNode, heuristic, path);
    
        // Handle the result of the pathfinding
        if (success) {
            if (path.getCount() < 14) {
                System.out.println("Percorso trovato con successo!");
            }
        } else {
            System.out.println("Percorso non trovato, aggiungo il nodo finale.");
            if (path.getCount() == 0 || path.get(path.getCount() - 1) != endNode) {
                path.add(endNode);
            }
        }
    }

    public void drawPath(ShapeRenderer shapeRenderer) {
        // Controlla se il percorso è vuoto o nullo
        if (path == null || path.nodes.isEmpty()) {
            return; // Nessun percorso da disegnare
        }
        // Colore delle linee

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
