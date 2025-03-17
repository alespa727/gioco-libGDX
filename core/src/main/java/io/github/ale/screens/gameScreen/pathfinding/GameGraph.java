package io.github.ale.screens.gameScreen.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

import io.github.ale.screens.gameScreen.maps.Map;

public class GameGraph implements IndexedGraph<Node> {

    private final Array<Node> nodes = new Array<>(); // Lista di tutti i nodi
    private final int width, height; // Dimensioni della griglia
    private final boolean[][] walkable; // Celle attraversabili

    // Costruttore
    public GameGraph(int width, int height, boolean[][] collisions) {
        this.width = width;
        this.height = height;
        this.walkable = new boolean[width][height];

        //trasformo le posizioni con collisioni a quelle camminabili
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                this.walkable[x][y] = !collisions[x][y]; // true = attraversabile, false = ostacolo
            }
        }

        // Genera il grafo con nodi e connessioni
        generateGraph();
    }

    @Override
    public Array<Connection<Node>> getConnections(Node fromNode) {
        // Restituisce tutte le connessioni di un nodo
        return fromNode.connessioni; // Assumi che "connessioni" sia un Array di Connection<Node> nel nodo
    }

    @Override
    public int getIndex(Node node) {
        // Restituisce un indice unico per ogni nodo
        return node.y * Map.getWidth() + node.x; // Deve essere basato sulle coordinate x, y
    }
    



    @Override
    public int getNodeCount() {
        // Restituisce il numero di nodi nel grafo
        return nodes.size;
    }

    /**
     * genera il grafo
     */
    private void generateGraph() {
        Node[][] grid = new Node[width][height];

        // crea i nodi
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (walkable[x][y]) { // Solo celle attraversabili
                    Node node = new Node(x, y);
                    grid[x][y] = node;
                    nodes.add(node); // Aggiungi alla lista dei nodi
                }
            }
        }

        // per ogni nodo crea le connessioni ai vicini
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (grid[x][y] != null) {
                    connectNeighbors(grid, x, y); // Connetti il nodo con i suoi vicini
                }
            }
        }
    }

    private void connectNeighbors(Node[][] grid, int x, int y) {
        Node node = grid[x][y];
        int[][] directions = {
            { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 },   // Direzioni x/y
            { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 } // Diagonali
        };
    
        for (int[] dir : directions) {
            int nx = x + dir[0];
            int ny = y + dir[1];
    
            // Verifica che il nodo vicino sia valido e attraversabile
            if (nx >= 0 && nx < width && ny >= 0 && ny < height && grid[nx][ny] != null) {
                if (Math.abs(dir[0]) + Math.abs(dir[1]) == 2) { 
                    int checkX = x + dir[0];
                    int checkY = y; // Direzione orizzontale
                    int checkX2 = x;
                    int checkY2 = y + dir[1]; // Direzione verticale
    
                    // salta direzione diagonale se le altre direzioni sono occupate
                    if (grid[checkX][checkY] == null || grid[checkX2][checkY2] == null) {
                        continue;
                    }
                }
    
                // Calcola il costo della connessione
                float cost = (dir[0] == 0 || dir[1] == 0) ? 1.0f : 1.4f; // se una delle due coordinate sono uguali a zero allora il costo sarà 1 se no 1.41
                node.addConnection(grid[nx][ny], cost); // Aggiungi la connessione
            }
        }
    }

    /**
     * disegna nodi
     * @param shapeRenderer
     */
    public void drawNodes(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.BLUE); // Colore dei nodi

        for (Node node : nodes) {
            // Disegna ogni nodo come un cerchio al centro della cella
            shapeRenderer.circle(node.x + 0.5f, node.y + 0.5f, 0.2f, 10); // Raggio 0.2 per nodi
        }

        shapeRenderer.setColor(Color.BLACK);
    }

    /**
     * disegna connessioni
     * @param shapeRenderer
     */
    public void drawConnections(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.BLACK); // Colore delle linee di connessione
    
        for (Node node : nodes) {
            for (Connection<Node> connection : node.connessioni) {
                Node toNode = connection.getToNode();
    
                // Disegna una linea tra il nodo corrente e il nodo connesso
                shapeRenderer.line(node.x + 0.5f, node.y + 0.5f, toNode.x + 0.5f, toNode.y + 0.5f);
            }
        }
    
        shapeRenderer.setColor(Color.BLACK);
    }

    /**
     * @param x
     * @param y
     * @return
     */
    public Node getNodeAt(int x, int y) {
        for (Node node : nodes) {
            // Controlla se le coordinate corrispondono
            if (node.x == x && node.y == y) {
                return node;
            }
        }
        // Restituisce null se nessun nodo corrisponde
        return null;
    }


    /**
     * restituisci nodo più vicino
     * @param x
     * @param y
     * @return
     */
    public Node getClosestNode(float x, float y) {
        Node closestNode = null;
        float closestDistance = Float.MAX_VALUE;
    
        for (Node node : nodes) {
            // Calcola la distanza euclidea tra il nodo corrente e le coordinate fornite
            float distance = (float) Math.sqrt(Math.pow(node.x+0.5f - x, 2) + Math.pow(node.y+0.5f - y, 2));
    
            // Trova il nodo con la distanza minima
            if (distance < closestDistance) {
                closestDistance = distance;
                closestNode = node;
            }
        }
    
        return closestNode; // Restituisce il nodo più vicino
    }
    

}