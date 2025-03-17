package io.github.ale.screens.gameScreen.pathfinding;

import com.badlogic.gdx.ai.pfa.Heuristic;

public class HeuristicDistance implements Heuristic<Node> {

    /**
     * calcola una distanza approssimata per raggiungere l'obbiettivo
     */
    @Override
    public float estimate(Node node, Node endNode) {
        return (float) Math.sqrt(Math.pow(endNode.x - node.x, 2) + Math.pow(endNode.y - node.y, 2));
    }

}