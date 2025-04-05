package progetto.gameplay.manager.entity.movement;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import progetto.gameplay.entities.types.HumanEntity;
import progetto.gameplay.manager.map.MapManager;
import progetto.gameplay.map.Map;
import progetto.gameplay.map.graph.HeuristicDistance;
import progetto.gameplay.map.graph.node.Node;

public class EntityPathFinder implements Disposable {
    private final HumanEntity entity;
    private final DefaultGraphPath<Node> path;

    private Node startNode;
    private Node endNode;

    private volatile int map = -1;
    private Heuristic<Node> heuristic;
    private IndexedAStarPathFinder<Node> pathFinder;

    public boolean success;

    public EntityPathFinder(HumanEntity entity) {
        this.path = new DefaultGraphPath<>();
        this.entity = entity;
    }

    public void renderPath(float x, float y, float delta) {
        //INIZIALIZZAZIONE EVENTUALE (in caso cambio mappa/grafico non loaddato)

        if (Map.isGraphLoaded && map != MapManager.getMapIndex()) {
            map = MapManager.getMapIndex();
            pathFinder = new IndexedAStarPathFinder<>(Map.getGraph());
        }

        if (entity.movement().isReady()){
            search(x, y);
        }

    }


    public void search(float x, float y) {
        //System.out.println("Calcolo del percorso!");
        path.clear();
        //setta il nodo di inizio e fine
        startNode = Map.getGraph().getClosestNode(entity.getPosition().x, entity.getPosition().y);
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

        success = pathFinder.searchNodePath(startNode, endNode, heuristic, path);

        entity.movement().setPath(path);
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
            }
            previousNode = node;
        }
        shapeRenderer.circle(startNode.x + 0.5f, startNode.y + 0.5f, 0.2f, 10);
        shapeRenderer.circle(endNode.x + 0.5f, endNode.y + 0.5f, 0.2f, 10);
        shapeRenderer.setColor(Color.BLACK);
    }

    public void debug() {

        System.out.println("Start Node Index: " + startNode.getIndex());
        System.out.println("End Node Index: " + endNode.getIndex());
        System.out.println("Graph Node Count: " + Map.getGraph().getNodeCount());

    }

    public void clear() {
        path.clear();
    }

    @Override
    public void dispose() {

    }
}
