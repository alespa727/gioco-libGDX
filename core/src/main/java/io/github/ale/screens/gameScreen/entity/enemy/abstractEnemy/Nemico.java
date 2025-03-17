package io.github.ale.screens.gameScreen.entity.enemy.abstractEnemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import io.github.ale.screens.gameScreen.entity.abstractEntity.Entity;
import io.github.ale.screens.gameScreen.entity.abstractEntity.EntityConfig;
import io.github.ale.screens.gameScreen.entity.abstractEntity.movement.EntityMovementManager;
import io.github.ale.screens.gameScreen.entity.enemy.abstractEnemy.awareness.EnemyAwareness;
import io.github.ale.screens.gameScreen.entity.enemy.abstractEnemy.state.EnemyState;
import io.github.ale.screens.gameScreen.entity.player.Player;
import io.github.ale.screens.gameScreen.maps.Map;
import io.github.ale.screens.gameScreen.pathfinding.HeuristicDistance;
import io.github.ale.screens.gameScreen.pathfinding.Node;

public abstract class Nemico extends Entity {
    private final Player player;

    private Node startNode;
    private Node endNode;
    Heuristic<Node> heuristic;

    private boolean hasLoadedGraph = false;
    private IndexedAStarPathFinder<Node> pathFinder;
    private DefaultGraphPath<Node> path;

    private final EntityMovementManager movement;
    public final EnemyState stati;
    private EnemyAwareness awareness;

    private float areaInseguimento;
    private float areaAttacco;

    public final float ATTACK_COOLDOWN = 2f; // Cooldown in secondi

    public Nemico(EntityConfig config, Player player) {
        super(config);
        path = new DefaultGraphPath<>();
        this.player = player;
        this.movement = new EntityMovementManager();
        this.stati = new EnemyState();
        this.awareness = new EnemyAwareness();
    }

    @Override
    public void drawHitbox(ShapeRenderer renderer) {
        renderer.rect(hitbox().x, hitbox().y, hitbox().width, hitbox().height);
        if (stati.searching()) {
            renderer.rectLine(awareness.getVisione().a.x, awareness.getVisione().a.y,
                    awareness.getObbiettivoDrawCoord().x, awareness.getObbiettivoDrawCoord().y, 0.1f);
            renderer.rectLine(hitbox().x, hitbox().y, player().hitbox().x, player().hitbox().y, 0.0001f);
            renderer.rectLine(hitbox().x + hitbox().width, hitbox().y, player().hitbox().x + player().hitbox().width,
                    player().hitbox().y, 0.0001f);
            renderer.rectLine(hitbox().x, hitbox().y + hitbox().height, player().hitbox().x,
                    player().hitbox().y + player().hitbox().height, 0.0001f);
            renderer.rectLine(hitbox().x + hitbox().width, hitbox().y + hitbox().height,
                    player().hitbox().x + player().hitbox().width, player().hitbox().y + player().hitbox().height,
                    0.0001f);
        }
        if (stati.isPursuing() && !stati.searching()) {
            renderer.rectLine(awareness.getVisione().a.x, awareness.getVisione().a.y, awareness.getVisione().b.x,
                    awareness.getVisione().b.y, 0.1f);
        }
        // renderer.rect(awareness.getObbiettivoDrawCoord().x -
        // getAwareness().getRange().width/2, awareness.getObbiettivoDrawCoord().y -
        // getAwareness().getRange().height/2, getAwareness().getRange().width,
        // getAwareness().getRange().height);
    }

    /**
     * aggiorna lo stato del nemico
     * 
     * @param delta variabile del tempo
     * @param p
     */
    @Override
    public void updateEntity() {

        float delta = Gdx.graphics.getDeltaTime();
        // inAreaInseguimento();
        inAreaAttacco();
        mantieniNeiLimiti();
        gestioneInseguimento(delta);
        adjustHitbox();

        awareness.setRange(coordinateCentro().x - areaAttacco / 2, coordinateCentro().y - areaAttacco / 2, areaAttacco,
                areaAttacco);
        awareness.setAreaInseguimento(getX() + getSize().getWidth() / 2, getY() + getSize().getHeight() / 2,
                areaInseguimento);

        awareness.setVisione(coordinateCentro().x, coordinateCentro().y, player.coordinateCentro().x,
                player.coordinateCentro().y);

        if (atkCooldown() > 0) {
            setAtkCooldown(atkCooldown() - delta);
        }

        if (stati.idle()) {
            wandering();
        }
    }

    /**
     * disegna il range del nemico (attacco e inseguimento)
     * 
     * @param renderer
     */
    @Override
    public void drawRange(ShapeRenderer renderer) {
        if (stati.inRange()) {
            renderer.setColor(Color.BLUE);
        }
        renderer.rect(awareness.getRange().x, awareness.getRange().y, awareness.getRange().width,
                awareness.getRange().height);
        if (stati.isPursuing()) {
            renderer.setColor(Color.VIOLET);
        }
        renderer.circle(awareness.getAreaInseguimento().x, awareness.getAreaInseguimento().y,
                awareness.getAreaInseguimento().radius, 100);
        renderer.setColor(Color.BLACK);
    }

    public void renderPath(float x, float y) {
        // System.out.println(direzione());
        // Carica il grafo una volta che è stato caricato
        if (!hasLoadedGraph && Map.isGraphLoaded) {
            pathFinder = new IndexedAStarPathFinder<>(Map.getGraph());
            hasLoadedGraph = true;
            System.out.println("Caricato il grafo!");
            calcolaPercorso(x, y);
            getMovementManager().setGoal(path.get(0), path.get(1));
        }

        if (getMovementManager().sulNodo) {
            calcolaPercorso(x, y);
            if (path.getCount() > 1)
                getMovementManager().setGoal(path.get(0), path.get(1));
        }

        // System.out.println(path.getCount());
        if (path.getCount() > 1 && path.getCount() < 14) {
            // Aggiorna il movimento del nemico
            getMovementManager().update(this);
        } else {
            getMovementManager().setFermo(this);
        }
    }

    public void calcolaPercorso(float x, float y) {
        path.clear();
        startNode = Map.getGraph().getClosestNode(hitbox().x + hitbox().width / 2, hitbox().y + hitbox().height / 2);
        endNode = Map.getGraph().getClosestNode(x, y);
        heuristic = new HeuristicDistance();

        boolean success = pathFinder.searchNodePath(startNode, endNode, heuristic, path);
        if (success && path.getCount() < 14) {
            System.out.println("Percorso trovato!");
        } else {
            if (path.getCount() == 0 || path.get(path.getCount() - 1) != endNode) {
                path.add(endNode);
            }
            System.out.println("Percorso non trovato");
        }
    }

    public void drawPath(ShapeRenderer shapeRenderer) {
        // Controlla se il percorso è vuoto o nullo
        if (path == null || path.nodes.isEmpty()) {
            return; // Nessun percorso da disegnare
        }
        // Colore delle linee

        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.circle(startNode.x + 0.5f, startNode.y + 0.5f, 0.2f, 10);
        shapeRenderer.circle(endNode.x + 0.5f, endNode.y + 0.5f, 0.2f, 10);
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

    public EnemyState getEnemyStates() {
        return stati;
    }

    public EntityMovementManager getMovementManager() {
        return movement;
    }

    protected Player player() {
        return player;
    }

    /**
     * area di inseguimento, area in cui puo fare attack()
     * 
     * @param inseguimento
     * @param attacco
     */
    public void setAree(float inseguimento, float attacco) {
        this.areaInseguimento = inseguimento;
        this.areaAttacco = attacco;
    }

    /**
     * 
     * @param p
     * @param delta
     */
    private void gestioneInseguimento(float delta) {
        boolean inseguimento = true;
        if (inseguimento) {

        }

        if (stati.idle()) {

        }
    }

    /**
     * il nemico attacca
     * 
     * @param p
     */
    public abstract void attack();

    /**
     * controlla se il player è nel range attacco
     */
    private void inAreaAttacco() {
        Rectangle hitboxPlayer = player.hitbox();
        if (awareness.getRange().overlaps(hitboxPlayer)) {
            stati.setInRange(true);
            attack();
        } else
            stati.setInRange(false);
    }

    /**
     * setta l'obbiettivo del nemico
     * 
     * @param p
     */
    private void inAreaInseguimento() {

    }

    public EnemyAwareness getAwareness() {
        return awareness;
    }

    public void setAwareness(EnemyAwareness awareness) {
        this.awareness = awareness;
    }

    public void wandering() {
        areaInseguimento = 1f;
    }

}
