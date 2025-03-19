package io.github.ale.screens.gameScreen.entity.enemy.abstractEnemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import io.github.ale.screens.gameScreen.entity.EntityManager;
import io.github.ale.screens.gameScreen.entity.abstractEntity.Entity;
import io.github.ale.screens.gameScreen.entity.abstractEntity.EntityConfig;
import io.github.ale.screens.gameScreen.entity.abstractEntity.movement.EntityMovementManager;
import io.github.ale.screens.gameScreen.entity.enemy.abstractEnemy.awareness.EnemyAwareness;
import io.github.ale.screens.gameScreen.entity.enemy.abstractEnemy.state.EnemyState;
import io.github.ale.screens.gameScreen.entity.player.Player;
import io.github.ale.screens.gameScreen.maps.Map;
import io.github.ale.screens.gameScreen.maps.MapManager;
import io.github.ale.screens.gameScreen.pathfinding.HeuristicDistance;
import io.github.ale.screens.gameScreen.pathfinding.Node;

public abstract class Nemico extends Entity {
    private final Player player;

    private Node startNode;
    private Node endNode;
    private int map=-1;
    private Heuristic<Node> heuristic;

    private final float maxDamageTime = 0.273f;
    private float countdownDamage = 0.273f;

    private boolean hasLoadedGraph = false;
    private IndexedAStarPathFinder<Node> pathFinder;
    private DefaultGraphPath<Node> path;

    private final EntityMovementManager movement;
    public final EnemyState stati;
    private EnemyAwareness awareness;

    private float areaInseguimento;
    private float areaAttacco;

    public final float ATTACK_COOLDOWN = 2f; // Cooldown in secondi

    public Nemico(EntityConfig config, EntityManager manager, Player player) {
        super(config);
        this.manager = manager;
        path = new DefaultGraphPath<>();
        this.player = player;
        this.movement = new EntityMovementManager();
        this.stati = new EnemyState();
        this.awareness = new EnemyAwareness();
    }

    @Override
    public void drawHitbox(ShapeRenderer renderer) {
        renderer.rect(hitbox().x, hitbox().y, hitbox().width, hitbox().height);
    }

    /**
     * aggiorna lo stato del nemico
     * 
     * @param delta variabile del tempo
     * @param p
     */
    @Override
    public void updateEntity() {

        delta = Gdx.graphics.getDeltaTime();

        if (statistiche().gotDamaged) {
            countdownDamage -= delta;
            if (countdownDamage <= 0) {
                countdownDamage = maxDamageTime;
                statistiche().gotDamaged = false;
            }
        }
        if (atkCooldown() >= 0) {
            setAtkCooldown(atkCooldown() - delta);
            //System.out.println(atkCooldown());
        }else{
            setAtkCooldown(1f);
            if(manager.isentityinrect(0, awareness.getRange().x, awareness.getRange().y, awareness.getRange().width, awareness.getRange().height)){
                System.out.println("ATTACCO");
                attack();
            } 
        }
        
        // inAreaInseguimento();
        mantieniNeiLimiti();
        adjustHitbox();

        awareness.setRange(coordinateCentro().x - areaAttacco / 2, coordinateCentro().y - areaAttacco / 2, areaAttacco,
                areaAttacco);
        awareness.setAreaInseguimento(getX() + getSize().getWidth() / 2, getY() + getSize().getHeight() / 2,
                areaInseguimento);

        awareness.setVisione(coordinateCentro().x, coordinateCentro().y, player.coordinateCentro().x,
                player.coordinateCentro().y);

        

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
        
        renderer.setColor(Color.BLACK);
    }

    public void renderPath(float x, float y) {
        // Carica il grafo una volta che è stato caricato

        if (hasLoadedGraph && map!=MapManager.currentmap()) {
            map = MapManager.currentmap();
            pathFinder = new IndexedAStarPathFinder<>(Map.getGraph());
            hasLoadedGraph = true;
            System.out.println("Caricato il grafo!");
        }
        
        if (!hasLoadedGraph && Map.isGraphLoaded) {
            map = MapManager.currentmap();
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
        if (path.getCount() > 1 && coordinateCentro().dst(x, y)<20f) {
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
        //System.out.println(Map.getGraph().getNodeCount());
        heuristic = new HeuristicDistance();

        boolean success = pathFinder.searchNodePath(startNode, endNode, heuristic, path);
        if (success && path.getCount() < 14) {
            //System.out.println("Percorso trovato!");
        } else {
            if (path.getCount() == 0 || path.get(path.getCount() - 1) != endNode) {
                path.add(endNode);
            }
            //System.out.println("Percorso non trovato");
        }
        
    }

    public boolean checkIfDead() {
        // Logica per controllare se il giocatore è morto
        if (statistiche().getHealth() <= 0) {
            this.stati().setIsAlive(false);
            System.out.println("Il giocatore è morto");
            System.out.println("Rianimazione..");
        }
        return this.stati().isAlive();
    }

    public void drawPath(ShapeRenderer shapeRenderer) {
        // Controlla se il percorso è vuoto o nullo
        if (path == null || path.nodes.isEmpty()) {
            return; // Nessun percorso da disegnare
        }
        // Colore delle linee

        shapeRenderer.setColor(Color.BLACK);
        
        shapeRenderer.setColor(Color.RED);
        Node previousNode = null; // Nodo precedente inizializzato a null
        for (Node node : path.nodes) {
            if (previousNode != null) {
                // Disegna una linea dal nodo precedente al nodo corrente
                shapeRenderer.rectLine(previousNode.getX(), previousNode.getY(), node.getX(), node.getY(), 0.1f);
                shapeRenderer.circle(previousNode.x + 0.5f, previousNode.y + 0.5f, 0.2f, 10);
            }
            previousNode = node; // Aggiorna il nodo precedente
        }
        shapeRenderer.circle(startNode.x + 0.5f, startNode.y + 0.5f, 0.2f, 10);
        shapeRenderer.circle(endNode.x + 0.5f, endNode.y + 0.5f, 0.2f, 10);
        shapeRenderer.setColor(Color.BLACK);
    }

    public void pursue(float x, float y){
        renderPath(x, y);
    }

    public void evade(float x, float y){
        float directionX = x - coordinateCentro().x;
        float directionY = y - coordinateCentro().y;

        // Correctly calculate the opposite direction
        Vector2 oppositeDirection = new Vector2(-directionX, -directionY);

        renderPath(coordinateCentro().x + oppositeDirection.x, coordinateCentro().y + oppositeDirection.y);
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
     * il nemico attacca
     * 
     * @param p
     */
    public abstract void attack();

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
