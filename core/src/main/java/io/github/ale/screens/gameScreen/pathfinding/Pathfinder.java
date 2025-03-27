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
import io.github.ale.screens.gameScreen.entityType.livingEntity.LivingEntity;
import io.github.ale.screens.gameScreen.maps.Map;
import io.github.ale.screens.gameScreen.maps.MapManager;

public class Pathfinder implements Disposable{
    private final LivingEntity entity;
    private Node startNode;
    private Node endNode;
    private volatile int map = -1;
    private Heuristic<Node> heuristic;
    private IndexedAStarPathFinder<Node> pathFinder;
    private final DefaultGraphPath<Node> path;
    private final Cooldown cooldown = new Cooldown(0.8f);

    public Pathfinder(LivingEntity entity) {
        this.path = new DefaultGraphPath<>();
        this.entity = entity;
        cooldown.isReady = true;
    }

    public DefaultGraphPath<Node> getPath() {return path;}

    public void countdown(float delta) {
        cooldown.update(delta);
        if (cooldown.isReady) {
            cooldown.reset(0.8f);
            path.clear();
            entity.movement().searchingfornext = true;
        }
    }

    public void renderPath(float x, float y, float delta) {
        countdown(delta);
        //executor.submit(() -> calculatePathTo(x, y));
        calculatePathTo(x, y);
    }

    public void calculatePathTo(float x, float y){
            //INIZIALIZZAZIONE EVENTUALE (in caso cambio mappa/grafico non loaddato)
            if (Map.isGraphLoaded && map != MapManager.currentmap()) {
                map = MapManager.currentmap();
                pathFinder = new IndexedAStarPathFinder<>(Map.getGraph());
                search(x, y);
                if (path.getCount() > 1) entity.movement().setGoal(path.get(0), path.get(1));
            }

            //CALCOLO DEL PERCORSO
            if (entity.movement().searchingfornext) {
                search(x, y);
                if (path.getCount() > 1) entity.movement().setGoal(path.get(0), path.get(1));
            }
    }


    public void search(float x, float y) {
        //System.out.println("Calcolo del percorso!");
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
        if (path.nodes.isEmpty()) {
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
        cooldown.reset(0f);
    }

    @Override
    public void dispose() {

    }
}
