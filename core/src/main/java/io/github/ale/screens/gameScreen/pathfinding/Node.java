package io.github.ale.screens.gameScreen.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.utils.Array;

public class Node{
    public int x, y;
    public Array<Connection<Node>> connessioni = new Array<>();
    private int index;

    private boolean walkable = true;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * coordinata x del nodo
     * @return
     */
    public float getX(){
        return x+0.5f;
    }

    /**
     * coordinata y del nodo
     * @return
     */
    public float getY(){
        return y+0.5f;
    }

    /**
     * aggiunge una connessione ad un nodo
     * @param toNode
     * @param cost
     */
    public void addConnection(Node toNode, float cost) {
        connessioni.add(new NodeConnection(this, toNode, cost));
    }

    /**
     * restituisce tutte le connessioni
     * @return
     */
    public Array<Connection<Node>> getConnections() {
        return connessioni;
    }

    /**
     * restituisce tutti i nodi vicini
     * @return
     */
    public Array<Node> getNeighbors() {
        Array<Node> neighbors = new Array<>();
        for (Connection<Node> conn : connessioni) {
            neighbors.add(conn.getToNode()); // Ottieni il nodo di destinazione della connessione
        }
        return neighbors;
    }
}



