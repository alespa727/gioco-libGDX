package io.github.ale.screens.gameScreen.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.utils.Array;

public class Node{
    public int x, y;
    public Array<Connection<Node>> connessioni = new Array<>();

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public float getX(){
        return x+0.5f;
    }

    public float getY(){
        return y+0.5f;
    }
    // Metodo per aggiungere una connessione
    public void addConnection(Node toNode, float cost) {
        connessioni.add(new NodeConnection(this, toNode, cost));
    }

    public Array<Connection<Node>> getConnections() {
        return connessioni;
    }
    
    
}



