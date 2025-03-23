package io.github.ale.screens.gameScreen.pathfinding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;

import io.github.ale.cooldown.Cooldown;
import io.github.ale.screens.gameScreen.entity.livingEntity.LivingEntity;
import io.github.ale.screens.gameScreen.maps.Map;
import io.github.ale.screens.gameScreen.maps.MapManager;

public class Pathfinder implements Disposable{
    LivingEntity entity;
    private Node startNode;
    private Node endNode;
    private volatile int map = -1;
    private Heuristic<Node> heuristic;
    private volatile boolean hasLoadedGraph = false;
    private IndexedAStarPathFinder<Node> pathFinder;
    private final DefaultGraphPath<Node> path;

    private final Cooldown cooldown = new Cooldown(2f);
    private final ExecutorService executor = Executors.newSingleThreadExecutor();


    public Pathfinder(LivingEntity entity) {
        this.path = new DefaultGraphPath<>();
        this.entity = entity;
        cooldown.isReady = true;
    }

    public void countdown() {
        cooldown.update(Gdx.graphics.getDeltaTime());
        if (cooldown.isReady) {
            entity.movement().searchingfornext = true;
            cooldown.reset();
        }
    }
    
    public void renderPath(float x, float y) {
        countdown();
        executor.submit(() -> renderPathAsincrono(x, y));
    }

    public void renderPathAsincrono(float x, float y){
        Gdx.app.postRunnable(() -> {
            //INIZIALIZZAZIONE EVENTUALE (in caso cambio mappa/grafico non loaddato)
            if (Map.isGraphLoaded && map != MapManager.currentmap()) {
                map = MapManager.currentmap();
                pathFinder = new IndexedAStarPathFinder<>(Map.getGraph());
                hasLoadedGraph = true;
                System.out.println("Caricato il grafo!");
                calcolaPercorso(x, y);
                entity.movement().setGoal(path.get(0), path.get(1));
            }
    
            //CALCOLO DEL PERCORSO
            if (entity.movement().searchingfornext) {
                calcolaPercorso(x, y);
                if (path.getCount() > 1) entity.movement().setGoal(path.get(0), path.get(1));
            }
        });
    }

    public void calcolaPercorso(float x, float y) {
        path.clear();
    
        //setta il nodo di inizio e fine
        startNode = Map.getGraph().getClosestNode(entity.coordinateCentro().x, entity.coordinateCentro().y);
        endNode = Map.getGraph().getClosestNode(x, y);
    
        //nodi validi?
        if (startNode == null || endNode == null) {
            System.err.println("Start or end node is null!");
            return;
        }
    
        //INIZIALIZZA CALCOLO DISTANZA
        if (heuristic == null) {
            heuristic = new HeuristicDistance();
        }

        boolean success = pathFinder.searchNodePath(startNode, endNode, heuristic, path);
    
        //aggiunta nodo finale se non si trova un percorso
        if (!success) {
            if (path.getCount() == 0 || path.get(path.getCount() - 1) != endNode) {
                path.add(endNode);
            }
        }
    }

    public void drawPath(ShapeRenderer shapeRenderer) {
        if (path == null || path.nodes.isEmpty()) {
            return; 
        }

        shapeRenderer.setColor(Color.RED);
        Node previousNode = null; 
        for (Node node : path.nodes) {
            if (previousNode != null) {
                shapeRenderer.rectLine(previousNode.getX(), previousNode.getY(), node.getX(), node.getY(), 0.1f);
                shapeRenderer.circle(previousNode.x + 0.5f, previousNode.y + 0.5f, 0.2f, 10);
            }
            previousNode = node;
        }
        shapeRenderer.circle(startNode.x + 0.5f, startNode.y + 0.5f, 0.2f, 10);
        shapeRenderer.circle(endNode.x + 0.5f, endNode.y + 0.5f, 0.2f, 10);
        shapeRenderer.setColor(Color.BLACK);
    }

    public void debug(){
                
        System.out.println("Start Node Index: " + startNode.getIndex());
        System.out.println("End Node Index: " + endNode.getIndex());
        System.out.println("Graph Node Count: " + Map.getGraph().getNodeCount());
        
    }

    public void clear(){
        path.clear();
    }

    @Override
    public void dispose() {
        executor.shutdown();
    }
}
