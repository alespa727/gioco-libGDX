package io.github.ale.screens.gameScreen.maps;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

public class GameGraph implements IndexedGraph<Node>{
    private Array<Node> nodes = new Array<>();
    private int width, height;
    private boolean[][] walkable; // Mappa delle celle attraversabili

    public GameGraph(int width, int height, boolean[][] collisions) {
        this.width = width;
        this.height = height;
        this.walkable = collisions;
        for (int i = 0; i < walkable.length; i++) {
            for (int j = 0; j < walkable[i].length; j++) {
                walkable[i][j] = !walkable[i][j];
            }
        }
        generateGraph();
    }

    private void generateGraph() {
        Node[][] grid = new Node[width][height];

        // Creazione dei nodi
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (walkable[x][y]) { // Solo celle attraversabili
                    grid[x][y] = new Node(x, y);
                    
                    nodes.add(grid[x][y]);
                    
                }
            }
        }

        // Creazione delle connessioni tra nodi adiacenti
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (grid[x][y] != null) {
                        connectNeighbors(grid, x, y);
                }
            }
        }
    }


    private void connectNeighbors(Node[][] grid, int x, int y) {
        Node node = grid[x][y];

        // Offset per le 8 direzioni (su, giù, sinistra, destra, diagonali)
        int[][] directions = {
            {0, 1}, {1, 0}, {0, -1}, {-1, 0}, // Cardinali (N, E, S, O)
            {1, 1}, {1, -1}, {-1, 1}, {-1, -1} // Diagonali
        };

        for (int[] dir : directions) {
            int nx = x + dir[0];
            int ny = y + dir[1];

            if (nx >= 0 && nx < width && ny >= 0 && ny < height && grid[nx][ny] != null) {
                float cost = (dir[0] == 0 || dir[1] == 0) ? 1.0f : 1.4f; // 1 per cardinali, 1.4 per diagonali
                
                    node.addConnection(grid[nx][ny], cost);
                
                
            }
        }
    }

    @Override
    public Array<Connection<Node>> getConnections(Node fromNode) {
        return fromNode.connections;
    }

    public Array<Node> getNodes() {
        return nodes;
    }

    public Node getNodeAt(int x, int y) {
        for (Node node : nodes) {
            if (node.x == x && node.y == y) return node;
        }
        return null;
    }
  
    public void drawGraphConnections(ShapeRenderer shapeRenderer, GameGraph graph) {
        
        shapeRenderer.setColor(Color.RED);
        
        for (Node node : graph.getNodes()) {
            for (Connection<Node> connection : node.connections) {
                Node toNode = connection.getToNode();
                shapeRenderer.line(node.x+0.5f, node.y+0.5f, toNode.x+0.5f, toNode.y+0.5f);
            }
        }
        
    }

    @Override
    public int getIndex(Node node) {
        return 0;
    }

    @Override
    public int getNodeCount() {
        
        return 0;
    }

}
