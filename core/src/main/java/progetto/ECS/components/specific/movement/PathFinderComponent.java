package progetto.ECS.components.specific.movement;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.utils.Disposable;
import progetto.ECS.components.base.Component;
import progetto.ECS.entities.specific.living.Humanoid;
import progetto.world.graph.HeuristicDistance;
import progetto.world.graph.node.Node;
import progetto.world.map.Map;
import progetto.world.map.MapManager;

public class PathFinderComponent extends Component implements Disposable {
    private final Humanoid entity;
    private final DefaultGraphPath<Node> path;
    public boolean success;
    private Node startNode;
    private Node endNode;
    private volatile int map = -1;
    private Heuristic<Node> heuristic;
    private IndexedAStarPathFinder<Node> pathFinder;

    public PathFinderComponent(Humanoid entity) {
        this.path = new DefaultGraphPath<>();
        this.entity = entity;
    }

    public boolean renderPath(float x, float y, float delta) {
        //INIZIALIZZAZIONE EVENTUALE (in caso cambio mappa/grafico non loaddato)
        if (Map.isGraphLoaded && map != MapManager.getMapIndex()) {
            map = MapManager.getMapIndex();
            pathFinder = new IndexedAStarPathFinder<>(Map.getGraph());
        }

        if (entity.getMovementManager().isReady()) {
            search(x, y);
        }

        return success;
    }

    public boolean render(float x, float y, float delta) {
        //INIZIALIZZAZIONE EVENTUALE (in caso cambio mappa/grafico non loaddato)
        if (Map.isGraphLoaded && map != MapManager.getMapIndex()) {
            map = MapManager.getMapIndex();
            pathFinder = new IndexedAStarPathFinder<>(Map.getGraph());
        }

        search(x, y);

        return success;
    }


    public void search(float x, float y) {
        path.clear();
        //setta il nodo di inizio e fine
        startNode = entity.components.get(NodeComponent.class).node;
        endNode = Map.getGraph().getClosestNode(x, y);

        if (startNode == null || endNode == null) {
            return;
        }

        //INIZIALIZZA CALCOLO DISTANZA
        if (heuristic == null) {
            heuristic = new HeuristicDistance();
        }

        success = pathFinder.searchNodePath(startNode, endNode, heuristic, path);
        if (entity.getMovementManager().isReady()) {
            entity.getMovementManager().setPath(path);
        }
    }

    public void clear() {
        path.clear();
    }

    @Override
    public void dispose() {

    }
}
