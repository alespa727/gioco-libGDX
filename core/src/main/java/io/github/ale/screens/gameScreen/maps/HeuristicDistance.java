package io.github.ale.screens.gameScreen.maps;

import com.badlogic.gdx.ai.pfa.Heuristic;

public class HeuristicDistance implements Heuristic<Node> {

    @Override
    public float estimate(Node node, Node endNode) {
        return Math.abs(endNode.x - node.x) + Math.abs(endNode.y - node.y);
    }

}