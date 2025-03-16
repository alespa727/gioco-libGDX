package io.github.ale.screens.gameScreen.maps;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.utils.Array;

public class Node{
    public float x, y;
    public Array<Connection<Node>> connections = new Array<>();

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }
    // Metodo per aggiungere una connessione
    public void addConnection(Node toNode, float cost) {
        connections.add(new NodeConnection(this, toNode, cost));
    }

    public Array<Connection<Node>> getConnections() {
        return connections;
    }
    
    
}



