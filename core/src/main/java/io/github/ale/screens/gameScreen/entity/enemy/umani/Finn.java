package io.github.ale.screens.gameScreen.entity.enemy.umani;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import io.github.ale.screens.gameScreen.entity.abstractEntity.EntityConfig;
import io.github.ale.screens.gameScreen.entity.enemy.abstractEnemy.Nemico;
import io.github.ale.screens.gameScreen.entity.player.Player;
import io.github.ale.screens.gameScreen.maps.Map;
import io.github.ale.screens.gameScreen.pathfinding.HeuristicDistance;
import io.github.ale.screens.gameScreen.pathfinding.Node;

public final class Finn extends Nemico {

    Node startNode;
    Node endNode;
    Heuristic<Node> heuristic;
    Array<Vector2> directions;

    boolean hasLoadedGraph = false;

    float followCooldown = 0f;
    float follow = .2f;

    IndexedAStarPathFinder<Node> pathFinder;
    DefaultGraphPath<Node> path;

    ShapeRenderer renderer = new ShapeRenderer();


    public Finn(EntityConfig config, Player p) {
        super(config, p);
        directions = new Array<>();
        create();
    }

    @Override
    public void updateEntityType() {
        if (Map.isGraphLoaded && !hasLoadedGraph) {
            pathFinder = new IndexedAStarPathFinder<>(Map.getGraph());
            hasLoadedGraph = true;
            System.out.println("Caricato il grafo!");
        }

        if (cooldownFollowing>=0) {
            cooldownFollowing-=Gdx.graphics.getDeltaTime();
        }else{
            cooldownFollowing = follow;
            calcolaPercorso(player().hitbox().x+player().hitbox().width/2, player().hitbox().y+player().hitbox().height/2);
        }
        
        getMovementManager().update(path, directions, this);
        
        
    }

    public void calcolaPercorso(float x, float y) {
        
        // Inizializza il percorso e i nodi
        path = new DefaultGraphPath<>();
        startNode = Map.getGraph().getClosestNode(hitbox().x+hitbox().width/2, hitbox().y+hitbox().height/2);
        endNode = Map.getGraph().getClosestNode(x, y);
        // Definizione dell'euristica
        heuristic = new HeuristicDistance();
        boolean success = pathFinder.searchNodePath(startNode, endNode, heuristic, path);

        // Pulisce le azioni precedenti
        directions.clear();

        if (success && path.getCount() < 12) {
            System.out.println("Percorso trovato!");
            Node previousNode = null;

            // Itera sul percorso calcolato
            for (Node node : path) {
                System.out.println("Nodo: (" + node.x + ", " + node.y + ")");
                if (previousNode != null) {
                    // Calcola il vettore direzionale tra il nodo precedente e il nodo attuale
                    float dx = node.x - previousNode.x;
                    float dy = node.y - previousNode.y;
                    Vector2 direction = new Vector2(dx, dy); // Normalizza il vettore
                    directions.add(direction);
                }
                previousNode = node;
            }
        } else {
            directions.add(new Vector2(0, 0));
            System.out.println("Percorso non trovato");
        }
        for (Vector2 d : directions) {
            System.out.println(d);
        }
    }


    @Override
    public void create() {
        setAree(5.5f, 1f);
    }

    @Override
    public void attack() {
        if (atkCooldown() <= 0) {
            System.out.println("Finn attacca il giocatore!");

            player().statistiche().inflictDamage(statistiche().getAttackDamage(), player().stati().immortality());

            float angolo = calcolaAngolo(coordinateCentro().x, coordinateCentro().y, player().coordinateCentro().x,
                    player().coordinateCentro().y);

            System.out.println("Angolo di attacco: " + angolo + "°");

            player().knockback(angolo); // startKnockback(angolo);
            System.out.println(player().statistiche().getHealth());

            setAtkCooldown(ATTACK_COOLDOWN);
        }
    }

    public void drawPath(ShapeRenderer shapeRenderer) {
    // Controlla se il percorso è vuoto o nullo
    if (path == null || path.nodes.isEmpty()) {
        return; // Nessun percorso da disegnare
    }
     // Colore delle linee

     shapeRenderer.setColor(Color.BLACK);
    shapeRenderer.circle(startNode.x+0.5f, startNode.y+0.5f, 0.2f, 10);
    shapeRenderer.circle(endNode.x+0.5f, endNode.y+0.5f, 0.2f, 10);
    shapeRenderer.setColor(Color.RED);
    Node previousNode = null; // Nodo precedente inizializzato a null
    for (Node node : path.nodes) {
        if (previousNode != null) {
            // Disegna una linea dal nodo precedente al nodo corrente
            shapeRenderer.line(
                previousNode.x + 0.5f, previousNode.y + 0.5f, // Coordinate nodo precedente
                node.x + 0.5f, node.y + 0.5f // Coordinate nodo corrente
            );
        }
        previousNode = node; // Aggiorna il nodo precedente
    }

}

}
